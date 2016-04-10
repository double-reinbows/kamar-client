package com.martabak.kamar.domain.permintaan;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Consumable content type of a {@link Permintaan}.
 */
public class Consumable extends Content {

    public final List<OrderItem> items;

    @SerializedName("total_price") public final Integer totalPrice;

    public Consumable() {
        super();
        this.items = null;
        this.totalPrice = null;
    }

    public Consumable(String message, List<OrderItem> items, int totalPrice) {
        super(message);
        this.items = items;
        this.totalPrice = totalPrice;
    }

    public String getType() {
        return "CONSUMABLE";
    }

}