package com.martabak.kamar.domain.permintaan;

import com.google.gson.annotations.SerializedName;
import com.martabak.kamar.domain.Model;

import java.util.Date;
import java.util.List;


/**
 * A guest's request for something.
 */
public class Permintaan<T extends Content> extends Model {

    /**
     * The owner of the request. One of:
     * - RESTAURANT
     * - FRONT DESK
     */
    public final String owner;

    /**
     * The type of the request. One of:
     * - HOUSEKEEPING
     * - MAINTENANCE
     * - BELLBOY
     * - CHECKOUT
     * - TRANSPORT
     * - CONSUMABLE
     */
    public final String type;

    /**
     * The guest's room number.
     */
    @SerializedName("room_number") public final String roomNumber;

    /**
     * The guest's ID.
     */
    @SerializedName("guest_id") public final String guestId;

    /**
     * The request sub-type.
     */
    public final T content;

    /**
     * State of this request. One of:
     * - NEW
     * - IN PROGRESS
     * - IN DELIVERY
     * - COMPLETE
     * - DELETED
     * - CANCELLED
     */
    public final String state;

    /**
     * The creation time of the request.
     */
    public final Date created;

    /**
     * The last updated time of the request.
     */
    public final Date updated;

    public Permintaan() {
        this.owner = null;
        this.type = null;
        this.roomNumber = null;
        this.guestId = null;
        this.content = null;
        this.state = null;
        this.created = null;
        this.updated = null;
    }

    public Permintaan(String owner, String roomNumber, String guestId, T content) {
        this.owner = owner;
        this.type = content.getType();
        this.roomNumber = roomNumber;
        this.guestId = guestId;
        this.content = content;
        this.state = "NEW";
        this.created = null;// TODO Date.nowOrSomething?();
        this.updated = null;
    }

}