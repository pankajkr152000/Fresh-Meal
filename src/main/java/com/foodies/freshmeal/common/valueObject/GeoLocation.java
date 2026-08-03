package com.foodies.freshmeal.common.valueObject;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents geographical coordinates.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeoLocation implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Latitude.
     */
    private Double latitude;

    /**
     * Longitude.
     */
    private Double longitude;

}