package com.meng.chatonline.service.impl;

import com.meng.chatonline.dao.BaseDao;
import com.meng.chatonline.model.ActiveUser;
import com.meng.chatonline.model.Message;
import com.meng.chatonline.service.MessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author xindemeng
 */
@Service("messageService")
public class MessageServiceImpl extends BaseServiceImpl<Message> implements MessageService
{
    //重写该方法，目的是覆盖超类中该方法的注解，指明注入的DAO对象，否则spring无法确定注入哪一个DAO
    @Resource(name="messageDao")
    @Override
    public void setDao(BaseDao<Message> baseDao)
    {
        super.setDao(baseDao);
    }

    //获得与指定用户的聊天记录
    @Transactional
    public List<Message> getHistoryChatRecord(Integer toUserId, Integer myId)
    {
        String jpql = "select m from Message m join m.toUser join fetch m.fromUser where (m.toUser.id = ? and m.fromUser.id = ?)" +
                " or (m.toUser.id = ? and m.fromUser.id = ?)" +
                " order by m.date desc";
        List<Message> chatRecord = this.findEntityByJPQL(jpql, toUserId, myId, myId, toUserId);
        for (Message m : chatRecord)
        {
            m.setToUser(new ActiveUser(m.getToUser().getId(),m.getToUser().getAccount(),m.getToUser().getName()));
            m.setFromUser(new ActiveUser(m.getFromUser().getId(),m.getFromUser().getAccount(),m.getFromUser().getName()));
        }
        return chatRecord;
    }
}
