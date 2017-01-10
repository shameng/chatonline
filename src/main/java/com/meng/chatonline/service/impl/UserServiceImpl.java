package com.meng.chatonline.service.impl;

import com.meng.chatonline.dao.BaseDao;
import com.meng.chatonline.model.User;
import com.meng.chatonline.model.security.Role;
import com.meng.chatonline.security.MyRealm;
import com.meng.chatonline.service.RoleService;
import com.meng.chatonline.service.UserService;
import com.meng.chatonline.utils.SecurityUtils;
import com.meng.chatonline.utils.ValidationUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;

/**
 * @Author xindemeng
 */
@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService
{
    @Resource
    private RoleService roleService;
    @Resource
    private MyRealm realm;

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

    @Transactional
    //保存用户，使用md5加密密码
    public void saveUser(User user)
    {
        String salt = SecurityUtils.generateSalt();
        user.setSalt(salt);
        user.setPassword(SecurityUtils.md5Default(user.getPassword(), salt));

        //分配公有角色
        String jpql = "from Role r where r.common = ?";
        List<Role> roles = this.roleService.findEntityByJPQL(jpql, true);
        user.setRoles(new HashSet<Role>(roles));

        this.saveEntity(user);
    }

    @Transactional
    //获得所有用户及其Role
    public List<User> findUsersWithRole()
    {
        String jpql = "select DISTINCT u from User u left join fetch u.roles r left join fetch r.authorities";
        List<User> users = this.findEntityByJPQL(jpql);
        return users;
    }

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

    @Transactional
    //清除该用户的权限
    public void clearAuthorities(Integer userId)
    {
        User user = this.getEntity(userId);
        user.setRoles(null);

        //清除完权限以后要清除系统的权限缓存
        realm.clearCachedAuthorizationInfo(userId);
    }

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
