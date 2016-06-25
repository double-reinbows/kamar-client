package com.martabak.kamar.domain.managers;

import com.martabak.kamar.domain.permintaan.RestaurantOrder;

/**
 * Keeps the restaurant order in memory to be passed from RestaurantActivity to
 * RestaurantConfirmationActivity.
 */
public class RestaurantOrderManager {

    private static RestaurantOrderManager instance;

    private RestaurantOrder order = null;

    private RestaurantOrderManager() {}

    public static RestaurantOrderManager getInstance() {
        if (instance == null) {
            instance = new RestaurantOrderManager();
        }
        return instance;
    }

    /**
     * @return The current restaurant order, if it has been set.
     */
    public RestaurantOrder getOrder() {
        return order;
    }

    /**
     * Set the current restaurant order.
     * @param order The current restaurant order.
     */
    public void setOrder(RestaurantOrder order) {
        this.order = order;
    }

}
