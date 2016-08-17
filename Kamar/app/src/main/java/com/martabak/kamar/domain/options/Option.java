package com.martabak.kamar.domain.options;

import com.martabak.kamar.domain.Model;

/**
 * Abstract parent for {@link EngineeringOption}, {@link HousekeepingOption}, {@link MassageOption}.
 */
public abstract class Option extends Model {

    /**
     * The name of the option, in English.
     */
    public final String name_en;

    /**
     * The name of the option, in Indonesian Bahasa.
     */
    public final String name_in;

    /**
     * The name of the option, in Chinese.
     */
    public final String name_zh;

    /**
     * The order that this option should appear relative to other options.
     */
    public final Integer order;

    public Option() {
        this.name_en = null;
        this.name_in = null;
        this.name_zh = null;
        this.order = null;
    }

    public Option(String name_en, String name_in, String name_zh, Integer order) {
        super(null, null);
        this.name_en = name_en;
        this.name_in = name_in;
        this.name_zh = name_zh;
        this.order = order;
    }

    public Option(String _id, String _rev, String name_en, String name_in, String name_zh, Integer order) {
        super(_id, _rev);
        this.name_en = name_en;
        this.name_in = name_in;
        this.name_zh = name_zh;
        this.order = order;
    }

}
