package org.jmqtt.controller.model.page;

import com.google.common.base.MoreObjects;

public class ResponseStruct implements java.io.Serializable {

    private static final long serialVersionUID = 3856547080362520800L;

    private String code;

    private boolean success;

    private String msg = "";

    private Object obj = null;

    public static ResponseStruct build() {
        return new ResponseStruct();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("code", code)
                .add("success", success)
                .add("msg", msg)
                .add("obj", obj)
                .toString();
    }

    public String getCode() {
        return code;
    }

    public ResponseStruct setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ResponseStruct setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Object getObj() {
        return obj;
    }

    public ResponseStruct setObj(Object obj) {
        this.obj = obj;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public ResponseStruct setSuccess(boolean success) {
        this.success = success;
        return this;
    }
}
