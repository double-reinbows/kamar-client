package com.martabak.kamar.service;

import android.content.Context;

import com.martabak.kamar.domain.Guest;
import com.martabak.kamar.domain.PostResponse;
import com.martabak.kamar.domain.ViewResponse;

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
        super(c);
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

    /**
     * Get the guest currently in the given room, if there is one.
     * @param roomNumber The room number.
     * @return The guest in the room, if there is one.
     */
    public Observable<Guest> getGuestInRoom(String roomNumber) {
        return service.getGuestInRoom('"' + roomNumber + '"')
                .flatMap(new Func1<ViewResponse<Guest>, Observable<Guest>>() {
                    @Override public Observable<Guest> call(ViewResponse<Guest> response) {
                        for (ViewResponse<Guest>.ViewResult<Guest> i : response.rows) {
                            return Observable.just(i.value);
                        }
                        return Observable.just(null);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
