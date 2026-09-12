package com.foodies.freshmeal.authentication.dto;

import com.foodies.freshmeal.common.audit.annotation.Sensitive;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {

    @Sensitive
    private TokenResponse token;
}
