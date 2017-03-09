package com.martabak.kamar.domain.permintaan;

/**
 * Bellboy content type of a {@link Permintaan}.
 */
public class LaundryOrder extends Content {

    public LaundryOrder() {
        super();
    }

    public LaundryOrder(String message) {
        super(message);
    }

    public String getType() {
        return Permintaan.TYPE_LAUNDRY;
    }
}
