package com.martabak.kamar.service;

import android.content.Context;

import com.martabak.kamar.domain.Guest;
import com.martabak.kamar.domain.PostResponse;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Exposes {@link StaffService}.
 */
public class StaffServer extends Server {

    /**
     * The singleton instance.
     */
    private static StaffServer instance;

    /**
     * The service api conf.
     */
    private StaffService service;

    /**
     * Constructor.
     */
    private StaffServer(Context c) {
        super(c, GuestService.BASE_URL);
        service = createService(StaffService.class);
    }

    /**
     * Obtains singleton instance.
     * @return The singleton instance.
     */
    public static StaffServer getInstance(Context c) {
        if (instance == null)
            instance = new StaffServer(c);
        return instance;
    }

    /**
     * Attempt to login as a staff member using a password.
     * @param password The staff password.
     * @return Whether or not the login was successful.
     */
    public Observable<Boolean> login(String password) {
        return service.login(password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
