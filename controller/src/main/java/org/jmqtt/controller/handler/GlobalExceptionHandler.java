package org.jmqtt.controller.handler;

import org.jmqtt.persistent.exception.BaseException;
import org.jmqtt.persistent.exception.InvalidParamsException;
import org.jmqtt.persistent.i18n.LocaleMessageSource;
import org.jmqtt.persistent.model.page.ResponseStruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;


@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class GlobalExceptionHandler {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected LocaleMessageSource localeMessageSource;

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ResponseStruct> handleBaseException(HttpServletRequest request,
                                                              BaseException e) {
        return ResponseEntity.ok(ResponseStruct.build().setCode(e.getErrorCode().toString())
                .setMsg(localeMessageSource.getMessage(String.valueOf(e.getErrorCode()))));
    }

    @ExceptionHandler(InvalidParamsException.class)
    public ResponseEntity<ResponseStruct> handleInvalidParamsException(HttpServletRequest request,
                                                                       InvalidParamsException e) {
        return ResponseEntity.ok(ResponseStruct.build().setCode(e.getErrorCode().toString())
                .setMsg(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseStruct> handleUnexpectedException(HttpServletRequest request,
                                                                    Exception e) {
        log.error("Unexpected exception received {}!!", e.getMessage());

        e.printStackTrace();
        // TODO write a right response format and properly handle exception

        return ResponseEntity.ok().body(ResponseStruct.build().setMsg(e.getMessage()).setCode("500"));
    }

}
