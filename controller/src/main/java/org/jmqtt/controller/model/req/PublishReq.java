package org.jmqtt.controller.model.req;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author Alex Liu
 * @date 2019/12/07
 */
@Data
@Builder
public class PublishReq {

    @NotBlank
    private String topic;
    @NotNull
    private String message;
    @Min(0)
    @Max(2)
    private Integer qos;
    private boolean retain;

    @Tolerate
    public PublishReq(){}
}
