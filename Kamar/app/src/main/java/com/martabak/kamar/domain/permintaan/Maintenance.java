package com.martabak.kamar.domain.permintaan;

public class Maintenance extends Content {

    public Maintenance() {
        super();
    }

    public Maintenance(String message) {
        super(message);
    }

    public String getType() {
        return "HOUSEKEEPING";
    }
}
