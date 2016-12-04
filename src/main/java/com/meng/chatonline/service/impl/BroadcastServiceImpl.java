package com.meng.chatonline.service.impl;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.TriggersRemove;
import com.meng.chatonline.dao.BaseDao;
import com.meng.chatonline.model.Broadcast;
import com.meng.chatonline.service.BroadcastService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Author xindemeng
 */
@Service("broadcastService")
public class BroadcastServiceImpl extends BaseServiceImpl<Broadcast> implements BroadcastService
{
    //重写该方法，目的是覆盖超类中该方法的注解，指明注入的DAO对象，否则spring无法确定注入哪一个DAO
    @Resource(name="broadcastDao")
    @Override
    public void setDao(BaseDao<Broadcast> baseDao)
    {
        super.setDao(baseDao);
    }

    @Transactional
    public void saveBroadcast(Broadcast broadcast)
    {
        broadcast.setDate(new Date());
        this.saveEntity(broadcast);
    }

    //获得公告列表，时间越近越靠前
    @Cacheable(cacheName = "sampleCache1")
    @Transactional
    public List<Broadcast> getBroadcastList()
    {
        String jpql = "from Broadcast b order by b.date desc";
        List<Broadcast> list = this.findEntityByJPQL(jpql);
        return list;
    }

    //执行删除更新保存时清除缓存
    @TriggersRemove(cacheName="sampleCache1",removeAll=true)
    @Transactional
    public void deleteBroadcast(Integer id)
    {
        Broadcast b = this.getEntity(id);
        this.deleteEntity(b);
    }
}
