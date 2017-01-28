package com.meng.chatonline.controller;

import com.meng.chatonline.model.User;
import com.meng.chatonline.model.security.Role;
import com.meng.chatonline.service.RoleService;
import com.meng.chatonline.service.UserService;
import com.meng.chatonline.utils.CollectionUtils;
import com.meng.chatonline.utils.MySecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author xindemeng
 */
@Controller
@RequestMapping("/user")
public class UserController
{
    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;

    @RequiresPermissions("user:query")
    @RequestMapping({"", "/"})
    public String userList(Map<String, Object> map)
    {
        List<User> users = this.userService.findUsersWithRole();
        //把password和salt属性置为null
        users = MySecurityUtils.passwordAndSaltBeNull(users);
        map.put("users", users);
        return "security/userList";
    }

    @RequiresPermissions("user:updateRole")
    @RequestMapping(value="/editUserRole", method = RequestMethod.GET)
    public String editUserRole(@RequestParam(name="userId", required = true)Integer userId,
                               Map<String, Object> map)
    {
        map.put("user", userService.getEntity(userId));
        //属于该用户的角色
        List<Role> ownRoles = this.roleService.findOwnRoles(userId);
        CollectionUtils.sort(ownRoles);
        map.put("ownRoles", ownRoles);
        //不属于该用户的角色
        List<Role> notOwnRoles = this.roleService.findNotOwnRoles(userId);
        CollectionUtils.sort(notOwnRoles);
        map.put("notOwnRoles", notOwnRoles);
        return "security/editUserRole";
    }

    @RequiresPermissions("user:updateRole")
    @RequestMapping(value="/editUserRole", method = RequestMethod.POST)
    public String editUserRole(@RequestParam(name="userId",required = true)Integer userId,
                               @RequestParam(name="ownRoleIds",required = false)String[] ownRoleIds)
    {
        System.out.println(userId + "................." +ownRoleIds);
        //更新用户角色
        this.userService.updateUserRole(userId, ownRoleIds);
        return "redirect:/user";
    }

    @RequiresPermissions("user:clearAuthorities")
    @ResponseBody
    @RequestMapping("/clearAuthorities")
    public String clearAuthorities(@RequestParam(name="userId",required = true)Integer userId)
    {
        try
        {
            this.userService.clearAuthorities(userId);
            return "1";
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return "0";
        }
    }

    @RequiresPermissions("user:delete")
    @ResponseBody
    @RequestMapping("/deleteUser")
    public String deleteUser(@RequestParam(name="userId",required = true)Integer userId)
    {
        try
        {
            this.userService.deleteUser(userId);
            return "1";
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return "0";
        }
    }
}
