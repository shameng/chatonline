package com.meng.websocket.test;

import com.meng.chatonline.model.oauth2.Client;
import com.meng.chatonline.service.oauth2.ClientService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author xindemeng
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-beans.xml","classpath:spring-shiro.xml"})
public class ClientTest
{
    @Resource
    private ClientService clientService;

    @Test
    public void testCreateClient()
    {
        Client client = new Client();
        client.setClientName("aaa");
//        Client newClient = clientService.createClient(client);
//        System.out.println(newClient);
    }
}
