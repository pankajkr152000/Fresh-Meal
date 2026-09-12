package com.foodies.freshmeal.authentication.dto;

import com.foodies.freshmeal.common.audit.annotation.Sensitive;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "Username or email is required.")
    @Size(max = 100, message = "Username or email must not exceed 100 characters.")
    private String identifier;

    @Sensitive
    @NotBlank(message = "Password is required.")
    @Size(max = 100, message = "Password must not exceed 100 characters.")
    private String password;
}
