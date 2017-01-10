package com.meng.websocket.test;

import com.meng.chatonline.Param;
import com.meng.chatonline.model.User;
import com.meng.chatonline.model.security.Authority;
import com.meng.chatonline.model.security.Role;
import com.meng.chatonline.service.AuthorityService;
import com.meng.chatonline.service.RoleService;
import com.meng.chatonline.service.UserService;
import com.meng.chatonline.utils.SecurityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author xindemeng
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-beans.xml", "classpath:spring-shiro.xml"})
public class SecurityTest
{
    @Resource
    private AuthorityService authorityService;
    @Resource
    private RoleService roleService;
    @Resource
    private UserService userService;

    @Test
    public void testMd5()
    {
        String salt = SecurityUtils.generateSalt();
        System.out.println(salt);

        String md5 = SecurityUtils.md5Default("222", salt);
        System.out.println(md5);
    }

    @Test
    public void testCalculation()
    {
        System.out.println(2048%35);
    }

    @Test
    public void testGetAuthorities()
    {
        List<Authority> authorities = this.authorityService.getAuthoritiesByUserId(1);
        System.out.println(authorities);
    }

    @Test
    public void testSortAuthorities()
    {
        List<Authority> authorities = authorityService.findAllEntities();
        Collections.sort(authorities);
        System.out.println(authorities);
    }

    @Test
    public void testFindOwnAuthorities()
    {
        List<Authority> authorities = this.authorityService.findOwnAuthorities(1);
        System.out.println(authorities);
    }

    @Test
    public void testFindNotOwnAuthorities()
    {
        List<Authority> authorities = this.authorityService.findNotOwnAuthorities(1);
        System.out.println(authorities);
    }

    @Test
    public void testFindAllRolesWithAuthorities()
    {
        List<Role> roles = this.roleService.findAllRolesWithAuthorities();
        System.out.println(roles);
    }

    @Test
    public void testSaveRole()
    {
        Set<Authority> authorities = new HashSet<Authority>();
        authorities.add(new Authority(2));
        authorities.add(new Authority(3));
        authorities.add(new Authority(4));
        Role role = new Role("aaa", true);
        role.getAuthorities().addAll(authorities);
        this.roleService.saveEntity(role);
    }

    @Test
    public void testDeleteRole()
    {
        this.roleService.deleteRole(14);
    }

    @Test
    public void testFindAllUsers()
    {
        List<User> users = this.userService.findUsersWithRole();
        System.out.println(users);
    }

    @Test
    public void testFindOwnRoles()
    {
        String jpql = "from Role r where r.id in (" +
                "select r2.id from User u join u.roles r2 where u.id = ?)";
        List<Role> roles = this.roleService.findEntityByJPQL(jpql, 1);
        System.out.println(roles);
    }

    @Test
    public void testGetUser()
    {
        User role = this.userService.getEntity(1);
        System.out.println(role);
    }

    @Test
    public void testClearAuthorities()
    {
        this.userService.clearAuthorities(22);
    }

    @Test
    public void testDeleteAuthority()
    {
        this.authorityService.deleteAuthority(9);
    }

}
