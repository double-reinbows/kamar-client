package com.martabak.kamar.domain.chat;

import com.google.gson.annotations.SerializedName;
import com.martabak.kamar.domain.Model;

import java.util.Date;

/**
 * A single chat message.
 */
public class ChatMessage extends Model {

    @SerializedName("guest_id") public final String guestId;

    public final String from;

    public final String message;

    public final Date sent;

    public final Date read;

    public ChatMessage() {
        this.guestId = null;
        this.from = null;
        this.message = null;
        this.sent = null;
        this.read = null;
    }

    public ChatMessage(String guestId, String from, String message, Date sent, Date read) {
        super();
        this.guestId = guestId;
        this.from = from;
        this.message = message;
        this.sent = sent;
        this.read = read;
    }

}
