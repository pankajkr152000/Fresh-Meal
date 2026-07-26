package com.foodies.freshmeal.common.exception;

public class DuplicateResourceException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public DuplicateResourceException(ErrorCode errorCode) {
        super(errorCode);
    }

}