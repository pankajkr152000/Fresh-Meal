package com.foodies.freshmeal.authentication.dto;

import java.util.List;

import com.foodies.freshmeal.common.audit.annotation.Sensitive;
import com.foodies.freshmeal.common.constants.RoleType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {

    @Sensitive
    private TokenResponse token;
    
    private List<RoleType> roles;
}
