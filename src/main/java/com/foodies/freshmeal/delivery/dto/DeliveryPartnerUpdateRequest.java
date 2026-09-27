package com.foodies.freshmeal.delivery.dto;

import com.foodies.freshmeal.delivery.constants.VehicleType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPartnerUpdateRequest {
    private String partnerNumber;
    // =========================================================================
    // Vehicle Information
    // =========================================================================

    /**
     * Registration number of the vehicle used by the delivery partner.
     */
    @NotBlank
    @Size(max = 30)
    private String vehicleNumber;

    /**
     * Type of vehicle used by the delivery partner.
     */
    @NotNull
    private VehicleType vehicleType;
}
