package com.meng.chatonline.service;

import com.meng.chatonline.model.User;
import com.meng.chatonline.model.UserRunAs;

import java.util.List;

/**
 * @Author xindemeng
 */
public interface UserRunAsService extends BaseService<UserRunAs>
{

    //获得被授予该用户身份的用户
    List<User> findGrantedUsersByMySelf(User loginUser);

    /**
     * 获得可授予该用户身份的用户
     * @param grantedUsersByMyself 已被本用户授予的用户
     * @param loginUser 本用户
     * @return
     */
    List<User> findCanBeGrantedUserByMySelf(List<User> grantedUsersByMyself, User loginUser);

    /**
     * 获得该用户被授予的身份
     * @param loginUser 本用户
     * @return
     */
    List<User> findGrantedUsersByOthers(User loginUser);

    /**
     * 授予用户身份
     * @param fromUserId 授予身份者
     * @param toUserId 接收身份者
     */
    void grantRunAs(Integer fromUserId, Integer toUserId);

    /**
     * 是否存在该身份授予关系
     * @param fromUserId 授予身份者
     * @param toUserId 接收身份者
     * @return
     */
    boolean existRunAs(Integer fromUserId, Integer toUserId);

    /**
     * 回收用户身份
     * @param fromUserId 授予身份者
     * @param toUserId 接收身份者
     */
    void revokeRunAs(Integer fromUserId, Integer toUserId);
}
