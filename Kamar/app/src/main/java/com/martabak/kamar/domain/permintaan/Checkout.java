package com.martabak.kamar.domain.permintaan;

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