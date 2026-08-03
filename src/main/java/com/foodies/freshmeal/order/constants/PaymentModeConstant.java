package com.foodies.freshmeal.order.constants;

import com.foodies.freshmeal.common.contract.IDisplayOption;

public enum PaymentModeConstant implements IDisplayOption {

    CASH_ON_DELIVERY("Cash On Delivery"),
    ONLINE("Online"),
    UPI("UPI"),
    CREDIT_CARD("Credit Card"),
    DEBIT_CARD("Debit Card"),
    NET_BANKING("Net Banking"),
    WALLET("Wallet");

    /**
     * User-friendly display label.
     */
    private final String displayName;

    PaymentModeConstant(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getLabel() {
        return displayName;
    }

    @Override
    public String getValue() {
        return name();
    }

}