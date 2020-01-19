package org.jmqtt.controller.controller;

import com.google.common.base.Strings;
import org.jmqtt.persistent.model.page.SingleChannelDeviceResp;
import org.jmqtt.persistent.model.req.YunBaTicketReq;
import org.jmqtt.persistent.service.TicketService;
import org.jmqtt.persistent.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 模拟替换yunba的TicketServer
 *
 * @author Alex Liu
 * @date 2020/01/16
 */
@RestController
@RequestMapping("/device")
public class SingleChannelDeviceController extends BaseController{

    @Autowired
    private TicketService ticketService;

    @PostMapping("/reg")
    public ResponseEntity ticketForSingleChannel(@RequestBody String body) throws Exception{
        if (Strings.isNullOrEmpty(body)) {
            throw new Exception("请求体中没有任何数据");
        }
        logger.info("received request from single channel device to get ticket body is {}", body);
        SingleChannelDeviceResp singleChannelDeviceResp = ticketService.servForSingleChannelDevice(JsonUtils.parseJsonString(body, YunBaTicketReq.class));
        return ResponseEntity.ok(singleChannelDeviceResp);
    }
}
