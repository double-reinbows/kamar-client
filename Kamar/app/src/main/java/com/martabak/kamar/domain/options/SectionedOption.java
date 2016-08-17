package com.martabak.kamar.domain.options;

/**
 * A Housekeeping {@link SectionedOption}.
 */
public abstract class SectionedOption extends Option {

    /**
     * The name of the option's section, in English.
     */
    public final String section_en;

    /**
     * The name of the option's section, in Indonesian Bahasa.
     */
    public final String section_in;

    /**
     * The name of the option's section, in Chinese.
     */
    public final String section_zh;

    public SectionedOption() {
        this.section_en = null;
        this.section_in = null;
        this.section_zh = null;
    }

    public SectionedOption(String name_en, String name_in, String name_zh, Integer order, String section_en, String section_in, String section_zh) {
        super(null, null, name_en, name_in, name_zh, order);
        this.section_en = section_en;
        this.section_in = section_in;
        this.section_zh = section_zh;
    }

    public SectionedOption(String _id, String _rev, String name_en, String name_in, String name_zh, Integer order, String section_en, String section_in, String section_zh) {
        super(_id, _rev, name_en, name_in, name_zh, order);
        this.section_en = section_en;
        this.section_in = section_in;
        this.section_zh = section_zh;
    }

}
