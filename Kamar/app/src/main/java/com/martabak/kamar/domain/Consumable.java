package com.martabak.kamar.domain;

import com.google.gson.annotations.SerializedName;
import com.martabak.kamar.service.MenuServer;
import com.martabak.kamar.service.MenuService;

/**
 * A consumable item on a menu that a {@link Guest} can order from the restaurant.
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
    public final String name;

    /**
     * The description; e.g. Kluwek beef soup served with rice, sambal, lime and crackers.
     */
    @SerializedName("descriptionEn") public final String descriptionEn;

    /**
     * The description in INDO; e.g. dwklajdawlkdjd akwjdwadawlk  dka jldkjwal d.
     */
    @SerializedName("descriptionIn") public final String descriptionIn;

    /**
     * The section; e.g. INDONESIAN.
     */
    public final String section;

    /**
     * The subsection; e.g. MAIN COURSE.
     */
    public final String subsection;

    /**
     * The order that this item should show up in the menu's section or subsection; e.g. 2.
     */
    public final Integer order;

    /**
     * The price; e.g. 65.
     */
    public final Integer price;

    public Consumable() {
        this.name = null;
        this.descriptionEn = null;
        this.descriptionIn = null;
        this.section = null;
        this.subsection = null;
        this.order = null;
        this.price = null;
    }

    public Consumable(String name, String descriptionEn, String descriptionIn, String section,
                      String subsection, Integer order, Integer price) {
        this.name = name;
        this.descriptionEn = descriptionEn;
        this.descriptionIn = descriptionIn;
        this.section = section;
        this.subsection = subsection;
        this.order = order;
        this.price = price;
    }

    /**
     * @return The URL of the image of this consumable.
     * E.g. http://theserver:5984/219310931202313/image.jpg.
     */
    public String getImageUrl() {
        return MenuServer.getBaseUrl() + "/menu/" + this._id + "/" + MenuService.CONSUMABLE_IMAGE_PATH;
    }

}
