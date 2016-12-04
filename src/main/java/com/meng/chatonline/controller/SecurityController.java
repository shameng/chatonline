package com.meng.chatonline.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author xindemeng
 */
@Controller
public class SecurityController
{
    //没有权限返回拒绝访问页面
    @RequestMapping("/refuse")
    public String refuse()
    {
        return "refuse";
    }
}
