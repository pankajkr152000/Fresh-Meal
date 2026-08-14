package com.foodies.freshmeal.order.valueObject;

import java.io.Serial;
import java.io.Serializable;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * OrderNotes
 * ============================================================================
 *
 * Contains order-level notes that do not belong to a specific food item,
 * address or cancellation event.
 *
 * ============================================================================
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderNotes implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * General customer note associated with the order.
     */
    @Size(max = 500)
    private String customerNote;

    /**
     * Restaurant operational note.
     */
    @Size(max = 500)
    private String restaurantNote;

    /**
     * Internal administrative note.
     *
     * Should not be exposed to customers.
     */
    @Size(max = 1000)
    private String internalNote;

}