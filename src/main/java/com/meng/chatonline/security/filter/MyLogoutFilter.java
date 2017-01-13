package com.meng.chatonline.security.filter;

import com.meng.chatonline.model.ActiveUser;
import com.meng.chatonline.websocket.MyWebSocketHandler;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @Author xindemeng
 *
 * 自定义logout拦截器方法
 */
public class MyLogoutFilter extends LogoutFilter
{
    @Resource
    private MyWebSocketHandler webSocketHandler;

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception
    {
        Subject subject = getSubject(request, response);
        ActiveUser user = (ActiveUser) subject.getPrincipal();
        if (user != null)
            //广播退出登陆
            webSocketHandler.broadcastLogoutMsg(user);

        return super.preHandle(request, response);
    }

}
