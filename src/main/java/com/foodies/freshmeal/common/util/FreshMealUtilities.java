package com.foodies.freshmeal.common.util;

import java.util.Locale;

import org.springframework.util.StringUtils;

/**
 * ============================================================================
 * FreshMeal Utilities
 * ============================================================================
 *
 * <p>
 * Provides common, stateless utility operations used throughout the
 * FreshMeal application.
 * </p>
 *
 * <h3>Responsibilities</h3>
 * <ul>
 * <li>Format and normalize individual name fields.</li>
 * <li>Extract first and last names from full names.</li>
 * <li>Capitalize the first character of a string.</li>
 * <li>Normalize whitespace in text values.</li>
 * <li>Safely handle blank string values.</li>
 * <li>Extract simple entity/class names from fully qualified names.</li>
 * </ul>
 *
 * <h3>Design</h3>
 * <p>
 * This class contains only stateless utility methods. It must not contain
 * business rules, persistence logic, module-specific behavior, or mutable
 * state.
 * </p>
 *
 * <p>
 * Module-specific utilities should remain inside their respective modules.
 * Generic utilities that can safely be reused across FreshMeal modules belong
 * here.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public final class FreshMealUtilities {

    /**
     * Private constructor to prevent instantiation.
     */
    private FreshMealUtilities() {
        throw new IllegalStateException("Utility class");
    }

    // =========================================================================
    // String Utilities
    // =========================================================================

    /**
     * Determines whether the supplied string contains actual text.
     *
     * <p>
     * Unlike {@link String#isEmpty()}, this method also treats whitespace-only
     * values as blank.
     * </p>
     *
     * @param value string to check
     * @return {@code true} when the value contains non-whitespace text;
     *         otherwise {@code false}
     */
    public static boolean hasText(final String value) {
        return StringUtils.hasText(value);
    }

    /**
     * Returns {@code null} when the supplied string is blank.
     *
     * <p>
     * Non-blank values are trimmed before being returned.
     * </p>
     *
     * @param value string to normalize
     * @return trimmed value, or {@code null} when blank
     */
    public static String nullIfBlank(final String value) {

        if (!StringUtils.hasText(value)) {
            return null;
        }

        return value.trim();
    }

    /**
     * Normalizes consecutive whitespace characters into a single space.
     *
     * <p>
     * Leading and trailing whitespace is also removed.
     * </p>
     *
     * <p>
     * Example:
     * </p>
     *
     * <pre>
     * "  Pankaj    Kumar  " → "Pankaj Kumar"
     * </pre>
     *
     * @param value string to normalize
     * @return normalized string, or {@code null} when blank
     */
    public static String normalizeWhitespace(final String value) {

        if (!StringUtils.hasText(value)) {
            return null;
        }

        return value.trim().replaceAll("\\s+", " ");
    }

    // =========================================================================
    // Name Utilities
    // =========================================================================

    /**
     * Formats an individual name field.
     *
     * <p>
     * This method is intended for already-separated fields such as a User's
     * {@code firstName} or {@code lastName}.
     * </p>
     *
     * <p>
     * Multiple words are individually capitalized.
     * </p>
     *
     * <pre>
     * "pankaj"             → "Pankaj"
     * "kUMAR"              → "KUMAR"
     * "pankaj kumar"       → "Pankaj Kumar"
     * "  pankaj   kumar "  → "Pankaj Kumar"
     * </pre>
     *
     * <p>
     * The method intentionally does not force the remaining characters to
     * lowercase. This avoids corrupting names containing meaningful casing.
     * </p>
     *
     * @param name individual name field
     * @return formatted name, or {@code null} when blank
     */
    public static String formatName(final String name) {

        String normalizedName = normalizeWhitespace(name);

        if (normalizedName == null) {
            return null;
        }

        String[] parts = normalizedName.split(" ");

        StringBuilder formattedName = new StringBuilder();

        for (String part : parts) {

            if (formattedName.length() > 0) {
                formattedName.append(" ");
            }

            formattedName.append(capitalizeFirstLetter(part));
        }

        return formattedName.toString();
    }

    /**
     * Extracts the first name from a full name.
     *
     * <p>
     * The method supports names containing multiple components and also
     * handles names beginning with an initial.
     * </p>
     *
     * <p>
     * Examples:
     * </p>
     *
     * <pre>
     * "Pankaj Kumar"       → "Pankaj"
     * "Pankaj Kumar Singh" → "Pankaj"
     * "P. Kumar"           → "P. Kumar"
     * "P. Kumar Singh"     → "P. Kumar"
     * </pre>
     *
     * @param fullName complete name
     * @return extracted first name, or {@code null} when the supplied name is
     *         blank
     */
    public static String firstNameFromString(final String fullName) {

        String normalizedName = normalizeWhitespace(fullName);

        if (normalizedName == null) {
            return null;
        }

        String[] parts = normalizedName.split(" ");

        if (parts.length == 0) {
            return null;
        }

        /*
         * When the first component is an initial, retain the second component
         * as part of the first name.
         */
        if (parts[0].length() <= 1 && parts.length > 1) {

            return (capitalizeFirstLetter(parts[0])
                    + " "
                    + capitalizeFirstLetter(parts[1])).strip();
        }

        return capitalizeFirstLetter(parts[0]);
    }

    /**
     * Extracts the last name from a full name.
     *
     * <p>
     * Multiple components after the first name are retained as the last name.
     * When the name starts with an initial, the second component is treated as
     * part of the first name.
     * </p>
     *
     * <p>
     * Examples:
     * </p>
     *
     * <pre>
     * "Pankaj Kumar"       → "Kumar"
     * "Pankaj Kumar Singh" → "Kumar Singh"
     * "P. Kumar"           → null
     * "P. Kumar Singh"     → "Singh"
     * </pre>
     *
     * @param fullName complete name
     * @return extracted last name, or {@code null} when no last name exists
     */
    public static String lastNameFromString(final String fullName) {

        String normalizedName = normalizeWhitespace(fullName);

        if (normalizedName == null) {
            return null;
        }

        String[] parts = normalizedName.split(" ");

        if (parts.length <= 1) {
            return null;
        }

        int startIndex = 1;

        /*
         * When the first component is an initial, the second component belongs
         * to the first name.
         */
        if (parts[0].length() <= 1) {

            if (parts.length <= 2) {
                return null;
            }

            startIndex = 2;
        }

        StringBuilder lastName = new StringBuilder();

        for (int i = startIndex; i < parts.length; i++) {

            if (lastName.length() > 0) {
                lastName.append(" ");
            }

            lastName.append(capitalizeFirstLetter(parts[i]));
        }

        return lastName.length() == 0
                ? null
                : lastName.toString();
    }

    /**
     * Capitalizes the first character of the supplied string.
     *
     * <p>
     * If the supplied string is {@code null} or empty, {@code null} is
     * returned. If the first character is not a letter, the original string is
     * returned unchanged.
     * </p>
     *
     * @param value string to capitalize
     * @return string with the first character capitalized, or {@code null}
     *         when the input is null or empty
     */
    public static String capitalizeFirstLetter(final String value) {

        if (value == null || value.isEmpty()) {
            return null;
        }

        char firstChar = value.charAt(0);

        if (Character.isLetter(firstChar)) {
            return Character.toUpperCase(firstChar) + value.substring(1);
        }

        return value;
    }

    // =========================================================================
    // Entity Utilities
    // =========================================================================

    /**
     * Extracts the simple entity/class name from a fully qualified name.
     *
     * <p>
     * Example:
     * </p>
     *
     * <pre>
     * com.foodies.freshmeal.user.entity.UserEntity
     * </pre>
     *
     * <p>
     * returns:
     * </p>
     *
     * <pre>
     * UserEntity
     * </pre>
     *
     * @param value fully qualified class/entity name
     * @return simple class/entity name, or {@code null} when blank
     */
    public static String getOnlyEntityName(final String value) {

        if (!StringUtils.hasText(value)) {
            return null;
        }

        int lastDotIndex = value.lastIndexOf('.');

        if (lastDotIndex != -1) {
            return value.substring(lastDotIndex + 1);
        }

        return value;
    }

    /**
     * =========================================================================
     * Email Normalization
     * =========================================================================
     */

    /**
     * Normalizes an email address into the canonical representation used by
     * FreshMeal.
     *
     * <p>
     * Leading and trailing whitespace is removed and the email address is
     * converted to lowercase using {@link Locale#ROOT}.
     * </p>
     *
     * <p>
     * This method performs normalization only. Email-format validation remains
     * the responsibility of the corresponding validation layer.
     * </p>
     *
     * @param email email address to normalize.
     * @return normalized email address, or {@code null} when the supplied value
     *         is {@code null}.
     */
    public static String normalizeEmail(String email) {

        if (email == null) {
            return null;
        }

        return email.trim().toLowerCase(Locale.ROOT);
    }
}