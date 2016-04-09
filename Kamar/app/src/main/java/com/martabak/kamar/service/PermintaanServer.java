package com.martabak.kamar.service;

import android.content.Context;

import com.martabak.kamar.domain.Permintaan;
import com.martabak.kamar.domain.PostResponse;
import com.martabak.kamar.domain.PutResponse;
import com.martabak.kamar.domain.ViewResponse;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Exposes {@link PermintaanService}.
 */
public class PermintaanServer extends Server {

    /**
     * The singleton instance.
     */
    private static PermintaanServer instance;

    /**
     * The service api conf.
     */
    private PermintaanService service;

    /**
     * Constructor.
     */
    private PermintaanServer(Context c) {
        super(c);
        service = createService(PermintaanService.class);
    }

    /**
     * Obtains singleton instance.
     * @return The singleton instance.
     */
    public static PermintaanServer getInstance(Context c) {
        if (instance == null)
            instance = new PermintaanServer(c);
        return instance;
    }

    /**
     * Get a permintaan, given its id.
     * @param id The permintaan's id.
     * @return The permintaan.
     */
    public Observable<Permintaan> getPermintaan(String id) {
        return service.getPermintaan(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Get all the permintaans for a guest.
     * @param guestId The guest's ID.
     * @return Observable on the guest's permintaans.
     */
    public Observable<Permintaan> getPermintaansForGuest(String guestId) {
        return service.getPermintaansForGuest('"' + guestId + '"')
                .flatMap(new Func1<ViewResponse<Permintaan>, Observable<Permintaan>>() {
                    @Override
                    public Observable<Permintaan> call(ViewResponse<Permintaan> response) {
                        List<Permintaan> perms = new ArrayList<>(response.total_rows);
                        for (ViewResponse<Permintaan>.ViewResult<Permintaan> i : response.rows) {
                            perms.add(i.value);
                        }
                        return Observable.from(perms);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Create a new permintaan.
     * @param permintaan The permintaan model to be created.
     * @return The permintaan model that was added.
     */
    public Observable<Permintaan> createPermintaan(Permintaan permintaan) {
        return service.createPermintaan(permintaan)
                .flatMap(new Func1<PostResponse, Observable<Permintaan>>() {
                    @Override public Observable<Permintaan> call(PostResponse response) {
                        return service.getPermintaan(response.id);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Update a permintaan.
     * @param permintaan The permintaan model to be created.
     * @return The permintaan model that was added.
     */
    public Observable<Boolean> updatePermintaan(Permintaan permintaan) {
        return service.updatePermintaan(permintaan.id, permintaan)
                .map(new Func1<PutResponse, Boolean>() {
                    @Override public Boolean call(PutResponse response) {
                        return response.ok;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
