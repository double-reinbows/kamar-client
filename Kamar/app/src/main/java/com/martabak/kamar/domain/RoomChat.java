package com.martabak.kamar.domain;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Contains an entire room chat log.
 */
public class RoomChat {

    public final List<Message> messages;

    public RoomChat() {
        this.messages = null;
    }

    public RoomChat(List<Message> messages) {
        this.messages = messages;
    }

    public class Message extends Model {

        @SerializedName("guest_id") public final String guestId;

        public final String from;

        public final String message;

        public final Date sent;

        public final Date read;

        public Message() {
            this.guestId = null;
            this.from = null;
            this.message = null;
            this.sent = null;
            this.read = null;
        }

        public Message(String guestId, String from, String message, Date sent, Date read) {
            super();
            this.guestId = guestId;
            this.from = from;
            this.message = message;
            this.sent = sent;
            this.read = read;
        }
    }

}
