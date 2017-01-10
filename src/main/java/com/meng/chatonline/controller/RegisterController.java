package com.meng.chatonline.controller;

import com.meng.chatonline.model.User;
import com.meng.chatonline.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author xindemeng
 */
@Controller
@RequestMapping("/register")
public class RegisterController
{
    @Resource
    private UserService userService;

    @RequestMapping(value = {"","/"}, method = RequestMethod.GET)
    public String toRegister(Map<String, Object> map)
    {
        map.put("user", new User());
        return "register";
    }

    //检查用户名是否已经被注册
    @ResponseBody
    @RequestMapping("/checkUserName")
    public String checkUserName(@RequestParam(value = "name", required = true) String name)
    {
        User user = userService.checkUserName(name);
        return user==null? "0" : "1";
    }

    //检查账号是否已经被注册
    @ResponseBody
    @RequestMapping("/checkUserAccount")
    public String checkUserAccount(@RequestParam(value = "account", required = true) String account)
    {
        User user = userService.getUserByAccount(account);
        return user==null? "0" : "1";
    }

    @ResponseBody
    @RequestMapping(value = {"","/"}, method = RequestMethod.POST)
    public String register(User user)
    {
        System.out.println("--------------" + user + "-----------------------");
        try
        {
            this.userService.saveUser(user);
            return "1";
        } catch (Exception e)
        {
            return "0";
        }
    }
}
