package com.martabak.kamar.domain.permintaan;

import com.martabak.kamar.domain.options.HousekeepingOption;

/**
 * Housekeeping content type of a {@link Permintaan}.
 */
<<<<<<< HEAD
public class Housekeeping extends Content {

    /**
     * The housekeeping option.
     */
    public final HousekeepingOption option;
    /**
     * The housekeeping option.
     */
    public final Integer quantity;

    public Housekeeping() {
        super();
        this.option = null;
        this.quantity = null;
    }

    public Housekeeping(String message, Integer quantity, HousekeepingOption option) {
        super(message);
        this.option = option;
        this.quantity = quantity;
    }

    public String getType() { return Permintaan.TYPE_HOUSEKEEPING; }
=======
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

>>>>>>> develop
}