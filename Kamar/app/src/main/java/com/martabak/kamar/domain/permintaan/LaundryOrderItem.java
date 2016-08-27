package com.martabak.kamar.domain.permintaan;

import com.martabak.kamar.domain.options.LaundryOption;

/**
 * Created by adarsh on 28/08/16.
 */
public class LaundryOrderItem {

    public final Integer quantity;
    /**
     * The laundry option.
     */
    public final LaundryOption option;

    public final Integer price;

    public LaundryOrderItem(Integer quantity, Integer price, LaundryOption option) {
        this.quantity = quantity;
        this.price = price;
        this.option = option;
    }
}
