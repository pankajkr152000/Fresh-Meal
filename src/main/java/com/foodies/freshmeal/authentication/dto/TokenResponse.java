package com.foodies.freshmeal.authentication.dto;

import com.foodies.freshmeal.common.audit.annotation.Sensitive;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResponse {

    @Sensitive
    private String accessToken;

    @Sensitive
    private String refreshToken;
    private String tokenType;
    private long expiresIn;

    @Sensitive
    private String loginSessionId;
}
