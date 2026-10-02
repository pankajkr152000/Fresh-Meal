
package com.foodies.freshmeal.common.constants;

/**
 * Defines the supported decimal precision levels for monetary calculations.
 *
 * <p>
 * Precision determines the maximum number of digits retained after the
 * decimal point when a monetary value is rounded to a specific scale.
 * </p>
 *
 * <p>
 * Examples:
 * </p>
 * <ul>
 * <li>TWO: 125.68</li>
 * <li>THREE: 125.679</li>
 * <li>FIVE: 125.67890</li>
 * </ul>
 *
 * <p>
 * This enum only defines precision. Rounding behavior is controlled
 * separately through {@link java.math.RoundingMode}.
 * </p>
 *
 * @author Pankaj Kumar
 */
public enum MoneyPrecision {

    /** Standard precision for most currency amounts. */
    TWO(2),

    /** Extended precision for calculations requiring three decimal places. */
    THREE(3),

    /** High precision for intermediate or specialized monetary calculations. */
    FIVE(5);

    private final int scale;

    MoneyPrecision(int scale) {
        this.scale = scale;
    }

    /**
     * Returns the number of fractional digits supported by this precision.
     *
     * @return the decimal scale
     */
    public int getScale() {
        return scale;
    }
}