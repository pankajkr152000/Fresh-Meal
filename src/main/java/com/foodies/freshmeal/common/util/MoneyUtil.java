
package com.foodies.freshmeal.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import com.foodies.freshmeal.common.constants.MoneyPrecision;
import com.foodies.freshmeal.common.valueObject.Money;

/**
 * Utility class providing centralized operations for monetary calculations.
 *
 * <p>
 * This class ensures consistent precision, rounding, and currency
 * validation across the application.
 * </p>
 *
 * <p>
 * All monetary calculations use {@link BigDecimal} to avoid the
 * precision issues associated with floating-point arithmetic.
 * </p>
 *
 * <p>
 * Intermediate calculations should retain their required precision.
 * Rounding should generally be applied at explicitly defined business
 * boundaries, such as when calculating a final payable amount.
 * </p>
 *
 * @author Pankaj Kumar
 */
public final class MoneyUtil {

    /**
     * Default rounding mode used when no explicit rounding mode is supplied.
     */
    public static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;

    private MoneyUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Rounds a monetary amount to the specified precision.
     *
     * @param amount       the amount to round
     * @param precision    the required decimal precision
     * @param roundingMode the rounding strategy
     * @return the rounded amount
     */
    public static BigDecimal round(
            BigDecimal amount,
            MoneyPrecision precision,
            RoundingMode roundingMode) {

        Objects.requireNonNull(amount, "Amount must not be null.");
        Objects.requireNonNull(precision, "Precision must not be null.");
        Objects.requireNonNull(roundingMode, "Rounding mode must not be null.");

        return amount.setScale(precision.getScale(), roundingMode);
    }

    /**
     * Rounds a monetary amount using the default rounding mode.
     *
     * @param amount    the amount to round
     * @param precision the required decimal precision
     * @return the rounded amount
     */
    public static BigDecimal round(
            BigDecimal amount,
            MoneyPrecision precision) {

        return round(amount, precision, DEFAULT_ROUNDING_MODE);
    }

    /**
     * Adds two monetary amounts after validating their currencies.
     *
     * <p>
     * The result retains the maximum scale of the input amounts.
     * No additional rounding is applied.
     * </p>
     *
     * @param first  the first monetary amount
     * @param second the second monetary amount
     * @return the sum
     */
    public static Money add(Money first, Money second) {

        validateSameCurrency(first, second);

        BigDecimal result = first.getAmount().add(second.getAmount());

        return Money.of(result, first.getCurrency());
    }

    /**
     * Subtracts one monetary amount from another after validating currencies.
     *
     * <p>
     * The result retains the maximum scale of the input amounts.
     * No additional rounding is applied.
     * </p>
     *
     * @param first  the amount from which to subtract
     * @param second the amount to subtract
     * @return the difference
     */
    public static Money subtract(Money first, Money second) {

        validateSameCurrency(first, second);

        BigDecimal result = first.getAmount().subtract(second.getAmount());

        return Money.of(result, first.getCurrency());
    }

    /**
     * Multiplies a monetary amount by a factor.
     *
     * <p>
     * No rounding is applied automatically. The caller is responsible
     * for rounding the result when required by a business rule.
     * </p>
     *
     * @param money  the monetary amount
     * @param factor the multiplication factor
     * @return the product
     */
    public static Money multiply(Money money, BigDecimal factor) {

        Objects.requireNonNull(money, "Money must not be null.");
        Objects.requireNonNull(money.getAmount(), "Money amount must not be null.");
        Objects.requireNonNull(factor, "Multiplication factor must not be null.");

        return Money.of(
                money.getAmount().multiply(factor),
                money.getCurrency());
    }

    /**
     * Divides a monetary amount by a divisor using the specified precision
     * and rounding mode.
     *
     * @param money        the monetary amount
     * @param divisor      the divisor
     * @param precision    the required result precision
     * @param roundingMode the rounding strategy
     * @return the rounded quotient
     * @throws ArithmeticException if the divisor is zero
     */
    public static Money divide(
            Money money,
            BigDecimal divisor,
            MoneyPrecision precision,
            RoundingMode roundingMode) {

        Objects.requireNonNull(money, "Money must not be null.");
        Objects.requireNonNull(money.getAmount(), "Money amount must not be null.");
        Objects.requireNonNull(divisor, "Divisor must not be null.");

        if (divisor.compareTo(BigDecimal.ZERO) == 0) {
            throw new ArithmeticException("Cannot divide a monetary amount by zero.");
        }

        BigDecimal result = money.getAmount()
                .divide(divisor, precision.getScale(), roundingMode);

        return Money.of(result, money.getCurrency());
    }

    /**
     * Divides a monetary amount using the default rounding mode.
     *
     * @param money     the monetary amount
     * @param divisor   the divisor
     * @param precision the required result precision
     * @return the rounded quotient
     */
    public static Money divide(
            Money money,
            BigDecimal divisor,
            MoneyPrecision precision) {

        return divide(money, divisor, precision, DEFAULT_ROUNDING_MODE);
    }

    /**
     * Rounds a monetary value to the specified precision.
     *
     * @param money        the monetary value
     * @param precision    the required precision
     * @param roundingMode the rounding strategy
     * @return a new Money instance containing the rounded amount
     */
    public static Money round(
            Money money,
            MoneyPrecision precision,
            RoundingMode roundingMode) {

        Objects.requireNonNull(money, "Money must not be null.");

        return Money.of(
                round(money.getAmount(), precision, roundingMode),
                money.getCurrency());
    }

    /**
     * Rounds a monetary value using the default rounding mode.
     *
     * @param money     the monetary value
     * @param precision the required precision
     * @return a new Money instance containing the rounded amount
     */
    public static Money round(Money money, MoneyPrecision precision) {

        return round(money, precision, DEFAULT_ROUNDING_MODE);
    }

    /**
     * Compares two monetary amounts after validating their currencies.
     *
     * @param first  the first amount
     * @param second the second amount
     * @return a negative value, zero, or a positive value
     *         as the first amount is less than, equal to, or greater
     *         than the second
     */
    public static int compare(Money first, Money second) {

        validateSameCurrency(first, second);

        return first.getAmount().compareTo(second.getAmount());
    }

    /**
     * Checks whether two monetary amounts are equal by numeric value
     * and currency, ignoring differences in BigDecimal scale.
     *
     * @param first  the first amount
     * @param second the second amount
     * @return true if both amounts have the same numeric value and currency
     */
    public static boolean isEqual(Money first, Money second) {

        if (first == null || second == null) {
            return first == second;
        }

        return Objects.equals(first.getCurrency(), second.getCurrency())
                && first.getAmount() != null
                && second.getAmount() != null
                && first.getAmount().compareTo(second.getAmount()) == 0;
    }

    /**
     * Validates that both monetary values exist and use the same currency.
     *
     * @param first  the first monetary value
     * @param second the second monetary value
     * @throws IllegalArgumentException if either value is null,
     *                                  an amount is missing, or currencies differ
     */
    private static void validateSameCurrency(Money first, Money second) {

        Objects.requireNonNull(first, "First money value must not be null.");
        Objects.requireNonNull(second, "Second money value must not be null.");

        Objects.requireNonNull(first.getAmount(), "First amount must not be null.");
        Objects.requireNonNull(second.getAmount(), "Second amount must not be null.");

        if (first.getCurrency() == null || second.getCurrency() == null
                || !first.getCurrency().equalsIgnoreCase(second.getCurrency())) {

            throw new IllegalArgumentException(
                    "Monetary operations require matching currencies.");
        }
    }
}