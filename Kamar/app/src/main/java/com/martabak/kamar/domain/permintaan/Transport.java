package com.martabak.kamar.domain.permintaan;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Transport content type of a {@link Permintaan}.
 */
public class Transport extends Content {

    public final Integer passengers;

    @SerializedName("departing_in") public final String departingIn;

    public final String destination;

    public Transport() {
        super();
        this.passengers = null;
        this.departingIn = null;
        this.destination = null;
    }

    public Transport(String message, Integer passengers, String departingIn, String destination) {
        super(message);
        this.passengers = passengers;
        this.departingIn = departingIn;
        this.destination = destination;
    }

    public String getType() {
        return Permintaan.TYPE_TRANSPORT;
    }
}