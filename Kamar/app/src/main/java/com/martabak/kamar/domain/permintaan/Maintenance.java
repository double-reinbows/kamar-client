package com.martabak.kamar.domain.permintaan;

/**
 * Maintenance content type of a {@link Permintaan}.
 */
public class Maintenance extends Content {

    public Maintenance() {
        super();
    }

    public Maintenance(String message) {
        super(message);
    }

    public String getType() {
        return Permintaan.TYPE_MAINTENANCE;
    }
}
