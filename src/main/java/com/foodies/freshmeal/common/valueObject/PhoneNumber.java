package com.foodies.freshmeal.common.valueObject;

import java.io.Serial;
import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a phone number.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneNumber implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank
    @Pattern(regexp = "^[6-9][0-9]{9}$", message = "Invalid mobile number.")
    private String value;

}