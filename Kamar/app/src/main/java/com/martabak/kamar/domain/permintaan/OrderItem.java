package com.martabak.kamar.domain.permintaan;

/**
 * A single item from a {@link RestaurantOrder}.
 */
public class OrderItem {

    public final Integer quantity;

    public final String name;

    public final Integer price;

    public final String note;

    public OrderItem(Integer quantity, String name, Integer price, String note) {
        this.quantity = quantity;
        this.name = name;
        this.price = price;
        this.note = note;
    }
}
