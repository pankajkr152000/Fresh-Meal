package com.foodies.freshmeal.cart.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.foodies.freshmeal.cart.constants.CartErrorConstants;
import com.foodies.freshmeal.cart.constants.CartStatusConstant;
import com.foodies.freshmeal.cart.valueObject.FoodSnapshot;
import com.foodies.freshmeal.common.entity.ABaseEntity;
import com.foodies.freshmeal.common.entity.IEntity;
import com.foodies.freshmeal.common.exception.BusinessException;
import com.foodies.freshmeal.common.valueObject.Money;

import lombok.Getter;

/**
 * Aggregate root representing a customer's shopping cart.
 *
 * <p>
 * CartEntity owns its CartItem collection and is responsible for maintaining
 * cart-level invariants such as item uniqueness, restaurant consistency,
 * quantity totals, subtotal calculation, and cart lifecycle transitions.
 * </p>
 *
 * <p>
 * Cross-aggregate validations such as food availability, food status,
 * current food price, and restaurant existence are intentionally handled
 * by the service and validation layers.
 * </p>
 */
@Getter
@Document(collection = "fm_cart")
public class CartEntity extends ABaseEntity {

    private static final long serialVersionUID = 7147063570513022400L;

    /**
     * Business identifier of the cart.
     */
    @Indexed(unique = true)
    private String cartNumber;

    /**
     * Business identifier of the customer who owns the cart.
     */
    @Indexed
    private String userNumber;

    /**
     * Business identifier of the restaurant associated with the cart.
     */
    @Indexed
    private String restaurantNumber;

    /**
     * Business identifier of the restaurant branch associated with the cart.
     */
    @Indexed
    private String restaurantBranchNumber;

    /**
     * Current lifecycle status of the cart.
     */
    @Field("status")
    private CartStatusConstant status = CartStatusConstant.ACTIVE;

    /**
     * Timestamp at which the cart entered its current status.
     */
    private LocalDateTime statusUpdatedAt;

    /**
     * Timestamp after which the cart should be considered expired.
     */
    private LocalDateTime expiresAt;

    /**
     * Timestamp at which checkout was initiated.
     */
    private LocalDateTime checkoutStartedAt;

    /**
     * Food items contained in the cart.
     */
    private List<CartItem> items = new ArrayList<>();

    /**
     * Cart subtotal calculated from item price snapshots.
     *
     * <p>
     * This is not the final checkout amount. Taxes, delivery charges,
     * discounts, payment charges, and other checkout-level pricing
     * components are calculated outside the Cart aggregate.
     * </p>
     */
    private Money subtotal = Money.defaultMoney();

    /**
     * Number of distinct food lines in the cart.
     */
    private int totalItemCount;

    /**
     * Total quantity across all cart items.
     */
    private int totalQuantity;

    /**
     * Creates an empty CartEntity instance for framework/factory use.
     *
     * @return new CartEntity
     */
    public static IEntity create() {
        return new CartEntity();
    }

    /**
     * Creates a new active cart for a customer.
     *
     * @param cartNumber business identifier of the cart
     * @param userNumber business identifier of the customer
     * @return newly created cart
     */
    public static CartEntity create(
            String cartNumber,
            String userNumber) {

        CartEntity cart = new CartEntity();

        cart.cartNumber = cartNumber;
        cart.userNumber = userNumber;
        cart.status = CartStatusConstant.ACTIVE;
        cart.statusUpdatedAt = LocalDateTime.now();
        cart.items = new ArrayList<>();
        cart.subtotal = Money.defaultMoney();

        return cart;
    }

    // -------------------------------------------------------------------------
    // Cart Item Operations
    // -------------------------------------------------------------------------

    /**
     * Adds a food item to the cart.
     *
     * <p>
     * If the food already exists, its quantity is increased instead of
     * creating a duplicate cart line.
     * </p>
     *
     * @param foodSnapshot           food information snapshot
     * @param unitPrice              price snapshot
     * @param quantity               quantity to add
     * @param restaurantNumber       restaurant business identifier
     * @param restaurantBranchNumber branch business identifier
     * @param addedAt                timestamp when the item was added
     */
    public void addItem(
            FoodSnapshot foodSnapshot,
            Money unitPrice,
            int quantity,
            String restaurantNumber,
            String restaurantBranchNumber,
            LocalDateTime addedAt) {

        validateActiveCart();
        validateFoodSnapshot(foodSnapshot);
        validateQuantity(quantity);

        validateRestaurantContext(
                restaurantNumber,
                restaurantBranchNumber);

        assignRestaurantContextIfRequired(
                restaurantNumber,
                restaurantBranchNumber);

        String foodNumber = foodSnapshot.getFoodNumber();

        Optional<CartItem> existingItem = findItem(foodNumber);

        if (existingItem.isPresent()) {

            CartItem item = existingItem.get();

            /*
             * The price is resolved freshly by the service.
             * Refresh the cart's price snapshot before recalculating
             * the item and cart totals.
             */
            item.updateUnitPrice(unitPrice);
            item.updateQuantity(
                    item.getQuantity() + quantity);

        } else {

            CartItem item = CartItem.create(
                    foodSnapshot,
                    unitPrice,
                    quantity,
                    addedAt);

            items.add(item);
        }

        recalculateTotals();
    }

    /**
     * Updates an existing cart item's final quantity and price snapshot.
     *
     * @param foodNumber food business identifier
     * @param quantity   final desired quantity
     * @param unitPrice  current server-side price snapshot
     */
    public void updateItemQuantity(
            final String foodNumber,
            final int quantity,
            final Money unitPrice) {

        validateActiveCart();
        validateQuantity(quantity);

        if (unitPrice == null) {
            throw new IllegalArgumentException(
                    "Unit price must not be null.");
        }

        final CartItem item = findItem(foodNumber).orElseThrow(
                () -> new BusinessException(CartErrorConstants.CART_ALREADY_EMPTY));

        item.updateUnitPrice(unitPrice);
        item.updateQuantity(quantity);

        recalculateTotals();
    }

    /**
     * Removes a food item from the cart.
     *
     * <p>
     * Removing the final item does not remove the cart itself. The cart
     * remains available for future use.
     * </p>
     *
     * @param foodNumber business identifier of the food
     */
    public void removeItem(String foodNumber) {

        validateActiveCart();

        if (foodNumber == null || foodNumber.isBlank()) {
            return;
        }

        final boolean removed = items.removeIf(
                item -> item.getFoodSnapshot() != null
                        && foodNumber.equals(
                                item.getFoodSnapshot().getFoodNumber()));

        if (!removed) {
            throw new BusinessException(CartErrorConstants.CART_ITEM_NOT_FOUND);
        }

        recalculateTotals();
    }

    /**
     * Removes all items from the cart.
     */
    public void clearItems() {

        validateActiveCart();
        if (items.isEmpty()) {
            return;
        }
        items.clear();

        recalculateTotals();
    }

    /**
     * Finds an item by food business identifier.
     *
     * @param foodNumber food business identifier
     * @return matching item if present
     */
    public Optional<CartItem> findItem(String foodNumber) {

        if (foodNumber == null || foodNumber.isBlank()) {
            return Optional.empty();
        }

        return items.stream()
                .filter(item -> item.getFoodSnapshot() != null)
                .filter(item -> foodNumber.equals(
                        item.getFoodSnapshot().getFoodNumber()))
                .findFirst();
    }

    /**
     * Determines whether the cart contains a food.
     *
     * @param foodNumber food business identifier
     * @return true when the food exists in the cart
     */
    public boolean containsFood(String foodNumber) {
        return findItem(foodNumber).isPresent();
    }

    /**
     * Determines whether the cart contains items.
     *
     * @return true when the cart contains at least one item
     */
    public boolean hasItems() {
        return !items.isEmpty();
    }

    /**
     * Determines whether the cart is empty.
     *
     * @return true when the cart contains no items
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Returns an immutable view of the cart items.
     *
     * @return immutable cart item collection
     */
    public List<CartItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    /**
     * Recalculates cart totals from the embedded cart items.
     */
    public void recalculateTotals() {

        int itemCount = 0;
        int quantity = 0;
        Money calculatedSubtotal = Money.defaultMoney();

        for (CartItem item : items) {

            item.recalculateItemTotal();

            itemCount++;
            quantity += item.getQuantity();

            calculatedSubtotal = addMoney(
                    calculatedSubtotal,
                    item.getItemTotal());
        }

        this.totalItemCount = itemCount;
        this.totalQuantity = quantity;
        this.subtotal = calculatedSubtotal;
    }

    // -------------------------------------------------------------------------
    // Cart Lifecycle
    // -------------------------------------------------------------------------

    /**
     * Starts the checkout process.
     *
     * <p>
     * The transition is:
     * ACTIVE → CHECKOUT_IN_PROGRESS
     * </p>
     */
    public void startCheckout() {

        transitionTo(CartStatusConstant.CHECKOUT_IN_PROGRESS);

        this.checkoutStartedAt = LocalDateTime.now();
    }

    /**
     * Returns the cart to ACTIVE after an unsuccessful checkout attempt.
     *
     * <p>
     * The transition is:
     * CHECKOUT_IN_PROGRESS → ACTIVE
     * </p>
     */
    public void resumeAfterCheckoutFailure() {

        transitionTo(CartStatusConstant.ACTIVE);

        this.checkoutStartedAt = null;
    }

    /**
     * Marks the cart as successfully converted into an order.
     *
     * <p>
     * The transition is:
     * CHECKOUT_IN_PROGRESS → CONVERTED
     * </p>
     */
    public void markConverted() {

        transitionTo(CartStatusConstant.CONVERTED);
    }

    /**
     * Marks the cart as abandoned.
     *
     * <p>
     * The transition is:
     * ACTIVE → ABANDONED
     * </p>
     */
    public void abandon() {

        transitionTo(CartStatusConstant.ABANDONED);
    }

    /**
     * Reactivates an abandoned cart.
     *
     * <p>
     * The transition is:
     * ABANDONED → ACTIVE
     * </p>
     */
    public void reactivate() {

        transitionTo(CartStatusConstant.ACTIVE);
    }

    /**
     * Marks an abandoned cart as expired.
     *
     * <p>
     * The transition is:
     * ABANDONED → EXPIRED
     * </p>
     */
    public void expire() {

        transitionTo(CartStatusConstant.EXPIRED);
    }

    /**
     * Determines whether the cart is currently active.
     *
     * @return true when the cart status is ACTIVE
     */
    public boolean isActive() {
        return CartStatusConstant.ACTIVE.equals(status);
    }

    /**
     * Determines whether checkout is currently in progress.
     *
     * @return true when checkout is in progress
     */
    public boolean isCheckoutInProgress() {
        return CartStatusConstant.CHECKOUT_IN_PROGRESS.equals(status);
    }

    /**
     * Determines whether the cart has been converted.
     *
     * @return true when the cart is converted
     */
    public boolean isConverted() {
        return CartStatusConstant.CONVERTED.equals(status);
    }

    /**
     * Determines whether the cart has expired.
     *
     * @return true when the cart is expired
     */
    public boolean isExpired() {
        return CartStatusConstant.EXPIRED.equals(status);
    }

    /**
     * Determines whether the cart has been abandoned.
     *
     * @return true when the cart is abandoned
     */
    public boolean isAbandoned() {
        return CartStatusConstant.ABANDONED.equals(status);
    }

    /**
     * Performs a validated lifecycle transition.
     *
     * @param targetStatus target lifecycle status
     */
    private void transitionTo(
            CartStatusConstant targetStatus) {

        if (targetStatus == null) {
            throw new IllegalArgumentException(
                    "Target cart status is required.");
        }

        if (status == targetStatus) {
            return;
        }

        if (!status.canTransitionTo(targetStatus)) {
            throw new IllegalStateException(
                    "Invalid cart status transition from "
                            + status.getValue()
                            + " to "
                            + targetStatus.getValue());
        }

        this.status = targetStatus;
        this.statusUpdatedAt = LocalDateTime.now();

        if (targetStatus == CartStatusConstant.ACTIVE) {
            this.checkoutStartedAt = null;
        }
    }

    // -------------------------------------------------------------------------
    // Restaurant Context
    // -------------------------------------------------------------------------

    /**
     * Determines whether restaurant and branch context has been assigned.
     *
     * @return true when both identifiers are available
     */
    public boolean hasRestaurantContext() {

        return restaurantNumber != null
                && !restaurantNumber.isBlank()
                && restaurantBranchNumber != null
                && !restaurantBranchNumber.isBlank();
    }

    /**
     * Validates that the supplied restaurant context is compatible
     * with this cart.
     *
     * @param restaurantNumber       restaurant business identifier
     * @param restaurantBranchNumber branch business identifier
     */
    public void validateRestaurantContext(
            String restaurantNumber,
            String restaurantBranchNumber) {

        if (restaurantNumber == null
                || restaurantNumber.isBlank()) {

            throw new IllegalArgumentException(
                    "Restaurant number is required.");
        }

        if (restaurantBranchNumber == null
                || restaurantBranchNumber.isBlank()) {

            throw new IllegalArgumentException(
                    "Restaurant branch number is required.");
        }

        if (!hasRestaurantContext()) {
            return;
        }

        if (!restaurantNumber.equals(this.restaurantNumber)
                || !restaurantBranchNumber.equals(
                        this.restaurantBranchNumber)) {

            throw new IllegalArgumentException(
                    "Food must belong to the same restaurant and branch as the cart.");
        }
    }

    /**
     * Assigns restaurant context when the first item is added.
     */
    private void assignRestaurantContextIfRequired(
            String restaurantNumber,
            String restaurantBranchNumber) {

        if (hasRestaurantContext()) {
            return;
        }

        this.restaurantNumber = restaurantNumber;
        this.restaurantBranchNumber = restaurantBranchNumber;
    }

    // -------------------------------------------------------------------------
    // Internal Validation
    // -------------------------------------------------------------------------

    /**
     * Ensures that only ACTIVE carts can be modified.
     */
    private void validateActiveCart() {

        if (!isActive()) {
            throw new IllegalStateException(
                    "Only an ACTIVE cart can be modified.");
        }
    }

    /**
     * Validates basic quantity rules.
     *
     * <p>
     * The maximum quantity is intentionally not hard-coded here. That
     * business rule will be centralized in CartValidator.
     * </p>
     */
    private void validateQuantity(int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Cart item quantity must be greater than zero.");
        }
    }

    /**
     * Validates the minimum food snapshot required by the aggregate.
     */
    private void validateFoodSnapshot(
            FoodSnapshot foodSnapshot) {

        if (foodSnapshot == null) {
            throw new IllegalArgumentException(
                    "Food snapshot is required.");
        }

        if (foodSnapshot.getFoodNumber() == null
                || foodSnapshot.getFoodNumber().isBlank()) {

            throw new IllegalArgumentException(
                    "Food number is required.");
        }
    }

    // -------------------------------------------------------------------------
    // Monetary Calculations
    // -------------------------------------------------------------------------

    /**
     * Calculates a cart item total using its price snapshot.
     */
    private Money calculateItemTotal(
            Money unitPrice,
            int quantity) {

        if (unitPrice == null
                || unitPrice.getAmount() == null) {

            return Money.defaultMoney();
        }

        return Money.of(
                unitPrice.getAmount()
                        .multiply(
                                BigDecimal.valueOf(quantity)),
                unitPrice.getCurrency());
    }

    /**
     * Adds two monetary values.
     */
    private Money addMoney(
            Money first,
            Money second) {

        if (first == null
                || first.getAmount() == null) {

            return second != null
                    ? second
                    : Money.defaultMoney();
        }

        if (second == null
                || second.getAmount() == null) {

            return first;
        }

        return Money.of(
                first.getAmount()
                        .add(second.getAmount()),
                first.getCurrency());
    }
}