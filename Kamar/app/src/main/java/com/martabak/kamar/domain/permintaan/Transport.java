package com.martabak.kamar.domain.permintaan;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Transport content type of a {@link Permintaan}.
 */
public class Transport extends Content {

    public final Integer passengers;

    @SerializedName("departure_time") public final Date departureTime;

    public final String destination;

    public Transport() {
        super();
        this.passengers = null;
        this.departureTime = null;
        this.destination = null;
    }

    public Transport(String message, int passengers, Date departureTime, String destination) {
        super(message);
        this.passengers = passengers;
        this.departureTime = departureTime;
        this.destination = destination;
    }

    public String getType() {
        return "TRANSPORT";
    }
}