package com.meng.websocket.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.meng.chatonline.model.Message;
import com.meng.chatonline.model.User;
import org.junit.Test;

import java.util.Date;

/**
 * @Author xindemeng
 */
public class GsonTest
{
    @Test
    public void testToJson()
    {
        Message message = new Message();
        message.setId(1);
        message.setDate(new Date());
        message.setText("lalalal");

        User toUser = new User();
        toUser.setId(1);
        User fromUser = new User();
        fromUser.setId(2);

        message.setToUser(toUser);
        message.setFromUser(fromUser);

        String json = new GsonBuilder().create().toJson(message);
        System.out.println(json);
    }

    @Test
    public void testFromJson()
    {
        String json = "{'id':1,'toUser':{'id':1},'fromUser':{'id':2},'text':'lalalal','date':'Nov 7, 2016 3:10:18 PM'}";
        Message message = new Gson().fromJson(json, Message.class);
        System.out.println(message);

//        String json2 = "{'id':1,'toUser.id:1,'fromUser.id:2,'text':'lalalal','date':'Nov 7, 2016 3:10:18 PM'}";
//        Message message2 = new Gson().fromJson(json2, Message.class);
//        System.out.println(message2);
    }

}
