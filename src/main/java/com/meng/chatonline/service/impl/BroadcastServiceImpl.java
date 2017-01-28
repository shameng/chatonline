package com.meng.chatonline.service.impl;

import com.meng.chatonline.dao.BaseDao;
import com.meng.chatonline.model.ActiveUser;
import com.meng.chatonline.model.Broadcast;
import com.meng.chatonline.service.BroadcastService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Author xindemeng
 */
//这里定义了之后就不必在下面的每个方法上写 cacheNames="broadcastCache" 了
@CacheConfig(cacheNames = "broadcastCache")
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

    //执行删除更新保存时清除全部缓存
    @CacheEvict(allEntries = true)
//    @TriggersRemove(cacheName="sampleCache1",removeAll=true)
    @Transactional
    public void saveBroadcast(Broadcast broadcast)
    {
//        System.out.println("保存公告");
        broadcast.setDate(new Date());
        this.saveEntity(broadcast);
    }

    //获得公告列表，时间越近越靠前
    @Cacheable
//    @Cacheable(cacheName = "sampleCache1")
    @Transactional
    public List<Broadcast> getBroadcastList()
    {
//        System.out.println("查询公告");
        String jpql = "from Broadcast b order by b.date desc";
        List<Broadcast> list = this.findEntityByJPQL(jpql);
        return list;
    }

    /*执行删除更新保存时清除全部缓存
        beforeInvocation：是否在方法执行前就清空，缺省为 false
     */
    @CacheEvict(allEntries = true, beforeInvocation = false)
//    @TriggersRemove(cacheName="sampleCache1",removeAll=true)
    @Transactional
    public void deleteBroadcast(Integer id)
    {
//        System.out.println("删除公告");
        Broadcast b = this.getEntity(id);
        this.deleteEntity(b);
    }

    /**
     * 该公告是否是由指定用户发布的
     *
     * @param id   公告id
     * @param user 指定用户
     * @return
     */
    @Transactional
    public boolean belong(Integer id, ActiveUser user)
    {
        Broadcast broadcast = this.getEntity(id);
        return broadcast.getUtterer().getId().equals(user.getId());
    }

}
