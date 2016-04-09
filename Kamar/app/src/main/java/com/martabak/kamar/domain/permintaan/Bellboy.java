package com.martabak.kamar.domain.permintaan;

/**
 * Bellboy content type of a {@link Permintaan}.
 */
public class Bellboy extends Content {

    public Bellboy() {
        super();
    }

    public Bellboy(String message) {
        super(message);
    }

    public String getType() {
        return "BELLBOY";
    }
}
