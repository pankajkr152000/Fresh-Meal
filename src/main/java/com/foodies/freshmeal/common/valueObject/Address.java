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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (houseNumber != null && !houseNumber.isEmpty()) {
            sb.append(houseNumber).append(", ");
        }
        if (apartmentName != null && !apartmentName.isEmpty()) {
            sb.append(apartmentName).append(", ");
        }
        if (street != null && !street.isEmpty()) {
            sb.append(street).append(", ");
        }
        if (area != null && !area.isEmpty()) {
            sb.append(area).append(", ");
        }
        if (city != null && !city.isEmpty()) {
            sb.append(city).append(", ");
        }
        if (district != null && !district.isEmpty()) {
            sb.append(district).append(", ");
        }
        if (state != null && !state.isEmpty()) {
            sb.append(state).append(", ");
        }
        if (country != null && !country.isEmpty()) {
            sb.append(country).append(", ");
        }
        if (pincode != null && !pincode.isEmpty()) {
            sb.append(pincode);
        }
        return sb.toString();
    }

    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        if (houseNumber != null && !houseNumber.isEmpty()) {
            sb.append(houseNumber).append(", ");
        }
        if (apartmentName != null && !apartmentName.isEmpty()) {
            sb.append(apartmentName).append(", ");
        }
        if (street != null && !street.isEmpty()) {
            sb.append(street).append(", ");
        }
        if (area != null && !area.isEmpty()) {
            sb.append(area).append(", ");
        }
        if (city != null && !city.isEmpty()) {
            sb.append(city).append(", ");
        }
        if (district != null && !district.isEmpty()) {
            sb.append(district).append(", ");
        }
        if (state != null && !state.isEmpty()) {
            sb.append(state).append(", ");
        }
        if (country != null && !country.isEmpty()) {
            sb.append(country).append(", ");
        }
        if (pincode != null && !pincode.isEmpty()) {
            sb.append(pincode);
        }
        return sb.toString();
    }

    public static Address fromString(String addressString) {
        String[] parts = addressString.split(", ");
        Address address = new Address();
        int index = 0;

        if (parts.length > index) {
            address.setHouseNumber(parts[index++]);
        }
        if (parts.length > index) {
            address.setApartmentName(parts[index++]);
        }
        if (parts.length > index) {
            address.setStreet(parts[index++]);
        }
        if (parts.length > index) {
            address.setArea(parts[index++]);
        }
        if (parts.length > index) {
            address.setCity(parts[index++]);
        }
        if (parts.length > index) {
            address.setDistrict(parts[index++]);
        }
        if (parts.length > index) {
            address.setState(parts[index++]);
        }
        if (parts.length > index) {
            address.setCountry(parts[index++]);
        }
        if (parts.length > index) {
            address.setPincode(parts[index]);
        }

        return address;
    }

}