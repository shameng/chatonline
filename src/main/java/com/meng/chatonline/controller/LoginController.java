package com.meng.chatonline.controller;

import com.meng.chatonline.model.User;
import com.meng.chatonline.service.UserService;
import com.meng.chatonline.utils.ValidationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Author xindemeng
 */
@Controller
public class LoginController
{
    @Resource
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Map<String, Object> map)
    {
        map.put("user", new User());
        return "login";
    }

    //使用了shiro框架以后，只有登陆验证有异常时才会调用该方法
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String doLogin(HttpServletRequest request)
    {
        String shiroLoginFailure = (String) request.getAttribute("shiroLoginFailure");
        if (ValidationUtils.validateStr(shiroLoginFailure))
        {
            System.out.println("---------------------" + shiroLoginFailure + "----------------");
            return "redirect:/login";
        }
        return "redirect:/chatonline";
    }

}