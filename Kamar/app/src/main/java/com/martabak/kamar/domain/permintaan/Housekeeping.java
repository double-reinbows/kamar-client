package com.martabak.kamar.domain.permintaan;
/**
 * Housekeeping content type of a {@link Permintaan}.
 */
public class Housekeeping extends Content {

    public Housekeeping() {
        super();
    }

    public Housekeeping(String message) {
        super(message);
    }

    public String getType() {
        return "HOUSEKEEPING";
    }
}