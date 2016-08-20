package com.martabak.kamar.domain.permintaan;

import com.martabak.kamar.domain.options.MassageOption;


/**
 * Massage content type of a {@link Permintaan}.
 */
public class Massage extends Content {

    /**
     * The massage option.
     */
    public final MassageOption option;

    public Massage() {
        super();
        this.option = null;
    }

    public Massage(String message, MassageOption option) {
        super(message);
        this.option = option;
    }

    public String getType() {
        return Permintaan.TYPE_MASSAGE;
    }
}
