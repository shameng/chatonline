package com.meng.chatonline.security.filter;

import com.meng.chatonline.constant.Constants;
import com.meng.chatonline.exception.CaptchaException;
import com.meng.chatonline.security.captcha.JCaptchaUtils;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author xindemeng
 *
 * 验证码验证过滤器
 */
public class CaptchaValidationFilter extends AccessControlFilter
{
    private static final String DEFAULT_CAPTCHA_PARAM = "captcha";

    //是否开启验证码验证
    private boolean captchaEnabled = false;
    //前台提交的验证码参数
    private String captchaParam = DEFAULT_CAPTCHA_PARAM;

    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception
    {
        //1、设置验证码是否开启属性，页面可以根据该属性来决定是否显示验证码
        request.setAttribute("captchaEnabled", captchaEnabled);

        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        //2、判断验证码是否禁用 或 不是表单提交（允许访问）
        if (!captchaEnabled || !"post".equalsIgnoreCase(httpServletRequest.getMethod()))
            return true;

        //3、此时是表单提交，验证验证码是否正确
        return JCaptchaUtils.validateResponse(httpServletRequest,
                httpServletRequest.getParameter(getCaptchaParam()));
    }

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception
    {
        //如果验证失败，储存失败key属性
        CaptchaException captchaException = new CaptchaException();
        request.setAttribute(Constants.SHIRO_LOGIN_FAILURE, captchaException.toString());
        return true;
    }

    public boolean isCaptchaEnabled()
    {
        return captchaEnabled;
    }

    public void setCaptchaEnabled(boolean captchaEnabled)
    {
        this.captchaEnabled = captchaEnabled;
    }

    public String getCaptchaParam()
    {
        return captchaParam;
    }

    public void setCaptchaParam(String captchaParam)
    {
        this.captchaParam = captchaParam;
    }

}
