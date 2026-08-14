package com.foodies.freshmeal.order.dto;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * OrderNotesResponse
 * ============================================================================
 *
 * Represents order-level notes displayed on the Admin Order Details page.
 *
 * This DTO is an API representation of OrderNotes.
 *
 * Notes are separated by their business purpose:
 *
 * - Customer note
 * - Restaurant operational note
 * - Internal administrative note
 *
 * ============================================================================
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderNotesResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * General customer note associated with the order.
     */
    private String customerNote;

    /**
     * Restaurant operational note.
     */
    private String restaurantNote;

    /**
     * Internal administrative note.
     *
     * This field must only be exposed to authorized Admin users.
     */
    private String internalNote;

}