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

    /**
     * The guest's welcome message.
     */
    @SerializedName("welcome_message") public final String welcomeMessage;

    /**
     * The path to the guest's promotional image on the server.
     */
    @SerializedName("promo_img_id") public final String promoImgId;

    public Guest() {
        this.firstName = null;
        this.lastName = null;
        this.phone = null;
        this.email = null;
        this.checkIn = null;
        this.checkOut = null;
        this.roomNumber = null;
        this.welcomeMessage = null;
        this.promoImgId = null;
    }

    public Guest(String firstName, String lastName, String phone, String email, Date checkIn, Date checkOut, String roomNumber, String welcomeMessage, String promoImgId) {
        super(null, null);
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.roomNumber = roomNumber;
        this.welcomeMessage = welcomeMessage;
        this.promoImgId = promoImgId;
    }

    public Guest(String _id, String _rev, String firstName, String lastName, String phone, String email, Date checkIn, Date checkOut, String roomNumber, String welcomeMessage, String promoImgId) {
        super(_id, _rev);
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.roomNumber = roomNumber;
        this.welcomeMessage = welcomeMessage;
        this.promoImgId = promoImgId;
    }

}
