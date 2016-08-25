package com.martabak.kamar.domain.permintaan;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by adarsh on 21/08/16.
 */
public class Laundry extends Content {

    public final List<OrderItem> items;

    @SerializedName("total_price") public final Integer totalPrice;

    public Laundry() {
        super();
        this.items = null;
        this.totalPrice = null;
    }

    public Laundry(String message, List<OrderItem> items, int totalPrice) {
        super(message);
        this.items = items;
        this.totalPrice = totalPrice;
    }

    public String getType() {
        return Permintaan.TYPE_LAUNDRY;
    }

}
