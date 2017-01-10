package com.meng.websocket.test;

import com.meng.chatonline.model.User;
import com.meng.chatonline.model.security.Role;
import com.meng.chatonline.service.AuthorityService;
import com.meng.chatonline.service.RoleService;
import com.meng.chatonline.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author xindemeng
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-beans.xml","classpath:spring-shiro.xml"})
public class JPATest
{

    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;

    @Test
    public void testSave()
    {
        User user = new User("test", "333");
        userService.saveEntity(user);
    }

    @Test
    public void testUpdate()
    {
        User user = userService.getEntity(6);
        user.setPassword("888");
        userService.updateEntity(user);

        user.setPassword("777");
        userService.saveOrUpdateEntity(user);
    }
    
    @Test
    public void testQueryByHql()
    {
        String hql = "from User user where user.id > ? and user.name = ?";
        List<User> users = userService.findEntityByJPQL(hql, 5, "test");
        System.out.println(users.size());
    }

    @Test
    public void testNativeQuery()
    {
        String sql = "select * from users where id > ?";
        List<User> users = userService.executeSQLQuery(User.class, sql, 5);
        System.out.println(users);
    }

    @Test
    public void testFindAllRolesWithAuthorities()
    {
        List<Role> roles = this.roleService.findAllRolesWithAuthorities();
        System.out.println(roles);
    }

    @Test
    public void testGetUsersBySQL()
    {
        String sql = "select id from users";
        List ids = this.userService.executeSQLQuery(null, sql);
        System.out.println(ids);
    }

}
