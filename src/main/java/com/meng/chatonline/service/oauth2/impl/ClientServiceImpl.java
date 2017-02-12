package com.meng.chatonline.service.oauth2.impl;

import com.meng.chatonline.dao.BaseDao;
import com.meng.chatonline.model.oauth2.Client;
import com.meng.chatonline.service.impl.BaseServiceImpl;
import com.meng.chatonline.service.oauth2.ClientService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * @Author xindemeng
 */
@Service("clientService")
public class ClientServiceImpl extends BaseServiceImpl<Client> implements ClientService
{
    //重写该方法，目的是覆盖超类中该方法的注解，指明注入的DAO对象，否则spring无法确定注入哪一个DAO
    @Resource(name="clientDao")
    @Override
    public void setDao(BaseDao<Client> baseDao)
    {
        super.setDao(baseDao);
    }

    public Client findByClientId(String clientId)
    {
        String jpql = "from Client c where c.clientId = ?";
        List<Client> clients = this.findEntityByJPQL(jpql, clientId);
        return CollectionUtils.isEmpty(clients)? null: clients.get(0);
    }

    public Client findByClientSecret(String clientSecret)
    {
        String jpql = "from Client c where c.clientSecret = ?";
        return (Client) this.uniqueResult(jpql, clientSecret);
    }

    @Transactional
    public Client createClient(Client client)
    {
        client.setClientId(UUID.randomUUID().toString());
        client.setClientSecret(UUID.randomUUID().toString());
        return this.saveOrUpdateEntity(client);
    }

    @Transactional
    public void updateClientName(Long cId, String clientName)
    {
        String jpql = "update Client c set c.clientName = ? where c.id = ?";
        this.BatchEntityByJPQL(jpql, clientName, cId);
    }

    @Transactional
    public void deleteClient(Long cId)
    {
        String jpql = "delete from Client c where c.id = ?";
        this.BatchEntityByJPQL(jpql, cId);
    }
}
