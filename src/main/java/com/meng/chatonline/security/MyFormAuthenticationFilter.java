package com.meng.chatonline.security;

import com.meng.chatonline.exception.MyExceptionResolver;
import com.meng.chatonline.model.ActiveUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Author xindemeng
 *
 * 自定义表单认证过滤器
 */
public class MyFormAuthenticationFilter extends FormAuthenticationFilter
{
    //是否一个用户只允许在一处登陆
    private boolean onlyOnePermitLogin = true;

    //重写该方法，使successUrl属性生效
    @Override
    protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception
    {
        String successUrl = this.getSuccessUrl();
        boolean contextRelative = true;
        SavedRequest savedRequest = WebUtils.getAndClearSavedRequest(request);
        //如果没有设置successUrl属性
        if (successUrl.equals(DEFAULT_SUCCESS_URL))
        {
            if (savedRequest != null && savedRequest.getMethod().equalsIgnoreCase(AccessControlFilter.GET_METHOD))
            {
                //跳转到上一个请求路径
                successUrl = savedRequest.getRequestUrl();
                contextRelative = false;

                if (successUrl == null)
                {
                    throw new IllegalStateException("Success URL not available via saved request or via the " +
                            "successUrlFallback method parameter. One of these must be non-null for " +
                            "issueSuccessRedirect() to work.");
                }
            }
        }
        WebUtils.issueRedirect(request, response, successUrl, null, contextRelative, true);
    }

    //重写该方法来控制一个用户只能在一处登陆
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception
    {
        if (isOnlyOnePermitLogin())
        {
            ActiveUser user = (ActiveUser) subject.getPrincipal();
            ShiroSecurityHelper.checkLogined(user);
        }

        return super.onLoginSuccess(token, subject, request, response);
    }

    public boolean isOnlyOnePermitLogin()
    {
        return onlyOnePermitLogin;
    }

    public void setOnlyOnePermitLogin(boolean onlyOnePermitLogin)
    {
        this.onlyOnePermitLogin = onlyOnePermitLogin;
    }
}
