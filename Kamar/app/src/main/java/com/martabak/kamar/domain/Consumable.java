package com.martabak.kamar.domain;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.martabak.kamar.service.MenuServer;
import com.martabak.kamar.service.MenuService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

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
    public static final Set<String> SECTION_BEVERAGES = new HashSet<>(Arrays.asList("DRINKS", "MINUMAN", "АЗИАНАПИТКИТСКИЙ", "饮料"));

    /**
     * The name; e.g. RAWON DAGING INDOLUXE.
     */
    @SerializedName("name_en") public final String nameEn;

    /**
     * The name; e.g. RAWON DAGING INDOLUXE.
     */
    @SerializedName("name_in") public final String nameIn;

    /**
     * The name; e.g. RAWON DAGING INDOLUXE.
     */
    @SerializedName("name_ru") public final String nameRu;

    /**
     * The name; e.g. RAWON DAGING INDOLUXE.
     */
    @SerializedName("name_zh") public final String nameZh;
    /**
     * The description; e.g. Kluwek beef soup served with rice, sambal, lime and crackers.
     */
    @SerializedName("description_en") public final String descriptionEn;

    /**
     * The description in INDO; e.g. dwklajdawlkdjd akwjdwadawlk  dka jldkjwal d.
     */
    @SerializedName("description_in") public final String descriptionIn;

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
    @SerializedName("section_en") public final String sectionEn;

    /**
     * The section/tab; e.g. INDONESIAN.
     */
    @SerializedName("section_in") public final String sectionIn;

    /**
     * The section/tab; e.g. INDONESIAN.
     */
    @SerializedName("section_ru") public final String sectionRu;

    /**
     * The section/tab; e.g. INDONESIAN.
     */
    @SerializedName("section_zh") public final String sectionZh;

    /**
     * The subsection/expandable list dropdown; e.g. MAIN COURSE.
     */
    @SerializedName("subsection_en") public final String subsectionEn;

    /**
     * The subsection/expandable list dropdown; e.g. MAIN COURSE.
     */
    @SerializedName("subsection_in") public final String subsectionIn;

    /**
     * The subsection/expandable list dropdown; e.g. MAIN COURSE.
     */
    @SerializedName("subsection_ru") public final String subsectionRu;

    /**
     * The subsection/expandable list dropdown; e.g. MAIN COURSE.
     */
    @SerializedName("subsection_zh") public final String subsectionZh;

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

    /**
     * Whether the Consumable is active or not
     */
    public final Boolean active;

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
        this.active = null;
    }
/*
    public Consumable(String nameEn, String nameIn, String nameRu, String nameZh,
                      String descriptionEn, String descriptionIn, String descriptionZh, String descriptionRu,
                      String sectionEn, String sectionIn, String sectionRu, String sectionZh,
                      String subsectionEn, String subsectionIn, String subsectionRu, String subsectionZh,
                      Integer order, Integer price, String attachmentName, Boolean active) {
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
        this.active = active;
    }
*/
    public Consumable(String _id, String _rev,
                      String nameEn, String nameIn, String nameRu, String nameZh,
                      String descriptionEn, String descriptionIn, String descriptionZh, String descriptionRu,
                      String sectionEn, String sectionIn, String sectionRu, String sectionZh,
                      String subsectionEn, String subsectionIn, String subsectionRu, String subsectionZh,
                      Integer order, Integer price, String attachmentName, Boolean active) {
        super(_id, _rev);
        this.nameEn = nameEn;
        this.nameIn = nameIn;
        this.nameRu = nameRu;
        this.nameZh = nameZh;
        this.descriptionEn = descriptionEn;
        this.descriptionIn = descriptionIn;
        this.descriptionZh = descriptionZh;
        this.descriptionRu = descriptionRu;
        this.sectionEn = sectionEn;
        this.sectionIn = sectionIn;
        this.sectionRu = sectionRu;
        this.sectionZh = sectionZh;
        this.subsectionEn = subsectionEn;
        this.subsectionIn = subsectionIn;
        this.subsectionRu = subsectionRu;
        this.subsectionZh = subsectionZh;
        this.order = order;
        this.price = price;
        this.attachmentName = attachmentName;
        this.active = active;
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

    /**
     * @return Whether or not this permintaan is cancellable.
     */
    public boolean isDrinks() {
        return SECTION_BEVERAGES.contains(getSection());
    }

}
