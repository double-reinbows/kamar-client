package com.martabak.kamar.service;

import android.content.Context;

import com.martabak.kamar.domain.GuestChat;
import com.martabak.kamar.domain.ViewResponse;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
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
     * Get the full chat log for a particular guest.
     * @param guestId The guest's ID.
     * @return The guest chat.
     */
    public Observable<GuestChat> getGuestChat(String guestId) {
        return service.getGuestChat('"' + guestId + '"')
                .flatMap(new Func1<ViewResponse<GuestChat.Message>, Observable<GuestChat>>() {
                    @Override public Observable<GuestChat> call(ViewResponse<GuestChat.Message> response) {
                        List<GuestChat.Message> messages = new ArrayList<>(response.total_rows);
                        for (ViewResponse<GuestChat.Message>.ViewResult<GuestChat.Message> i : response.rows) {
                            messages.add(i.value);
                        }
                        return Observable.just(new GuestChat(messages));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
