package com.foodies.freshmeal.checkout.service.impl;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.foodies.freshmeal.authentication.constants.AuthenticationErrorConstants;
import com.foodies.freshmeal.cart.constants.CartStatusConstant;
import com.foodies.freshmeal.cart.entity.CartEntity;
import com.foodies.freshmeal.cart.entity.CartItem;
import com.foodies.freshmeal.cart.repository.ICartRepository;
import com.foodies.freshmeal.checkout.constants.CheckoutErrorConstants;
import com.foodies.freshmeal.checkout.constants.CheckoutStatusConstant;
import com.foodies.freshmeal.checkout.dto.CheckoutResponse;
import com.foodies.freshmeal.checkout.dto.CheckoutReviewResponse;
import com.foodies.freshmeal.checkout.dto.ConfirmCheckoutRequest;
import com.foodies.freshmeal.checkout.entity.CheckoutEntity;
import com.foodies.freshmeal.checkout.mapper.CheckoutMapper;
import com.foodies.freshmeal.checkout.repository.ICheckoutRepository;
import com.foodies.freshmeal.checkout.service.ICheckoutService;
import com.foodies.freshmeal.checkout.validation.CheckoutValidator;
import com.foodies.freshmeal.checkout.valueObject.CheckoutAddressSnapshot;
import com.foodies.freshmeal.checkout.valueObject.CheckoutItemSnapshot;
import com.foodies.freshmeal.checkout.valueObject.CheckoutPricingSnapshot;
import com.foodies.freshmeal.common.constants.MoneyPrecision;
import com.foodies.freshmeal.common.constants.SequenceConstants;
import com.foodies.freshmeal.common.date.AppCalendar;
import com.foodies.freshmeal.common.dto.DisplayOptionResponse;
import com.foodies.freshmeal.common.enums.EntityName;
import com.foodies.freshmeal.common.exception.BusinessException;
import com.foodies.freshmeal.common.io.service.IServiceContext;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.common.io.service.impl.ServiceInput;
import com.foodies.freshmeal.common.io.service.impl.ServiceOutput;
import com.foodies.freshmeal.common.sequence.service.IDatabaseSequenceService;
import com.foodies.freshmeal.common.util.FreshMealUtilities;
import com.foodies.freshmeal.common.util.MoneyUtil;
import com.foodies.freshmeal.common.valueObject.Money;
import com.foodies.freshmeal.food.constants.FoodStatusConstant;
import com.foodies.freshmeal.food.dto.FoodIdRequest;
import com.foodies.freshmeal.food.entity.FoodEntity;
import com.foodies.freshmeal.food.service.IFoodService;
import com.foodies.freshmeal.restaurant.constants.RestaurantStatusConstant;
import com.foodies.freshmeal.restaurant.dto.RestaurantBranchDetailsResponse;
import com.foodies.freshmeal.restaurant.dto.RestaurantBranchIdRequest;
import com.foodies.freshmeal.restaurant.dto.RestaurantDetailsResponse;
import com.foodies.freshmeal.restaurant.dto.RestaurantIdRequest;
import com.foodies.freshmeal.restaurant.service.IRestaurantBranchService;
import com.foodies.freshmeal.restaurant.service.IRestaurantService;
import com.foodies.freshmeal.user.dto.AddressResponse;
import com.foodies.freshmeal.user.service.IAddressService;

@Service
public class CheckoutServiceImpl implements ICheckoutService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutServiceImpl.class);

    private final ICartRepository cartRepository;
    private final ICheckoutRepository checkoutRepository;
    private final IDatabaseSequenceService databaseSequenceService;
    private final CheckoutMapper checkoutMapper;
    private final CheckoutValidator checkoutRequestValidator;
    private final Duration checkoutSessionExpiry;
    private final IRestaurantService restaurantService;
    private final IRestaurantBranchService restaurantBranchService;
    private final IFoodService foodService;
    private final IAddressService addressService;

    public CheckoutServiceImpl(
            final ICartRepository cartRepository,
            final ICheckoutRepository checkoutRepository,
            final IDatabaseSequenceService databaseSequenceService,
            final CheckoutMapper checkoutMapper,
            final CheckoutValidator checkoutRequestValidator,
            final IRestaurantService restaurantService,
            final IRestaurantBranchService restaurantBranchService,
            final IFoodService foodService,
            final IAddressService addressService,
            @Value("${freshmeal.checkout.session-expiry:PT15M}") final Duration checkoutSessionExpiry) {

        this.cartRepository = Objects.requireNonNull(
                cartRepository, "Cart repository must not be null.");

        this.checkoutRepository = Objects.requireNonNull(
                checkoutRepository, "Checkout repository must not be null.");

        this.databaseSequenceService = Objects.requireNonNull(
                databaseSequenceService, "Database sequence service must not be null.");

        this.checkoutMapper = Objects.requireNonNull(
                checkoutMapper, "Checkout mapper must not be null.");

        this.checkoutRequestValidator = Objects.requireNonNull(
                checkoutRequestValidator, "Checkout request validator must not be null.");

        this.checkoutSessionExpiry = Objects.requireNonNull(
                checkoutSessionExpiry, "Checkout session expiry must not be null.");

        this.restaurantService = Objects.requireNonNull(
                restaurantService, "Restaurant service must not be null.");

        this.restaurantBranchService = Objects.requireNonNull(
                restaurantBranchService, "Restaurant branch service must not be null.");

        this.foodService = Objects.requireNonNull(
                foodService, "Food service must not be null.");

        this.addressService = Objects.requireNonNull(
                addressService, "Address service must not be null.");
    }

    /**
     * Initiates a checkout session for the authenticated customer.
     *
     * <p>
     * The customer's identity is resolved exclusively from the service context.
     * The operation requires an active cart containing at least one item and
     * valid restaurant context.
     * </p>
     *
     * <p>
     * Checkout initiation is a persisted state transition. It does not create
     * an order or perform final price validation.
     * </p>
     *
     * @param input service input containing the authenticated service context
     * @return service output containing the newly initiated checkout
     */
    @Override
    public IServiceOutput<CheckoutResponse> initiateCheckout(final IServiceInput<Void> input) {

        Objects.requireNonNull(input, "Checkout service input must not be null.");

        IServiceContext context = input.getServiceContext();

        if (context == null) {
            throw new BusinessException(CheckoutErrorConstants.CHECKOUT_INITIALIZATION_FAILED);
        }

        // ---------------------------------------------------------------------
        // 1. Resolve authenticated customer
        // ---------------------------------------------------------------------

        String userNumber = resolveAuthenticatedUser(context);

        // ---------------------------------------------------------------------
        // 2. Load the customer's active cart
        // ---------------------------------------------------------------------

        CartEntity cart = cartRepository
                .findByUserNumberAndStatus(userNumber, CartStatusConstant.ACTIVE)
                .orElseThrow(() -> new BusinessException(CheckoutErrorConstants.CART_NOT_FOUND));

        // ---------------------------------------------------------------------
        // 3. Validate cart eligibility
        // ---------------------------------------------------------------------

        if (cart.isEmpty()) {
            throw new BusinessException(CheckoutErrorConstants.CART_EMPTY);
        }

        if (!cart.isActive()) {
            throw new BusinessException(CheckoutErrorConstants.CART_NOT_ACTIVE);
        }

        if (!cart.hasRestaurantContext()) {
            throw new BusinessException(CheckoutErrorConstants.CART_CHECKOUT_CONTEXT_INVALID);
        }

        // ---------------------------------------------------------------------
        // 4. Inspect previous checkout attempts
        // ---------------------------------------------------------------------

        List<CheckoutEntity> previousAttempts = checkoutRepository.findByCartNumber(cart.getCartNumber());

        /*
         * Inspect the existing attempts and reject an unresolved attempt.
         *
         * Do not reject merely because historical attempts exist. Completed,
         * cancelled, and expired attempts are part of the audit history.
         *
         * The exact unresolved-state predicate should use the current
         * CheckoutStatusConstant lifecycle and reconciliation rules.
         */

        validateNoUnresolvedCheckout(previousAttempts);

        // ---------------------------------------------------------------------
        // 5. Generate checkout number
        // ---------------------------------------------------------------------

        long sequence = databaseSequenceService.generateSequence(
                context,
                SequenceConstants.CHECKOUT_SEQUENCE);

        String checkoutId = String.format(
                SequenceConstants.CHECKOUT_DB_ID_PATTERN,
                sequence);

        String checkoutNumber = String.format(
                SequenceConstants.CHECKOUT_NUMBER_PATTERN,
                sequence);

        // ---------------------------------------------------------------------
        // 6. Calculate expiry using configured duration
        // ---------------------------------------------------------------------

        LocalDateTime initiatedAt = AppCalendar.getBusinessLocalDateTime();

        LocalDateTime expiresAt = initiatedAt.plus(checkoutSessionExpiry);

        // ---------------------------------------------------------------------
        // 7. Create checkout entity
        // ---------------------------------------------------------------------

        CheckoutEntity checkout = CheckoutEntity.create(
                checkoutId,
                checkoutNumber,
                userNumber,
                cart.getCartNumber(),
                cart.getRestaurantNumber(),
                cart.getRestaurantBranchNumber(),
                expiresAt);

        // ---------------------------------------------------------------------
        // 8. Transition cart into checkout
        // ---------------------------------------------------------------------

        cart.startCheckout();

        // ---------------------------------------------------------------------
        // 9. Persist checkout and cart
        // ---------------------------------------------------------------------

        CheckoutEntity savedCheckout = checkoutRepository.save(checkout);

        cartRepository.save(cart);

        // ---------------------------------------------------------------------
        // 10. Build response
        // ---------------------------------------------------------------------

        CheckoutResponse response = checkoutMapper.toCheckoutResponse(savedCheckout);

        LOGGER.info("Checkout initiated successfully. checkoutNumber={}, cartNumber={}, userNumber={}",
                savedCheckout.getCheckoutNumber(),
                savedCheckout.getCartNumber(),
                userNumber);

        return new ServiceOutput<>(response);
    }

    /**
     * Resolves the authenticated user's unique identifier from the service context.
     *
     * <p>
     * The user identifier is never accepted from client input. This ensures
     * that checkout operations are always performed on behalf of the authenticated
     * principal.
     * </p>
     *
     * @param context the service context containing authenticated user information
     * @return the authenticated user's unique identifier
     * @throws BusinessException if the context or authenticated user information
     *                           is unavailable
     */
    private String resolveAuthenticatedUser(final IServiceContext context) {

        if (context == null
                || context.getUserProfile() == null
                || context.getUserProfile().getUserNumber() == null
                || context.getUserProfile().getUserNumber().isBlank()) {

            LOGGER.warn("Checkout access denied due to missing authenticated user.");

            throw new BusinessException(CheckoutErrorConstants.CHECKOUT_ACCESS_DENIED);
        }

        return context.getUserProfile().getUserNumber();
    }

    /**
     * Ensures that the cart does not have a previous checkout attempt that is
     * still active or requires reconciliation before another attempt can begin.
     *
     * <p>
     * Completed, cancelled, and expired attempts do not block a new checkout.
     * An unexpired checkout awaiting confirmation blocks duplicate initiation.
     * Attempts in intermediate or uncertain states must be reconciled rather
     * than silently ignored.
     * </p>
     *
     * @param previousAttempts all checkout attempts associated with the cart
     * @throws BusinessException if an unresolved checkout attempt exists
     */
    @SuppressWarnings("UnnecessaryContinue")
    private void validateNoUnresolvedCheckout(
            final List<CheckoutEntity> previousAttempts) {

        if (previousAttempts == null || previousAttempts.isEmpty()) {
            return;
        }

        LocalDateTime now = AppCalendar.getBusinessLocalDateTime();

        for (CheckoutEntity previousAttempt : previousAttempts) {

            if (previousAttempt == null) {
                continue;
            }

            CheckoutStatusConstant status = previousAttempt.getStatus();

            if (status == null) {
                LOGGER.error("Checkout attempt has no status. checkoutNumber={}",
                        previousAttempt.getCheckoutNumber());

                throw new BusinessException(CheckoutErrorConstants.CHECKOUT_INITIALIZATION_FAILED);
            }

            switch (status) {

                case ORDER_CREATED, CANCELLED, EXPIRED -> {
                    // Terminal attempts do not prevent a new checkout.
                    LOGGER.info("Checkout initiation status is : {}", status.getLabel());
                    continue;
                }
                case READY_FOR_CONFIRMATION -> {
                    if (previousAttempt.getExpiresAt() != null
                            && previousAttempt.getExpiresAt().isAfter(now)) {

                        LOGGER.info(
                                "Checkout initiation blocked by an existing "
                                        + "ready checkout. checkoutNumber={}, cartNumber={}",
                                previousAttempt.getCheckoutNumber(),
                                previousAttempt.getCartNumber());

                        throw new BusinessException(CheckoutErrorConstants.CART_ALREADY_IN_CHECKOUT);
                    }

                    // An expired READY_FOR_CONFIRMATION attempt must be marked
                    // EXPIRED through the appropriate lifecycle operation.
                    // Do not silently mutate or ignore it here.
                    throw new BusinessException(CheckoutErrorConstants.CART_ALREADY_IN_CHECKOUT);
                }
                case INITIATED, VALIDATING, CONFIRMATION_IN_PROGRESS, FAILED -> {
                    LOGGER.warn("Checkout initiation blocked by an unresolved "
                            + "checkout attempt. checkoutNumber={}, status={}",
                            previousAttempt.getCheckoutNumber(),
                            status);

                    throw new BusinessException(CheckoutErrorConstants.CART_ALREADY_IN_CHECKOUT);
                }

                default -> {
                    LOGGER.error("Unsupported checkout status encountered. "
                            + "checkoutNumber={}, status={}",
                            previousAttempt.getCheckoutNumber(),
                            status);

                    throw new BusinessException(CheckoutErrorConstants.CHECKOUT_INITIALIZATION_FAILED);
                }
            }
        }
    }

    /**
     * Retrieves an existing checkout session belonging to the authenticated user.
     *
     * <p>
     * This operation is strictly read-only. It does not modify the checkout
     * status, update expiry information, or change the associated cart.
     * </p>
     *
     * @param input service input containing the checkout number and service context
     * @return service output containing the checkout response
     * @throws BusinessException if the input is invalid, the checkout does not
     *                           exist, or the authenticated user does not own it
     */
    @Override
    public IServiceOutput<CheckoutResponse> getCheckout(final IServiceInput<String> input) {

        Objects.requireNonNull(input, "Checkout service input must not be null.");

        // Step 1: Resolve and validate the service context.
        IServiceContext context = input.getServiceContext();

        if (context == null) {
            throw new BusinessException(CheckoutErrorConstants.CHECKOUT_CONTEXT_REQUIRED);
        }

        // Step 2: Resolve the authenticated user.
        String userNumber = resolveAuthenticatedUser(context);

        // Step 3: Validate the checkout number.
        String checkoutNumber = input.getInput();

        checkoutRequestValidator.validateCheckoutNumber(checkoutNumber);

        // Step 4: Retrieve the checkout session.
        CheckoutEntity checkout = checkoutRepository
                .findByCheckoutNumber(checkoutNumber)
                .orElseThrow(() -> new BusinessException(CheckoutErrorConstants.CHECKOUT_NOT_FOUND));

        // Step 5: Enforce checkout ownership.
        if (!userNumber.equals(checkout.getUserNumber())) {

            LOGGER.warn("Checkout access denied. checkoutNumber={}, userNumber={}",
                    checkoutNumber,
                    userNumber);

            throw new BusinessException(CheckoutErrorConstants.CHECKOUT_NOT_FOUND);
        }

        // Step 6: Map the entity to the response DTO.
        CheckoutResponse response = checkoutMapper.toCheckoutResponse(checkout);

        LOGGER.debug("Checkout retrieved successfully. checkoutNumber={}, userNumber={}",
                checkoutNumber,
                userNumber);

        return new ServiceOutput<>(response);
    }

    /**
     * Prepares a checkout session for customer review.
     *
     * <p>
     * The operation validates the authenticated customer, resolves the checkout
     * session, verifies ownership, and ensures that the session is eligible
     * for review before loading or modifying any checkout data.
     * </p>
     *
     * <p>
     * This method is stateful. Once all review validations and calculations
     * succeed, the checkout will be transitioned to
     * {@code READY_FOR_CONFIRMATION} and the resulting snapshots will be persisted.
     * </p>
     *
     * @param input service input containing the checkout number and authenticated
     *              service context
     * @return service output containing the prepared checkout review
     * @throws BusinessException when the input, checkout, ownership, or lifecycle
     *                           validation fails
     */
    @Override
    public IServiceOutput<CheckoutReviewResponse> prepareCheckoutReview(final IServiceInput<String> input) {

        Objects.requireNonNull(input, "Checkout service input must not be null.");

        // ---------------------------------------------------------------------
        // 1. Validate service context
        // ---------------------------------------------------------------------

        IServiceContext context = input.getServiceContext();

        if (context == null) {
            throw new BusinessException(CheckoutErrorConstants.CHECKOUT_INITIALIZATION_FAILED);
        }

        // ---------------------------------------------------------------------
        // 2. Resolve authenticated customer
        // ---------------------------------------------------------------------

        String userNumber = resolveAuthenticatedUser(context);

        // ---------------------------------------------------------------------
        // 3. Validate checkout number
        // ---------------------------------------------------------------------

        String checkoutNumber = input.getInput();

        checkoutRequestValidator.validateCheckoutNumber(checkoutNumber);

        // ---------------------------------------------------------------------
        // 4. Retrieve checkout session
        // ---------------------------------------------------------------------

        CheckoutEntity checkout = checkoutRepository
                .findByCheckoutNumber(checkoutNumber)
                .orElseThrow(() -> new BusinessException(CheckoutErrorConstants.CHECKOUT_NOT_FOUND));

        // ---------------------------------------------------------------------
        // 5. Verify checkout ownership
        // ---------------------------------------------------------------------

        if (!userNumber.equals(checkout.getUserNumber())) {

            LOGGER.warn("Checkout review access denied. checkoutNumber={}, userNumber={}",
                    checkoutNumber,
                    userNumber);

            // Do not reveal whether another customer's checkout exists.
            throw new BusinessException(AuthenticationErrorConstants.AUTHENTICATION_FAILED);
        }

        // ---------------------------------------------------------------------
        // 6. Validate checkout lifecycle state
        // ---------------------------------------------------------------------
        validateCheckoutReviewEligibility(checkout);

        // ---------------------------------------------------------------------
        // 7. Begin checkout validation
        // ---------------------------------------------------------------------

        checkout.startValidation();

        // ---------------------------------------------------------------------
        // 7. Retrieve the cart associated with this checkout
        // ---------------------------------------------------------------------

        CartEntity cart = cartRepository
                .findByCartNumber(checkout.getCartNumber())
                .orElseThrow(() -> new BusinessException(CheckoutErrorConstants.CART_NOT_FOUND));

        validateCartForCheckoutReview(cart, checkout, userNumber);

        // ---------------------------------------------------------------------
        // 8. Verify cart ownership and checkout consistency
        // ---------------------------------------------------------------------

        if (!userNumber.equals(cart.getUserNumber())) {

            LOGGER.error("Checkout-cart ownership mismatch. checkoutNumber={}, cartNumber={}",
                    checkoutNumber,
                    cart.getCartNumber());

            throw new BusinessException(CheckoutErrorConstants.CHECKOUT_DATA_INCONSISTENT);
        }

        if (!checkout.getCartNumber().equals(cart.getCartNumber())) {

            LOGGER.error("Checkout-cart reference mismatch. checkoutNumber={}, cartNumber={}",
                    checkoutNumber,
                    cart.getCartNumber());

            throw new BusinessException(CheckoutErrorConstants.CHECKOUT_DATA_INCONSISTENT);
        }

        // ---------------------------------------------------------------------
        // 9. Validate cart contents
        // ---------------------------------------------------------------------

        if (cart.isEmpty()) {
            throw new BusinessException(CheckoutErrorConstants.CART_EMPTY);
        }

        // ---------------------------------------------------------------------
        // 10. Validate cart lifecycle
        // ---------------------------------------------------------------------

        if (!cart.isActive() && !cart.isCheckoutInProgress()) {

            LOGGER.warn("Cart is not eligible for checkout review. cartNumber={}, status={}",
                    cart.getCartNumber(),
                    cart.getStatus());

            throw new BusinessException(CheckoutErrorConstants.CART_NOT_ACTIVE);
        }

        // ---------------------------------------------------------------------
        // 11. Validate restaurant context
        // ---------------------------------------------------------------------

        if (!cart.hasRestaurantContext()) {

            LOGGER.error("Cart has incomplete restaurant context. cartNumber={}",
                    cart.getCartNumber());

            throw new BusinessException(CheckoutErrorConstants.CART_CHECKOUT_CONTEXT_INVALID);
        }

        // ---------------------------------------------------------------------
        // 12. Verify checkout and cart restaurant context consistency
        // ---------------------------------------------------------------------

        if (!checkout.getRestaurantNumber().equals(cart.getRestaurantNumber())
                || !checkout.getRestaurantBranchNumber().equals(cart.getRestaurantBranchNumber())) {

            LOGGER.error("Checkout-cart restaurant context mismatch. checkoutNumber={}, cartNumber={}",
                    checkoutNumber,
                    cart.getCartNumber());

            throw new BusinessException(CheckoutErrorConstants.CHECKOUT_DATA_INCONSISTENT);
        }

        // ---------------------------------------------------------------------
        // 13. Retrieve restaurant details
        // ---------------------------------------------------------------------

        RestaurantIdRequest restaurantRequest = RestaurantIdRequest.builder()
                .restaurantId(cart.getRestaurantNumber())
                .build();

        IServiceInput<RestaurantIdRequest> restaurantInput = new ServiceInput<>(
                restaurantRequest,
                context,
                input.getDataContext());

        IServiceOutput<RestaurantDetailsResponse> restaurantOutput = restaurantService.getById(restaurantInput);

        if (restaurantOutput == null || restaurantOutput.getOutput() == null) {

            LOGGER.error("Restaurant service returned an empty response. restaurantNumber={}",
                    cart.getRestaurantNumber());

            throw new BusinessException(CheckoutErrorConstants.CHECKOUT_DEPENDENCY_UNAVAILABLE);
        }

        RestaurantDetailsResponse restaurant = restaurantOutput.getOutput();

        // ---------------------------------------------------------------------
        // 14. Validate restaurant lifecycle and availability
        // ---------------------------------------------------------------------

        validateRestaurantAvailability(restaurant);

        // ---------------------------------------------------------------------
        // 15. Retrieve restaurant branch details
        // ---------------------------------------------------------------------

        RestaurantBranchIdRequest branchRequest = RestaurantBranchIdRequest.builder()
                .branchId(cart.getRestaurantBranchNumber())
                .build();

        IServiceInput<RestaurantBranchIdRequest> branchInput = new ServiceInput<>(
                branchRequest,
                context,
                input.getDataContext());

        IServiceOutput<RestaurantBranchDetailsResponse> branchOutput = restaurantBranchService.getById(branchInput);

        if (branchOutput == null || branchOutput.getOutput() == null) {

            LOGGER.error("Restaurant branch service returned an empty response. branchNumber={}",
                    cart.getRestaurantBranchNumber());

            throw new BusinessException(CheckoutErrorConstants.CHECKOUT_DEPENDENCY_UNAVAILABLE);
        }

        RestaurantBranchDetailsResponse branch = branchOutput.getOutput();

        // ---------------------------------------------------------------------
        // 16. Validate branch lifecycle, availability, and association
        // ---------------------------------------------------------------------

        validateBranchAvailability(branch, restaurant.getId());

        // ---------------------------------------------------------------------
        // 17. Revalidate all cart food items and prepare checkout snapshots
        // ---------------------------------------------------------------------

        List<CheckoutItemSnapshot> checkoutItems = new ArrayList<>();

        for (CartItem cartItem : cart.getItems()) {

            if (cartItem == null
                    || cartItem.getFoodSnapshot() == null
                    || cartItem.getFoodSnapshot().getFoodNumber() == null
                    || cartItem.getFoodSnapshot().getFoodNumber().isBlank()) {

                LOGGER.error("Invalid food snapshot found in cart. cartNumber={}",
                        cart.getCartNumber());

                throw new BusinessException(CheckoutErrorConstants.FOOD_SNAPSHOT_INVALID);
            }

            String foodNumber = cartItem.getFoodSnapshot().getFoodNumber();

            // Build the food lookup request.
            FoodIdRequest foodRequest = new FoodIdRequest();
            foodRequest.setFoodId(foodNumber);

            IServiceInput<FoodIdRequest> foodInput = new ServiceInput<>(
                    foodRequest,
                    context,
                    input.getDataContext());

            // Load the latest food record.
            IServiceOutput<FoodEntity> foodOutput = foodService.loadFood(foodInput);

            if (foodOutput == null || foodOutput.getOutput() == null) {

                LOGGER.warn("Food could not be loaded during checkout review. foodNumber={}", foodNumber);

                throw new BusinessException(CheckoutErrorConstants.FOOD_NOT_FOUND);
            }

            FoodEntity food = validateFoodForCheckout(foodOutput.getOutput(), cartItem, cart);

            // Validate quantity before calculating the item total.
            if (cartItem.getQuantity() <= 0) {

                LOGGER.error("Invalid cart item quantity. foodNumber={}, quantity={}",
                        foodNumber,
                        cartItem.getQuantity());

                throw new BusinessException(CheckoutErrorConstants.FOOD_QUANTITY_UNAVAILABLE);
            }

            // Use the current server-side price, not the cart's historical price.
            final CheckoutItemSnapshot checkoutItem = buildCheckoutItemSnapshot(food, cartItem);

            checkoutItems.add(checkoutItem);
        }

        // ---------------------------------------------------------------------
        // 18. Retrieve authenticated customer's active addresses
        // ---------------------------------------------------------------------

        IServiceInput<Void> addressInput = new ServiceInput<>(
                null,
                context,
                input.getDataContext());

        IServiceOutput<List<AddressResponse>> addressOutput = addressService.getMyAddresses(addressInput);

        if (addressOutput == null || addressOutput.getOutput() == null) {

            LOGGER.error("Address service returned an empty response during checkout review. userNumber={}",
                    userNumber);

            throw new BusinessException(CheckoutErrorConstants.CHECKOUT_DEPENDENCY_UNAVAILABLE);
        }

        List<AddressResponse> addresses = addressOutput.getOutput();

        // ---------------------------------------------------------------------
        // 19. Resolve customer's default delivery address
        // ---------------------------------------------------------------------

        final AddressResponse defaultAddress = resolveDefaultDeliveryAddress(addresses);

        // ---------------------------------------------------------------------
        // 20. Validate and snapshot delivery address
        // ---------------------------------------------------------------------

        validateDeliveryAddress(defaultAddress);

        final CheckoutAddressSnapshot addressSnapshot = buildCheckoutAddressSnapshot(defaultAddress);

        // ---------------------------------------------------------------------
        // 21. Calculate checkout pricing
        // ---------------------------------------------------------------------

        final CheckoutPricingSnapshot pricingSnapshot = calculateCheckoutPricing(checkoutItems);

        // ---------------------------------------------------------------------
        // 22. Mark checkout as ready for customer confirmation
        // ---------------------------------------------------------------------

        checkout.markReadyForConfirmation(checkoutItems, addressSnapshot, pricingSnapshot);

        // ---------------------------------------------------------------------
        // 23. Persist the validated checkout
        // ---------------------------------------------------------------------

        CheckoutEntity savedCheckout = checkoutRepository.save(checkout);

        // ---------------------------------------------------------------------
        // 24. Map the persisted checkout to the review response
        // ---------------------------------------------------------------------

        CheckoutReviewResponse response = checkoutMapper.toCheckoutReviewResponse(savedCheckout);

        // ---------------------------------------------------------------------
        // 25. Log successful checkout review preparation
        // ---------------------------------------------------------------------

        LOGGER.info("Checkout review prepared successfully. checkoutNumber={}, userNumber={}, status={}",
                savedCheckout.getCheckoutNumber(),
                userNumber,
                savedCheckout.getStatus());

        // ---------------------------------------------------------------------
        // 26. Return the prepared checkout review
        // ---------------------------------------------------------------------

        return new ServiceOutput<>(response);
    }

    /**
     * Validates whether a restaurant or branch is operationally available
     * for checkout.
     *
     * @param status       current lifecycle status
     * @param isAvailable  current operational availability
     * @param entityType   entity description used in logs
     * @param entityNumber business-facing entity identifier
     */
    private void validateOperationalAvailability(
            final DisplayOptionResponse status,
            final boolean isAvailable,
            final String entityType,
            final String entityNumber) {

        if (status == null || !RestaurantStatusConstant.ACTIVE.name()
                .equals(status.value())) {

            LOGGER.warn("{} is not active. entityNumber={}, status={}",
                    entityType,
                    entityNumber,
                    status != null ? status.value() : null);

            throw new BusinessException(CheckoutErrorConstants.CHECKOUT_REVALIDATION_REQUIRED);
        }

        if (!isAvailable) {

            LOGGER.warn("{} is currently unavailable. entityNumber={}",
                    entityType,
                    entityNumber);

            throw new BusinessException(CheckoutErrorConstants.CHECKOUT_REVALIDATION_REQUIRED);
        }

    }

    /**
     * Validates branch identity, restaurant association, and availability.
     *
     * @param branch               branch details returned by the branch service
     * @param expectedRestaurantId MongoDB identifier of the expected restaurant
     * @throws BusinessException when the branch is invalid or unavailable
     */
    private void validateBranchAvailability(
            final RestaurantBranchDetailsResponse branch,
            final String expectedRestaurantId) {

        if (branch == null) {
            throw new BusinessException(
                    CheckoutErrorConstants.CHECKOUT_REVALIDATION_REQUIRED);
        }

        if (isBlank(expectedRestaurantId)
                || isBlank(branch.getRestaurantId())
                || isBlank(branch.getBranchNumber())) {

            throw new BusinessException(
                    CheckoutErrorConstants.CHECKOUT_DATA_INCONSISTENT);
        }

        // Ensure that the branch belongs to the expected restaurant.
        if (!Objects.equals(
                expectedRestaurantId,
                branch.getRestaurantId())) {

            throw new BusinessException(
                    CheckoutErrorConstants.CHECKOUT_DATA_INCONSISTENT);
        }

        validateOperationalAvailability(
                branch.getStatus(),
                branch.isAvailable(),
                EntityName.RESTAURANT_BRANCH_ENTITY.getDisplayName(),
                branch.getBranchNumber());
    }

    /**
     * Validates the latest food record before including it in checkout.
     *
     * <p>
     * The cart stores a historical food snapshot. Checkout must independently
     * validate the current food record to ensure that the food still exists,
     * belongs to the selected restaurant and branch, and is available for
     * ordering.
     * </p>
     *
     * @param food     latest food entity returned by the food service
     * @param cartItem item currently stored in the cart
     * @param cart     cart containing the item
     * @return validated current food entity
     * @throws BusinessException when food data is invalid or unavailable
     */
    private FoodEntity validateFoodForCheckout(
            final FoodEntity food,
            final CartItem cartItem,
            final CartEntity cart) {

        if (food == null
                || cartItem == null
                || cartItem.getFoodSnapshot() == null) {

            throw new BusinessException(
                    CheckoutErrorConstants.FOOD_SNAPSHOT_INVALID);
        }

        final String requestedFoodNumber = cartItem.getFoodSnapshot().getFoodNumber();

        if (isBlank(requestedFoodNumber)
                || isBlank(food.getFoodNumber())) {

            throw new BusinessException(
                    CheckoutErrorConstants.FOOD_SNAPSHOT_INVALID);
        }

        // Ensure the service returned the requested food record.
        if (!Objects.equals(
                requestedFoodNumber,
                food.getFoodNumber())) {

            LOGGER.error(
                    "Food identity mismatch during checkout. requestedFoodNumber={}, returnedFoodNumber={}",
                    requestedFoodNumber,
                    food.getFoodNumber());

            throw new BusinessException(
                    CheckoutErrorConstants.CHECKOUT_DATA_INCONSISTENT);
        }

        // Food name is required for a meaningful checkout snapshot.
        if (isBlank(food.getFoodName())) {

            LOGGER.error(
                    "Food name is missing. foodNumber={}",
                    food.getFoodNumber());

            throw new BusinessException(
                    CheckoutErrorConstants.FOOD_VALIDATION_FAILED);
        }

        // Validate current food lifecycle status.
        if (food.getStatus() != FoodStatusConstant.AVAILABLE) {

            LOGGER.warn(
                    "Food is not available for checkout. foodNumber={}, status={}",
                    food.getFoodNumber(),
                    food.getStatus());

            throw new BusinessException(
                    CheckoutErrorConstants.FOOD_UNAVAILABLE);
        }

        // Validate operational availability.
        if (!food.isAvailable()) {

            LOGGER.warn(
                    "Food is marked unavailable. foodNumber={}",
                    food.getFoodNumber());

            throw new BusinessException(
                    CheckoutErrorConstants.FOOD_UNAVAILABLE);
        }

        // Validate restaurant association.
        if (!Objects.equals(
                cart.getRestaurantNumber(),
                food.getRestaurantNumber())) {

            LOGGER.error(
                    "Food restaurant mismatch. foodNumber={}, cartNumber={}",
                    food.getFoodNumber(),
                    cart.getCartNumber());

            throw new BusinessException(
                    CheckoutErrorConstants.FOOD_RESTAURANT_MISMATCH);
        }

        // Validate branch association.
        if (!Objects.equals(
                cart.getRestaurantBranchNumber(),
                food.getRestaurantBranchNumber())) {

            LOGGER.error(
                    "Food branch mismatch. foodNumber={}, cartNumber={}",
                    food.getFoodNumber(),
                    cart.getCartNumber());

            throw new BusinessException(
                    CheckoutErrorConstants.FOOD_BRANCH_MISMATCH);
        }

        // Reject invalid monetary values.
        final double currentPrice = food.getPrice();

        if (!Double.isFinite(currentPrice) || currentPrice < 0) {

            LOGGER.error(
                    "Food has an invalid price. foodNumber={}, price={}",
                    food.getFoodNumber(),
                    currentPrice);

            throw new BusinessException(
                    CheckoutErrorConstants.FOOD_VALIDATION_FAILED);
        }

        return food;
    }

    /**
     * Validates the minimum information required to prepare a delivery address
     * snapshot for checkout.
     *
     * @param address saved address response
     */
    private void validateDeliveryAddress(final AddressResponse address) {

        if (address == null
                || isBlank(address.getAddressNumber())
                || isBlank(address.getRecipientName())
                || address.getPhoneNumber() == null
                || isBlank(address.getPhoneNumber().getValue())
                || isBlank(address.getAddressLine1())
                || isBlank(address.getCity())
                || isBlank(address.getDistrict())
                || isBlank(address.getState())
                || isBlank(address.getCountry())
                || isBlank(address.getPostalCode())) {

            LOGGER.warn("Delivery address is incomplete and cannot be used for checkout.");

            throw new BusinessException(CheckoutErrorConstants.CHECKOUT_REVALIDATION_REQUIRED);
        }
    }

    /**
     * Checks whether a string is null, empty, or whitespace-only.
     *
     * @param value value to inspect
     * @return true when the value is blank
     */
    private boolean isBlank(final String value) {
        return value == null || value.isBlank();
    }

    /**
     * Calculates the pricing breakdown for a checkout review.
     *
     * <p>
     * The calculation uses validated checkout item snapshots and performs
     * all monetary operations through {@link MoneyUtil}.
     * </p>
     *
     * <p>
     * Unsupported pricing components are initialized to zero until their
     * respective business rules are implemented.
     * </p>
     *
     * <p>
     * This method does not mutate the checkout entity or persist any data.
     * </p>
     *
     * @param items validated checkout item snapshots
     * @return backend-calculated checkout pricing snapshot
     */
    private CheckoutPricingSnapshot calculateCheckoutPricing(
            final List<CheckoutItemSnapshot> items) {

        Objects.requireNonNull(items, "Checkout items must not be null.");

        if (items.isEmpty()) {
            throw new IllegalArgumentException(
                    "Checkout must contain at least one item.");
        }

        final String currency = "INR";

        Money itemSubtotal = Money.defaultMoney(currency);

        /*
         * Calculate the subtotal using the already validated item totals.
         * MoneyUtil.add() ensures currency consistency and preserves
         * intermediate precision.
         */
        for (CheckoutItemSnapshot item : items) {

            if (item == null || item.getItemTotal() == null) {
                throw new IllegalArgumentException(
                        "Checkout item total must not be null.");
            }

            Money itemTotal = item.getItemTotal();

            if (itemTotal.getAmount() == null
                    || itemTotal.getAmount().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException(
                        "Checkout item total must be a non-negative amount.");
            }

            if (!currency.equalsIgnoreCase(itemTotal.getCurrency())) {
                throw new IllegalArgumentException(
                        "Checkout item currency does not match checkout currency.");
            }

            itemSubtotal = MoneyUtil.add(itemSubtotal, itemTotal);
        }

        /*
         * Apply currency precision at the subtotal boundary.
         */
        itemSubtotal = MoneyUtil.round(
                itemSubtotal,
                MoneyPrecision.TWO);

        /*
         * Initialize unsupported pricing components.
         */
        final Money discountAmount = Money.defaultMoney(currency);
        final Money taxAmount = Money.defaultMoney(currency);
        final Money deliveryFee = Money.defaultMoney(currency);
        final Money packagingCharge = Money.defaultMoney(currency);
        final Money platformFee = Money.defaultMoney(currency);
        final Money rainCharge = Money.defaultMoney(currency);

        /*
         * Calculate the final payable amount.
         *
         * Formula:
         *
         * Total Payable =
         * Item Subtotal
         * - Discount
         * + Tax
         * + Delivery Fee
         * + Packaging Charge
         * + Platform Fee
         * + Rain Charge
         */
        Money totalPayable = MoneyUtil.subtract(itemSubtotal, discountAmount);

        totalPayable = MoneyUtil.add(totalPayable, taxAmount);
        totalPayable = MoneyUtil.add(totalPayable, deliveryFee);
        totalPayable = MoneyUtil.add(totalPayable, packagingCharge);
        totalPayable = MoneyUtil.add(totalPayable, platformFee);
        totalPayable = MoneyUtil.add(totalPayable, rainCharge);

        /*
         * Apply final monetary precision.
         */
        totalPayable = MoneyUtil.round(
                totalPayable,
                MoneyPrecision.TWO);

        /*
         * Protect the checkout from invalid final pricing.
         */
        if (totalPayable.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Checkout total payable cannot be negative.");
        }

        return CheckoutPricingSnapshot.builder()
                .itemSubtotal(itemSubtotal)
                .discountAmount(discountAmount)
                .taxAmount(taxAmount)
                .deliveryFee(deliveryFee)
                .packagingCharge(packagingCharge)
                .platformFee(platformFee)
                .rainCharge(rainCharge)
                .totalPayable(totalPayable)
                .build();
    }

    /**
     * Validates whether the checkout session is eligible for review.
     *
     * <p>
     * Only newly initiated checkouts and checkouts already presented
     * for customer confirmation may enter the review process.
     * </p>
     *
     * @param checkout checkout session to validate
     * @throws BusinessException when the checkout cannot be reviewed
     */
    private void validateCheckoutReviewEligibility(final CheckoutEntity checkout) {

        if (checkout == null) {
            throw new BusinessException(CheckoutErrorConstants.CHECKOUT_NOT_FOUND);
        }

        if (checkout.isExpired()) {
            throw new BusinessException(CheckoutErrorConstants.CHECKOUT_EXPIRED);
        }

        final CheckoutStatusConstant status = checkout.getStatus();

        if (status != CheckoutStatusConstant.INITIATED
                && status != CheckoutStatusConstant.READY_FOR_CONFIRMATION) {

            throw new BusinessException(CheckoutErrorConstants.INVALID_CHECKOUT_STATUS);
        }
    }

    /**
     * Validates the cart associated with a checkout review.
     *
     * <p>
     * This validation protects checkout from stale, malformed, or inconsistent
     * cart data before any external dependency is queried.
     * </p>
     *
     * @param cart                    cart associated with the checkout
     * @param checkout                checkout session being reviewed
     * @param authenticatedUserNumber authenticated customer number
     * @throws BusinessException when cart integrity or eligibility validation fails
     */
    private void validateCartForCheckoutReview(
            final CartEntity cart,
            final CheckoutEntity checkout,
            final String authenticatedUserNumber) {

        if (cart == null) {
            throw new BusinessException(
                    CheckoutErrorConstants.CART_NOT_FOUND);
        }

        if (checkout == null || isBlank(authenticatedUserNumber)) {
            throw new BusinessException(
                    CheckoutErrorConstants.CHECKOUT_DATA_INCONSISTENT);
        }

        // 1. Verify cart ownership.
        if (!authenticatedUserNumber.equals(cart.getUserNumber())) {
            throw new BusinessException(
                    CheckoutErrorConstants.CART_USER_MISMATCH);
        }

        // 2. Verify checkout-cart association.
        if (!Objects.equals(checkout.getCartNumber(), cart.getCartNumber())) {
            throw new BusinessException(
                    CheckoutErrorConstants.CHECKOUT_DATA_INCONSISTENT);
        }

        // 3. Ensure the cart contains items.
        if (cart.isEmpty()) {
            throw new BusinessException(
                    CheckoutErrorConstants.CART_EMPTY);
        }

        // 4. Validate cart lifecycle.
        CartStatusConstant cartStatus = cart.getStatus();

        if (cartStatus != CartStatusConstant.ACTIVE
                && cartStatus != CartStatusConstant.CHECKOUT_IN_PROGRESS) {

            throw new BusinessException(
                    CheckoutErrorConstants.CART_NOT_ACTIVE);
        }

        // 5. Validate restaurant and branch context.
        if (!cart.hasRestaurantContext()) {
            throw new BusinessException(
                    CheckoutErrorConstants.CART_CHECKOUT_CONTEXT_INVALID);
        }

        // 6. Validate checkout and cart restaurant association.
        if (!Objects.equals(
                checkout.getRestaurantNumber(),
                cart.getRestaurantNumber())) {

            throw new BusinessException(
                    CheckoutErrorConstants.CART_RESTAURANT_MISMATCH);
        }

        if (!Objects.equals(
                checkout.getRestaurantBranchNumber(),
                cart.getRestaurantBranchNumber())) {

            throw new BusinessException(
                    CheckoutErrorConstants.CART_BRANCH_MISMATCH);
        }

        // 7. Validate item collection integrity.
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new BusinessException(
                    CheckoutErrorConstants.CART_ITEM_SNAPSHOT_INVALID);
        }

        for (CartItem cartItem : cart.getItems()) {

            if (cartItem == null
                    || cartItem.getFoodSnapshot() == null
                    || isBlank(cartItem.getFoodSnapshot().getFoodNumber())) {

                throw new BusinessException(
                        CheckoutErrorConstants.CART_ITEM_SNAPSHOT_INVALID);
            }
        }
    }

    /**
     * Validates restaurant identity and operational availability.
     *
     * @param restaurant restaurant details returned by the restaurant service
     * @throws BusinessException when the restaurant is invalid or unavailable
     */
    private void validateRestaurantAvailability(
            final RestaurantDetailsResponse restaurant) {

        if (restaurant == null) {
            throw new BusinessException(
                    CheckoutErrorConstants.CHECKOUT_REVALIDATION_REQUIRED);
        }

        if (isBlank(restaurant.getRestaurantNumber())) {
            throw new BusinessException(
                    CheckoutErrorConstants.CHECKOUT_DATA_INCONSISTENT);
        }

        validateOperationalAvailability(
                restaurant.getStatus(),
                restaurant.isAvailable(),
                EntityName.RESTAURANT_ENTITY.getDisplayName(),
                restaurant.getRestaurantNumber());
    }

    /**
     * Creates a checkout item snapshot using the latest validated food data.
     *
     * <p>
     * The unit price is taken from the current food entity, never from the
     * historical cart snapshot.
     * </p>
     *
     * @param food     validated current food entity
     * @param cartItem cart item containing the requested quantity
     * @return immutable checkout item snapshot
     */
    private CheckoutItemSnapshot buildCheckoutItemSnapshot(
            final FoodEntity food,
            final CartItem cartItem) {

        Objects.requireNonNull(food, "Validated food is required.");
        Objects.requireNonNull(cartItem, "Cart item is required.");

        final int quantity = cartItem.getQuantity();

        if (quantity <= 0) {

            LOGGER.warn(
                    "Invalid cart item quantity. foodNumber={}, quantity={}",
                    food.getFoodNumber(),
                    quantity);

            throw new BusinessException(
                    CheckoutErrorConstants.FOOD_QUANTITY_UNAVAILABLE);
        }

        final Money unitPrice = Money.of(
                BigDecimal.valueOf(food.getPrice()));

        final BigDecimal itemTotalAmount = unitPrice.getAmount()
                .multiply(BigDecimal.valueOf(quantity));

        final Money itemTotal = Money.of(
                itemTotalAmount,
                unitPrice.getCurrency());

        return CheckoutItemSnapshot.builder()
                .foodNumber(food.getFoodNumber())
                .foodName(food.getFoodName())
                .foodImage(food.getFoodImage())
                .unitPrice(unitPrice)
                .quantity(quantity)
                .itemTotal(itemTotal)
                .build();
    }

    /**
     * Resolves the authenticated customer's default delivery address.
     *
     * <p>
     * The Address service returns addresses belonging to the authenticated
     * customer. Checkout must ensure that exactly one default address exists
     * before creating a delivery address snapshot.
     * </p>
     *
     * @param addresses customer's active addresses
     * @return resolved default address
     */
    private AddressResponse resolveDefaultDeliveryAddress(final List<AddressResponse> addresses) {

        if (addresses == null || addresses.isEmpty()) {
            throw new BusinessException(CheckoutErrorConstants.CHECKOUT_REVALIDATION_REQUIRED);
        }

        final List<AddressResponse> defaultAddresses = addresses
                .stream()
                .filter(Objects::nonNull)
                .filter(addressResponse -> addressResponse.isDefaultAddress())
                .toList();

        if (defaultAddresses.isEmpty()) {
            throw new BusinessException(CheckoutErrorConstants.CHECKOUT_REVALIDATION_REQUIRED);
        }

        if (defaultAddresses.size() > 1) {
            LOGGER.error("Multiple default addresses found for customer during checkout review. count={}",
                    defaultAddresses.size());

            throw new BusinessException(CheckoutErrorConstants.CHECKOUT_DATA_INCONSISTENT);
        }

        return defaultAddresses.get(0);
    }

    /**
     * Builds a normalized delivery address snapshot from a validated address.
     *
     * <p>
     * The snapshot preserves the address information reviewed by the customer
     * and remains independent of subsequent changes to the saved address.
     * </p>
     *
     * @param address validated customer address
     * @return normalized checkout address snapshot
     */
    private CheckoutAddressSnapshot buildCheckoutAddressSnapshot(
            final AddressResponse address) {

        validateDeliveryAddress(address);

        final String addressLine = Stream.of(
                address.getAddressLine1(),
                address.getAddressLine2())
                .filter(value -> value != null && !value.isBlank())
                .map(string -> string.trim())
                .collect(Collectors.joining(", "));

        return CheckoutAddressSnapshot.builder()
                .addressNumber(address.getAddressNumber().trim())
                .recipientName(address.getRecipientName().trim())
                .phoneNumber(address.getPhoneNumber().getValue().trim())
                .addressLine(addressLine)
                .landmark(FreshMealUtilities.normalizeWhitespace(address.getLandmark()))
                .city(address.getCity().trim())
                .district(address.getDistrict().trim())
                .state(address.getState().trim())
                .country(address.getCountry().trim())
                .pincode(address.getPostalCode().trim())
                .build();
    }

    @Override
    public IServiceOutput<CheckoutResponse> confirmCheckout(
            final IServiceInput<ConfirmCheckoutRequest> input) {

        Objects.requireNonNull(input, "Checkout service input must not be null.");

        // ---------------------------------------------------------------------
        // 1. Validate service context
        // ---------------------------------------------------------------------

        IServiceContext context = input.getServiceContext();

        if (context == null) {
            throw new BusinessException(CheckoutErrorConstants.CHECKOUT_CONTEXT_REQUIRED);
        }

        // ---------------------------------------------------------------------
        // 2. Resolve authenticated customer
        // ---------------------------------------------------------------------

        String userNumber = resolveAuthenticatedUser(context);

        // ---------------------------------------------------------------------
        // 3. Validate confirmation request
        // ---------------------------------------------------------------------

        ConfirmCheckoutRequest request = input.getInput();

        checkoutRequestValidator.validateConfirmCheckoutRequest(request);

        // ---------------------------------------------------------------------
        // 4. Retrieve checkout session
        // ---------------------------------------------------------------------

        String checkoutNumber = request.getCheckoutNumber();

        CheckoutEntity checkout = checkoutRepository
                .findByCheckoutNumber(checkoutNumber)
                .orElseThrow(() -> new BusinessException(CheckoutErrorConstants.CHECKOUT_NOT_FOUND));

        // ---------------------------------------------------------------------
        // 5. Verify checkout ownership
        // ---------------------------------------------------------------------

        if (!userNumber.equals(checkout.getUserNumber())) {

            LOGGER.warn("Checkout confirmation access denied. checkoutNumber={}, userNumber={}",
                    checkoutNumber,
                    userNumber);

            // Do not reveal whether another customer's checkout exists.
            throw new BusinessException(CheckoutErrorConstants.CHECKOUT_NOT_FOUND);
        }

        // 6. bind Idempotency key
        try {
            checkout.bindConfirmationIdempotencyKey(request.getIdempotencyKey());
        } catch (IllegalStateException exception) {
            throw new BusinessException(CheckoutErrorConstants.CHECKOUT_OPERATION_NOT_ALLOWED);
        }

        if (checkout.getStatus() != CheckoutStatusConstant.READY_FOR_CONFIRMATION || checkout.isExpired()) {
            throw new BusinessException(CheckoutErrorConstants.CHECKOUT_CONFIRMATION_REQUIRES_REVALIDATION);
        }
        // 7 : save data to DB with idempotency key
        checkoutRepository.save(checkout);

        // 8: Transition checkout to CONFIRMATION_IN_PROGRESS
        checkout.beginConfirmation();

        // 9: Persist again the confirmation state
        checkoutRepository.save(checkout);

        // 10: get the current saved cart and validate it
        CartEntity cart = cartRepository.findByCartNumber(
                checkout.getCartNumber())
                .orElseThrow(() -> new BusinessException(CheckoutErrorConstants.CART_NOT_FOUND));

        validateCartForCheckoutReview(cart, checkout, userNumber);

        // 11: Validate the persisted checkout snapshot
        if (checkout.getItems().isEmpty() || checkout.getDeliveryAddress() == null || checkout.getPricing() == null) {
            throw new BusinessException(CheckoutErrorConstants.CHECKOUT_SNAPSHOT_REQUIRED);
        }
        // 12: Verify that the cart still matches the reviewed checkout
        if (cart.getItems().size() != checkout.getItems().size()) {
            throw new BusinessException(CheckoutErrorConstants.CHECKOUT_REVIEW_OUTDATED);
        }

        // 13: Verify each food item and quantity
        for (CheckoutItemSnapshot checkoutItem : checkout.getItems()) {
            CartItem cartItem = cart.findItem(checkoutItem.getFoodNumber()).orElse(null);

            if (cartItem == null || cartItem.getQuantity() != checkoutItem.getQuantity()) {
                throw new BusinessException(CheckoutErrorConstants.CHECKOUT_REVIEW_OUTDATED);
            }
        }

        // 14: Revalidate current food records
        List<CheckoutItemSnapshot> currentItems = new ArrayList<>();

        for (CartItem cartItem : cart.getItems()) {
            String foodNumber = cartItem.getFoodSnapshot().getFoodNumber();

            FoodIdRequest foodRequest = new FoodIdRequest();
            foodRequest.setFoodId(foodNumber);

            IServiceInput<FoodIdRequest> foodInput = new ServiceInput<>(
                    foodRequest,
                    context,
                    input.getDataContext());

            IServiceOutput<FoodEntity> foodOutput = foodService.loadFood(foodInput);

            if (foodOutput == null || foodOutput.getOutput() == null) {
                LOGGER.warn("Food could not be loaded during checkout confirmation. foodNumber={}", foodNumber);
                throw new BusinessException(CheckoutErrorConstants.FOOD_NOT_FOUND);
            }

            FoodEntity food = validateFoodForCheckout(foodOutput.getOutput(), cartItem, cart);

            currentItems.add(buildCheckoutItemSnapshot(food, cartItem));
        }

        // 15: Verify that food prices have not changed
        for (CheckoutItemSnapshot currentItem : currentItems) {
            CheckoutItemSnapshot reviewedItem = checkout.getItems().stream()
                    .filter(item -> item.getFoodNumber().equals(currentItem.getFoodNumber()))
                    .findFirst()
                    .orElseThrow(() -> new BusinessException(CheckoutErrorConstants.CHECKOUT_REVIEW_OUTDATED));

            if (reviewedItem.getUnitPrice().getAmount().compareTo(currentItem.getUnitPrice().getAmount()) != 0
                    || !reviewedItem.getUnitPrice().getCurrency().equals(currentItem.getUnitPrice().getCurrency())) {
                LOGGER.warn(
                        "Food price changed during checkout confirmation. foodNumber={}, reviewedPrice={}, currentPrice={}",
                        currentItem.getFoodNumber(),
                        reviewedItem.getUnitPrice(),
                        currentItem.getUnitPrice());

                throw new BusinessException(CheckoutErrorConstants.CHECKOUT_PRICE_CHANGED);
            }
        }

        // 16: Revalidate restaurant and branch availability
        RestaurantIdRequest restaurantRequest = RestaurantIdRequest.builder()
                .restaurantId(cart.getRestaurantNumber())
                .build();

        IServiceInput<RestaurantIdRequest> restaurantInput = new ServiceInput<>(
                restaurantRequest,
                context,
                input.getDataContext());

        IServiceOutput<RestaurantDetailsResponse> restaurantOutput = restaurantService.getById(restaurantInput);

        if (restaurantOutput == null || restaurantOutput.getOutput() == null) {
            LOGGER.error(
                    "Restaurant service returned an empty response during checkout confirmation. restaurantNumber={}",
                    cart.getRestaurantNumber());
            throw new BusinessException(CheckoutErrorConstants.CHECKOUT_DEPENDENCY_UNAVAILABLE);
        }

        RestaurantDetailsResponse restaurant = restaurantOutput.getOutput();
        validateRestaurantAvailability(restaurant);

        RestaurantBranchIdRequest branchRequest = RestaurantBranchIdRequest.builder()
                .branchId(cart.getRestaurantBranchNumber())
                .build();

        IServiceInput<RestaurantBranchIdRequest> branchInput = new ServiceInput<>(
                branchRequest,
                context,
                input.getDataContext());

        IServiceOutput<RestaurantBranchDetailsResponse> branchOutput = restaurantBranchService.getById(branchInput);

        if (branchOutput == null || branchOutput.getOutput() == null) {
            LOGGER.error(
                    "Restaurant branch service returned an empty response during checkout confirmation. branchNumber={}",
                    cart.getRestaurantBranchNumber());
            throw new BusinessException(CheckoutErrorConstants.CHECKOUT_DEPENDENCY_UNAVAILABLE);
        }

        validateBranchAvailability(branchOutput.getOutput(), restaurant.getId());

        // 17: Recalculate and verify checkout pricing
        CheckoutPricingSnapshot currentPricing = calculateCheckoutPricing(currentItems);

        CheckoutPricingSnapshot reviewedPricing = checkout.getPricing();

        if (reviewedPricing.getItemSubtotal().getAmount()
                .compareTo(currentPricing.getItemSubtotal().getAmount()) != 0
                || !reviewedPricing.getItemSubtotal().getCurrency()
                        .equals(currentPricing.getItemSubtotal().getCurrency())
                || reviewedPricing.getTotalPayable().getAmount()
                        .compareTo(currentPricing.getTotalPayable().getAmount()) != 0
                || !reviewedPricing.getTotalPayable().getCurrency()
                        .equals(currentPricing.getTotalPayable().getCurrency())) {

            LOGGER.error("Checkout pricing snapshot mismatch. checkoutNumber={}",
                    checkout.getCheckoutNumber());

            throw new BusinessException(CheckoutErrorConstants.CHECKOUT_PRICING_SNAPSHOT_MISMATCH);
        }

        // 18: Retrieve the customer's current delivery address
        IServiceInput<Void> addressInput = new ServiceInput<>(
                null,
                context,
                input.getDataContext());

        IServiceOutput<List<AddressResponse>> addressOutput = addressService.getMyAddresses(addressInput);

        if (addressOutput == null || addressOutput.getOutput() == null) {
            LOGGER.error("Address service returned an empty response during checkout confirmation. userNumber={}",
                    userNumber);
            throw new BusinessException(CheckoutErrorConstants.CHECKOUT_DEPENDENCY_UNAVAILABLE);
        }

        AddressResponse currentDefaultAddress = resolveDefaultDeliveryAddress(addressOutput.getOutput());
        validateDeliveryAddress(currentDefaultAddress);

        // 19: Compare the current delivery address with the reviewed snapshot
        CheckoutAddressSnapshot currentAddressSnapshot = buildCheckoutAddressSnapshot(currentDefaultAddress);

        CheckoutAddressSnapshot reviewedAddress = checkout.getDeliveryAddress();

        if (!Objects.equals(reviewedAddress.getAddressNumber(), currentAddressSnapshot.getAddressNumber())
                || !Objects.equals(reviewedAddress.getRecipientName(), currentAddressSnapshot.getRecipientName())
                || !Objects.equals(reviewedAddress.getPhoneNumber(), currentAddressSnapshot.getPhoneNumber())
                || !Objects.equals(reviewedAddress.getAddressLine(), currentAddressSnapshot.getAddressLine())
                || !Objects.equals(reviewedAddress.getLandmark(), currentAddressSnapshot.getLandmark())
                || !Objects.equals(reviewedAddress.getCity(), currentAddressSnapshot.getCity())
                || !Objects.equals(reviewedAddress.getDistrict(), currentAddressSnapshot.getDistrict())
                || !Objects.equals(reviewedAddress.getState(), currentAddressSnapshot.getState())
                || !Objects.equals(reviewedAddress.getCountry(), currentAddressSnapshot.getCountry())
                || !Objects.equals(reviewedAddress.getPincode(), currentAddressSnapshot.getPincode())) {

            LOGGER.warn("Delivery address changed after checkout review. checkoutNumber={}",
                    checkout.getCheckoutNumber());

            throw new BusinessException(CheckoutErrorConstants.CHECKOUT_REVALIDATION_REQUIRED);
        }

        // TODO:20: Prepare the order creation request

        // persist into DB

        return null;
    }

    @Override
    public IServiceOutput<CheckoutResponse> cancelCheckout(IServiceInput<String> input) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
