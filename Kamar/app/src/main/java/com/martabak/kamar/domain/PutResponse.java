package com.martabak.kamar.domain;

/**
 * A response from a PUT request.
 */
public class PutResponse {

    /**
     * The operation status.
     */
    public final boolean ok;

    /**
     * The error type.
     */
    public final String error;

    /**
     * The error description.
     */
    public final String reason;

    public PutResponse() {
        this.ok = false;
        this.error = null;
        this.reason = null;
    }

    public PutResponse(boolean ok, String error, String reason) {
        this.ok = ok;
        this.error = error;
        this.reason = reason;
    }

}
