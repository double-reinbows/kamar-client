package com.martabak.kamar.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * A consumable item on a menu that a {@link Guest} can order from the restaurant.
 */
public class Consumable extends Model implements Parcelable {

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

    public static final Parcelable.Creator<Consumable> CREATOR = new Creator<Consumable>() {
        @Override
        public Consumable createFromParcel(Parcel source) {
            Consumable mConsumable = new Consumable(
                    source.readString(), source.readString(), source.readString(),
                    source.readString(), source.readString(), source.readInt(), source.readInt()
            );
            return mConsumable;
        }

        @Override
        public Consumable[] newArray(int size) {
            return new Consumable[size];
        }
    };
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.name);
        parcel.writeString(this.description_en);
        parcel.writeString(this.description_in);
        parcel.writeString(this.section);
        parcel.writeString(this.subsection);
        parcel.writeInt(this.order);
        parcel.writeInt(this.price);
    }

}
