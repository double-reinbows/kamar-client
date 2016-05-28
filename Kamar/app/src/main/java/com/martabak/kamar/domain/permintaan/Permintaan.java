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
     * The RESTAURANT owner string.
     */
    public static final String OWNER_RESTAURANT = "RESTAURANT";
    /**
     * The FRONTDESK owner string.
     */
    public static final String OWNER_FRONTDESK = "FRONTDESK";

    /**
     * The BELLBOY type string.
     */
    public static final String TYPE_BELLBOY = "BELLBOY";
    /**
     * The CHECKOUT type string.
     */
    public static final String TYPE_CHECKOUT = "CHECKOUT";
    /**
     * The CONSUMABLE type string.
     */
    public static final String TYPE_CONSUMABLE = "CONSUMABLE";
    /**
     * The BELLBOY type string.
     */
    public static final String TYPE_HOUSEKEEPING = "HOUSEKEEPING";
    /**
     * The MAINTENANCE type string.
     */
    public static final String TYPE_MAINTENANCE = "MAINTENANCE";
    /**
     * The BELLBOY type string.
     */
    public static final String TYPE_TRANSPORT = "TRANSPORT";

    /**
     * The NEW state string.
     */
    public static final String STATE_NEW = "NEW";
    /**
     * The IN-PROGRESS state string.
     */
    public static final String STATE_INPROGRESS= "IN-PROGRESS";
    /**
     * The IN-DELIVERY state string.
     */
    public static final String STATE_INDELIVERY= "IN-DELIVERY";
    /**
     * The COMPLETE state string.
     */
    public static final String STATE_COMPLETED = "COMPLETED";
    /**
     * The DELETED state string.
     */
    public static final String STATE_DELETED = "DELETED";
    /**
     * The CANCELLED state string.
     */
    public static final String STATE_CANCELLED = "CANCELLED";

    /**
     * The owner of the request. One of:
     * <ul>
     * <li>RESTAURANT</li>
     * <li>FRONTDESK</li>
     * </ul>
     */
    public final String owner;

    /**
     * The type of the request. One of:
     * <ul>
     * <li>BELLBOY</li>
     * <li>CHECKOUT</li>
     * <li>CONSUMABLE</li>
     * <li>HOUSEKEEPING</li>
     * <li>MAINTENANCE</li>
     * <li>TRANSPORT</li>
     * </ul>
     */
    public final String type;

    /**
     * The guest's room number.
     */
    @SerializedName("room_number")
    public final String roomNumber;

    /**
     * The guest's ID.
     */
    @SerializedName("guest_id")
    public final String guestId;

    /**
     * The request sub-type.
     */
    public final T content;

    /**
     * State of this request. One of:
     * <ul>
     * <li>NEW</li>
     * <li>IN-PROGRESS</li>
     * <li>IN-DELIVERY</li>
     * <li>COMPLETE</li>
     * <li>DELETED</li>
     * <li>CANCELLED</li>
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

    @Override
    public String toString() {
        return type + " - Room No. " + roomNumber + " - ID: " + guestId + " - Owner: " + owner;
    }

}