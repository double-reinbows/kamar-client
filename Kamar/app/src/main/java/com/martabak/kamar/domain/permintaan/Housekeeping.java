package com.martabak.kamar.domain.permintaan;

import com.martabak.kamar.domain.options.HousekeepingOption;

/**
 * Housekeeping content type of a {@link Permintaan}.
 */
public class Housekeeping extends Content {

    /**
     * The housekeeping option.
     */
    public final HousekeepingOption option;

    public Housekeeping() {
        super();
        this.option = null;
    }

    public Housekeeping(String message, HousekeepingOption option) {
        super(message);
        this.option = option;
    }

    public String getType() {
        return Permintaan.TYPE_HOUSEKEEPING;
    }
}