package com.meng.chatonline.service.impl;

import com.meng.chatonline.dao.BaseDao;
import com.meng.chatonline.model.User;
import com.meng.chatonline.model.security.Role;
import com.meng.chatonline.security.MyRealm;
import com.meng.chatonline.service.RoleService;
import com.meng.chatonline.service.UserService;
import com.meng.chatonline.service.helper.PasswordHelper;
import com.meng.chatonline.utils.MySecurityUtils;
import com.meng.chatonline.utils.ValidationUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;

/**
 * @Author xindemeng
 */
@CacheConfig(cacheNames = "userCache")
@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService
{
    @Resource
    private RoleService roleService;
    @Resource
    private MyRealm realm;
    @Resource
    private PasswordHelper passwordHelper;

    //重写该方法，目的是覆盖超类中该方法的注解，指明注入的DAO对象，否则spring无法确定注入哪一个DAO
    @Resource(name="userDao")
    @Override
    public void setDao(BaseDao<User> baseDao)
    {
        super.setDao(baseDao);
    }

    //登陆验证
    @Transactional
    public User checkLogin(User user)
    {
        User correctUser = this.getUserByName(user.getName());
        if(correctUser != null)
        {
            if(correctUser.getPassword().equals(user.getPassword()))
                return correctUser;
        }
        return null;
    }

    @Transactional
    public User getUserByName(String name)
    {
        String jpql = "from User u where u.name = ?";
        List<User> list = this.findEntityByJPQL(jpql, name);
        if(ValidationUtils.validateCollection(list))
            return list.get(0);
        return null;
    }

    @Transactional
    //检查用户名是否已经被注册
    public User checkUserName(String name)
    {
        return this.getUserByName(name);
    }

    @Transactional
    public User getUserByAccount(String account)
    {
        String jpql = "from User u where u.account = ?";
        List<User> list = this.findEntityByJPQL(jpql, account);
        return ValidationUtils.validateCollection(list) ? list.get(0) : null;
    }

    @CacheEvict(allEntries = true)
    @Transactional
    //保存用户
    public void saveUser(User user)
    {
        System.out.println("------------------新建用户----------------------");
        //分配salt和加密密码
        passwordHelper.encryptPassword(user);

        //分配公有角色
        List<Role> commonRoles = this.roleService.findCommonRoles();
        user.setRoles(new HashSet<Role>(commonRoles));

        this.saveEntity(user);
    }

    @Cacheable
    @Transactional
    //获得所有用户及其Role，超级管理员排在前面
    public List<User> findUsersWithRole()
    {
        System.out.println("------------------查询所有用户----------------------");
        String jpql = "select DISTINCT u from User u left join fetch u.roles r " +
                "left join fetch r.authorities order by u.superAdmin desc";
        List<User> users = this.findEntityByJPQL(jpql);
        return users;
    }

    @CacheEvict(allEntries = true)
    @Transactional
    //更新用户角色
    public void updateUserRole(Integer userId, String[] ownRoleIds)
    {
        User user = this.getEntity(userId);
        if (ValidationUtils.validateArray(ownRoleIds))
        {
            List<Role> roles = this.roleService.findRolesInRange(ownRoleIds);
            user.setRoles(new HashSet<Role>(roles));
        }
        else
            user.setRoles(null);
        this.updateEntity(user);

        //更新完权限以后要清除系统的权限缓存
        realm.clearCachedAuthorizationInfo(userId);
    }

    @CacheEvict(allEntries = true)
    @Transactional
    //清除该用户的权限
    public void clearAuthorities(Integer userId)
    {
        User user = this.getEntity(userId);
        user.setRoles(null);

        //清除完权限以后要清除系统的权限缓存
        realm.clearCachedAuthorizationInfo(userId);
    }

    @CacheEvict(allEntries = true)
    @Transactional
    public void deleteUser(Integer userId)
    {
        //先删除User和Role关联表的相关记录
        String sql = "delete from user_role where user_id = ?";
        this.executeSql(sql, userId);

        //再删除User表
        String sql2 = "delete from users where id = ?";
        this.executeSql(sql2, userId);

        //清除系统的权限缓存
        realm.clearCachedAuthorizationInfo(userId);
    }

}
