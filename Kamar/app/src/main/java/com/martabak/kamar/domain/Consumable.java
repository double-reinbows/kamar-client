package com.martabak.kamar.domain;

import java.util.List;

/**
 * A consumable item on a menu that a {@link Guest} can order from the restaurant.
 */
public class Consumable extends Model {

    /**
     * The name; e.g. RAWON DAGING INDOLUXE.
     */
    public final String name;

    /**
     * The description; e.g. Kluwek beef soup served with rice, sambal, lime and crackers.
     */
    public final String description;

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

    /**
     * A list of potential tags that this consumable may have. Can be one of:
     * <li>
     *     <ul>SIGNATURE</ul>
     *     <ul>VEGETARIAN</ul>
     *     <ul>CHILLI</ul>
     *     <ul>RECOMMENDED</ul>
     * </li>
     */
    public final List<String> tags;

    public Consumable() {
        this.name = null;
        this.description = null;
        this.section = null;
        this.subsection = null;
        this.order = null;
        this.price = null;
        this.tags = null;
    }

    public Consumable(String name, String description, String section, String subsection,
                      Integer order, Integer price, List<String> tags) {
        this.name = name;
        this.description = description;
        this.section = section;
        this.subsection = subsection;
        this.order = order;
        this.price = price;
        this.tags = tags;
    }

}
