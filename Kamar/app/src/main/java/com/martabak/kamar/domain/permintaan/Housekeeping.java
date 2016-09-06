package com.martabak.kamar.domain.permintaan;

import com.martabak.kamar.domain.options.HousekeepingOption;

/**
 * Housekeeping content type of a {@link Permintaan}.
 */
public class Housekeeping extends OptionedContent {

    public Housekeeping() {
        super();
    }

    public Housekeeping(String message, HousekeepingOption option) {
        super(message, option);
    }

    public String getType() {
        return Permintaan.TYPE_HOUSEKEEPING;
    }

}