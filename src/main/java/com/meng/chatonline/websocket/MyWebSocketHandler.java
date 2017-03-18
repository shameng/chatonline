package com.meng.chatonline.websocket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.meng.chatonline.model.ActiveUser;
import com.meng.chatonline.model.Broadcast;
import com.meng.chatonline.model.Message;
import com.meng.chatonline.model.User;
import com.meng.chatonline.service.MessageService;
import com.meng.chatonline.service.UserService;
import com.meng.chatonline.utils.MySecurityUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.meng.chatonline.constant.Constants.LOGIN_BROADCAST_TYPE;
import static com.meng.chatonline.constant.Constants.LOGOUT_BROADCAST_TYPE;


/**
 * @Author xindemeng
 *
 * WebSocket处理器
 */
@Component
public class MyWebSocketHandler implements WebSocketHandler
{
    @Resource
    private MessageService messageService;
    @Resource
    private UserService userService;

    //键是已登录的用户，值是用户对应的 WebSocketSession
    private static final Map<ActiveUser, WebSocketSession> userSocketSessionMap;

    static
    {
        userSocketSessionMap = new HashMap<ActiveUser, WebSocketSession>();
    }

    //建立连接后
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception
    {
        ActiveUser user = (ActiveUser) webSocketSession.getAttributes().get("user");
        if (user != null)
        {
            //给在线用户广播该用户登陆消息
            Broadcast broadcast = new Broadcast();
            broadcast.setType(LOGIN_BROADCAST_TYPE);
            broadcast.setUtterer(user);
            this.broadcast(broadcast);

            if (userSocketSessionMap.get(user) == null)
                userSocketSessionMap.put(user, webSocketSession);
        }
    }

    /**
     * 消息处理，在客户端通过Websocket API发送的消息会经过这里，然后进行相应的处理
     */
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception
    {
        if (webSocketMessage.getPayloadLength() == 0)
            return;
        //把JSON转换成对应的实体类
        Message msg = new Gson().fromJson(webSocketMessage.getPayload().toString(),
                Message.class);
        msg.setDate(new Date());

        //把消息保存到数据库
        messageService.saveEntity(msg);

        User fromUser = userService.getEntity(msg.getFromUser().getId());
        User toUser = userService.getEntity(msg.getToUser().getId());
        msg.setFromUser(MySecurityUtils.userToActiveUser(fromUser));
        msg.setToUser(MySecurityUtils.userToActiveUser(toUser));

        TextMessage textMessage = new TextMessage(new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(msg));
        this.sendMessageToUser(msg.getToUser(), textMessage);
    }

    //给某个用户发送消息
    private void sendMessageToUser(User toUser, TextMessage textMessage) throws IOException
    {
        WebSocketSession session = userSocketSessionMap.get(toUser);
        if (session != null && session.isOpen())
        {
            session.sendMessage(textMessage);
        }
    }

    /**
     * 消息传输错误处理
     */
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception
    {
        if (webSocketSession.isOpen())
            webSocketSession.close();
        //移除socket会话
        for(Map.Entry<ActiveUser, WebSocketSession> entry : userSocketSessionMap.entrySet())
        {
            if (entry.getValue().getId().equals(webSocketSession.getId()))
            {
                User user = new User(entry.getKey().getId());
                userSocketSessionMap.remove(user);
                System.out.println("socket会话发生错误并且已经移除了用户" + entry.getKey());

                //广播用户注销消息
                broadcastLogoutMsg(entry.getKey());

                break;
            }
        }
    }

    /**
     * 关闭连接后
     */
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception
    {
        System.out.println("WebSocket:" + webSocketSession.getId() + "已经关闭");
        for (Map.Entry<ActiveUser, WebSocketSession> entry : userSocketSessionMap.entrySet())
        {
            if (entry.getValue().getId().equals(webSocketSession.getId()))
            {
//                User user = new User(entry.getKey().getId());
//                userSocketSessionMap.remove(user);
                System.out.println("socket会话关闭并且已经移除了用户ID" + entry.getKey());

                //广播用户注销消息
                broadcastLogoutMsg(entry.getKey());

                break;
            }
        }
    }

    public boolean supportsPartialMessages()
    {
        return false;
    }

    //广播公告给所有在线用户
    public void broadcast(Broadcast broadcast)
    {
        System.out.println("广播新公告");
        final TextMessage textMessage = new TextMessage(new GsonBuilder().
                setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(broadcast));
        for (final Map.Entry<ActiveUser, WebSocketSession> entry : userSocketSessionMap.entrySet())
        {
            //多线程发送
            if (entry.getValue().isOpen())
            {
                new Thread(new Runnable()
                {
                    public void run()
                    {
                        if (entry.getValue().isOpen())
                        {
                            try
                            {
                                entry.getValue().sendMessage(textMessage);
                            } catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        }
    }

    //返回在线用户
    public static Set<ActiveUser> getOnlineUsers()
    {
        return userSocketSessionMap.keySet();
    }

    //广播用户注销消息
    public void broadcastLogoutMsg(User user)
    {
        if (user != null)
        {
            userSocketSessionMap.remove(user);

            Broadcast broadcast = new Broadcast();
            broadcast.setType(LOGOUT_BROADCAST_TYPE);
            broadcast.setUtterer(user);
            this.broadcast(broadcast);
        }
    }

}
