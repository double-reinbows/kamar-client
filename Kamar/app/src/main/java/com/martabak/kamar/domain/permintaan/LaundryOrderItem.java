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

    public final Boolean laundry;

    public final Boolean pressing;

    public LaundryOrderItem(Integer quantity, Integer price, Boolean laundry,
                            Boolean pressing, LaundryOption option) {
        this.quantity = quantity;
        this.price = price;
        this.laundry = laundry;
        this.pressing = pressing;
        this.option = option;
    }
}
