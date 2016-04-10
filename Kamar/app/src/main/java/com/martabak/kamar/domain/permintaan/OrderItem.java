package com.martabak.kamar.domain.permintaan;

/**
 * A single item from a {@link Consumable} order.
 */
public class OrderItem {

    public final Integer quantity;

    public final String name;

    public OrderItem(Integer quantity, String name) {
        this.quantity = quantity;
        this.name = name;
    }
}
