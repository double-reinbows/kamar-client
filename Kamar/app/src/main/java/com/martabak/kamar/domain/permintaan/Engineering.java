package com.martabak.kamar.domain.permintaan;

import com.martabak.kamar.domain.options.EngineeringOption;

/**
 * Engineering content type of a {@link Permintaan}.
 */
public class Engineering extends OptionedContent {

    public Engineering() {
        super();
    }

    public Engineering(String message, EngineeringOption option) {
        super(message, option);
    }

    public String getType() {
        return Permintaan.TYPE_ENGINEERING;
    }
}
