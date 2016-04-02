package com.martabak.kamar.domain;

/**
 * A response from a POST request.
 */
public class PostResponse {

    /**
     * The document ID.
     */
    public final String id;

    /**
     * The operation status.
     */
    public final boolean ok;

    /**
     * The revision info.
     */
    public final String rev;

    public PostResponse() {
        this.id = null;
        this.ok = false;
        this.rev = null;
    }

}
