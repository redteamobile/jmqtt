package org.jmqtt.controller.model.req;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author Alex Liu
 * @date 2019/12/07
 */
@Data
@Builder
public class PublishReq {

    private String topic;
    private String message;
    @Min(0)
    @Max(2)
    private Integer qos;

    @Tolerate
    public PublishReq(){}
}
