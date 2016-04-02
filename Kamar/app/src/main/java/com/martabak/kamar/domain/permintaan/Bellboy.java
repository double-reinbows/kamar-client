package com.martabak.kamar.domain.permintaan;

public class Bellboy extends Content {

    public Bellboy() {
        super();
    }

    public Bellboy(String message) {
        super(message);
    }

    public String getType() {
        return "HOUSEKEEPING";
    }
}

