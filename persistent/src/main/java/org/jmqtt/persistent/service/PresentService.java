package org.jmqtt.persistent.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

    private static final String CLIENT_DISCONNECTED= "client_disconnected";
    private static final String CLIENT_CONNECTED = "client_connected";
    public final static String CLIENT_SUBSCRIBE = "client_subscribe";
    public final static String CLIENT_UNSUBSCRIBE = "client_unsubscribe";

    @Value("${cthulhu.callback.url}")
    private String cthulhuCallbackUrl;

    @Value("${cthulhu.callback.username}")
    private String userName;

    public void disconnect(String clientId , String reason){
        PresentToCthulhuReq presentToCthulhuReq = PresentToCthulhuReq.build()
                .setClientId(clientId).setUsername(userName).setAction(CLIENT_DISCONNECTED).setReason(reason);
        Client.build(cthulhuCallbackUrl , true).presentToCthulhu(presentToCthulhuReq);
    }

    //将设备上线消息持久化到cthulhu
    public void connect(String clientId){
        PresentToCthulhuReq presentToCthulhuReq = PresentToCthulhuReq.build()
                .setClientId(clientId).setUsername(userName).setAction(CLIENT_CONNECTED);
        Client.build(cthulhuCallbackUrl , true).presentToCthulhu(presentToCthulhuReq);

    }

    public void subscribe(String topic , String clientId){
        PresentToCthulhuReq presentToCthulhuReq = PresentToCthulhuReq.build()
                .setClientId(clientId).setUsername(userName).setAction(CLIENT_SUBSCRIBE).setTopic(topic);
        Client.build(cthulhuCallbackUrl , true).presentToCthulhu(presentToCthulhuReq);
    }

    public void unsubscribe(String topic , String clientId){
        PresentToCthulhuReq presentToCthulhuReq = PresentToCthulhuReq.build()
                .setClientId(clientId).setUsername(userName).setAction(CLIENT_UNSUBSCRIBE).setTopic(topic);
        Client.build(cthulhuCallbackUrl , true).presentToCthulhu(presentToCthulhuReq);
    }
}
