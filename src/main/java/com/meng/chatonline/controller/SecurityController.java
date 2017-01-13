package com.meng.chatonline.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author xindemeng
 */
@Controller
public class SecurityController
{
    @RequestMapping("/invalidSession")
    public String invalidSession()
    {
        return "error/invalidSession";
    }

}
