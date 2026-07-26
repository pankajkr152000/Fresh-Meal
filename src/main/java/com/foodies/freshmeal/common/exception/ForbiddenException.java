package com.foodies.freshmeal.common.exception;

public class ForbiddenException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public ForbiddenException() {
        super(ErrorCode.ACCESS_DENIED);
    }

}