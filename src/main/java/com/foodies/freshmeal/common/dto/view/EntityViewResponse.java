package com.foodies.freshmeal.common.dto.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ============================================================================
 * Generic View Response
 * ============================================================================
 *
 * Wraps a single entity along with navigation information.
 *
 * Can be used for:
 * - Food
 * - Restaurant
 * - User
 * - Category
 * - Order
 * - Offer
 *
 * ============================================================================
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityViewResponse<T> {

    private T data;

    private EntityNavigation navigation;

}