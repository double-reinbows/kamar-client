package com.martabak.kamar.domain.options;

/**
 * A Massge {@link Option}.
 */
public class MassageOption extends Option {

    /**
     * The price of this massage option.
     */
    public final Integer price;

    public MassageOption() {
        this.price = null;
    }

    public MassageOption(String name_en, String name_in, String name_zh, Integer order, Integer price) {
        super(null, null, name_en, name_in, name_zh, order);
        this.price = price;
    }

    public MassageOption(String _id, String _rev, String name_en, String name_in, String name_zh, Integer order, Integer price) {
        super(_id, _rev, name_en, name_in, name_zh, order);
        this.price = price;
    }

}
