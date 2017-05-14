package com.martabak.kamar.domain.permintaan;

import com.google.gson.annotations.SerializedName;
import com.martabak.kamar.domain.Model;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;


/**
 * A guest's request for something.
 */
public class Permintaan<T extends Content> extends Model {

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
     * The success int
     */
    public static final int SUCCESS = 1;

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
    private static final Set<String> REGRESSABLE_STATES = new HashSet<>(Arrays.asList(STATE_INPROGRESS, STATE_INDELIVERY));

    /**
     * The set of states where displaying an ETA makes sense.
     */
    private static final Set<String> SHOW_ETA_STATES = new HashSet<>(Arrays.asList(STATE_INPROGRESS, STATE_INDELIVERY));

    /**
     * The owner of the request. One of:
     * <ul>
     * <li>RESTAURANT</li>
     * <li>FRONTDESK</li>
     * </ul>
     */
    public final String owner;

    /**
     * The creator of the request. One of:
     * <ul>
     * <li>GUEST</li>
     * <li>STAFF</li>
     * </ul>
     */
    public final String creator;

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

    /**
     * The permintaan's Estimated Time of Arrival to the ordering guest's room, in minutes.
     */
    public final Integer eta;

    /**
     * The permintaan creator's selected language country code (when this permintaan was created).
     */
    @SerializedName("country_code")
    public final String countryCode;

    public Permintaan() {
        this.owner = null;
        this.creator = null;
        this.type = null;
        this.roomNumber = null;
        this.guestId = null;
        this.content = null;
        this.state = null;
        this.created = null;
        this.updated = null;
        this.assignee = null;
        this.eta = null;
        this.countryCode = null;
    }
    /**
     * for creating new permintaans
     */
    public Permintaan(String owner, String creator, String type, String roomNumber, String guestId, String state,
                      Date created, T content) {
        super(null, null);
        this.owner = owner;
        this.creator = creator;
        this.type = type;
        this.roomNumber = roomNumber;
        this.guestId = guestId;
        this.state = state;
        this.created = created;
        this.updated = null;
        this.content = content;
        this.assignee = "none";
        this.eta = 0;
        this.countryCode = Locale.getDefault().getCountry();
    }

    /**
     * For updating permintaans (make sure you get the latest _rev which changes immediately after
     */
    public Permintaan(String _id, String _rev, String owner, String creator, String type, String roomNumber,
                      String guestId, String state, Date created, Date updated, String assignee,
                      Integer eta, String countryCode, T content) {
        super(_id, _rev);
        this.owner = owner;
        this.creator = creator;
        this.type = type;
        this.roomNumber = roomNumber;
        this.guestId = guestId;
        this.state = state;
        this.created = created;
        this.updated = updated;
        this.assignee = assignee;
        this.eta = eta;
        this.countryCode = countryCode;
        this.content = content;
    }

    /**
     * @return Whether or not this permintaan is cancellable.
     */
    public boolean isCancelable() {
        return CANCELLABLE_STATES.contains(state);
    }

    /**
     * @return Whether or not this permintaan is cancellable.
     */
    public boolean isProgressable() {
        return CANCELLABLE_STATES.contains(state);
    }

    /**
     * @return Whether or not this permintaan is cancellable.
     */
    public boolean isRegressable() {
        return REGRESSABLE_STATES.contains(state);
    }

    /**
     * @return Whether or not this permintaan is in a state where the ETA makes sense and an ETA
     * exists.
     */
    public boolean shouldShowEta() {
        return SHOW_ETA_STATES.contains(state) && eta != null && eta != 0;
    }

    @Override
    public String toString() {
        return type + " - Room No. " + roomNumber + " - ID: " + guestId + " - Owner: " + owner;
    }

}