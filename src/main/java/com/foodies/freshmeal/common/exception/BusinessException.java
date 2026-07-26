package com.foodies.freshmeal.common.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(ErrorMessage.get(errorCode));
        this.errorCode = errorCode;
    }

}