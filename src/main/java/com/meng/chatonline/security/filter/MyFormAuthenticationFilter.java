package com.meng.chatonline.security.filter;

import com.meng.chatonline.Constants;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @Author xindemeng
 *
 * 自定义表单认证过滤器
 */
public class MyFormAuthenticationFilter extends FormAuthenticationFilter
{
    //重写该方法，使successUrl属性生效
    @Override
    protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception
    {
        String successUrl = this.getSuccessUrl();
        boolean contextRelative = true;
        SavedRequest savedRequest = WebUtils.getAndClearSavedRequest(request);
        //如果没有设置successUrl属性，默认值值为"/"
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

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception
    {
        //把User放到session里
        Session session = subject.getSession(false);
        if (session != null)
        {
            //登陆的用户
            session.setAttribute(Constants.CURRENT_USER, subject.getPrincipal());
            //shiro主体用户，与切换身份以后分别开来
            session.setAttribute(Constants.PRINCIPAL, subject.getPrincipal());
        }

        return super.onLoginSuccess(token, subject, request, response);
    }

}
