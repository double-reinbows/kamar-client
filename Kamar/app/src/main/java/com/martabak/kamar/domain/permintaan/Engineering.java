package com.martabak.kamar.domain.permintaan;

/**
 * Engineering content type of a {@link Permintaan}.
 */
public class Engineering extends Content {

    /**
     * The engineering option, e.g. TELEVISION.
     */
    public final String option;

    public Engineering() {
        super();
        this.option = null;
    }

    public Engineering(String message, String option) {
        super(message);
        this.option = option;
    }

    public String getType() {
        return Permintaan.TYPE_ENGINEERING;
    }
}
