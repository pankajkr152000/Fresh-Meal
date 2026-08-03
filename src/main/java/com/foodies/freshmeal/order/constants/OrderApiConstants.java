package com.foodies.freshmeal.order.constants;

/**
 * Order API endpoint constants.
 *
 * @author Pankaj Kumar
 * @version 1.0
 */
public final class OrderApiConstants {

    private OrderApiConstants() {
    }

    // =========================================================
    // Base URL
    // =========================================================

    public static final String BASE_URL = "/api/orders";

    // =========================================================
    // Order CRUD APIs
    // =========================================================

    public static final String CREATE = "/create";
    public static final String UPDATE = "/update";
    public static final String DELETE = "/delete";
    public static final String VIEW = "/view";
    public static final String READ_ALL = "/readAll";

    // =========================================================
    // Order Search APIs
    // =========================================================

    public static final String SEARCH = "/search";
    public static final String SEARCH_BY_ORDER_ID = "/search/order-id";
    public static final String SEARCH_BY_CUSTOMER = "/search/customer";
    public static final String SEARCH_BY_RESTAURANT = "/search/restaurant";
    public static final String SEARCH_BY_DELIVERY_PARTNER = "/search/delivery-partner";
    public static final String SEARCH_BY_STATUS = "/search/status";
    public static final String SEARCH_BY_PAYMENT_STATUS = "/search/payment-status";
    public static final String SEARCH_BY_ORDER_TYPE = "/search/order-type";

    // =========================================================
    // Order Filter APIs
    // =========================================================

    public static final String FILTER = "/filter";
    public static final String FILTER_BY_DATE = "/filter/date";
    public static final String FILTER_BY_STATUS = "/filter/status";
    public static final String FILTER_BY_PAYMENT_STATUS = "/filter/payment-status";
    public static final String FILTER_BY_RESTAURANT = "/filter/restaurant";
    public static final String FILTER_BY_DELIVERY_PARTNER = "/filter/delivery-partner";
    public static final String FILTER_BY_ORDER_TYPE = "/filter/order-type";

    // =========================================================
    // Order Status APIs
    // =========================================================

    public static final String UPDATE_STATUS = "/update-status";
    public static final String CANCEL = "/cancel";
    public static final String TRACK = "/track";
    public static final String TIMELINE = "/timeline";

    // =========================================================
    // Delivery APIs
    // =========================================================

    public static final String ASSIGN_DELIVERY_PARTNER = "/assign-delivery-partner";
    public static final String UPDATE_DELIVERY_STATUS = "/update-delivery-status";

    // =========================================================
    // Payment APIs
    // =========================================================

    public static final String UPDATE_PAYMENT = "/update-payment";
    public static final String UPDATE_PAYMENT_STATUS = "/update-payment-status";
    public static final String REFUND = "/refund";

    // =========================================================
    // Order Notes APIs
    // =========================================================

    public static final String UPDATE_NOTES = "/update-notes";

    // =========================================================
    // Order Pagination APIs
    // =========================================================

    public static final String PAGE = "/page";

    // =========================================================
    // Order Analytics APIs
    // =========================================================

    public static final String COUNT = "/count";
    public static final String STATISTICS = "/statistics";
    public static final String TODAY_ORDERS = "/today-orders";
    public static final String PENDING_ORDERS = "/pending-orders";
    public static final String RECENT_ORDERS = "/recent-orders";
    public static final String REVENUE = "/revenue";

    // =========================================================
    // Order Metadata APIs
    // =========================================================

    public static final String METADATA = "/metadata";
}
