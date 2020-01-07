package org.jmqtt.persistent.entity;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author Alex Liu
 * @date 2019/12/05
 */
@Data
@Builder
public class Client {
    private Integer id;//no comment
    private String clientId;//no comment
    private String username;//no comment
    private Boolean status;//0下线，1上线
    private Date createTime;//no comment
    private Date lastConnectTime;//no comment
    private Date lastDisconnectTime;//no comment
    private String type;//数据类型，DEVICE, VSIM, UICC, OTHER

    @Tolerate
    public Client(){}

}
