package org.jmqtt.controller.controller;

import org.jmqtt.broker.YunbaMessageUtil;
import org.jmqtt.controller.model.req.PublishReq;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Alex Liu
 * @date 2019/12/07
 */
@RestController
@RequestMapping("/api/v1")
public class PublishController {

    @PostMapping("/publish")
    public ResponseEntity pubish(@Valid @RequestBody PublishReq request){
        //TODO：qos暂时是不生效的，这里发送的都是0
        YunbaMessageUtil.pushMessage(request.getTopic() , request.getMessage() , request.getQos() , request.isRetain());
        return ResponseEntity.ok().build();
    }

}
