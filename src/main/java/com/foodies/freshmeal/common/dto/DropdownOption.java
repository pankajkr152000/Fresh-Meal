package com.foodies.freshmeal.common.dto;

/**
 * Represents a generic dropdown option.
 *
 * @param label Text displayed in the UI.
 * @param value Value submitted to the backend.
 */
public record DropdownOption(
        String label,
        String value) {

}
