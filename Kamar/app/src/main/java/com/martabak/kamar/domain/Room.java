package com.martabak.kamar.domain;

/**
 * A room that a {@link Guest} checks into.
 */
public class Room extends Model {

    /**
     * The room number.
     */
    public final String number;

    public Room() {
        this.number = null;
    }

    public Room(String number) {
        this.number = number;
    }

}
