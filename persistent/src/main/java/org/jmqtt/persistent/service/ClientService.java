package org.jmqtt.persistent.service;

import org.jmqtt.persistent.dao.ClientDao;
import org.jmqtt.persistent.entity.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Alex Liu
 * @date 2019/12/05
 */
@Service
public class ClientService {

    @Autowired
    private ClientDao clientDao;

    public void save(Client client){
        clientDao.save(client);
    }

    public Client findByClientId(String clientId){
        return clientDao.findByClientId(clientId);
    }

    public void disconnect(String clientId){
        Client client = findByClientId(clientId);
        if(client != null){
            client.setStatus(false);
            client.setLastDisconnectTime(new Date());
            clientDao.save(client);
        }
    }
}
