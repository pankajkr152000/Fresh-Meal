package com.foodies.freshmeal.common.util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.foodies.freshmeal.common.contract.IDisplayOption;
import com.foodies.freshmeal.common.dto.DisplayOptionResponse;

/**
 * ============================================================================
 * Utility : DisplayOptionMapperUtil
 * ============================================================================
 *
 * Provides reusable utility methods for converting enums implementing
 * {@link IDisplayOption} into UI-friendly {@link DisplayOptionResponse}
 * objects.
 *
 * <p>
 * Features:
 * <ul>
 * <li>Generic conversion of enums to display options.</li>
 * <li>Alphabetical sorting based on display labels.</li>
 * <li>Places "Other" as the last option using the common comparator.</li>
 * <li>Preserves sorted order using {@link LinkedHashSet}.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Typical use cases:
 * <ul>
 * <li>Food Categories</li>
 * <li>Diet Categories</li>
 * <li>Cuisine Categories</li>
 * <li>Food Status</li>
 * <li>Restaurant Status</li>
 * <li>Order Status</li>
 * </ul>
 * </p>
 *
 * <p>
 * This is a utility class and cannot be instantiated.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 1.0
 *        ============================================================================
 */
public final class DisplayOptionMapperUtil {

    /**
     * Prevents instantiation.
     */
    private DisplayOptionMapperUtil() {
        throw new AssertionError("Utility class should not be instantiated.");
    }

    /**
     * Converts the specified enum into an alphabetically sorted collection of
     * {@link DisplayOptionResponse}.
     *
     * <p>
     * Sorting is performed using the display label while ensuring that "Other"
     * always appears at the end of the collection.
     * </p>
     *
     * @param <E>       enum type implementing {@link DisplayOption}
     * @param enumClass enum class to convert
     *
     * @return ordered set of display options
     */
    public static <E extends Enum<E> & IDisplayOption> Set<DisplayOptionResponse> toDisplayOptions(Class<E> enumClass) {

        return Arrays.stream(enumClass.getEnumConstants())
                .sorted(Comparator.comparing((IDisplayOption option) -> option.getLabel(),
                        CommonUtils.alphabeticalWithOtherLast()))
                .map(option -> new DisplayOptionResponse(option.getLabel(), option.getValue()))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * Resolves an enum constant from either its display label or internal value.
     *
     * <p>
     * The comparison is case-insensitive.
     * </p>
     *
     * <p>
     * Example:
     * 
     * <pre>
     * FoodStatus status = DisplayOptionMapperUtil.fromValue(FoodStatus.class, "Out of Stock");
     *
     * FoodStatus status = DisplayOptionMapperUtil.fromValue(FoodStatus.class, "OUT_OF_STOCK");
     * </pre>
     * </p>
     *
     * @param <E>       enum type implementing {@link IDisplayOption}
     * @param enumClass enum class
     * @param value     label or value
     *
     * @return matching enum constant
     *
     * @throws IllegalArgumentException if no matching constant exists
     */
    public static <E extends Enum<E> & IDisplayOption> E fromValue(Class<E> enumClass, String value) {

        return Arrays.stream(enumClass.getEnumConstants())
                .filter(option -> option.getValue().equalsIgnoreCase(value)
                        || option.getLabel().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No enum constant found for value: " + value));
    }
    
   /*
    * Converts enum to value & label, value->backend, label->UI
    */
    public static DisplayOptionResponse from(IDisplayOption option) {
        return new DisplayOptionResponse(
                option.getLabel(),
                option.getValue());
    }
    

}