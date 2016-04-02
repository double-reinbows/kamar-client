package com.martabak.kamar.domain.permintaan;

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
