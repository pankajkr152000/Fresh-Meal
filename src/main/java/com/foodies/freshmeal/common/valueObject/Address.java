package com.foodies.freshmeal.common.valueObject;

import java.io.Serial;
import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Generic reusable address.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Size(max = 100)
    private String houseNumber;

    @Size(max = 150)
    private String apartmentName;

    @NotBlank
    @Size(max = 150)
    private String street;

    @NotBlank
    @Size(max = 150)
    private String area;

    @NotBlank
    @Size(max = 100)
    private String city;

    @Size(max = 100)
    private String district;

    @NotBlank
    @Size(max = 100)
    private String state;

    @Builder.Default
    private String country = "India";

    @Pattern(regexp = "^[0-9]{6}$", message = "Invalid pincode.")
    private String pincode;

    @Size(max = 200)
    private String landmark;

    /**
     * Geographical coordinates.
     */
    private GeoLocation geoLocation;

}