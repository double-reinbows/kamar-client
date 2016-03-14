package com.martabak.kamar.domain;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Contains an entire room chat log.
 */
public class RoomChat {

    public final List<ChatMessage> messages;

    public RoomChat() {}

    public RoomChat(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public class ChatMessage extends Model {

        @SerializedName("guest_id") public final String guestId;

        public final String from;

        public final String message;

        public final Date sent;

        public final Date read;

        public ChatMessage() {}

        public ChatMessage(String guestId, String from, String message, Date sent, Date read) {
            super();
            this.guestId = guestId;
            this.from = from;
            this.message = message;
            this.sent = sent;
            this.read = read;
        }
    }

}
