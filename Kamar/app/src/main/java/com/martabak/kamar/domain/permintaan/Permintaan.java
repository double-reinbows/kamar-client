package com.martabak.kamar.domain.permintaan;

import com.google.gson.annotations.SerializedName;
import com.martabak.kamar.domain.Model;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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
     * The HOUSEKEEPING type string.
     */
    public static final String TYPE_HOUSEKEEPING = "HOUSEKEEPING";
    /**
     * The ENGINEERING type string.
     */
    public static final String TYPE_ENGINEERING = "ENGINEERING";
    /**
     * The MASSAGE type string.
     */
    public static final String TYPE_MASSAGE = "MASSAGE";
    /**
     * The LAUNDRY type string.
     */
    public static final String TYPE_LAUNDRY = "LAUNDRY";
    /**
     * The RESTAURANT type string.
     */
    public static final String TYPE_RESTAURANT = "RESTAURANT";
    /**
     * The TRANSPORT type string.
     */
    public static final String TYPE_TRANSPORT = "TRANSPORT";
    /**
     * The EVENT type string.
     */
    public static final String TYPE_EVENT = "EVENTS";

    /**
     * The SURVEY type string.
     */
    public static final String TYPE_SURVEY = "COMMENTS";
    /**
     * The CHAT type string.
     */
    public static final String TYPE_CHAT = "CHAT";

    /**
     * The NEW state string. A.k.a. SENT.
     */
    public static final String STATE_NEW = "ORDER-SENT";
    //public static final String STATE_NEW = "NEW";
    /**
     * The IN-PROGRESS state string. A.k.a. PROCESSED.
     */
    public static final String STATE_INPROGRESS= "ORDER-PROCESSED";
    //public static final String STATE_INPROGRESS= "IN-PROGRESS";
    /**
     * The IN-DELIVERY state string. Not in use.
     */
    public static final String STATE_INDELIVERY= "IN-DELIVERY";
    /**
     * The COMPLETED state string.
     */
    public static final String STATE_COMPLETED = "COMPLETED";
    /**
     * The CANCELLED state string. Not in use.
     */
    public static final String STATE_CANCELLED = "CANCELLED";

    /**
     * The set of cancellable states.
     */
    private static final Set<String> CANCELLABLE_STATES = new HashSet<>(Arrays.asList(STATE_NEW, STATE_INPROGRESS, STATE_INDELIVERY));

    /**
     * The set of regressable states.
     */
    private static final Set<String> REGRESSABLE_STATES = new HashSet<>(Arrays.asList(STATE_INPROGRESS, STATE_INDELIVERY, STATE_COMPLETED));

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

    /**
     * The staff member responsible for this permintaan. E.g. Ratna Sulaman.
     */
    public final String assignee;

    public Permintaan() {
        this.owner = null;
        this.type = null;
        this.roomNumber = null;
        this.guestId = null;
        this.content = null;
        this.state = null;
        this.created = null;
        this.updated = null;
        this.assignee = null;
    }

    public Permintaan(String owner, String type, String roomNumber, String guestId, String state,
                      Date created, Date updated, String assignee, T content) {
        super(null, null);
        this.owner = owner;
        this.type = type;
        this.roomNumber = roomNumber;
        this.guestId = guestId;
        this.state = state;
        this.created = created;
        this.updated = updated;
        this.assignee = assignee;
        this.content = content;
    }

    public Permintaan(String _id, String _rev, String owner, String type, String roomNumber, String guestId, String state,
                      Date created, Date updated, String assignee, T content) {
        super(_id, _rev);
        this.owner = owner;
        this.type = type;
        this.roomNumber = roomNumber;
        this.guestId = guestId;
        this.state = state;
        this.created = created;
        this.updated = updated;
        this.assignee = assignee;
        this.content = content;
    }

    /**
     * @return Whether or not this permintaan is cancellable.
     */
    public boolean isCancellable() {
        return CANCELLABLE_STATES.contains(state);
    }

    /**
     * @return Whether or not this permintaan is cancellable.
     */
    public boolean isRegressable() {
        return REGRESSABLE_STATES.contains(state);
    }

    @Override
    public String toString() {
        return type + " - Room No. " + roomNumber + " - ID: " + guestId + " - Owner: " + owner;
    }

}