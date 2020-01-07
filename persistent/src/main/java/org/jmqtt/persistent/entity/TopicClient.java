package org.jmqtt.persistent.entity;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import javax.annotation.sql.DataSourceDefinition;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author Alex Liu
 * @date 2019/12/06
 */
@Data
@Builder
public class TopicClient {
    private Integer id;//no comment
    private String username;
    private String topic;//no comment
    private String clientId;//no comment
    private Boolean subscribe;//no comment
    private Date createTime;//no comment
    private Date lastSubscribeTime;//no comment
    private Date lastUnsubscribeTime;//no comment

    @Tolerate
    public TopicClient(){}
}
