package com.martabak.kamar.domain.options;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.martabak.kamar.service.StaffServer;

import java.util.HashMap;

/**
 * A Housekeeping {@link SectionedOption}.
 */
public class HousekeepingOption extends SectionedOption {

    public Integer max;

    public HousekeepingOption() {}

    public HousekeepingOption(String nameEn, String nameIn, String nameZh, String nameRu,
                              Integer order, String attachmentName, String sectionEn, String sectionIn,
                              String sectionZh, String sectionRu, Integer max) {
        super(null, null, nameEn, nameIn, nameZh, nameRu, order, attachmentName, sectionEn, sectionIn, sectionZh, sectionRu);
        this.max = max;
    }

    public HousekeepingOption(String _id, String _rev, String nameEn, String nameIn, String nameZh,
                              String nameRu, Integer order, String attachmentName, String sectionEn,
                              String sectionIn, String sectionZh, String sectionRu, Integer max) {
        super(_id, _rev, nameEn, nameIn, nameZh, nameRu, order, attachmentName, sectionEn, sectionIn, sectionZh, sectionRu);
        this.max = max;
    }
    /**
     * @return The URL of the image of this option.
     * E.g. http://theserver:5984/219310931202313/image.jpg.
     */
    public String getImageUrl() {
        return StaffServer.getBaseUrl() + "/housekeeping_option/" + this._id + "/" + this.attachmentName;
    }

}
