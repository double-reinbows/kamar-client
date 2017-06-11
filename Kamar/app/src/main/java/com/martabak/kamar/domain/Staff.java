package com.martabak.kamar.domain;

import com.google.gson.annotations.SerializedName;
import com.martabak.kamar.service.MenuServer;
import com.martabak.kamar.service.StaffServer;

import java.util.Date;
import java.util.List;

/**
 * A staff member.
 */
public class Staff extends Model {

    /**
     * The Massage Responsibility string.
     */
    public static final String RESP_MASSAGE = "MASSAGE";

    /**
     * The Engineering Responsibility string.
     */
    public static final String RESP_ENGINEERING = "ENGINEERING";

    /**
     * The Housekeeping Responsibility string.
     */
    public static final String RESP_HOUSEKEEPING = "HOUSEKEEPING";

    /**
     * The staff member's first name.
     */
    @SerializedName("first_name") public final String firstName;

    /**
     * The staff member's last name.
     */
    @SerializedName("last_name") public final String lastName;

    /**
     * The staff member's phone number.
     */
    public final String phone;

    /**
     * The staff member's division, e.g. MASSAGE.
     */
    public final String division;

    /**
     * The staff member's areas of expertise, e.g. SHIATSU, TRADITIONAL.
     */
    public final List<String> expertise;

    /**
     * The staff member's most recent shift start time.
     */
    @SerializedName("start_time") public final Date startTime;

    /**
     * The staff member's most recent shift end time.
     */
    @SerializedName("end_time") public final Date endTime;

    /**
     * The name of the image attachment.
     */
    @SerializedName("attachment_name") public final String attachmentName;

    public Staff() {
        this.firstName = null;
        this.lastName = null;
        this.phone = null;
        this.division = null;
        this.expertise = null;
        this.startTime = null;
        this.endTime = null;
        this.attachmentName = null;
    }

    public Staff(String firstName, String lastName, String phone, String division,
                 List<String> expertise, Date startTime, Date endTime, String attachmentName) {
        super(null, null);
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.division = division;
        this.expertise = expertise;
        this.startTime = startTime;
        this.endTime = endTime;
        this.attachmentName = attachmentName;
    }

    public Staff(String _id, String _rev, String firstName, String lastName, String phone,
                 String division, List<String> expertise, Date startTime, Date endTime, String attachmentName) {
        super(_id, _rev);
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.division = division;
        this.expertise = expertise;
        this.startTime = startTime;
        this.endTime = endTime;
        this.attachmentName = attachmentName;
    }

    /**
     * @return The URL of the image of this consumable.
     * E.g. http://theserver:5984/219310931202313/image.jpg.
     */
    public String getImageUrl() {
        return StaffServer.getBaseUrl() + "/staff/" + this._id + "/" + this.attachmentName;
    }

}
