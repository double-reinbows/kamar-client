package com.martabak.kamar.domain;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * A guest.
 */
public class Guest extends Model {

    /**
     * The guest's first name.
     */
    @SerializedName("first_name") public final String firstName;

    /**
     * The guest's last name.
     */
    @SerializedName("last_name") public final String lastName;

    /**
     * The guest's phone number.
     */
    public final String phone;

    /**
     * The guest's email.
     */
    public final String email;

    /**
     * The guest's check in date.
     */
    @SerializedName("check_in") public final Date checkIn;

    /**
     * The guest's check out date.
     */
    @SerializedName("check_out") public final Date checkOut;

    /**
     * The guest's room number.
     */
    @SerializedName("room_number") public final String roomNumber;

    public Guest() {}

    public Guest(String firstName, String lastName, String phone, String email, Date checkIn, Date checkOut, String roomNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.roomNumber = roomNumber;
    }

}
