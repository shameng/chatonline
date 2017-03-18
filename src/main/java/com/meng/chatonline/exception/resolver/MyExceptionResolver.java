package com.meng.chatonline.exception.resolver;

import com.meng.chatonline.constant.Constants;
import com.meng.chatonline.exception.CaptchaException;
import com.meng.chatonline.exception.LoginException;
import com.meng.chatonline.exception.OAuth2LoginException;
import com.meng.chatonline.exception.RunAsException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author xindemeng
 *
 * 异常处理类
 */
@ControllerAdvice
public class MyExceptionResolver implements HandlerExceptionResolver
{
    public ModelAndView resolveException(HttpServletRequest httpServletRequest,
                        HttpServletResponse httpServletResponse, Object o, Exception e)
    {
        e.printStackTrace();

        ModelAndView mav = new ModelAndView();

        //如果是登陆异常
        if (e instanceof LoginException)
        {
            this.handleModelAndView(e, mav);
            mav.setViewName(((LoginException) e).VIEW_NAME);
        }
        //如果是OAuth2登陆异常
        else if (e instanceof OAuth2LoginException)
        {
            this.handleModelAndView(e, mav);
            mav.setViewName(((OAuth2LoginException) e).VIEW_NAME);
        }
        //如果是权限验证异常，例如没有权限
        else if (e instanceof UnauthorizedException)
            mav.setViewName("error/refuse");
        else if (e instanceof RunAsException)
        {
            mav.addObject(Constants.ERROR_MSG, "runAs状态下不能访问！");
            mav.setViewName("error/error");
        }
        //如果是未知错误
        else
        {
            mav.addObject(Constants.ERROR_MSG, "未知错误！");
            mav.setViewName("error/error");
        }
        return mav;
    }

    private void handleModelAndView(Exception e, ModelAndView mav)
    {
        if (e.getMessage().equals(CaptchaException.class.getName()))
            mav.addObject(Constants.ERROR_MSG, "验证码错误！");
        else if (e.getMessage().equals(UnknownAccountException.class.getName()))
            mav.addObject(Constants.ERROR_MSG, "用户名不存在！");
        else if (e.getMessage().equals(IncorrectCredentialsException.class.getName()))
            mav.addObject(Constants.ERROR_MSG, "用户名或密码错误！");
        else if (e.getMessage().equals(ExcessiveAttemptsException.class.getName()))
            mav.addObject(Constants.ERROR_MSG, "多次登陆失败，请稍后重试");
        else if (e.getMessage().equals(AuthenticationException.class.getName()))
            mav.addObject(Constants.ERROR_MSG, "登陆错误，请重试！");
    }
}
