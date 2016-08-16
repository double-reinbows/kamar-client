package com.martabak.kamar.domain.permintaan;

import java.util.Date;

/**
 * Massage content type of a {@link Permintaan}.
 */
public class Massage extends Content {

    /**
     * The massage option, e.g. REFLEXOLOGY.
     */
    public final String option;

    /**
     * The starting time for this massage.
     */
    public final Date time;

    /**
     * The length of time for this massage, in minutes.
     */
    public final Integer length;

    /**
     * The price of this massage, in thousands of Rp.
     */
    public final Integer price;

    public Massage() {
        super();
        this.option = null;
        this.time = null;
        this.length = null;
        this.price = null;
    }

    public Massage(String message, String option, Date time, Integer length, Integer price) {
        super(message);
        this.option = option;
        this.time = time;
        this.length = length;
        this.price = price;
    }

    public String getType() {
        return Permintaan.TYPE_MASSAGE;
    }
}
