package com.martabak.kamar.domain.options;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.martabak.kamar.domain.Model;

import java.util.Locale;

/**
 * Abstract parent for {@link EngineeringOption}, {@link HousekeepingOption}, {@link MassageOption}.
 */
public abstract class Option extends Model {

    /**
     * The name of the option, in English.
     */
    @SerializedName("name_en") public final String nameEn;

    /**
     * The name of the option, in Indonesian Bahasa.
     */
    @SerializedName("name_in") public final String nameIn;

    /**
     * The name of the option, in Chinese.
     */
    @SerializedName("name_zh") public final String nameZh;

    /**
     * The name of the option, in Russian.
     */
    @SerializedName("name_ru") public final String nameRu;

    /**
     * The order that this option should appear relative to other options.
     */
    public final Integer order;

    public Option() {
        this.nameEn = null;
        this.nameIn = null;
        this.nameZh = null;
        this.nameRu = null;
        this.order = null;
    }

    public Option(String nameEn, String nameIn, String nameZh, String nameRu, Integer order) {
        super(null, null);
        this.nameEn = nameEn;
        this.nameIn = nameIn;
        this.nameZh = nameZh;
        this.nameRu = nameRu;
        this.order = order;
    }

    public Option(String _id, String _rev, String nameEn, String nameIn, String nameZh, String nameRu, Integer order) {
        super(_id, _rev);
        this.nameEn = nameEn;
        this.nameIn = nameIn;
        this.nameZh = nameZh;
        this.nameRu = nameRu;
        this.order = order;
    }

    /**
     * @return The name in the appropriate language.
     */
    public String getName() {
        if (Locale.getDefault().getLanguage().equals(Locale.ENGLISH.getLanguage())) {
            return this.nameEn;
        } else if (Locale.getDefault().getLanguage().equals(new Locale("in").getLanguage())) {
            return this.nameIn;
        } else if (Locale.getDefault().getLanguage().equals(Locale.CHINESE.getLanguage())) {
            return this.nameZh;
        } else if (Locale.getDefault().getLanguage().equals(new Locale("ru").getLanguage())) {
            return this.nameRu;
        } else {
            Log.e(Option.class.getCanonicalName(), "Unknown locale language: " + Locale.getDefault().getLanguage());
            return this.nameEn;
        }
    }

}
