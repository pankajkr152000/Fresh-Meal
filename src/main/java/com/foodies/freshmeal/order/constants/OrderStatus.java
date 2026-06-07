package com.foodies.freshmeal.order.constants;

public class OrderStatus {

    public enum PaymentStatus {
        PENDING,
        PAID,
        FAILED
    }

    public enum DeliveryStatus {
        PREPARING,
        SHIPPED,
        DELIVERED
    }
}
