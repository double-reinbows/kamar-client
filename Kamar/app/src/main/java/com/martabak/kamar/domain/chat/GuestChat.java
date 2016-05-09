package com.martabak.kamar.domain.chat;

import java.util.List;

/**
 * Contains an entire guest chat log.
 */
public class GuestChat {

    public final List<ChatMessage> messages;

    public GuestChat() {
        this.messages = null;
    }

    public GuestChat(List<ChatMessage> messages) {
        this.messages = messages;
    }


}
