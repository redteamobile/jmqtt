/*
package org.jmqtt.controller.controller;

import org.jmqtt.broker.YunbaMessageUtil;
import org.jmqtt.common.log.LoggerName;
import org.jmqtt.controller.model.req.PublishReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

*/
/**
 * @author Alex Liu
 * @date 2019/12/07
 *//*

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
*/
