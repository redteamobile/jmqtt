package org.jmqtt.controller.controller;

import org.jmqtt.persistent.asyncTask.AsyncTask;
import org.jmqtt.persistent.exception.InvalidParamsException;
import org.jmqtt.persistent.model.page.ResponseStruct;
import org.jmqtt.persistent.utils.APIMonitoringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Alex Liu
 * @date 2020/01/27
 */

@RestController
@RequestMapping("/api/v1")
public class MonitorController extends BaseController{

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private AsyncTask asyncTask;

    @Value("${api.monitor.keepTime}")
    private int keepTime;

    @GetMapping("/get")
    public ResponseStruct get(){
        return succ(asyncTask.getClinetNumber());
    }

    @GetMapping("/monitor")
    public ResponseStruct monitor(@RequestParam String apiName, @RequestParam String startTime,
                                  @RequestParam(required = false) String endTime){
        int startTimestamp;
        int endTimestamp;
        Date endDate;
        Date nowDate = new Date();

        //获取开始时间
        try{
            Date startDate = sdf.parse(startTime);
            startTimestamp = (int) (startDate.getTime() / 1000);
        }catch (ParseException e){
            throw new InvalidParamsException("startTime is invalid");
        }

        //获取结束时间
        try{
            if(endTime != null){
                endDate  = sdf.parse(endTime);
            }else{
                endDate = nowDate;
            }
            endTimestamp = (int) (endDate.getTime() / 1000);
        }catch (ParseException e){
            throw new InvalidParamsException("startTime is invalid");
        }

        //开始时间必须在结束时间之前
        if(endTimestamp < startTimestamp){
            throw new InvalidParamsException("The startTime should be before the endTime");
        }

        //检查开始时间的记录是否还存在
        if((int) (nowDate.getTime() / 1000) - keepTime * APIMonitoringUtils.SECOUNDS_OF_ONE_DAY > startTimestamp){
            throw new InvalidParamsException(String.format("records just keep for %d days" , keepTime));
        }

        Integer integer = APIMonitoringUtils.getCount(apiName , startTimestamp , endTimestamp);

        return succ(integer);
    }
}
