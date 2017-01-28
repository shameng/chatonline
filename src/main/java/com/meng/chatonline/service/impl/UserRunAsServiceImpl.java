package com.meng.chatonline.service.impl;

import com.meng.chatonline.dao.BaseDao;
import com.meng.chatonline.model.User;
import com.meng.chatonline.model.UserRunAs;
import com.meng.chatonline.service.UserRunAsService;
import com.meng.chatonline.service.UserService;
import com.meng.chatonline.utils.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author xindemeng
 */
@Service("userRunAsService")
public class UserRunAsServiceImpl extends BaseServiceImpl<UserRunAs> implements UserRunAsService
{
    @Resource
    private UserService userService;

    @Resource(name = "userRunAsDao")
    @Override
    public void setDao(BaseDao<UserRunAs> baseDao)
    {
        super.setDao(baseDao);
    }

    //获得被授予该用户身份的用户
    public List<User> findGrantedUsersByMySelf(User loginUser)
    {
        String jpql = "select new User(u.id,u.account,u.name) from User u where u.id in (" +
                "select ra.toUser.id from UserRunAs ra where ra.fromUser.id = ?)";
        List<User> users = this.userService.findEntityByJPQL(jpql, loginUser.getId());
        return users;
    }

    /**
     * 获得可授予该用户身份的用户
     *
     * @param grantedUsersByMyself 已被本用户授予的用户
     * @param loginUser    本用户
     * @return
     */
    @Transactional
    public List<User> findCanBeGrantedUserByMySelf(List<User> grantedUsersByMyself, User loginUser)
    {
        List<Integer> ids = new ArrayList<Integer>();
        ids.add(loginUser.getId());
        for (User user : grantedUsersByMyself)
        {
            ids.add(user.getId());
        }
        String idsStr = CollectionUtils.collToStr(ids);

        String jpql = "select new User(u.id, u.account, u.name) from User u " +
                "where u.id not in ("+idsStr+")";
        List<User> users = this.userService.findEntityByJPQL(jpql);
        return users;
    }

    /**
     * 获得该用户被授予的身份
     *
     * @param loginUser 本用户
     * @return
     */
    @Transactional
    public List<User> findGrantedUsersByOthers(User loginUser)
    {
        String jpql = "select new User(u.id, u.account, u.name) from User u where u.id in (" +
                "select ra.fromUser.id from UserRunAs ra where ra.toUser.id = ?)";
        List<User> users = this.userService.findEntityByJPQL(jpql, loginUser.getId());
        return users;
    }

    /**
     * 授予用户身份
     *
     * @param fromUserId 授予身份者
     * @param toUserId   接收身份者
     */
    @Transactional
    public void grantRunAs(Integer fromUserId, Integer toUserId)
    {
        String sql = "insert into user_runas(from_user_id, to_user_id) values(?, ?)";
        this.executeSql(sql, fromUserId, toUserId);
    }

    /**
     * 是否存在该身份授予关系
     *
     * @param fromUserId
     * @param toUserId
     * @return
     */
    @Transactional
    public boolean existRunAs(Integer fromUserId, Integer toUserId)
    {
        String sql = "select count(*) from user_runas where from_user_id = ? and to_user_id = ?";
        BigInteger count = (BigInteger) this.executeSQLQuery(null, sql, fromUserId, toUserId).get(0);
        return count.longValue() > 0;
    }

    /**
     * 回收用户身份
     *
     * @param fromUserId 授予身份者
     * @param toUserId   接收身份者
     */
    @Transactional
    public void revokeRunAs(Integer fromUserId, Integer toUserId)
    {
        String sql = "delete from user_runas where from_user_id = ? and to_user_id = ?";
        this.executeSql(sql, fromUserId, toUserId);
    }

}
