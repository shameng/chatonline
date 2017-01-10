package com.meng.chatonline.security;

import com.meng.chatonline.exception.LoginException;
import com.meng.chatonline.model.ActiveUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Author xindemeng
 */
public class ShiroSecurityHelper
{
    //检查是否已经登陆了，如果已经登陆过了，则把之前的踢出，保证一个账号只能在一处登陆
    public static void checkLogined(ActiveUser user)
    {
        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
        DefaultWebSessionManager sessionManager = (DefaultWebSessionManager) securityManager.getSessionManager();
        //获取当前已登录的用户session列表
        Collection<Session> sessions = sessionManager.getSessionDAO().getActiveSessions();

        List<Session> mySessions = new ArrayList<Session>();

        for(Session session : sessions){
            if(user.toString().equals(String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)))) {
                mySessions.add(session);
            }
        }

        //如果有两个一样的账号同时在登陆，则把之前的踢出
        if (mySessions.size() > 1)
        {
            Session preSession = null;
            for (Session session : mySessions)
            {
                if (preSession == null)
                    preSession = session;
                else if (preSession.getStartTimestamp().after(session.getStartTimestamp()))
                    preSession = session;
            }
            sessionManager.getSessionDAO().delete(preSession);
        }
    }

}
