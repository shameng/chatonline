package com.meng.chatonline.service.impl;

import com.meng.chatonline.dao.BaseDao;
import com.meng.chatonline.model.User;
import com.meng.chatonline.service.UserService;
import com.meng.chatonline.utils.ValidationUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author xindemeng
 */
@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService
{
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

    //检查用户名是否已经被注册
    public User checkUserName(String name)
    {
        return this.getUserByName(name);
    }

    public User getUserByAccount(String account)
    {
        String jpql = "from User u where u.account = ?";
        List<User> list = this.findEntityByJPQL(jpql, account);
        return ValidationUtils.validateCollection(list) ? list.get(0) : null;
    }
}
