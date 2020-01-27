package org.jmqtt.controller.controller;


import org.jmqtt.persistent.i18n.LocaleMessageSource;
import org.jmqtt.persistent.model.page.ResponseStruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BaseController {

    // 资源的国际化和数据库层面的国际化都应该放在这里
    @Autowired
    protected LocaleMessageSource localeMessageSource;

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public ResponseStruct failedWithMsg(String msg) {
        return ResponseStruct.build().setMsg(msg).setSuccess(false);
    }

    public ResponseStruct failed(String code) {
        return ResponseStruct.build().setSuccess(false)
                .setCode(code).setMsg(localeMessageSource.getMessage(code));
    }

    public <T extends ResponseStruct> T succ(T res) {
        res.setSuccess(true);
        return res;
    }

    public ResponseStruct succ(Object obj) {
        return ResponseStruct.build().setSuccess(true).setObj(obj);
    }

    public ResponseStruct succ() {
        return ResponseStruct.build().setSuccess(true);
    }

}
