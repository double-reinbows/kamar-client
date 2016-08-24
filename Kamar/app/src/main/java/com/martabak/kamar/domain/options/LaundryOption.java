package com.martabak.kamar.domain.options;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.martabak.kamar.service.StaffServer;

import java.util.Locale;

/**
 * Created by adarsh on 20/08/16.
 */
public class LaundryOption extends Option {

    /**
     * The laundry price
     */
    @SerializedName("laundry_price") public final Integer laundryPrice;

    /**
     * The pressing price
     */
    @SerializedName("pressing_price") public final Integer pressingPrice;


    public LaundryOption() {
        this.laundryPrice = null;
        this.pressingPrice = null;
    }

    public LaundryOption(String nameEn, String nameIn, String nameZh, String nameRu, Integer laundryPrice,
                         Integer pressingPrice, Integer order, String attachmentName) {
        super(null, null, nameEn, nameIn, nameZh, nameRu, order, attachmentName);
        this.laundryPrice = laundryPrice;
        this.pressingPrice = pressingPrice;

    }

    public LaundryOption(String _id, String _rev, String nameEn, String nameIn, String nameZh, String nameRu,
                         Integer laundryPrice, Integer pressingPrice, Integer order, String attachmentName) {
        super(_id, _rev, nameEn, nameIn, nameZh, nameRu, order, attachmentName);
        this.laundryPrice = laundryPrice;
        this.pressingPrice = pressingPrice;
    }

    /**
     * @return The URL of the image of this option.
     * E.g. http://theserver:5984/219310931202313/image.jpg.
     */
    public String getImageUrl() {
        return StaffServer.getBaseUrl() + "/massage_option/" + this._id + "/" + this.attachmentName;
    }
}

