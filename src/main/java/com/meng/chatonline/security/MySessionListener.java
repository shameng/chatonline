package com.meng.chatonline.security;

import com.google.gson.Gson;
import com.meng.chatonline.model.ActiveUser;
import com.meng.chatonline.model.User;
import com.meng.chatonline.websocket.MyWebSocketHandler;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListenerAdapter;
import org.apache.shiro.subject.support.DefaultSubjectContext;

import javax.annotation.Resource;

/**
 * @Author xindemeng
 *
 * session的监听器
 */
public class MySessionListener extends SessionListenerAdapter
{
    @Resource
    private MyWebSocketHandler webSocketHandler;

    @Override
    public void onStart(Session session)
    {
        ActiveUser user = (ActiveUser) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        System.out.println("-----------------"+user + "的session"+session.getId()+"创建--------------");
    }

    @Override
    public void onStop(Session session)
    {
        String userStr = "";
        User user = null;
        try
        {
            userStr = String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY));
            System.out.println("用户："+userStr+"的session终止了");

            user = new Gson().fromJson(userStr, User.class);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            //广播消息
            webSocketHandler.broadcastLogoutMsg(user);
        }
    }

    @Override
    //session过期后广播用户注销
    public void onExpiration(Session session)
    {
        String userStr = "";
        User user = null;
        try
        {
            userStr = String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY));
            System.out.println("用户："+userStr+"的session过期了");

            user = new Gson().fromJson(userStr, User.class);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            //广播消息
            webSocketHandler.broadcastLogoutMsg(user);
        }
    }
}