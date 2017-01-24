package com.meng.chatonline.exception;

import com.meng.chatonline.Constants;
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
            if (e.getMessage().equals(UnknownAccountException.class.getName()))
                mav.addObject(Constants.ERROR_MSG, "用户名不存在！");
            else if (e.getMessage().equals(IncorrectCredentialsException.class.getName()))
                mav.addObject(Constants.ERROR_MSG, "用户名或密码错误！");

            mav.setViewName(((LoginException) e).getViewName());
        }
        //如果是权限验证异常，例如没有权限
        else if (e instanceof UnauthorizedException)
        {
            mav.setViewName("error/refuse");
        }
        //如果是未知错误
        else
        {
            mav.addObject(Constants.ERROR_MSG, "未知错误！");
            mav.setViewName("error/error");
        }
        return mav;
    }
}
