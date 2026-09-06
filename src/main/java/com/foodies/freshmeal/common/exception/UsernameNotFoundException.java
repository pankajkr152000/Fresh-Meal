package com.foodies.freshmeal.common.exception;

public class UsernameNotFoundException extends AbstractBusinessException {

    private static final long serialVersionUID = 1L;

    public UsernameNotFoundException(final IBusinessError error) {

        super(error);
    }

}