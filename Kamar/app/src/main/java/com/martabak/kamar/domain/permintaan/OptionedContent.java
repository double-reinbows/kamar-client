package com.martabak.kamar.domain.permintaan;

import com.martabak.kamar.domain.options.Option;

/**
 * Optioned content type of a {@link Permintaan}.
 */
public abstract class OptionedContent extends Content {

    /**
     * The option.
     */
    public final Option option;

    public OptionedContent() {
        super();
        this.option = null;
    }

    public OptionedContent(String message, Option option) {
        super(message);
        this.option = option;
    }

}
