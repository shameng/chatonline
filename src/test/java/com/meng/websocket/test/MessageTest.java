package com.meng.websocket.test;

import com.meng.chatonline.model.Message;
import com.meng.chatonline.service.MessageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author xindemeng
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-beans.xml","classpath:spring-shiro.xml"})
public class MessageTest
{
    @Resource
    private MessageService messageService;

    @Test
    public void testHistoryMessage()
    {
        List<Message> messages = messageService.getHistoryChatRecord(2, 32);
        System.out.println(messages);
    }
}
