package org.jmqtt.persistent;

import org.jmqtt.persistent.entity.Client;
import org.jmqtt.persistent.service.ClientService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
@Rollback(false)
class PersistentApplicationTests {

    @Autowired
    private ClientService clientService;

    @Test
    void contextLoads() {
    }

    @Test
    public void test(){
        /*Client client = Client.builder().clientId("lthtest").build();
        clientService.save(client);*/
        Client client = clientService.findByClientId("lthtest");
        System.out.println(client);
    }



}
