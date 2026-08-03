package com.foodies.freshmeal.common.valueObject;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a monetary value.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Money implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Builder.Default
    @DecimalMin("0.00")
    @Digits(integer = 12, fraction = 2)
    private BigDecimal amount = BigDecimal.ZERO;

    /**
     * ISO 4217 Currency Code.
     * Example: INR, USD, EUR
     */
    @Builder.Default
    private String currency = "INR";

}