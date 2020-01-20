package org.jmqtt.persistent.service;

import org.apache.commons.lang.RandomStringUtils;
import org.jmqtt.persistent.model.page.SingleChannelDeviceResp;
import org.jmqtt.persistent.model.req.YunBaTicketReq;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    private final String USERNAME = "3217011410455896576";
    private final String PASSWORD = "21b9c374e1d39";

    public SingleChannelDeviceResp servForSingleChannelDevice(YunBaTicketReq yunBaTicketReq){
        SingleChannelDeviceResp singleChannelDeviceResp = new SingleChannelDeviceResp();
        singleChannelDeviceResp.setU(USERNAME);
        singleChannelDeviceResp.setP(PASSWORD);
        singleChannelDeviceResp.setD(yunBaTicketReq.getD());
        if(yunBaTicketReq.getD().length() > 23){
            singleChannelDeviceResp.setC(
                    yunBaTicketReq.getD().substring(yunBaTicketReq.getD().length() - 23 , yunBaTicketReq.getD().length()));
        }else if(yunBaTicketReq.getD().length() < 23){
            int padding = 23 - yunBaTicketReq.getD().length();
            String paddingString = RandomStringUtils.randomAlphabetic(padding);
            singleChannelDeviceResp.setC(yunBaTicketReq.getD() + paddingString);
        }else{
            singleChannelDeviceResp.setC(yunBaTicketReq.getD());

        }

        return singleChannelDeviceResp;
    }
}
