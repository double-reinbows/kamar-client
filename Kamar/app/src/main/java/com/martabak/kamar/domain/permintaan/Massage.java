package com.martabak.kamar.domain.permintaan;

import com.martabak.kamar.domain.options.MassageOption;


/**
 * Massage content type of a {@link Permintaan}.
 */
public class Massage extends OptionedContent {

    public Massage() {
        super();
    }

    public Massage(String message, MassageOption option) {
        super(message, option);
    }

    public String getType() {
        return Permintaan.TYPE_MASSAGE;
    }
}
