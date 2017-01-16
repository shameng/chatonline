package com.meng.websocket.test;

import com.meng.chatonline.model.User;
import com.meng.chatonline.service.AuthorityService;
import com.meng.chatonline.service.BroadcastService;
import com.meng.chatonline.service.RoleService;
import com.meng.chatonline.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xindemeng
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-beans.xml","classpath:spring-shiro.xml"})
public class CacheTest
{
    @Resource
    private BroadcastService broadcastService;
    @Resource
    private UserService userService;
    @Resource
    private AuthorityService authorityService;
    @Resource
    private RoleService roleService;

    @Test
    public void testCache1()
    {
        broadcastService.getBroadcastList();
        broadcastService.getBroadcastList();

//        broadcastService.saveBroadcast(new Broadcast(new User(1), "666", "666"));

        broadcastService.getBroadcastList();
    }

    @Test
    public void testUserCache1()
    {
        List<User> users = userService.findUsersWithRole();
        users = userService.findUsersWithRole();
    }

    @Test
    public void testUserCache2()
    {
        List<User> users = userService.findUsersWithRole();
        users = userService.findUsersWithRole();

        User user = new User();
        user.setName("qqq");
        user.setAccount("qqq");
        user.setPassword("qqq");
        userService.saveUser(user);
//        userService.deleteUser(34);

        users = userService.findUsersWithRole();
    }

    @Test
    public void testUserCache3()
    {
        List<User> users = userService.findUsersWithRole();
        users = userService.findUsersWithRole();

//        userService.deleteUser(35);

        users = userService.findUsersWithRole();
    }

    @Test
    public void testAuthorityCache()
    {
        authorityService.findAllAuthorities();
        authorityService.findAllAuthorities();
    }

    @Test
    public void testRoleCache1()
    {
        roleService.findAllRolesWithAuthorities();
        roleService.findAllRolesWithAuthorities();
    }
}
