package com.martabak.kamar.service;

import android.content.Context;


import com.martabak.kamar.domain.ViewResponse;

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
        super(c);
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
        return service.login('"' + password + '"')
                .flatMap(new Func1<ViewResponse<Boolean>, Observable<Boolean>>() {
                    @Override public Observable<Boolean> call(ViewResponse<Boolean> response) {
                        for (ViewResponse<Boolean>.ViewResult<Boolean> i : response.rows) {
                            return Observable.just(i.value);
                        }
                        return Observable.just(false);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
