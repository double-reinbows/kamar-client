package com.martabak.kamar.domain.permintaan;


/**
 * Massage content type of a {@link Permintaan}.
 */
public class Massage extends OptionedContent {

    public String getType() {
        return Permintaan.TYPE_MASSAGE;
    }

}
