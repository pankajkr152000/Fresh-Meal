package com.foodies.freshmeal.common.dto;

/**
 * ============================================================================
 * Record : DisplayOptionResponse
 * ============================================================================
 *
 * Represents a generic UI option consisting of a display label and
 * its corresponding internal value.
 *
 * <p>
 * This record is returned by metadata APIs and other endpoints that expose
 * selectable options to the frontend.
 * </p>
 *
 * <p>
 * Typical consumers include:
 * <ul>
 *     <li>Dropdowns</li>
 *     <li>Autocomplete fields</li>
 *     <li>Filter controls</li>
 *     <li>Radio groups</li>
 *     <li>Search components</li>
 * </ul>
 * </p>
 *
 * <p>
 * Example JSON:
 * </p>
 *
 * <pre>
 * {
 *     "label": "Out of Stock",
 *     "value": "OUT_OF_STOCK"
 * }
 * </pre>
 *
 * @param label user-friendly display text
 * @param value internal application value
 *
 * @author Pankaj Kumar
 * @since 1.0
 * ============================================================================
 */
public record DisplayOptionResponse(
        String label,
        String value) {
}