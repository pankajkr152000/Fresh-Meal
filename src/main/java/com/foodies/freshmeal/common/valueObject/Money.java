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

    @Override
    public String toString() {
        return String.format("%s %.2f", currency, amount);
    }

    public static Money of(BigDecimal amount, String currency) {
        return Money.builder()
                .amount(amount)
                .currency(currency)
                .build();
    }

    public static Money of(BigDecimal amount) {
        return Money.builder()
                .amount(amount)
                .currency("INR")
                .build();
    }

    public static Money of(String amount, String currency) {
        return Money.builder()
                .amount(new BigDecimal(amount))
                .currency(currency)
                .build();
    }

    public static Money of(String amount) {
        return Money.builder()
                .amount(new BigDecimal(amount))
                .currency("INR")
                .build();
    }

    public static Money defaultMoney() {
        return Money.builder()
                .amount(BigDecimal.ZERO)
                .currency("INR")
                .build();
    }

    public static Money defaultMoney(String currency) {
        return Money.builder()
                .amount(BigDecimal.ZERO)
                .currency(currency)
                .build();
    }

    public static BigDecimal toBigDecimal(Money money) {
        return money != null ? money.getAmount() : BigDecimal.ZERO;
    }

    public static Double getDoubleValue(Money money) {
        return money != null ? money.getAmount().doubleValue() : 0.0;
    }

}