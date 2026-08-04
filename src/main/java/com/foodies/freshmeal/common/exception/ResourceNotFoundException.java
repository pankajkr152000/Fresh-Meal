package com.foodies.freshmeal.common.exception;

public class ResourceNotFoundException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(ErrorCodeConstants errorCode) {
        super(errorCode);
    }

    public ResourceNotFoundException(String errorCode) {
        super(errorCode);
    }

}