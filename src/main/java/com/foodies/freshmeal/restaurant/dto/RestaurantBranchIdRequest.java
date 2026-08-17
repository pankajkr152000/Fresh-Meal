package com.foodies.freshmeal.restaurant.dto;

import java.io.Serial;
import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * RestaurantBranchIdRequest
 * ============================================================================
 *
 * Represents the request payload used when a restaurant branch operation
 * requires a branch identifier.
 *
 * ============================================================================
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantBranchIdRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * MongoDB identifier of the restaurant branch.
     */
    @NotBlank
    private String branchId;

}