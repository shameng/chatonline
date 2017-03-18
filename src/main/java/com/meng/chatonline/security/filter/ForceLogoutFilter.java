package com.meng.chatonline.security.filter;

import com.meng.chatonline.constant.Constants;
import org.apache.shiro.session.Session;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author xindemeng
 *
 * 强制退出过滤器
 */
public class ForceLogoutFilter extends AccessControlFilter
{
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception
    {
        Session session = getSubject(request, response).getSession(false);
        if (session == null)
            return true;

        return session.getAttribute(Constants.SESSION_FORCE_LOGOUT) == null;
    }

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception
    {
        //强制退出
        getSubject(request, response).logout();
        String loginUrl = getLoginUrl() + (getLoginUrl().contains("?")? "&": "?") + "forceLogout=1";
        WebUtils.issueRedirect(request, response, loginUrl);

        return false;
    }
}
