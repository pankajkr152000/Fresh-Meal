package com.foodies.freshmeal.cart.service.impl;

import java.util.Collections;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.foodies.freshmeal.cart.constants.CartErrorConstants;
import com.foodies.freshmeal.cart.constants.CartStatusConstant;
import com.foodies.freshmeal.cart.dto.AddCartItemRequest;
import com.foodies.freshmeal.cart.dto.CartResponse;
import com.foodies.freshmeal.cart.dto.CartSummaryResponse;
import com.foodies.freshmeal.cart.dto.RemoveCartItemRequest;
import com.foodies.freshmeal.cart.dto.UpdateCartItemRequest;
import com.foodies.freshmeal.cart.entity.CartEntity;
import com.foodies.freshmeal.cart.entity.CartItem;
import com.foodies.freshmeal.cart.mapper.CartMapper;
import com.foodies.freshmeal.cart.repository.ICartRepository;
import com.foodies.freshmeal.cart.service.ICartService;
import com.foodies.freshmeal.cart.validation.CartValidator;
import com.foodies.freshmeal.cart.valueObject.FoodSnapshot;
import com.foodies.freshmeal.common.constants.SequenceConstants;
import com.foodies.freshmeal.common.date.AppCalendar;
import com.foodies.freshmeal.common.exception.BusinessException;
import com.foodies.freshmeal.common.exception.ResourceNotFoundException;
import com.foodies.freshmeal.common.io.service.IServiceContext;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.common.io.service.impl.ServiceOutput;
import com.foodies.freshmeal.common.sequence.service.IDatabaseSequenceService;
import com.foodies.freshmeal.common.valueObject.Money;
import com.foodies.freshmeal.food.constants.FoodStatusConstant;
import com.foodies.freshmeal.food.entity.FoodEntity;
import com.foodies.freshmeal.food.repository.IFoodRepository;
import com.foodies.freshmeal.user.entity.UserProfile;

@Service
public class CartServiceImpl implements ICartService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CartServiceImpl.class);

    private final IServiceContext serviceContext;
    private final IDatabaseSequenceService databaseSequenceService;
    private final ICartRepository cartRepository;
    private final IFoodRepository foodRepository;
    private final CartValidator cartValidator;
    private final CartMapper cartMapper;

    public CartServiceImpl(IServiceContext serviceContext, IDatabaseSequenceService databaseSequenceService,
            ICartRepository cartRepository, IFoodRepository foodRepository, CartValidator cartValidator,
            CartMapper cartMapper) {

        this.serviceContext = serviceContext;
        this.databaseSequenceService = databaseSequenceService;
        this.cartRepository = cartRepository;
        this.foodRepository = foodRepository;
        this.cartValidator = cartValidator;
        this.cartMapper = cartMapper;
    }

    /**
     * Adds a food item to the authenticated customer's active cart.
     *
     * <p>
     * The customer identity is always derived from the authenticated security
     * context. Client supplied ownership information is intentionally not accepted.
     * </p>
     *
     * <p>
     * The current Food state is resolved server-side before modifying the cart.
     * Consequently, the client cannot control the stored price, restaurant, branch,
     * food name, image, availability, or other server-owned properties.
     * </p>
     *
     * <p>
     * When no active cart exists, the service creates one. The active-cart
     * uniqueness constraint remains the final authority when concurrent requests
     * attempt to create the first cart for the same customer.
     * </p>
     *
     * <p>
     * Existing cart modifications use MongoDB optimistic locking through the
     * inherited {@code @Version} field. An optimistic-lock conflict is deliberately
     * not retried because this operation is additive and retrying it blindly could
     * apply the requested quantity more than once.
     * </p>
     *
     * @param input service input containing the add-item request
     * @return service output containing the updated cart response
     */
    @Override
    public IServiceOutput<CartResponse> addItem(final IServiceInput<AddCartItemRequest> input) {

        Objects.requireNonNull(input, "Cart add-item service input must not be null.");

        /*
         * --------------------------------------------------------------------- 1.
         * Request validation
         * ---------------------------------------------------------------------
         *
         * CartValidator owns request-level validation such as:
         *
         * - foodNumber required - quantity required - quantity > 0
         *
         * It intentionally does not perform database/business-state validation.
         */
        cartValidator.validateAddCartItem(input);

        final AddCartItemRequest request = Objects.requireNonNull(input.getInput(),
                "Add cart item request must not be null.");

        final IServiceContext context = Objects.requireNonNull(input.getServiceContext(),
                "Service context must not be null.");

        /*
         * --------------------------------------------------------------------- 2.
         * Resolve authenticated customer
         * ---------------------------------------------------------------------
         */
        final UserProfile userProfile = resolveAuthenticatedUser(context);

        final String userNumber = userProfile.getUserNumber();

        LOGGER.info("Adding food [{}] to cart for authenticated user [{}]. quantity={}", request.getFoodNumber(),
                userNumber, request.getQuantity());

        /*
         * --------------------------------------------------------------------- 3.
         * Resolve the customer's active cart
         * ---------------------------------------------------------------------
         */
        CartEntity cart = findActiveCart(userNumber);

        /*
         * --------------------------------------------------------------------- 4.
         * Resolve the Food from the database
         * ---------------------------------------------------------------------
         *
         * Nothing concerning price, restaurant, branch, availability, name, or image is
         * accepted from the request.
         */
        final FoodEntity food = loadActiveFood(request.getFoodNumber());

        /*
         * --------------------------------------------------------------------- 5.
         * Validate Food orderability
         * ---------------------------------------------------------------------
         */
        validateFoodOrderability(food);

        /*
         * --------------------------------------------------------------------- 6.
         * Resolve the server-side price snapshot
         * ---------------------------------------------------------------------
         */
        final Money unitPrice = Money.of(food.getPrice());

        /*
         * --------------------------------------------------------------------- 7.
         * Resolve restaurant/branch context from Food
         * ---------------------------------------------------------------------
         */
        final String restaurantNumber = food.getRestaurantNumber();
        final String restaurantBranchNumber = food.getRestaurantBranchNumber();

        validateFoodRestaurantContext(restaurantNumber, restaurantBranchNumber);

        /*
         * --------------------------------------------------------------------- 8.
         * Build immutable Food snapshot
         * ---------------------------------------------------------------------
         *
         * CartItem stores the food information required for cart display/history
         * without coupling the cart to future Food mutations.
         */
        final FoodSnapshot foodSnapshot = buildFoodSnapshot(food);

        /*
         * --------------------------------------------------------------------- 9.
         * Create/reconcile cart when necessary
         * ---------------------------------------------------------------------
         *
         * This is intentionally done after Food validation. We do not create an empty
         * cart when the requested food itself is invalid.
         */
        if (cart == null) {

            cart = createActiveCart(userNumber, context);

        }

        /*
         * --------------------------------------------------------------------- 10.
         * Aggregate mutation
         * ---------------------------------------------------------------------
         *
         * CartEntity owns:
         *
         * - ACTIVE-state validation - restaurant/branch consistency - duplicate
         * food-line handling - quantity mutation - item-total calculation - cart
         * subtotal - total item count - total quantity
         */
        cart.addItem(foodSnapshot, unitPrice, request.getQuantity(), restaurantNumber, restaurantBranchNumber,
                AppCalendar.getBusinessLocalDateTime());

        /*
         * --------------------------------------------------------------------- 11.
         * Persist atomically with optimistic locking
         * ---------------------------------------------------------------------
         */
        final CartEntity persistedCart;

        try {

            persistedCart = cartRepository.save(cart);

        } catch (DuplicateKeyException exception) {

            /*
             * Two requests may have discovered "no active cart" at almost the same time.
             *
             * The database unique constraint is the synchronization mechanism.
             *
             * We reconcile only cart creation here. We do NOT blindly retry the entire add
             * operation because doing so could duplicate the requested quantity.
             */
            if (cartWasCreatedDuringThisOperation(cart)) {

                LOGGER.debug("Concurrent active cart creation detected for user [{}]. " + "Resolving the winning cart.",
                        userNumber);

                final CartEntity existingCart = findActiveCart(userNumber);

                if (existingCart != null) {

                    /*
                     * The winning cart may have been modified by the competing request. We
                     * deliberately do not retry this additive operation automatically.
                     */
                    throw new IllegalStateException(
                            "Concurrent cart creation detected. " + "The operation must be retried by the client.",
                            exception);
                }
            }

            throw exception;

        } catch (OptimisticLockingFailureException exception) {

            /*
             * Never retry an additive POST automatically.
             *
             * If the client retries after the original request actually succeeded, blindly
             * repeating the mutation would increase quantity twice.
             */
            LOGGER.warn("Optimistic locking conflict while adding food [{}] " + "to cart for user [{}].",
                    request.getFoodNumber(), userNumber);

            throw exception;
        }

        LOGGER.info("Food [{}] added successfully to cart [{}] for user [{}]. " + "quantity={}",
                request.getFoodNumber(), persistedCart.getCartNumber(), userNumber, request.getQuantity());

        /*
         * --------------------------------------------------------------------- 12. Map
         * aggregate → API response
         * ---------------------------------------------------------------------
         */
        return new ServiceOutput<>(cartMapper.toResponse(persistedCart));
    }

    /**
     * Resolves the authenticated FreshMeal user from the service context.
     *
     * @param context current service context
     * @return authenticated user profile
     */
    private UserProfile resolveAuthenticatedUser(final IServiceContext context) {

        final UserProfile userProfile = context.getUserProfile();

        if (userProfile == null || userProfile.getUserNumber() == null || userProfile.getUserNumber().isBlank()) {

            throw new BusinessException(CartErrorConstants.CART_ACCESS_DENIED);
        }

        return userProfile;
    }

    /**
     * Finds the authenticated customer's active cart.
     *
     * @param userNumber authenticated customer number
     * @return active cart, or {@code null} when none exists
     */
    private CartEntity findActiveCart(final String userNumber) {

        return cartRepository.findByUserNumberAndStatus(userNumber, CartStatusConstant.ACTIVE).orElse(null);
    }

    /**
     * Creates a new active cart for the authenticated customer.
     *
     * <p>
     * The cart identifier is generated through the existing FreshMeal sequence
     * infrastructure rather than by accepting an identifier from the client.
     * </p>
     *
     * @param userNumber authenticated customer number
     * @param context    current service context
     * @return newly created cart
     */
    private CartEntity createActiveCart(final String userNumber, final IServiceContext context) {

        final long sequence = databaseSequenceService.generateSequence(context, SequenceConstants.CART_SEQUENCE);

        final String cartNumber = String.format(SequenceConstants.CART_NUMBER_PATTERN, sequence);

        LOGGER.debug("Creating new active cart [{}] for user [{}].", cartNumber, userNumber);

        return CartEntity.create(cartNumber, userNumber);
    }

    /**
     * Loads a non-deleted Food using its business food number.
     *
     * @param foodNumber food business identifier
     * @return active Food entity
     */
    private FoodEntity loadActiveFood(final String foodNumber) {

        final Query query = Query.query(new Criteria().andOperator(Criteria.where("foodNumber").is(foodNumber),
                Criteria.where("deletedFlag").is(false)));

        return foodRepository.findOne(query)
                .orElseThrow(() -> new ResourceNotFoundException(CartErrorConstants.FOOD_NOT_FOUND));
    }

    /**
     * Validates whether the resolved Food can currently be added to a cart.
     *
     * @param food resolved Food entity
     */
    private void validateFoodOrderability(final FoodEntity food) {

        if (food == null) {

            throw new ResourceNotFoundException(CartErrorConstants.FOOD_NOT_FOUND);
        }

        if (!FoodStatusConstant.AVAILABLE.equals(food.getStatus())) {

            throw new BusinessException(CartErrorConstants.FOOD_OUT_OF_STOCK);
        }

        if (food.getPrice() < 0) {

            throw new BusinessException(CartErrorConstants.FOOD_PRICE_INVALID);
        }
    }

    /**
     * Validates that the Food contains the restaurant context required by Cart.
     *
     * @param restaurantNumber       restaurant business identifier
     * @param restaurantBranchNumber branch business identifier
     */
    private void validateFoodRestaurantContext(final String restaurantNumber, final String restaurantBranchNumber) {

        if (restaurantNumber == null || restaurantNumber.isBlank() || restaurantBranchNumber == null
                || restaurantBranchNumber.isBlank()) {

            throw new BusinessException(CartErrorConstants.RESTAURANT_MISMATCH);
        }
    }

    /**
     * Builds the Cart food snapshot from server-owned Food state.
     *
     * @param food resolved Food entity
     * @return immutable food snapshot
     */
    private FoodSnapshot buildFoodSnapshot(final FoodEntity food) {

        return FoodSnapshot.builder().foodNumber(food.getFoodNumber()).foodName(food.getFoodName())
                .foodImage(food.getFoodImage()).build();
    }

    /**
     * Determines whether the supplied cart was created during this operation.
     *
     * @param cart cart being persisted
     * @return true when the cart represents a newly created aggregate
     */
    private boolean cartWasCreatedDuringThisOperation(final CartEntity cart) {

        return cart.getVersion() == null;
    }

    /**
     * Updates the final desired quantity of an existing item in the authenticated
     * customer's active cart.
     *
     * <p>
     * The supplied quantity represents the final quantity that the customer wants
     * for the item. It is not treated as an increment.
     * </p>
     *
     * <p>
     * The Food is resolved again before the cart is modified. This is important
     * because a food may have become unavailable or its price may have changed
     * after it was originally added to the cart.
     * </p>
     *
     * <p>
     * The current server-side Food price is refreshed into the CartItem price
     * snapshot. Final checkout pricing remains the responsibility of Checkout.
     * </p>
     *
     * @param input service input containing the quantity update request
     *
     * @return service output containing the updated cart response
     */
    @Override
    public IServiceOutput<CartResponse> updateItemQuantity(final IServiceInput<UpdateCartItemRequest> input) {

        Objects.requireNonNull(input, "Cart update-item service input must not be null.");

        /*
         * --------------------------------------------------------------------- 1.
         * Request validation
         * ---------------------------------------------------------------------
         *
         * CartValidator validates only request-level rules:
         *
         * - foodNumber must be present - quantity must be greater than zero
         *
         * Business state is deliberately validated below.
         */
        cartValidator.validateUpdateCartItem(input);

        final UpdateCartItemRequest request = Objects.requireNonNull(input.getInput(),
                "Update cart item request must not be null.");

        final IServiceContext context = Objects.requireNonNull(input.getServiceContext(),
                "Service context must not be null.");

        /*
         * --------------------------------------------------------------------- 2.
         * Resolve authenticated customer
         * ---------------------------------------------------------------------
         */
        final UserProfile userProfile = resolveAuthenticatedUser(context);

        final String userNumber = userProfile.getUserNumber();

        LOGGER.info("Updating cart item [{}] quantity to [{}] for user [{}].", request.getFoodNumber(),
                request.getQuantity(), userNumber);

        /*
         * --------------------------------------------------------------------- 3.
         * Resolve the customer's ACTIVE cart
         * ---------------------------------------------------------------------
         *
         * We never accept cartNumber or userNumber from the client.
         */
        final CartEntity cart = findActiveCart(userNumber);

        if (cart == null) {

            throw new ResourceNotFoundException(CartErrorConstants.CART_NOT_FOUND);
        }

        /*
         * --------------------------------------------------------------------- 4.
         * Ensure the requested item actually belongs to this cart
         * ---------------------------------------------------------------------
         *
         * We perform this check before touching Food so that a request cannot use an
         * arbitrary Food number to perform unrelated Food lookups when the customer
         * does not even have that item in their cart.
         */
        final CartItem cartItem = cart.findItem(request.getFoodNumber())
                .orElseThrow(() -> new BusinessException(CartErrorConstants.CART_ALREADY_EMPTY));

        if (cartItem == null) {

            throw new ResourceNotFoundException(CartErrorConstants.CART_ITEM_NOT_FOUND);
        }

        /*
         * --------------------------------------------------------------------- 5.
         * Resolve the current Food state
         * ---------------------------------------------------------------------
         *
         * The cart snapshot is not trusted as the current Food state.
         */
        final FoodEntity food = loadActiveFood(request.getFoodNumber());

        /*
         * --------------------------------------------------------------------- 6.
         * Revalidate Food orderability
         * ---------------------------------------------------------------------
         *
         * A Food can become unavailable after being added to a cart.
         */
        validateFoodOrderability(food);

        /*
         * --------------------------------------------------------------------- 7.
         * Resolve current server-side price
         * ---------------------------------------------------------------------
         */
        final Money currentUnitPrice = Money.of(food.getPrice());

        /*
         * --------------------------------------------------------------------- 8.
         * Resolve and validate restaurant/branch context
         * ---------------------------------------------------------------------
         */
        final String restaurantNumber = food.getRestaurantNumber();

        final String restaurantBranchNumber = food.getRestaurantBranchNumber();

        validateFoodRestaurantContext(restaurantNumber, restaurantBranchNumber);

        /*
         * The Cart aggregate remains the final authority for restaurant consistency.
         */
        cart.validateRestaurantContext(restaurantNumber, restaurantBranchNumber);

        /*
         * --------------------------------------------------------------------- 9.
         * Update the aggregate
         * ---------------------------------------------------------------------
         *
         * IMPORTANT:
         *
         * request.quantity is the FINAL desired quantity.
         *
         * We intentionally do not do:
         *
         * existingQuantity + request.quantity
         *
         * because this operation is a replacement/update operation.
         */
        cart.updateItemQuantity(request.getFoodNumber(), request.getQuantity(), currentUnitPrice);

        /*
         * --------------------------------------------------------------------- 10.
         * Refresh the price snapshot
         * ---------------------------------------------------------------------
         *
         * The Food was revalidated and its current server-side price has been resolved.
         * The CartItem should therefore hold the current price snapshot for this cart
         * modification.
         */
        // cartItem.updateUnitPrice(
        // currentUnitPrice);

        /*
         * --------------------------------------------------------------------- 11.
         * Recalculate aggregate totals
         * ---------------------------------------------------------------------
         *
         * CartEntity owns this invariant.
         */
        cart.recalculateTotals();

        /*
         * --------------------------------------------------------------------- 12.
         * Persist with optimistic locking
         * ---------------------------------------------------------------------
         *
         * We intentionally do not blindly retry an optimistic locking failure.
         *
         * The operation changes an existing aggregate and automatically retrying it
         * after an unknown commit state could produce an incorrect final quantity.
         */
        final CartEntity persistedCart;

        try {

            persistedCart = cartRepository.save(cart);

        } catch (OptimisticLockingFailureException exception) {

            LOGGER.warn(
                    "Optimistic locking conflict while updating food [{}] " + "quantity in cart [{}] for user [{}].",
                    request.getFoodNumber(), cart.getCartNumber(), userNumber);

            throw exception;
        }

        LOGGER.info("Cart item [{}] quantity successfully updated to [{}] " + "in cart [{}] for user [{}].",
                request.getFoodNumber(), request.getQuantity(), persistedCart.getCartNumber(), userNumber);

        /*
         * --------------------------------------------------------------------- 13. Map
         * aggregate to API response
         * ---------------------------------------------------------------------
         */
        return new ServiceOutput<>(cartMapper.toResponse(persistedCart));
    }

    @Override
    public IServiceOutput<CartResponse> removeItem(final IServiceInput<RemoveCartItemRequest> input) {

        Objects.requireNonNull(input, "Cart remove-item service input must not be null.");

        cartValidator.validateRemoveCartItem(input);

        final RemoveCartItemRequest request = Objects.requireNonNull(input.getInput(),
                "Remove cart item request must not be null.");

        final IServiceContext context = Objects.requireNonNull(input.getServiceContext(),
                "Service context must not be null.");

        final UserProfile userProfile = resolveAuthenticatedUser(context);

        final String userNumber = userProfile.getUserNumber();

        LOGGER.info("Removing food [{}] from cart for user [{}].", request.getFoodNumber(), userNumber);

        /*
         * Cart ownership is always derived from the authenticated user. The client
         * never provides userNumber.
         */
        final CartEntity cart = findActiveCart(userNumber);

        if (cart == null) {
            throw new ResourceNotFoundException(CartErrorConstants.CART_NOT_FOUND);
        }

        /*
         * Fail fast before touching FoodEntity.
         *
         * Removing an item does not require the food to still be available, because the
         * customer is allowed to remove stale/unavailable items from their cart.
         */
        final CartItem cartItem = cart.findItem(request.getFoodNumber())
                .orElseThrow(() -> new BusinessException(CartErrorConstants.CART_ITEM_NOT_FOUND));

        if (cartItem == null) {
            throw new ResourceNotFoundException(CartErrorConstants.CART_ITEM_NOT_FOUND);
        }

        /*
         * The aggregate owns the actual item removal and total recalculation.
         *
         * This also guarantees that: - the item is removed atomically from the
         * aggregate - subtotal is recalculated - totalItemCount is recalculated -
         * totalQuantity is recalculated - an empty cart remains persisted and reusable
         */
        cart.removeItem(request.getFoodNumber());

        final CartEntity persistedCart;

        try {
            persistedCart = cartRepository.save(cart);
        } catch (OptimisticLockingFailureException exception) {

            LOGGER.warn("Optimistic locking conflict while removing food [{}] " + "from cart [{}] for user [{}].",
                    request.getFoodNumber(), cart.getCartNumber(), userNumber);

            /*
             * Do not blindly retry.
             *
             * Another request modified the same cart after this entity was loaded. Retrying
             * a destructive operation without re-reading the latest aggregate can produce
             * incorrect behavior.
             */
            throw exception;
        }

        LOGGER.info("Food [{}] successfully removed from cart [{}] for user [{}].", request.getFoodNumber(),
                persistedCart.getCartNumber(), userNumber);

        return new ServiceOutput<>(cartMapper.toResponse(persistedCart));
    }

    @Override
    public IServiceOutput<CartResponse> clearCart(final IServiceInput<Void> input) {

        Objects.requireNonNull(input, "Cart clear service input must not be null.");

        final IServiceContext context = Objects.requireNonNull(input.getServiceContext(),
                "Service context must not be null.");

        final UserProfile userProfile = resolveAuthenticatedUser(context);

        final String userNumber = userProfile.getUserNumber();

        LOGGER.info("Clearing active cart for user [{}].", userNumber);

        /*
         * Resolve the cart using the authenticated user's identity. The client cannot
         * specify another user's cart.
         */
        final CartEntity cart = findActiveCart(userNumber);

        if (cart == null) {
            throw new ResourceNotFoundException(CartErrorConstants.CART_NOT_FOUND);
        }

        /*
         * Clearing an already-empty cart is an idempotent operation.
         *
         * Return the existing state without saving it again. This avoids an unnecessary
         * database write and version increment.
         */
        if (cart.isEmpty()) {

            LOGGER.debug("Cart [{}] is already empty for user [{}].", cart.getCartNumber(), userNumber);

            return new ServiceOutput<>(cartMapper.toResponse(cart));
        }

        /*
         * CartEntity owns item removal and aggregate total recalculation. The cart
         * remains ACTIVE and reusable.
         */
        cart.clearItems();

        final CartEntity persistedCart;

        try {
            persistedCart = cartRepository.save(cart);
        } catch (OptimisticLockingFailureException exception) {

            LOGGER.warn("Optimistic locking conflict while clearing cart [{}] " + "for user [{}].",
                    cart.getCartNumber(), userNumber);

            /*
             * Do not retry blindly. A concurrent request may have added, removed, or
             * updated an item after this cart was read.
             */
            throw exception;
        }

        LOGGER.info("Cart [{}] successfully cleared for user [{}].", persistedCart.getCartNumber(), userNumber);

        return new ServiceOutput<>(cartMapper.toResponse(persistedCart));
    }

    @Override
    public IServiceOutput<CartResponse> getActiveCart(final IServiceInput<Void> input) {

        Objects.requireNonNull(input, "Get active cart service input must not be null.");

        final IServiceContext context = Objects.requireNonNull(input.getServiceContext(),
                "Service context must not be null.");

        /*
         * Resolve the authenticated customer.
         *
         * Cart ownership is derived exclusively from the security context. The client
         * must never be allowed to select another user's cart.
         */
        final UserProfile userProfile = resolveAuthenticatedUser(context);

        final String userNumber = userProfile.getUserNumber();

        LOGGER.debug("Retrieving active cart for user [{}].", userNumber);

        /*
         * Retrieve the customer's existing active cart.
         *
         * This operation is intentionally read-only. It must not create a MongoDB
         * document merely because the customer opened the cart.
         */
        final CartEntity cart = findActiveCart(userNumber);

        /*
         * A customer without a persisted cart is a normal business state, not an
         * application error.
         *
         * Return a consistent empty-cart response so the frontend can render the cart
         * page without special error handling.
         */
        if (cart == null) {

            LOGGER.debug("No active cart found for user [{}]. " + "Returning an empty cart response.", userNumber);

            return new ServiceOutput<>(createEmptyCartResponse());
        }

        /*
         * Map the persisted aggregate without modifying it. The stored totals are
         * authoritative for this read operation.
         */
        final CartResponse response = cartMapper.toResponse(cart);

        LOGGER.debug("Active cart [{}] retrieved successfully for user [{}].", cart.getCartNumber(), userNumber);

        return new ServiceOutput<>(response);
    }

    /**
     * Creates a consistent empty-cart response for an authenticated
     * customer who does not yet have a persisted active cart.
     *
     * <p>
     * This method does not create or persist a CartEntity.
     * The first successful add-item operation is responsible for
     * creating the customer's persistent cart.
     * </p>
     *
     * @return an empty cart response with zero totals
     */
    private CartResponse createEmptyCartResponse() {

        return CartResponse.builder()
                .cartNumber(null)
                .restaurantNumber(null)
                .restaurantBranchNumber(null)
                .status(CartStatusConstant.ACTIVE)
                .expiresAt(null)
                .items(Collections.emptyList())
                .subtotal(Money.defaultMoney())
                .totalItemCount(0)
                .totalQuantity(0)
                .build();
    }

    @Override
    public IServiceOutput<CartSummaryResponse> getCartSummary(
            final IServiceInput<Void> input) {

        Objects.requireNonNull(input, "Get cart summary service input must not be null.");

        final IServiceContext context = Objects.requireNonNull(input.getServiceContext(),
                "Service context must not be null.");

        /*
         * Resolve the authenticated customer.
         *
         * The service must never trust a user identifier supplied by the client for
         * cart ownership.
         */
        final UserProfile userProfile = resolveAuthenticatedUser(context);

        final String userNumber = userProfile.getUserNumber();

        LOGGER.debug("Retrieving cart summary for user [{}].", userNumber);

        /*
         * Retrieve the customer's active cart.
         *
         * This is a read-only operation and must not create a cart.
         */
        final CartEntity cart = findActiveCart(userNumber);

        /*
         * No active cart is a valid business state.
         *
         * Return a consistent empty summary instead of treating this situation as an
         * application error.
         */
        if (cart == null) {

            LOGGER.debug("No active cart found for user [{}]. " + "Returning an empty cart summary.", userNumber);

            return new ServiceOutput<>(createEmptyCartSummaryResponse());
        }

        /*
         * Map only summary information.
         *
         * The mapper must not load or traverse unrelated entities, perform database
         * operations, or recalculate aggregate totals.
         */
        final CartSummaryResponse response = cartMapper.toSummaryResponse(cart);

        LOGGER.debug("Cart summary retrieved successfully for cart [{}] " + "and user [{}].", cart.getCartNumber(),
                userNumber);

        return new ServiceOutput<>(response);
    }

    /**
     * Creates a consistent empty-cart summary for an authenticated
     * customer who does not yet have a persisted active cart.
     *
     * <p>
     * This method does not create or persist a CartEntity.
     * The first successful add-item operation is responsible for
     * creating the customer's persistent cart.
     * </p>
     *
     * @return an empty cart summary with zero totals
     */
    private CartSummaryResponse createEmptyCartSummaryResponse() {

        return CartSummaryResponse.builder()
                .cartNumber(null)
                .status(CartStatusConstant.ACTIVE)
                .totalItemCount(0)
                .totalQuantity(0)
                .subtotal(Money.defaultMoney())
                .build();
    }
}