package com.meng.chatonline.controller;

import com.meng.chatonline.model.security.Authority;
import com.meng.chatonline.model.security.Role;
import com.meng.chatonline.service.AuthorityService;
import com.meng.chatonline.service.RoleService;
import com.meng.chatonline.utils.CollectionUtils;
import org.apache.shiro.authz.annotation.Logical;
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
@RequestMapping("/role")
public class RoleController
{
    @Resource
    private RoleService roleService;
    @Resource
    private AuthorityService authorityService;

    @RequiresPermissions("role:query")
    @RequestMapping({"","/"})
    public String roleList(Map<String, Object> map)
    {
        map.put("roles", this.roleService.findAllRolesWithAuthorities());
        return "security/roleList";
    }

    @RequiresPermissions("role:new")
    @RequestMapping("/newRole")
    public String newRole(Map<String, Object> map)
    {
        //获得所有权限类型的权限
        List<Authority> authorities = this.authorityService.findAuthAuthorities();
        CollectionUtils.sort(authorities);
        map.put("notOwnAuthorities", authorities);
        map.put("role", new Role());
        return "security/editRole";
    }

    @RequiresPermissions("role:update")
    @RequestMapping(value="/editRole", method = RequestMethod.GET)
    public String editRole(@RequestParam(name="roleId", required = true) Integer roleId,
                           Map<String, Object> map)
    {
        Role role = roleService.getEntity(roleId);
        map.put("role", role);
        //获得属于该角色的Authority，只包含权限类型的
        List<Authority> ownAuthorities = this.authorityService.findOwnAuthorities(roleId);
        //根据菜单来排序
        CollectionUtils.sort(ownAuthorities);
        map.put("ownAuthorities", ownAuthorities);

        //获得不属于该角色的Authority，只包含权限类型的
        List<Authority> notOwnAuthorities = this.authorityService.findNotOwnAuthorities(roleId);
        //根据菜单排序
        CollectionUtils.sort(notOwnAuthorities);
        map.put("notOwnAuthorities", notOwnAuthorities);
        return "security/editRole";
    }

    @RequiresPermissions(value={"role:new","role:update"}, logical = Logical.OR)
    @RequestMapping(value="/editRole", method = RequestMethod.POST)
    public String editRole(@RequestParam(name="ownAuthIds", required = false) String[] ownAuthIds,
                           Role role)
    {
        System.out.println(role);
        this.roleService.saveOrUpdateRole(role, ownAuthIds);
        return "redirect:/role";
    }

    @RequiresPermissions("role:delete")
    @ResponseBody
    @RequestMapping("/deleteRole")
    public String deleteRole(@RequestParam(name="roleId", required = true) Integer roleId)
    {
        try
        {
            this.roleService.deleteRole(roleId);
            return "1";
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return "0";
        }
    }
}
