package com.foodies.freshmeal.food.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * DTO : EditFoodInputDTO
 * ============================================================================
 *
 * Service input used when updating an existing Food entity.
 *
 * <p>
 * This DTO intentionally separates the system-managed Food identifier from
 * the client-editable {@link FoodRequest}. The FoodRequest contains only
 * fields that can actually be modified by the caller.
 * </p>
 *
 * <p>
 * Food status is intentionally excluded because status changes are handled
 * through the dedicated Food status update operation.
 * </p>
 *
 * <p>
 * Restaurant and restaurant branch identifiers are also not part of this DTO.
 * Existing Food ownership must not be changed during a normal food edit.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditFoodInputDTO {

    // =========================================================================
    // Food Identity
    // =========================================================================

    /**
     * Business-independent persisted identifier of the Food entity being
     * updated.
     */
    private String foodId;

    // =========================================================================
    // Editable Food Data
    // =========================================================================

    /**
     * Client-editable Food information.
     */
    private FoodRequest foodRequest;

    // =========================================================================
    // Image
    // =========================================================================

    /**
     * Optional replacement image.
     *
     * <p>
     * When no image is supplied, the existing Food image remains unchanged.
     * </p>
     */
    private MultipartFile imageFile;
}