package org.jmqtt.persistent.exception;

import org.jmqtt.common.constant.ErrorCode;

public class InvalidParamsException extends BaseException {

    private static final long serialVersionUID = -9097394714104446010L;

    public InvalidParamsException(String message) {
        super(ErrorCode.INVALID_PARAMS, message);
    }
}
