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
     * <ul>
     *     <li>RESTAURANT</li>
     *     <li>FRONTDESK</li>
     * </ul>
     */
    public final String owner;

    /**
     * The type of the request. One of:
     * <ul>
     *   <li>BELLBOY</li>
     *   <li>CHECKOUT</li>
     *   <li>CONSUMABLE</li>
     *   <li>HOUSEKEEPING</li>
     *   <li>MAINTENANCE</li>
     *   <li>TRANSPORT</li>
     * </ul>
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
     * <ul>
     *   <li>NEW</li>
     *   <li>IN-PROGRESS</li>
     *   <li>IN-DELIVERY</li>
     *   <li>COMPLETE</li>
     *   <li>DELETED</li>
     *   <li>CANCELLED</li>
     * </ul>
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

    public Permintaan(String owner, String type, String roomNumber, String guestId, String state,
                      Date created, Date updated, T content) {
        super(null, null);
        this.owner = owner;
        this.type = type;
        this.roomNumber = roomNumber;
        this.guestId = guestId;
        this.state = state;
        this.created = created;
        this.updated = updated;
        this.content = content;
    }

    public Permintaan(String _id, String _rev, String owner, String type, String roomNumber, String guestId, String state,
                      Date created, Date updated, T content) {
        super(_id, _rev);
        this.owner = owner;
        this.type = type;
        this.roomNumber = roomNumber;
        this.guestId = guestId;
        this.state = state;
        this.created = created;
        this.updated = updated;
        this.content = content;
    }

}