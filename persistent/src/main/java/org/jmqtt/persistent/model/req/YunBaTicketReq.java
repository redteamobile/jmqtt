package org.jmqtt.persistent.model.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Alex Liu
 * @date 2020/01/16
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class YunBaTicketReq {

    private String a;
    private int p;
    private String d;
}
