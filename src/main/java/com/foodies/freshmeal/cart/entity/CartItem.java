package com.foodies.freshmeal.cart.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.foodies.freshmeal.cart.constants.CartErrorConstants;
import com.foodies.freshmeal.cart.valueObject.FoodSnapshot;
import com.foodies.freshmeal.common.exception.BusinessException;
import com.foodies.freshmeal.common.valueObject.Money;

import lombok.Getter;

/**
 * Represents an individual food item within a customer's cart.
 *
 * <p>
 * {@code CartItem} is an embedded child of {@link CartEntity} and does not
 * have an independent lifecycle or MongoDB document.
 * </p>
 *
 * <p>
 * The item stores snapshots of the food and pricing information captured
 * when the food was added to the cart. Current food information, availability,
 * status, and pricing must be revalidated before checkout.
 * </p>
 *
 * <p>
 * {@code CartEntity} is responsible for controlling the lifecycle and
 * modification of CartItem instances. Therefore, mutation methods are
 * intentionally package-private and should only be invoked by the Cart
 * aggregate.
 * </p>
 */
@Getter
public class CartItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Snapshot of the food information captured when the item was added.
     */
    private FoodSnapshot foodSnapshot;

    /**
     * Unit price captured when the food was added to the cart.
     *
     * <p>
     * This is a historical price snapshot and must not be treated as the
     * final checkout price.
     * </p>
     */
    private Money unitPrice;

    /**
     * Quantity of this food item in the cart.
     */
    private int quantity;

    /**
     * Total amount for this cart item.
     *
     * <p>
     * Calculated as:
     * {@code unitPrice × quantity}
     * </p>
     */
    private Money itemTotal;

    /**
     * Timestamp at which the food was added to the cart.
     */
    private LocalDateTime addedAt;

    /**
     * Persistence constructor.
     *
     * <p>
     * Required by Spring Data MongoDB.
     * </p>
     */
    protected CartItem() {
        // Required by Spring Data MongoDB.
    }

    /**
     * Creates a new CartItem.
     *
     * <p>
     * This method is intentionally package-private so that CartEntity,
     * as the aggregate root, controls creation of its child entities.
     * </p>
     *
     * @param foodSnapshot food information snapshot
     * @param unitPrice    unit price snapshot
     * @param quantity     requested quantity
     * @param addedAt      timestamp when the food was added
     * @return newly created cart item
     */
    static CartItem create(
            FoodSnapshot foodSnapshot,
            Money unitPrice,
            int quantity,
            LocalDateTime addedAt) {

        CartItem cartItem = new CartItem();

        cartItem.foodSnapshot = foodSnapshot;
        cartItem.unitPrice = unitPrice;
        cartItem.quantity = quantity;
        cartItem.addedAt = addedAt;

        cartItem.recalculateItemTotal();

        return cartItem;
    }

    /**
     * Updates the quantity of this cart item.
     *
     * <p>
     * This method is intentionally package-private. CartEntity must remain
     * responsible for deciding whether a quantity change is allowed.
     * </p>
     *
     * @param quantity new quantity
     */
    void updateQuantity(int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Cart item quantity must be greater than zero.");
        }

        this.quantity = quantity;

        recalculateItemTotal();
    }

    /**
     * Recalculates the total amount for this cart item using the stored
     * unit-price snapshot.
     *
     * <p>
     * This method is intentionally package-private so that the Cart
     * aggregate controls recalculation.
     * </p>
     */
    void recalculateItemTotal() {

        if (unitPrice == null
                || unitPrice.getAmount() == null) {

            this.itemTotal = Money.defaultMoney();
            return;
        }

        BigDecimal totalAmount = unitPrice.getAmount()
                .multiply(BigDecimal.valueOf(quantity));

        this.itemTotal = Money.of(
                totalAmount,
                unitPrice.getCurrency());
    }

    /**
     * Updates the unit-price snapshot of this cart item and recalculates
     * the line-item total.
     *
     * <p>
     * The operation is intentionally package-private so that only the Cart
     * aggregate can control modifications to its child items.
     * </p>
     *
     * @param unitPrice latest server-resolved price snapshot
     */
    void updateUnitPrice(Money unitPrice) {

        if (unitPrice == null) {
            throw new BusinessException(CartErrorConstants.FOOD_PRICE_REQUIRED);
        }

        this.unitPrice = unitPrice;
        recalculateItemTotal();
    }
}