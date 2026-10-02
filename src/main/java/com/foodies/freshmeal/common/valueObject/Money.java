
package com.foodies.freshmeal.common.valueObject;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import com.foodies.freshmeal.common.constants.MoneyPrecision;
import com.foodies.freshmeal.common.util.MoneyUtil;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a monetary value consisting of an amount and an ISO 4217
 * currency code.
 *
 * <p>
 * This class is responsible only for representing monetary values.
 * Arithmetic operations, precision adjustments, and rounding are
 * delegated to {@link MoneyUtil}.
 * </p>
 *
 * <p>
 * The amount uses {@link BigDecimal} to preserve decimal precision
 * and avoid floating-point calculation errors.
 * </p>
 *
 * <p>
 * The default currency is INR. The default precision for explicit
 * rounding operations is two decimal places.
 * </p>
 *
 * @author Pankaj Kumar
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Money implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Monetary amount.
     *
     * <p>
     * Supports up to five fractional digits. The actual scale is
     * determined by the calculation or business rule.
     * </p>
     */
    @Builder.Default
    @DecimalMin(value = "0.00")
    @Digits(integer = 12, fraction = 5)
    private BigDecimal amount = BigDecimal.ZERO;

    /**
     * ISO 4217 currency code.
     *
     * <p>
     * Defaults to INR.
     * </p>
     */
    @Builder.Default
    @NotBlank
    private String currency = "INR";

    /**
     * Creates a monetary value using the supplied amount and currency.
     *
     * <p>
     * No rounding is applied. The original BigDecimal scale is preserved.
     * </p>
     *
     * @param amount   monetary amount
     * @param currency ISO 4217 currency code
     * @return a new Money instance
     */
    public static Money of(BigDecimal amount, String currency) {

        return Money.builder()
                .amount(Objects.requireNonNull(amount, "Amount must not be null."))
                .currency(validateCurrency(currency))
                .build();
    }

    /**
     * Creates a monetary value using the default INR currency.
     *
     * @param amount monetary amount
     * @return a new Money instance
     */
    public static Money of(BigDecimal amount) {
        return of(amount, "INR");
    }

    /**
     * Creates a monetary value from a string amount and currency.
     *
     * @param amount   monetary amount as a string
     * @param currency ISO 4217 currency code
     * @return a new Money instance
     */
    public static Money of(String amount, String currency) {

        Objects.requireNonNull(amount, "Amount must not be null.");

        return of(new BigDecimal(amount), currency);
    }

    /**
     * Creates a monetary value from a string amount using INR.
     *
     * @param amount monetary amount as a string
     * @return a new Money instance
     */
    public static Money of(String amount) {
        return of(amount, "INR");
    }

    /**
     * Creates a monetary value from a Double using INR.
     *
     * <p>
     * This method is retained for backward compatibility.
     * Prefer BigDecimal or String-based factory methods for financial
     * calculations.
     * </p>
     *
     * @param amount monetary amount
     * @return a new Money instance
     * @deprecated Use {@link #of(BigDecimal)} instead.
     */
    @Deprecated(since = "1.0", forRemoval = false)
    public static Money of(Double amount) {

        Objects.requireNonNull(amount, "Amount must not be null.");

        if (!Double.isFinite(amount)) {
            throw new IllegalArgumentException(
                    "Monetary amount must be a finite number.");
        }

        return of(BigDecimal.valueOf(amount));
    }

    /**
     * Creates a monetary value rounded to the specified precision.
     *
     * @param amount       monetary amount
     * @param currency     ISO 4217 currency code
     * @param precision    required decimal precision
     * @param roundingMode rounding strategy
     * @return a new Money instance with the rounded amount
     */
    public static Money of(
            BigDecimal amount,
            String currency,
            MoneyPrecision precision,
            RoundingMode roundingMode) {

        BigDecimal roundedAmount = MoneyUtil.round(
                amount,
                precision,
                roundingMode);

        return of(roundedAmount, currency);
    }

    /**
     * Creates a monetary value rounded to the specified precision
     * using the default rounding mode.
     *
     * @param amount    monetary amount
     * @param currency  ISO 4217 currency code
     * @param precision required decimal precision
     * @return a new Money instance with the rounded amount
     */
    public static Money of(
            BigDecimal amount,
            String currency,
            MoneyPrecision precision) {

        return of(
                amount,
                currency,
                precision,
                MoneyUtil.DEFAULT_ROUNDING_MODE);
    }

    /**
     * Creates a zero-valued monetary amount in INR.
     *
     * @return zero-valued Money instance
     */
    public static Money defaultMoney() {
        return defaultMoney("INR");
    }

    /**
     * Creates a zero-valued monetary amount in the supplied currency.
     *
     * @param currency ISO 4217 currency code
     * @return zero-valued Money instance
     */
    public static Money defaultMoney(String currency) {
        return of(BigDecimal.ZERO, currency);
    }

    /**
     * Converts a Money instance to BigDecimal.
     *
     * <p>
     * Returns zero when the supplied Money instance or its amount is null.
     * This behavior is intended for optional monetary values only.
     * Required financial values should be validated before conversion.
     * </p>
     *
     * @param money monetary value
     * @return amount or BigDecimal.ZERO
     */
    public static BigDecimal toBigDecimal(Money money) {

        return money != null && money.getAmount() != null
                ? money.getAmount()
                : BigDecimal.ZERO;
    }

    /**
     * Returns the double representation of a monetary value.
     *
     * <p>
     * Provided for compatibility with existing integrations.
     * Do not use this method for financial calculations.
     * </p>
     *
     * @param money monetary value
     * @return double representation or 0.0 when null
     */
    @Deprecated(since = "1.0", forRemoval = false)
    public static Double getDoubleValue(Money money) {
        return toBigDecimal(money).doubleValue();
    }

    /**
     * Validates and normalizes a currency code.
     *
     * @param currency ISO 4217 currency code
     * @return normalized uppercase currency code
     */
    private static String validateCurrency(String currency) {

        Objects.requireNonNull(currency, "Currency must not be null.");

        String normalizedCurrency = currency.trim().toUpperCase();

        if (normalizedCurrency.isBlank()) {
            throw new IllegalArgumentException(
                    "Currency must not be blank.");
        }

        return normalizedCurrency;
    }

    /**
     * Returns a readable representation of this monetary value.
     *
     * @return currency and amount
     */
    @Override
    public String toString() {
        return String.format("%s %s", currency, amount.toPlainString());
    }
}