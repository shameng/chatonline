package com.meng.chatonline.websocket;

import com.meng.chatonline.model.ActiveUser;
import com.meng.chatonline.model.User;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Author xindemeng
 *
 * WebSocket握手拦截器用来拦截和处理客户端和服务器端分别在握手前和握手后的事件
 */
public class HandShake implements HandshakeInterceptor
{

    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception
    {
        if(serverHttpRequest instanceof ServletServerHttpRequest)
        {
            ServletServerHttpRequest request = (ServletServerHttpRequest) serverHttpRequest;
            HttpSession session = request.getServletRequest().getSession(false);
            ActiveUser user = (ActiveUser) session.getAttribute("user");
            if (user != null)
            {
                Integer uid = user.getId();
                //添加属性到 WebSocketSession 的attribute里
                map.put("user", user);
                System.out.println("WebSocket:用户[ID:"+ uid +"]已经建立连接");
                return true;
            }
        }
        return false;
    }

    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e)
    {

    }
}
