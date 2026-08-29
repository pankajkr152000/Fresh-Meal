package com.foodies.freshmeal.common.valueObject;

import java.io.Serial;
import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents an email address.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailAddress implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank
    @Email
    private String value;
    
    @Override
    public String toString() {
    	return value;
    }
    
	public static EmailAddress toEmailAddress(String emailAddress) {
		return EmailAddress.builder().value(emailAddress).build();
	}

}