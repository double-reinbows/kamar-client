package com.martabak.kamar.domain.options;

import com.martabak.kamar.service.StaffServer;

/**
 * A Housekeeping {@link SectionedOption}.
 */
public class HousekeepingOption extends SectionedOption {

    public HousekeepingOption() {
    }

    public HousekeepingOption(String nameEn, String nameIn, String nameZh, String nameRu,
                              Integer order, String attachmentName, String sectionEn, String sectionIn, String sectionZh, String sectionRu) {
        super(null, null, nameEn, nameIn, nameZh, nameRu, order, attachmentName, sectionEn, sectionIn, sectionZh, sectionRu);
    }

    public HousekeepingOption(String _id, String _rev, String nameEn, String nameIn, String nameZh,
                              String nameRu, Integer order, String attachmentName, String sectionEn, String sectionIn, String sectionZh, String sectionRu) {
        super(_id, _rev, nameEn, nameIn, nameZh, nameRu, order, attachmentName, sectionEn, sectionIn, sectionZh, sectionRu);
    }

    /**
     * @return The URL of the image of this option.
     * E.g. http://theserver:5984/219310931202313/image.jpg.
     */
    public String getImageUrl() {
        return StaffServer.getBaseUrl() + "/housekeeping_option/" + this._id + "/" + this.attachmentName;
    }

}
