package com.meng.chatonline.security.filter;

import com.meng.chatonline.constant.Constants;
import com.meng.chatonline.model.ActiveUser;
import com.meng.chatonline.websocket.MyWebSocketHandler;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @Author xindemeng
 *
 * 自定义表单认证过滤器
 */
public class MyFormAuthenticationFilter extends FormAuthenticationFilter
{
    //是否使用先前访问的页面作为登陆成功后跳转到的页面，默认使用
    private boolean useSaveRequestToBeSuccessUrl = true;

    @Resource
    private MyWebSocketHandler webSocketHandler;

    //重写该方法，是否启用successUrl属性
    @Override
    protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception
    {
        String successUrl = this.getSuccessUrl();
        //是否添加上下文，例如本项目的上下文是"/chatonline"
        boolean contextRelative = true;

        if (isUseSaveRequestToBeSuccessUrl())
        {
            SavedRequest savedRequest = WebUtils.getAndClearSavedRequest(request);
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

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        //如果发生了验证码异常，则不执行登陆操作
        if(request.getAttribute(Constants.SHIRO_LOGIN_FAILURE) != null) {
            return true;
        }

        //下面才执行用户登陆
        return super.onAccessDenied(request, response, mappedValue);
    }

    //重写此方法是用于一种情况：当用户先不注销登陆而直接再次登陆时，会先把之前登陆的用户注销掉再登陆
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
    {
        if (isLoginRequest(request, response))
        {
            if (isLoginSubmission(request, response))
            {
                //本次用户登陆账号
                String account = this.getUsername(request);

                Subject subject = this.getSubject(request, response);
                //之前登陆的用户
                ActiveUser user = (ActiveUser) subject.getPrincipal();
                //如果两次登陆的用户不一样，则先退出之前登陆的用户
                if (account != null && user != null && !account.equals(user.getAccount()))
                {
                    subject.logout();
                    //广播退出登陆
                    webSocketHandler.broadcastLogoutMsg(user);
                }
            }
        }

        return super.isAccessAllowed(request, response, mappedValue);
    }

    public boolean isUseSaveRequestToBeSuccessUrl()
    {
        return useSaveRequestToBeSuccessUrl;
    }

    public void setUseSaveRequestToBeSuccessUrl(boolean useSaveRequestToBeSuccessUrl)
    {
        this.useSaveRequestToBeSuccessUrl = useSaveRequestToBeSuccessUrl;
    }
}
