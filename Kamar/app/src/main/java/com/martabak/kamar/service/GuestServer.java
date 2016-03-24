package com.martabak.kamar.service;

import android.content.Context;

import com.martabak.kamar.domain.Guest;
import com.martabak.kamar.domain.PostResponse;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Exposes {@link GuestService}.
 */
public class GuestServer extends Server {

    /**
     * The singleton instance.
     */
    private static GuestServer instance;

    /**
     * The service api conf.
     */
    private GuestService service;

    /**
     * Constructor.
     */
    private GuestServer(Context c) {
        super(c, GuestService.BASE_URL);
        service = createService(GuestService.class);
    }

    /**
     * Obtains singleton instance.
     * @return The singleton instance.
     */
    public static GuestServer getInstance(Context c) {
        if (instance == null)
            instance = new GuestServer(c);
        return instance;
    }

    /**
     * Create a new guest.
     * @param guest The guest model to be created.
     * @return The guest model that was added.
     */
    public Observable<Guest> createGuest(Guest guest) {
        return service.createGuest(guest)
                .flatMap(new Func1<PostResponse, Observable<Guest>>() {
                    @Override public Observable<Guest> call(PostResponse response) {
                        return service.getGuest(response.id);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
