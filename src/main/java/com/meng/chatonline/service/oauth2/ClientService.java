package com.meng.chatonline.service.oauth2;

import com.meng.chatonline.model.oauth2.Client;
import com.meng.chatonline.service.BaseService;

/**
 * @author xindemeng
 */
public interface ClientService extends BaseService<Client>
{
    Client findByClientId(String clientId);

    Client findByClientSecret(String clientSecret);

    Client createClient(Client client);

    void updateClientName(Long cId, String clientName);

    void deleteClient(Long cId);
}
