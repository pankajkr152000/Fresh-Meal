
package com.foodies.freshmeal.checkout.entity;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.foodies.freshmeal.checkout.constants.CheckoutStatusConstant;
import com.foodies.freshmeal.checkout.valueObject.CheckoutAddressSnapshot;
import com.foodies.freshmeal.checkout.valueObject.CheckoutFailureDetails;
import com.foodies.freshmeal.checkout.valueObject.CheckoutItemSnapshot;
import com.foodies.freshmeal.checkout.valueObject.CheckoutOrderPreferencesSnapshot;
import com.foodies.freshmeal.checkout.valueObject.CheckoutPricingSnapshot;
import com.foodies.freshmeal.checkout.valueObject.CheckoutStatusHistory;
import com.foodies.freshmeal.common.date.AppCalendar;
import com.foodies.freshmeal.common.entity.ABaseEntity;
import com.foodies.freshmeal.order.constants.OrderTypeConstant;

import lombok.Getter;

/**
 * ============================================================================
 * Checkout Entity
 * ============================================================================
 *
 * Represents a persisted checkout session in the FreshMeal application.
 *
 * <p>
 * Checkout is a stateful aggregate responsible for maintaining the lifecycle
 * of a customer's purchase attempt.
 * </p>
 *
 * <p>
 * Responsibilities:
 * </p>
 *
 * <ul>
 * <li>Maintain checkout identity and ownership.</li>
 * <li>Track checkout lifecycle and status transitions.</li>
 * <li>Preserve item, address, and pricing snapshots.</li>
 * <li>Maintain checkout status history.</li>
 * <li>Track checkout expiry and confirmation timestamps.</li>
 * <li>Maintain the originating order reference.</li>
 * <li>Record structured failure information.</li>
 * <li>Protect aggregate invariants.</li>
 * </ul>
 *
 * <p>
 * Cross-aggregate validation, pricing calculations, cart synchronization,
 * order creation, and reconciliation are handled by the service layer.
 * </p>
 *
 * <p>
 * This entity deliberately does not expose unrestricted setters. State
 * changes must be performed through explicit domain operations.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Getter
@Document(collection = "fm_checkout")
public class CheckoutEntity extends ABaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    // =========================================================================
    // Identity / Ownership
    // =========================================================================

    /**
     * Unique business identifier for the checkout session.
     */
    @Indexed(unique = true)
    private String checkoutNumber;

    /**
     * Authenticated user who initiated the checkout.
     */
    @Indexed
    private String userNumber;

    /**
     * Source cart associated with this checkout.
     */
    @Indexed
    private String cartNumber;

    /**
     * Restaurant associated with the checkout.
     */
    private String restaurantNumber;

    /**
     * Restaurant branch associated with the checkout.
     */
    private String restaurantBranchNumber;

    // =========================================================================
    // Lifecycle
    // =========================================================================

    /**
     * Current checkout lifecycle status.
     */
    @Field("status")
    private CheckoutStatusConstant status = CheckoutStatusConstant.INITIATED;

    /**
     * Timestamp of the most recent status transition.
     */
    private LocalDateTime statusUpdatedAt;

    /**
     * Historical record of checkout status transitions.
     */
    private List<CheckoutStatusHistory> statusHistory = new ArrayList<>();

    /**
     * Timestamp when checkout was initiated.
     */
    private LocalDateTime initiatedAt;

    /**
     * Checkout expiry timestamp.
     */
    @Indexed
    private LocalDateTime expiresAt;

    /**
     * Timestamp when checkout confirmation began.
     */
    private LocalDateTime confirmedAt;

    /**
     * Timestamp when checkout reached its terminal completion state.
     */
    private LocalDateTime completedAt;

    // =========================================================================
    // Checkout Snapshots
    // =========================================================================

    /**
     * Immutable snapshot of items reviewed during checkout.
     */
    private List<CheckoutItemSnapshot> items = new ArrayList<>();

    /**
     * Delivery address snapshot reviewed during checkout.
     */
    private CheckoutAddressSnapshot deliveryAddress;

    /**
     * Pricing snapshot calculated by the server.
     */
    private CheckoutPricingSnapshot pricing;

    // =========================================================================
    // Order Reference
    // =========================================================================

    /**
     * Business identifier of the order created from this checkout.
     *
     * <p>
     * This field stores only the reference. The Order aggregate owns its
     * lifecycle and business data.
     * </p>
     */
    @Indexed(sparse = true)
    private String orderNumber;

    /**
     * Immutable snapshot of customer-selected order preferences.
     *
     * <p>
     * Captures customer intent independently of server-calculated pricing.
     * These preferences are preserved for confirmation and subsequent order
     * creation.
     * </p>
     *
     * <p>
     * The Order aggregate remains the authoritative owner of the final order
     * data and lifecycle.
     * </p>
     */
    private CheckoutOrderPreferencesSnapshot orderPreferences;

    // =========================================================================
    // Failure Details
    // =========================================================================

    /**
     * Structured information about the most recent checkout failure.
     */
    private CheckoutFailureDetails failureDetails;

    /**
     * <ul>
     * - checkoutNumber identifies the checkout session.
     * </ul>
     * 
     * <ul>
     * - confirmationIdempotencyKey identifies the specific confirmation attempt
     * associated with that checkout.
     * </ul>
     * 
     * <ul>
     * Think of it this way: one checkout can receive multiple HTTP requests, but
     * all retries of the same confirmation attempt must be recognized as one
     * logical operation.
     * </ul>
     */
    private String confirmationIdempotencyKey;

    // =========================================================================
    // Constructors
    // =========================================================================

    /**
     * Required by Spring Data MongoDB.
     */
    protected CheckoutEntity() {
        // Intentionally protected.
    }

    // =========================================================================
    // Factory
    // =========================================================================

    /**
     * Creates a new checkout session.
     *
     * @param checkoutNumber         unique checkout business identifier
     * @param userNumber             authenticated user identifier
     * @param cartNumber             source cart identifier
     * @param restaurantNumber       restaurant identifier
     * @param restaurantBranchNumber restaurant branch identifier
     * @param expiresAt              checkout expiry timestamp
     * @return initialized checkout entity
     */
    public static CheckoutEntity create(
            final String id,
            final String checkoutNumber,
            final String userNumber,
            final String cartNumber,
            final String restaurantNumber,
            final String restaurantBranchNumber,
            final LocalDateTime expiresAt) {

        validateRequired(checkoutNumber, "Checkout number");
        validateRequired(userNumber, "User number");
        validateRequired(cartNumber, "Cart number");
        validateRequired(restaurantNumber, "Restaurant number");
        validateRequired(restaurantBranchNumber, "Restaurant branch number");

        Objects.requireNonNull(expiresAt, "Checkout expiry time is required.");

        final LocalDateTime now = AppCalendar.getBusinessLocalDateTime();

        if (!expiresAt.isAfter(now)) {
            throw new IllegalArgumentException(
                    "Checkout expiry time must be in the future.");
        }

        final CheckoutEntity checkout = new CheckoutEntity();

        checkout.setId(id);
        checkout.checkoutNumber = checkoutNumber;
        checkout.userNumber = userNumber;
        checkout.cartNumber = cartNumber;
        checkout.restaurantNumber = restaurantNumber;
        checkout.restaurantBranchNumber = restaurantBranchNumber;

        checkout.status = CheckoutStatusConstant.INITIATED;
        checkout.initiatedAt = now;
        checkout.statusUpdatedAt = now;
        checkout.expiresAt = expiresAt;

        checkout.statusHistory = new ArrayList<>();
        checkout.items = new ArrayList<>();

        checkout.appendStatusHistory(
                CheckoutStatusConstant.INITIATED,
                "Checkout session initiated.",
                now);

        return checkout;
    }

    // =========================================================================
    // Lifecycle Operations
    // =========================================================================

    /**
     * Starts validation of the checkout session.
     */
    public void startValidation() {

        transitionTo(CheckoutStatusConstant.VALIDATING, "Checkout validation started.");
    }

    /**
     * Marks checkout as ready for customer confirmation.
     *
     * @param validatedItems validated item snapshots
     * @param address        validated delivery address snapshot
     * @param pricing        server-calculated pricing snapshot
     */
    public void markReadyForConfirmation(
            final List<CheckoutItemSnapshot> validatedItems,
            final CheckoutAddressSnapshot address,
            final CheckoutPricingSnapshot pricing,
            final CheckoutOrderPreferencesSnapshot orderPreferences) {

        if (status != CheckoutStatusConstant.VALIDATING) {
            throw new IllegalStateException("Checkout must be in VALIDATING status.");
        }

        if (validatedItems == null || validatedItems.isEmpty()) {
            throw new IllegalArgumentException("At least one checkout item is required.");
        }

        if (orderPreferences.getOrderType() == OrderTypeConstant.DELIVERY) {
            Objects.requireNonNull(address, "Delivery address snapshot is required for delivery orders.");
        }

        Objects.requireNonNull(pricing, "Checkout pricing snapshot is required.");

        Objects.requireNonNull(orderPreferences, "Checkout order preferences snapshot is required.");

        this.items = new ArrayList<>(validatedItems);
        this.deliveryAddress = address;
        this.pricing = pricing;
        this.failureDetails = null;
        this.orderPreferences = orderPreferences;

        transitionTo(CheckoutStatusConstant.READY_FOR_CONFIRMATION, "Checkout validation completed successfully.");
    }

    /**
     * Marks a checkout as failed when its validation process has exceeded
     * the permitted duration.
     *
     * <p>
     * A stale validation is treated as a definitive processing failure
     * because the review operation has not progressed to confirmation.
     * The checkout can be retried through the existing FAILED-to-INITIATED
     * lifecycle transition.
     * </p>
     *
     * @param failureDetails details describing the stale validation
     * @throws IllegalArgumentException if failure details are null
     * @throws IllegalStateException    if the checkout is not currently validating
     */
    public void markValidationTimedOut(
            final CheckoutFailureDetails failureDetails) {

        Objects.requireNonNull(
                failureDetails,
                "Checkout failure details must not be null.");

        if (this.status != CheckoutStatusConstant.VALIDATING) {
            throw new IllegalStateException(
                    "Only a checkout in VALIDATING status can be marked as timed out.");
        }

        if (failureDetails.isReconciliationRequired()) {
            throw new IllegalArgumentException(
                    "A definitive validation timeout must not require reconciliation.");
        }

        markFailed(failureDetails);
    }

    /**
     * Begins checkout confirmation.
     */
    public void beginConfirmation() {

        ensureNotExpired();

        if (status != CheckoutStatusConstant.READY_FOR_CONFIRMATION) {
            throw new IllegalStateException("Checkout is not ready for confirmation.");
        }

        this.confirmedAt = AppCalendar.getBusinessLocalDateTime();

        transitionTo(CheckoutStatusConstant.CONFIRMATION_IN_PROGRESS, "Checkout confirmation started.");
    }

    /**
     * Binds an idempotency key to the checkout confirmation attempt.
     *
     * <p>
     * The first key assigned to a checkout becomes permanent.
     * Repeated submissions using the same key are accepted,
     * while attempts using a different key are rejected.
     * </p>
     *
     * @param idempotencyKey unique key identifying the confirmation request
     * @return true when the key was assigned for the first time;
     *         false when the same key was already assigned
     * @throws IllegalArgumentException when the key is missing
     * @throws IllegalStateException    when a different key is already assigned
     */
    public boolean bindConfirmationIdempotencyKey(final String idempotencyKey) {

        validateRequired(idempotencyKey, "Confirmation idempotency key");

        if (this.confirmationIdempotencyKey == null) {
            this.confirmationIdempotencyKey = idempotencyKey;
            return true;
        }

        if (this.confirmationIdempotencyKey.equals(idempotencyKey)) {
            return false;
        }

        throw new IllegalStateException("Checkout is already bound to a different confirmation key.");
    }

    /**
     * Marks checkout as successfully converted into an order.
     *
     * <p>
     * Idempotent for the same order number. A different order number cannot
     * replace an existing order reference.
     * </p>
     *
     * @param orderNumber created order business identifier
     */
    public void markOrderCreated(final String orderNumber) {

        validateRequired(orderNumber, "Order number");

        if (status == CheckoutStatusConstant.ORDER_CREATED) {

            if (orderNumber.equals(this.orderNumber)) {
                return;
            }

            throw new IllegalStateException("Checkout is already linked to a different order.");
        }

        if (status != CheckoutStatusConstant.CONFIRMATION_IN_PROGRESS) {
            throw new IllegalStateException("Checkout confirmation is not in progress.");
        }

        final LocalDateTime now = AppCalendar.getBusinessLocalDateTime();

        this.orderNumber = orderNumber;
        this.completedAt = now;
        this.failureDetails = null;

        transitionTo(CheckoutStatusConstant.ORDER_CREATED, "Order successfully created from checkout.", now);
    }

    /**
     * Marks checkout as failed after a definitive failure.
     *
     * <p>
     * An unresolved external operation must not be marked as a definitive
     * failure. The service layer must reconcile uncertain outcomes first.
     * </p>
     *
     * @param failureDetails structured failure information
     */
    public void markFailed(final CheckoutFailureDetails failureDetails) {

        Objects.requireNonNull(failureDetails, "Checkout failure details are required.");

        if (failureDetails.isReconciliationRequired()) {
            throw new IllegalArgumentException("Checkout requires reconciliation before failure can be finalized.");
        }

        // Validate the transition before modifying the entity.
        if (status == null || !status.canTransitionTo(CheckoutStatusConstant.FAILED)) {
            throw new IllegalStateException("Checkout cannot transition to FAILED from status: " + status);
        }

        this.failureDetails = failureDetails;

        transitionTo(CheckoutStatusConstant.FAILED, "Checkout processing failed.");
    }

    /**
     * Cancels checkout.
     */
    public void cancel() {

        if (status == CheckoutStatusConstant.CANCELLED) {
            return;
        }

        transitionTo(CheckoutStatusConstant.CANCELLED, "Checkout cancelled.");
    }

    /**
     * Marks checkout as expired.
     */
    public void expire() {

        if (status == CheckoutStatusConstant.EXPIRED) {
            return;
        }

        ensureExpired();

        transitionTo(CheckoutStatusConstant.EXPIRED, "Checkout session expired.");
    }

    // =========================================================================
    // State Queries
    // =========================================================================

    /**
     * Checks whether checkout has expired.
     *
     * @return true when expiry time has passed
     */
    public boolean isExpired() {

        return expiresAt != null && !AppCalendar.getBusinessLocalDateTime().isBefore(expiresAt);
    }

    /**
     * Checks whether checkout is ready for confirmation.
     *
     * @return true when checkout is ready
     */
    public boolean isReadyForConfirmation() {

        return status == CheckoutStatusConstant.READY_FOR_CONFIRMATION;
    }

    /**
     * Checks whether checkout has been converted into an order.
     *
     * @return true when order creation is complete
     */
    public boolean isOrderCreated() {

        return status == CheckoutStatusConstant.ORDER_CREATED;
    }

    /**
     * Checks whether checkout is in a terminal state.
     *
     * @return true when checkout cannot continue its normal lifecycle
     */
    public boolean isTerminal() {

        return status == CheckoutStatusConstant.ORDER_CREATED
                || status == CheckoutStatusConstant.CANCELLED
                || status == CheckoutStatusConstant.EXPIRED;
    }

    // =========================================================================
    // Read-Only Collection Access
    // =========================================================================

    /**
     * Returns an unmodifiable view of checkout items.
     *
     * @return immutable item list
     */
    public List<CheckoutItemSnapshot> getItems() {

        return Collections.unmodifiableList(
                items == null ? Collections.emptyList() : items);
    }

    /**
     * Returns an unmodifiable view of status history.
     *
     * @return immutable status history
     */
    public List<CheckoutStatusHistory> getStatusHistory() {

        return Collections.unmodifiableList(
                statusHistory == null
                        ? Collections.emptyList()
                        : statusHistory);
    }

    // =========================================================================
    // Internal State Management
    // =========================================================================

    /**
     * Performs a validated lifecycle transition.
     *
     * @param targetStatus target checkout status
     * @param reason       transition reason
     */
    private void transitionTo(
            final CheckoutStatusConstant targetStatus,
            final String reason) {

        transitionTo(targetStatus, reason, AppCalendar.getBusinessLocalDateTime());
    }

    /**
     * Performs a validated lifecycle transition using a supplied timestamp.
     *
     * @param targetStatus target checkout status
     * @param reason       transition reason
     * @param changedAt    transition timestamp
     */
    private void transitionTo(
            final CheckoutStatusConstant targetStatus,
            final String reason,
            final LocalDateTime changedAt) {

        Objects.requireNonNull(targetStatus, "Target checkout status is required.");

        Objects.requireNonNull(changedAt, "Status transition timestamp is required.");

        if (status == null || !status.canTransitionTo(targetStatus)) {
            throw new IllegalStateException("Invalid checkout status transition: " + status + " -> " + targetStatus);
        }

        this.status = targetStatus;
        this.statusUpdatedAt = changedAt;

        appendStatusHistory(targetStatus, reason, changedAt);
    }

    /**
     * Appends a status history record.
     *
     * @param newStatus status being recorded
     * @param reason    transition reason
     * @param changedAt transition timestamp
     */
    private void appendStatusHistory(
            final CheckoutStatusConstant newStatus,
            final String reason,
            final LocalDateTime changedAt) {

        if (statusHistory == null) {
            statusHistory = new ArrayList<>();
        }

        statusHistory.add(
                CheckoutStatusHistory.builder()
                        .status(newStatus)
                        .changedAt(changedAt)
                        .reason(reason)
                        .build());
    }

    // =========================================================================
    // Expiry Validation
    // =========================================================================

    /**
     * Ensures checkout has not expired.
     */
    private void ensureNotExpired() {

        if (isExpired()) {
            throw new IllegalStateException("Checkout session has expired.");
        }
    }

    /**
     * Ensures checkout is eligible for expiration.
     */
    private void ensureExpired() {

        if (!isExpired()) {
            throw new IllegalStateException("Checkout session is not yet eligible for expiration.");
        }
    }

    // =========================================================================
    // Validation Helpers
    // =========================================================================

    /**
     * Validates a required string value.
     *
     * @param value     value to validate
     * @param fieldName field name for diagnostic context
     */
    private static void validateRequired(
            final String value,
            final String fieldName) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }
    }

    /**
     * Records a failure that requires reconciliation before checkout processing
     * can safely continue.
     *
     * <p>
     * The checkout remains in its current lifecycle state because the outcome
     * of the operation has not been conclusively established.
     * </p>
     *
     * <p>
     * This method does not authorize a retry, perform reconciliation, or
     * transition the checkout to FAILED. Those responsibilities belong to
     * the service layer.
     * </p>
     *
     * @param failureDetails structured failure information requiring reconciliation
     * @throws NullPointerException     if failure details are null
     * @throws IllegalArgumentException if reconciliation is not required
     */
    public void recordReconciliationRequired(
            final CheckoutFailureDetails failureDetails) {

        Objects.requireNonNull(
                failureDetails,
                "Checkout failure details are required.");

        if (!failureDetails.isReconciliationRequired()) {
            throw new IllegalArgumentException(
                    "Failure details must require reconciliation.");
        }

        if (status != CheckoutStatusConstant.VALIDATING
                && status != CheckoutStatusConstant.CONFIRMATION_IN_PROGRESS) {

            throw new IllegalStateException(
                    "Reconciliation cannot be recorded from status: " + status);
        }

        this.failureDetails = failureDetails;
    }

}