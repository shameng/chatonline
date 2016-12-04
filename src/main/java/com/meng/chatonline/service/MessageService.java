package com.meng.chatonline.service;

import com.meng.chatonline.model.Message;

import java.util.List;

/**
 * @Author xindemeng
 */
public interface MessageService extends BaseService<Message>
{
    //获得与指定用户的聊天记录
    List<Message> getHistoryChatRecord(Integer toUserId, Integer myId);
}
