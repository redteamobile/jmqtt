package org.jmqtt.persistent.exception;

import org.jmqtt.persistent.i18n.LocaleMessageSource;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1831611793249206868L;

    @Autowired
    protected LocaleMessageSource localeMessageSource;

    public BaseException() {
        super();
    }

    public BaseException(String message, Throwable cause, boolean enableSuppression,
                         boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(String message) {
        super(message);
    }
    public BaseException(Integer errorCode){
        super();
        this.errorCode = errorCode;
        this.errorMessage = localeMessageSource.getMessage(String.valueOf(errorCode));
    }
    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(Integer errorCode, String errorMessage) {
        super(errorCode + ": " + errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    private Integer errorCode;
    private String errorMessage;

    public Integer getErrorCode() {
        return errorCode;
    }

    public BaseException setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getLocalizedMessage() {
        return this.getErrorMessage();
    }
}
