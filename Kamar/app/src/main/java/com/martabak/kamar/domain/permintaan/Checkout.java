package com.martabak.kamar.domain.permintaan;
/**
 * Checkout content type of a {@link Permintaan}.
 */
public class Checkout extends Content {

    public Checkout() {
        super();
    }

    public Checkout(String message) {
        super(message);
    }

    public String getType() {
        return "CHECKOUT";
    }
}
