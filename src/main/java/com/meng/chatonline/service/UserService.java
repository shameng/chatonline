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

    //保存用户，使用md5加密密码
    void saveUser(User user);

    //获得所有用户及其Role
    List<User> findUsersWithRole();

    //更新用户角色
    void updateUserRole(Integer userId, String[] ownRoleIds);

    //清除该用户的权限
    void clearAuthorities(Integer userId);

    void deleteUser(Integer userId);
}
