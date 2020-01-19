package org.jmqtt.persistent.service;

import org.jmqtt.persistent.model.page.SingleChannelDeviceResp;
import org.jmqtt.persistent.model.req.YunBaTicketReq;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    private final String USERNAME = "redtea";
    private final String PASSWORD = "redtea123";

    public SingleChannelDeviceResp servForSingleChannelDevice(YunBaTicketReq yunBaTicketReq){
        SingleChannelDeviceResp singleChannelDeviceResp = new SingleChannelDeviceResp();
        singleChannelDeviceResp.setU(USERNAME);
        singleChannelDeviceResp.setP(PASSWORD);
        singleChannelDeviceResp.setD(yunBaTicketReq.getD());
        if(yunBaTicketReq.getD().length() > 23){
            singleChannelDeviceResp.setC(
                    yunBaTicketReq.getD().substring(yunBaTicketReq.getD().length() - 24 , yunBaTicketReq.getD().length()-1));
        }else{
            singleChannelDeviceResp.setC(yunBaTicketReq.getD());

        }

        return singleChannelDeviceResp;
    }
}
