package com.foodies.freshmeal.common.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ErrorCodeConstants errorCode;
    private final String errorMessage;

    public BusinessException(ErrorCodeConstants errorCode) {
        super(ErrorMessage.get(errorCode));
        this.errorCode = errorCode;
        this.errorMessage = "";
    }

    public BusinessException(String errorMessage) {
        this.errorCode = null;
        this.errorMessage = errorMessage;
    }

}