package com.meng.chatonline.service;

import com.meng.chatonline.model.ActiveUser;
import com.meng.chatonline.model.Broadcast;

import java.util.List;

/**
 * @Author xindemeng
 */
public interface BroadcastService extends BaseService<Broadcast>
{
    void saveBroadcast(Broadcast broadcast);

    //获得公告列表，时间越近越靠前
    List<Broadcast> getBroadcastList();

    void deleteBroadcast(Integer id);

    /**
     * 该公告是否是由指定用户发布的
     * @param id 公告id
     * @param user 指定用户
     * @return
     */
    boolean belong(Integer id, ActiveUser user);
}
