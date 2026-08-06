package com.foodies.freshmeal.common.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final IBusinessError error;

    public BusinessException(IBusinessError error) {
        super(error.getErrorMessage());
        this.error = error;
    }

    public IBusinessError getErrorCode() {
        return error;
    }

}