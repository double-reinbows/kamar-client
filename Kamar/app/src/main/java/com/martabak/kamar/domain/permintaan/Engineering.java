package com.martabak.kamar.domain.permintaan;

import com.martabak.kamar.domain.options.EngineeringOption;

/**
 * Engineering content type of a {@link Permintaan}.
 */
public class Engineering extends Content {

    /**
     * The ic_engineering option.
     */
    public final EngineeringOption option;

    public Engineering() {
        super();
        this.option = null;
    }

    public Engineering(String message, EngineeringOption option) {
        super(message);
        this.option = option;
    }

    public String getType() {
        return Permintaan.TYPE_ENGINEERING;
    }
}
