package com.foodies.freshmeal.common.io;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DataContext implements IDataContext {

    private static final long serialVersionUID = -7905530939546316873L;

    /*
     * ========================================================================
     * DataContext Attribute Names
     * ========================================================================
     *
     * Keep all commonly used DataContext keys here. These constants should be used
     * instead of hard-coded String values.
     */

    public static final String RESTAURANT_ID = "RESTAURANT_ID";
    public static final String BRANCH_ID = "BRANCH_ID";

    public static final String USER_ID = "USER_ID";
    public static final String USERNAME = "USERNAME";

    public static final String FOOD_ID = "FOOD_ID";
    public static final String ORDER_ID = "ORDER_ID";
    public static final String IMAGE_ID = "IMAGE_ID";

    public static final String REQUEST_ID = "REQUEST_ID";
    public static final String CORRELATION_ID = "CORRELATION_ID";

    public static final String IP_ADDRESS = "IP_ADDRESS";
    public static final String USER_AGENT = "USER_AGENT";

    public static final String API_NAME = "API_NAME";
    public static final String API_PATH = "API_PATH";
    public static final String HTTP_METHOD = "HTTP_METHOD";

    public static final String SOURCE = "SOURCE";
    public static final String CHANNEL = "CHANNEL";

    public static final String IMAGE_SEQUENCE = "IMAGE_SEQUENCE";
    public static final String FOOD_SEQUENCE = "FOOD_SEQUENCE";
    public static final String RESTAURANT_SEQUENCE = "RESTAURANT_SEQUENCE";
    public static final String RESTAURANT_BRANCH_SEQUENCE = "RESTAURANT_BRANCH_SEQUENCE";

    public static final String ADDRESS_SEQUENCE = "ADDRESS_SEQUENCE";
    /*
     * ========================================================================
     * Internal State
     * ========================================================================
     */

    private final Map<String, Object> attributes = new HashMap<>();

    private Date asOfBusinessDate;

    /*
     * ========================================================================
     * Attribute Operations
     * ========================================================================
     */

    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public boolean hasAttribute(String name) {
        return attributes.containsKey(name);
    }

    @Override
    public void setAttribute(String name, Object value) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("DataContext attribute name cannot be null or blank");
        }

        attributes.put(name, value);
    }

    @Override
    public Map<String, Object> getAllAttributes() {
        return new HashMap<>(attributes);
    }

    @Override
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes.clear();

        if (attributes != null) {
            this.attributes.putAll(attributes);
        }
    }

    /*
     * ========================================================================
     * Business Date
     * ========================================================================
     */

    @Override
    public Date getAsOfBusinessDate() {
        return asOfBusinessDate;
    }

    @Override
    public void setAsOfBusinessDate(Date asOfBusinessDate) {
        this.asOfBusinessDate = asOfBusinessDate;
    }

    /*
     * ========================================================================
     * Backward/Existing API Compatibility
     * ========================================================================
     */

    @Override
    public Date getAsOfDateBusiness() {
        return asOfBusinessDate;
    }
}