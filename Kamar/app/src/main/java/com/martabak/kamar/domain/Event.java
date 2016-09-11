package com.martabak.kamar.domain;

import com.google.gson.annotations.SerializedName;
import com.martabak.kamar.service.EventServer;
import com.martabak.kamar.service.EventService;

import java.util.Date;

/**
 * An event.
 */
public class Event extends Model {

    /**
     * The event's first name.
     */
    public final String name;

    /**
     * The event's english description.
     */
    @SerializedName("descriptionEn") public final String descriptionEn;

    /**
     * The event's indonesian description.
     */
    @SerializedName("descriptionIn") public final String descriptionIn;

    /**
     * The event's chinese description.
     */
    @SerializedName("descriptionZh") public final String descriptionZh;

    /**
     * The event's type, i.e Bali events or Hotel events.
     */
    @SerializedName("type") public final String type;

    /**
     * The event's start date.
     */
    @SerializedName("start_date") public final Date startDate;

    /**
     * The event's end date.
     */
    @SerializedName("end_date") public final Date endDate;

    /**
     * The name of the image attachment.
     */
    @SerializedName("attachment_name") public final String attachmentName;


    public Event() {
        this.name = null;
        this.descriptionEn = null;
        this.descriptionIn = null;
        this.descriptionZh = null;
        this.type = null;
        this.startDate = null;
        this.endDate = null;
        this.attachmentName = null;
    }

    public Event(String name, String descriptionEn, String descriptionIn, String descriptionZh, String type,
                 Date startDate, Date endDate, String attachmentName) {
        super(null, null);
        this.name = name;
        this.descriptionEn = descriptionEn;
        this.descriptionIn = descriptionIn;
        this.descriptionZh = descriptionZh;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.attachmentName = attachmentName;
    }

    public Event(String _id, String _rev, String name, String descriptionEn, String descriptionIn,
                 String descriptionZh, String type, Date startDate, Date endDate, String attachmentName) {
        super(_id, _rev);
        this.name = name;
        this.descriptionEn = descriptionEn;
        this.descriptionIn = descriptionIn;
        this.descriptionZh = descriptionZh;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.attachmentName = attachmentName;
    }

    /**
     * @return The URL of the image of this event.
     * E.g. http://theserver:5984/219310931202313/image.jpg.
     */
    public String getImageUrl() {
        return EventServer.getBaseUrl() + "/event/" + this._id + "/" + this.attachmentName;
    }

}
