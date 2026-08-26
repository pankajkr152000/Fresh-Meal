package com.foodies.freshmeal.user.constants;

import com.foodies.freshmeal.common.contract.IDisplayOption;

/**
 * ============================================================================
 * Enum : AddressType
 * ============================================================================
 *
 * Represents the type/classification of a customer's saved address.
 *
 * Address type is a classification only. It does not represent a lifecycle
 * state and therefore has no transition rules.
 *
 * Business Meaning
 * ----------------
 *
 * HOME
 * Customer's residential/home address.
 *
 * WORK
 * Customer's workplace/office address.
 *
 * OTHER
 * Any other address saved by the customer.
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public enum AddressTypeConstant implements IDisplayOption {

    HOME("Home"),

    WORK("Work"),

    OTHER("Other");

    /**
     * User-friendly display name of the address type.
     *
     * <p>
     * This value is intended for API responses and UI rendering,
     * avoiding the need for clients to format enum names.
     * </p>
     */
    private final String displayName;

    /**
     * Creates an address type with its corresponding display name.
     *
     * @param displayName human-readable address type name
     */
    AddressTypeConstant(String displayName) {
        this.displayName = displayName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return displayName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        return name();
    }

}