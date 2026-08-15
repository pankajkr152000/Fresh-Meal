package com.foodies.freshmeal.restaurant.valueObject;

import java.time.DayOfWeek;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * Value Object : OperatingHours
 * ============================================================================
 *
 * Represents the regular operating hours of a restaurant branch for a
 * particular day of the week.
 *
 * A branch may either:
 *
 * 1. Be open during the specified time range.
 * 2. Be closed for the day.
 *
 * ============================================================================
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OperatingHours {

    /**
     * Day of the week.
     */
    private DayOfWeek dayOfWeek;

    /**
     * Opening time of the branch.
     *
     * Null when the branch is closed for the day.
     */
    private LocalTime openingTime;

    /**
     * Closing time of the branch.
     *
     * Null when the branch is closed for the day.
     */
    private LocalTime closingTime;

    /**
     * Indicates whether the branch is closed for the day.
     */
    private boolean closed;
}