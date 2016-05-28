package com.martabak.kamar.domain;

import java.util.List;

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
    public final String description_en;

    /**
     * The description; e.g. Kluwek beef soup served with rice, sambal, lime and crackers.
     */
    public final String description_in;

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
        this.description_en = null;
        this.description_in = null;
        this.section = null;
        this.subsection = null;
        this.order = null;
        this.price = null;
    }

    public Consumable(String name, String description_en, String description_in, String section,
                      String subsection, Integer order, Integer price) {
        this.name = name;
        this.description_en = description_en;
        this.description_in = description_in;
        this.section = section;
        this.subsection = subsection;
        this.order = order;
        this.price = price;
    }

}
