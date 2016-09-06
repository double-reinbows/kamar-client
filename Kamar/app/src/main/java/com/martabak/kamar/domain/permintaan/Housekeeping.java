package com.martabak.kamar.domain.permintaan;

/**
 * Housekeeping content type of a {@link Permintaan}.
 */
public class Housekeeping extends OptionedContent {

    public String getType() {
        return Permintaan.TYPE_HOUSEKEEPING;
    }

}