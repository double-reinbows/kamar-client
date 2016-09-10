package com.martabak.kamar.domain.permintaan;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by adarsh on 21/08/16.
 */
public class LaundryOrder extends Content {

    public final List<LaundryOrderItem> items;


    @SerializedName("total_price") public final Integer totalPrice;

    public LaundryOrder() {
        super();
        this.items = null;
        this.totalPrice = null;
    }

    public LaundryOrder(String message, List<LaundryOrderItem> items, int totalPrice) {
        super(message);
        this.items = items;
        this.totalPrice = totalPrice;
    }

    public String getType() {
        return Permintaan.TYPE_LAUNDRY;
    }

}
