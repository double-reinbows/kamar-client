package com.martabak.kamar.domain;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.martabak.kamar.service.MenuServer;
import com.martabak.kamar.service.MenuService;

import java.util.Locale;

/**
 * A consumable item on a menu that a {@link Guest} can order from the ic_restaurant.
 */
public class Consumable extends Model {

    /**
     * The FOOD consumable section.
     */
    public static final String SECTION_FOOD = "FOOD";

    /**
     * The DESSERTS consumable section.
     */
    public static final String SECTION_DESSERTS = "DESSERTS";

    /**
     * The BEVERAGES consumable section.
     */
    public static final String SECTION_BEVERAGES = "BEVERAGES";

    /**
     * The name; e.g. RAWON DAGING INDOLUXE.
     */
    public final String nameEn;

    /**
     * The name; e.g. RAWON DAGING INDOLUXE.
     */
    public final String nameIn;

    /**
     * The name; e.g. RAWON DAGING INDOLUXE.
     */
    public final String nameRu;

    /**
     * The name; e.g. RAWON DAGING INDOLUXE.
     */
    public final String nameZh;
    /**
     * The description; e.g. Kluwek beef soup served with rice, sambal, lime and crackers.
     */
    @SerializedName("descriptionEn") public final String descriptionEn;

    /**
     * The description in INDO; e.g. dwklajdawlkdjd akwjdwadawlk  dka jldkjwal d.
     */
    @SerializedName("descriptionIn") public final String descriptionIn;

    /**
     * The description in CHINESE; e.g. xing xong xang.
     */
    @SerializedName("description_zh") public final String descriptionZh;

    /**
     * The description in RUSSIAN; e.g. vodka kommisar.
     */
    @SerializedName("description_ru") public final String descriptionRu;

    /**
     * The section/tab; e.g. INDONESIAN.
     */
    public final String sectionEn;

    /**
     * The section/tab; e.g. INDONESIAN.
     */
    public final String sectionIn;

    /**
     * The section/tab; e.g. INDONESIAN.
     */
    public final String sectionRu;

    /**
     * The section/tab; e.g. INDONESIAN.
     */
    public final String sectionZh;

    /**
     * The subsection/expandable list dropdown; e.g. MAIN COURSE.
     */
    public final String subsectionEn;

    /**
     * The subsection/expandable list dropdown; e.g. MAIN COURSE.
     */
    public final String subsectionIn;

    /**
     * The subsection/expandable list dropdown; e.g. MAIN COURSE.
     */
    public final String subsectionRu;

    /**
     * The subsection/expandable list dropdown; e.g. MAIN COURSE.
     */
    public final String subsectionZh;

    /**
     * The order that this item should show up in the menu's section or subsection; e.g. 2.
     */
    public final Integer order;

    /**
     * The name of the image attachment.
     */
    @SerializedName("attachment_name") public final String attachmentName;

    /**
     * The price; e.g. 65.
     */
    public final Integer price;

    public Consumable() {
        this.nameEn = null;
        this.nameIn = null;
        this.nameRu = null;
        this.nameZh = null;
        this.descriptionEn = null;
        this.descriptionIn = null;
        this.descriptionZh = null;
        this.descriptionRu = null;
        this.sectionEn = null;
        this.sectionIn = null;
        this.sectionRu = null;
        this.sectionZh = null;
        this.subsectionEn = null;
        this.subsectionIn = null;
        this.subsectionRu = null;
        this.subsectionZh = null;
        this.order = null;
        this.price = null;
        this.attachmentName = null;
    }

    public Consumable(String nameEn, String nameIn, String nameRu, String nameZh,
                      String descriptionEn, String descriptionIn, String descriptionZh, String descriptionRu,
                      String sectionEn, String sectionIn, String sectionRu, String sectionZh,
                      String subsectionEn, String subsectionIn, String subsectionRu, String subsectionZh,
                      Integer order, Integer price, String attachmentName) {
        this.nameEn = nameEn;
        this.nameIn = nameIn;
        this.nameRu = nameRu;
        this.nameZh = nameZh;
        this.descriptionEn = descriptionEn;
        this.descriptionIn = descriptionIn;
        this.descriptionZh = descriptionZh;
        this.descriptionRu = descriptionRu;
        this.sectionEn = null;
        this.sectionIn = null;
        this.sectionRu = null;
        this.sectionZh = null;
        this.subsectionEn = null;
        this.subsectionIn = null;
        this.subsectionRu = null;
        this.subsectionZh = null;
        this.order = order;
        this.price = price;
        this.attachmentName = attachmentName;
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
            Log.e(Consumable.class.getCanonicalName(), "Unknown locale language: " + Locale.getDefault().getLanguage());
            return this.descriptionEn;
        }
    }

    /**
     * @return The description in the appropriate language.
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
            Log.e(Consumable.class.getCanonicalName(), "Unknown locale language: " + Locale.getDefault().getLanguage());
            return this.nameEn;
        }
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
            Log.e(Consumable.class.getCanonicalName(), "Unknown locale language: " + Locale.getDefault().getLanguage());
            return this.descriptionEn;
        }
    }

    /**
     * @return The description in the appropriate language.
     */
    public String getSubsection() {
        if (Locale.getDefault().getLanguage().equals(Locale.ENGLISH.getLanguage())) {
            return this.subsectionEn;
        } else if (Locale.getDefault().getLanguage().equals(new Locale("in").getLanguage())) {
            return this.subsectionIn;
        } else if (Locale.getDefault().getLanguage().equals(Locale.CHINESE.getLanguage())) {
            return this.subsectionZh;
        } else if (Locale.getDefault().getLanguage().equals(new Locale("ru").getLanguage())) {
            return this.subsectionRu;
        } else {
            Log.e(Consumable.class.getCanonicalName(), "Unknown locale language: " + Locale.getDefault().getLanguage());
            return this.subsectionEn;
        }
    }

    /**
     * @return The URL of the image of this consumable.
     * E.g. http://theserver:5984/219310931202313/image.jpg.
     */
    public String getImageUrl() {
        return MenuServer.getBaseUrl() + "/menu/" + this._id + "/" + this.attachmentName;
    }

}
