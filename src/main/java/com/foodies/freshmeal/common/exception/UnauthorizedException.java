package com.foodies.freshmeal.common.exception;

public class UnauthorizedException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public UnauthorizedException() {
        super(ErrorCode.INVALID_CREDENTIALS);
    }

}