package org.jmqtt.persistent.service;

import org.jmqtt.persistent.client.Client;
import org.jmqtt.persistent.req.PresentToCthulhuReq;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Alex Liu
 * @date 2020/01/08
 */
@Service
public class PresentService {

    private static final String CLIENT_DISCONNECTED= "";
    private static final String CLIENT_CONNECTED = "client_connected";

    @Value("${cthulhu.callback.url}")
    private String cthulhuCallbackUrl;

    @Value("${cthulhu.callback.username}")
    private String userName;

    public void disconnect(String clientId){

    }

    //将设备上线消息持久化到cthulhu
    public void connect(String clientId){
        PresentToCthulhuReq presentToCthulhuReq = PresentToCthulhuReq.build()
                .setClientId(clientId).setUsername(userName).setAction(CLIENT_CONNECTED);
        Client.build(cthulhuCallbackUrl , true).presentToCthulhu(presentToCthulhuReq);

    }
}
