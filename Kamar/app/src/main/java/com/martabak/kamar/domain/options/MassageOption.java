package com.martabak.kamar.domain.options;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.martabak.kamar.service.StaffServer;

import java.util.Locale;

/**
 * A Massage {@link Option}.
 */
public class MassageOption extends Option {

    /**
     * The description of the option, in English.
     */
    @SerializedName("description_en") public final String descriptionEn;

    /**
     * The description of the option, in Indonesian Bahasa.
     */
    @SerializedName("description_in") public final String descriptionIn;

    /**
     * The description of the option, in Chinese.
     */
    @SerializedName("description_zh") public final String descriptionZh;

    /**
     * The description of the option, in Russian.
     */
    @SerializedName("description_ru") public final String descriptionRu;

    /**
     * The price of this massage option.
     */
    public final Integer price;

    /**
     * The length of this massage option, in minutes.
     */
    public final Integer length;

    public MassageOption() {
        this.descriptionEn = null;
        this.descriptionIn = null;
        this.descriptionZh = null;
        this.descriptionRu = null;
        this.price = null;
        this.length = null;
    }

    public MassageOption(String nameEn, String nameIn, String nameZh, String nameRu, String descriptionEn,
                         String descriptionIn, String descriptionZh, String descriptionRu,
                         Integer order,Integer price, Integer length, String attachmentName) {
        super(null, null, nameEn, nameIn, nameZh, nameRu, order, attachmentName);
        this.descriptionEn = descriptionEn;
        this.descriptionIn = descriptionIn;
        this.descriptionZh = descriptionZh;
        this.descriptionRu = descriptionRu;
        this.price = price;
        this.length = length;
    }

    public MassageOption(String _id, String _rev, String nameEn, String nameIn, String nameZh, String nameRu,
                         String descriptionEn, String descriptionIn, String descriptionZh, String descriptionRu,
                         Integer order, Integer price, Integer length, String attachmentName) {
        super(_id, _rev, nameEn, nameIn, nameZh, nameRu, order, attachmentName);
        this.descriptionEn = descriptionEn;
        this.descriptionIn = descriptionIn;
        this.descriptionZh = descriptionZh;
        this.descriptionRu = descriptionRu;
        this.price = price;
        this.length = length;
    }

    /**
     * @return The description in the appropriate language.
     */
    public String getDescription() {
        if (Locale.getDefault().getLanguage().equals(Locale.ENGLISH.getLanguage())) {
            return this.descriptionEn;
        } else if (Locale.getDefault().getLanguage().equals(new Locale("in").getLanguage())) {
            return this.descriptionIn;
        } else if (Locale.getDefault().getLanguage().equals(Locale.CHINESE.getLanguage())) {
            return this.descriptionZh;
        } else if (Locale.getDefault().getLanguage().equals(new Locale("ru").getLanguage())) {
            return this.descriptionRu;
        } else {
            Log.e(MassageOption.class.getCanonicalName(), "Unknown locale language: " + Locale.getDefault().getLanguage());
            return this.descriptionEn;
        }
    }

    /**
     * @return The URL of the image of this option.
     * E.g. http://theserver:5984/219310931202313/image.jpg.
     */
    public String getImageUrl() {
        return StaffServer.getBaseUrl() + "/massage_option/" + this._id + "/" + this.attachmentName;
    }

}
