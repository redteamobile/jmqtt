package org.jmqtt.common.enums;

/**
 * @author Alex Liu
 * @date 2020/01/08
 */
public enum DisconnectReason {
    CLOSED("closed"),
    IDLETIMEOUT("idle_timeout"),
    EXCEPTION("exception")
    ;

    DisconnectReason(String val) {
        this.val = val;
    }

    private String val;

    @Override
    public String toString() {
        return val;
    }
}
