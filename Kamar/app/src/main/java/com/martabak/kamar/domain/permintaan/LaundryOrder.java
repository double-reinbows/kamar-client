package com.martabak.kamar.domain.permintaan;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by adarsh on 21/08/16.
 */
public class LaundryOrder extends Content {

    public final List<LaundryOrderItem> items;

    public final List<String> instructions;

    @SerializedName("total_price") public final Integer totalPrice;

    public LaundryOrder() {
        super();
        this.items = null;
        this.instructions = null;
        this.totalPrice = null;
    }

    public LaundryOrder(String message, List<LaundryOrderItem> items, List<String> instructions,
                        int totalPrice) {
        super(message);
        this.items = items;
        this.instructions = instructions;
        this.totalPrice = totalPrice;
    }

    public String getType() {
        return Permintaan.TYPE_LAUNDRY;
    }

}
