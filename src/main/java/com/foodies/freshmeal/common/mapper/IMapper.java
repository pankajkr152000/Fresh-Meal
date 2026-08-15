package com.foodies.freshmeal.common.mapper;

/**
 * ============================================================================
 * Interface : IMapper
 * ============================================================================
 *
 * Defines a lightweight generic mapping contract used by the FreshMeal
 * application.
 *
 * <p>
 * The interface intentionally contains only the fundamental source-to-target
 * mapping operation. Domain-specific mappers remain responsible for defining
 * their own specialized mapping methods.
 * </p>
 *
 * <p>
 * This avoids forcing every domain into an artificial one-to-one mapping
 * structure when a single entity may have multiple API representations.
 * </p>
 *
 * @param <S> source type
 * @param <T> target type
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@FunctionalInterface
public interface IMapper<S, T> {

    /**
     * Maps the supplied source object to the target representation.
     *
     * @param source source object
     *
     * @return mapped target object
     */
    T map(S source);

}