package org.jmqtt.controller.controller;

import org.jmqtt.broker.YunbaMessageUtil;
import org.jmqtt.persistent.asyncTask.AsyncTask;
import org.jmqtt.persistent.model.page.ResponseStruct;
import org.jmqtt.persistent.model.req.PublishReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/api/v1")
public class PublishController extends BaseController{

    @Autowired
    private AsyncTask asyncTask;

    @PostMapping("/publish")
    public ResponseStruct publish(@Valid @RequestBody PublishReq request){
        YunbaMessageUtil.pushMessage(request.getTopic() , request.getMessage() , request.getQos() , request.isRetain());
        return succ();
    }

    @GetMapping("/get")
    public ResponseStruct get(){
        return succ(asyncTask.getClinetNumber());
    }

}
