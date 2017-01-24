package com.meng.chatonline.controller;

import com.meng.chatonline.Constants;
import com.meng.chatonline.exception.LoginException;
import com.meng.chatonline.utils.ValidationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author xindemeng
 */
@Controller
public class LoginController
{
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login()
    {
        return "login";
    }

    //使用了shiro框架以后，只有登陆验证有异常（或者没有注销登录但前往登录页面登录用户???）时才会调用该方法
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String doLogin(HttpServletRequest request) throws Exception
    {
        String shiroLoginFailure = (String) request.getAttribute(Constants.SHIRO_LOGIN_FAILURE);
        if (ValidationUtils.validateStr(shiroLoginFailure))
        {
            System.out.println("---------------------" + shiroLoginFailure + "----------------");
            throw new LoginException(shiroLoginFailure);
        }
        return "redirect:/login";
    }
}