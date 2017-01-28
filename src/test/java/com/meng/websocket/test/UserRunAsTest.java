package com.meng.websocket.test;

import com.meng.chatonline.model.User;
import com.meng.chatonline.service.UserRunAsService;
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
@ContextConfiguration({"classpath:spring-beans.xml", "classpath:spring-shiro.xml"})
public class UserRunAsTest
{
    @Resource
    private UserRunAsService userRunAsService;

    @Test
    public void testFindGrantedUsers()
    {
        List<User> users = this.userRunAsService.findGrantedUsersByMySelf(new User(1));
        System.out.println(users);
    }

    @Test
    public void testFindGrantedUsersByOthers()
    {
        List<User> users = this.userRunAsService.findGrantedUsersByOthers(new User(36));
        System.out.println(users);
    }

    @Test
    public void testExistRunAs()
    {
        boolean exist = this.userRunAsService.existRunAs(1, 2);
        System.out.println(exist);
    }

}
