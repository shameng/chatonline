package com.meng.chatonline.controller;

import com.meng.chatonline.constant.Constants;
import com.meng.chatonline.model.security.Authority;
import com.meng.chatonline.service.AuthorityService;
import com.meng.chatonline.utils.MySecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @Author xindemeng
 */
@Controller
@RequestMapping("/authority")
public class AuthorityController
{
    @Resource
    private AuthorityService authorityService;

    @RequiresPermissions("authority:query")
    @RequestMapping({"","/"})
    public String authorityList(Map<String, Object> map)
    {
        List<Authority> authorities = this.authorityService.findAllAuthorities();
        map.put("authorities", MySecurityUtils.getAuthoritiesSortedByMenu(authorities));
        return "security/authorityList";
    }

    @RequiresPermissions("authority:new")
    @RequestMapping("/newAuthority")
    public String newAuthority(Map<String, Object> map)
    {
        map.put("authority", new Authority());
        map.put("menus", authorityService.findMenuAuthorities());
        return "security/editAuthority";
    }

    @RequiresPermissions("authority:update")
    @RequestMapping(value="/editAuthority", method=RequestMethod.GET)
    public String editAuthority(Map<String, Object> map,
                           @RequestParam(name="authId", required = true) Integer authId)
    {
        map.put("authority", authorityService.getEntity(authId));
        map.put("menus", authorityService.findMenuAuthorities());
        return "security/editAuthority";
    }

    @RequiresPermissions(value={"authority:new","authority:update"}, logical = Logical.OR)
    @RequestMapping(value="/editAuthority", method = RequestMethod.POST)
    public String editAuthority(@Valid Authority authority, BindingResult result, Map<String, Object> map)
    {
        if (result.getFieldErrorCount() > 0)
        {
            map.put("menus", authorityService.findMenuAuthorities());
            return "security/editAuthority";
        }

        System.out.println(authority);
        if (authority.getType() == Constants.MENU_TYPE)
        {
            authority.setMenu(null);
            authority.setAvailable(true);
            authority.setCommon(false);
        }
        authorityService.saveOrUpdateAuthority(authority);
        return "redirect:/authority";
    }

    @RequiresPermissions("authority:delete")
    @ResponseBody
    @RequestMapping("deleteAuthority")
    public String deleteAuthority(@RequestParam(name = "authId", required = true) Integer authId)
    {
        try
        {
            authorityService.deleteAuthority(authId);
            return "1";
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return "0";
        }
    }

}
