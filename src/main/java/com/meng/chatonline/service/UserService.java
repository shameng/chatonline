package com.meng.chatonline.service;

import com.meng.chatonline.model.User;

import java.util.List;

/**
 * @Author xindemeng
 */
public interface UserService extends BaseService<User>
{
    //登陆验证
    User checkLogin(User user);

    User getUserByName(String name);

    //检查用户名是否已经被注册
    User checkUserName(String name);

    User getUserByAccount(String account);
}
