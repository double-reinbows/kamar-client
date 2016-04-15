package com.martabak.kamar.domain.permintaan;

/**
 * Abstract content of a {@link Permintaan}.
 */
public abstract class Content {

    public final String message;

    public Content() {
        this.message = null;
    }

    public Content(String message) {
        this.message = message;
    }

    public abstract String getType();
}