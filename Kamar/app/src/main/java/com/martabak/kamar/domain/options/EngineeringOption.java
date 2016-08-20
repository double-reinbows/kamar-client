package com.martabak.kamar.domain.options;

import com.martabak.kamar.service.StaffServer;

/**
 * An Engineering {@link Option}.
 */
public class EngineeringOption extends Option {

    public EngineeringOption() {
        super();
    }

    public EngineeringOption(String nameEn, String nameIn, String nameZh, String nameRu,
                             Integer order, String attachmentName) {
        super(null, null, nameEn, nameIn, nameZh, nameRu, order, attachmentName);
    }

    public EngineeringOption(String _id, String _rev, String nameEn, String nameIn, String nameZh,
                             String nameRu, Integer order, String attachmentName) {
        super(_id, _rev, nameEn, nameIn, nameZh, nameRu, order, attachmentName);
    }

    /**
     * @return The URL of the image of this option.
     * E.g. http://theserver:5984/219310931202313/image.jpg.
     */
    public String getImageUrl() {
        return StaffServer.getBaseUrl() + "/engineering_option/" + this._id + "/" + this.attachmentName;
    }

}
