package com.martabak.kamar.domain.options;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * A Housekeeping {@link SectionedOption}.
 */
public abstract class SectionedOption extends Option {

    /**
     * The name of the option's section, in English.
     */
    @SerializedName("section_en") public final String sectionEn;

    /**
     * The name of the option's section, in Indonesian Bahasa.
     */
    @SerializedName("section_in") public final String sectionIn;

    /**
     * The name of the option's section, in Chinese.
     */
    @SerializedName("section_zh") public final String sectionZh;

    /**
     * The name of the option's section, in Russian.
     */
    @SerializedName("section_ru") public final String sectionRu;

    public SectionedOption() {
        this.sectionEn = null;
        this.sectionIn = null;
        this.sectionZh = null;
        this.sectionRu = null;
    }


    public SectionedOption(String nameEn, String nameIn, String nameZh, String nameRu, Integer order, String attachmentName,
                           String sectionEn, String sectionIn, String sectionZh, String sectionRu) {

        super(null, null, nameEn, nameIn, nameZh, nameRu, order, attachmentName);
        this.sectionEn = sectionEn;
        this.sectionIn = sectionIn;
        this.sectionZh = sectionZh;
        this.sectionRu = sectionRu;
    }


    public SectionedOption(String _id, String _rev, String nameEn, String nameIn, String nameZh,
                           String nameRu, Integer order, String sectionEn, String sectionIn,
                           String sectionZh, String sectionRu, String attachmentName) {
        super(_id, _rev, nameEn, nameIn, nameZh, nameRu, order, attachmentName);
        this.sectionEn = sectionEn;
        this.sectionIn = sectionIn;
        this.sectionZh = sectionZh;
        this.sectionRu = sectionRu;
    }

    /**
     * @return The description in the appropriate language.
     */
    public String getSection() {
        if (Locale.getDefault().getLanguage().equals(Locale.ENGLISH.getLanguage())) {
            return this.sectionEn;
        } else if (Locale.getDefault().getLanguage().equals(new Locale("in").getLanguage())) {
            return this.sectionIn;
        } else if (Locale.getDefault().getLanguage().equals(Locale.CHINESE.getLanguage())) {
            return this.sectionZh;
        } else if (Locale.getDefault().getLanguage().equals(new Locale("ru").getLanguage())) {
            return this.sectionRu;
        } else {
            Log.e(MassageOption.class.getCanonicalName(), "Unknown locale language: " + Locale.getDefault().getLanguage());
            return this.sectionEn;
        }
    }

}
