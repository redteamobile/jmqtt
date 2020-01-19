package org.jmqtt.persistent.model.page;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author Alex Liu
 * @date 2020/01/15
 */
public class SingleChannelDeviceResp {
    private String u;
    private String p;
    private String d;
    private String c;

    public String getU() {
        return this.u;
    }

    public SingleChannelDeviceResp setU(String u) {
        this.u = u;
        return this;
    }

    public String getP() {
        return this.p;
    }

    public SingleChannelDeviceResp setP(String p) {
        this.p = p;
        return this;
    }

    public String getD() {
        return this.d;
    }

    public SingleChannelDeviceResp setD(String d) {
        this.d = d;
        return this;
    }

    public String getC() {
        return this.c;
    }

    public SingleChannelDeviceResp setC(String c) {
        this.c = c;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("u", u)
                .append("p", p)
                .append("d", d)
                .append("c", c)
                .toString();
    }
}
