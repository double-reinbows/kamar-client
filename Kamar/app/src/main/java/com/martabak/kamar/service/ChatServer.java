package com.martabak.kamar.service;

import android.content.Context;

import com.martabak.kamar.domain.RoomChat;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Exposes {@link ChatService}.
 */
public class ChatServer extends Server {

    /**
     * The singleton instance.
     */
    private static ChatServer instance;

    /**
     * The service api conf.
     */
    private ChatService service;

    /**
     * Constructor.
     */
    private ChatServer(Context c) {
        super(c);
        service = createService(ChatService.class);
    }

    /**
     * Obtains singleton instance.
     * @return The singleton instance.
     */
    public static ChatServer getInstance(Context c) {
        if (instance == null)
            instance = new ChatServer(c);
        return instance;
    }

    /**
     * Get the recent room chat for a particular room number.
     * @param roomNumber The room number.
     * @return The room chat.
     */
    public Observable<RoomChat> getRoomChat(String roomNumber) {
        return service.getRoomChat(roomNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
