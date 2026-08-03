package com.foodies.freshmeal.common.date;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a date range.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DateRange implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private LocalDate startDate;

    private LocalDate endDate;

}