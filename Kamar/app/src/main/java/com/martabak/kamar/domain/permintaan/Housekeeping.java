package com.martabak.kamar.domain.permintaan;
/**
 * Housekeeping content type of a {@link Permintaan}.
 */
public class Housekeeping extends Content {

    /**
     * The housekeeping option, e.g. HAND TOWEL.
     */
    public final String option;

    public Housekeeping() {
        super();
        this.option = null;
    }

    public Housekeeping(String message, String option) {
        super(message);
        this.option = option;
    }

    public String getType() {
        return Permintaan.TYPE_HOUSEKEEPING;
    }
}