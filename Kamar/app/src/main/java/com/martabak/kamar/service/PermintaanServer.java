package com.martabak.kamar.service;

import android.content.Context;
import android.util.Log;

import com.martabak.kamar.domain.options.EngineeringOption;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.service.response.AllResponse;
import com.martabak.kamar.service.response.PostResponse;
import com.martabak.kamar.service.response.PutResponse;
import com.martabak.kamar.service.response.ViewResponse;

import java.util.ArrayList;
import java.util.Date;
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
     *
     * @return The singleton instance.
     */
    public static PermintaanServer getInstance(Context c) {
        if (instance == null)
            instance = new PermintaanServer(c);
        return instance;
    }

    /**
     * Get a permintaan, given its id.
     *
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
     *
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
     * Get all the permintaans that match the states given.
     * e.g. {@code getPermintaansOfState("NEW", "INPROGRESS")}
     *
     * @param states The states to match on.
     * @return Observable on the permintaans.
     */
    public Observable<Permintaan> getPermintaansOfState(String... states) {
        List<Observable<Permintaan>> results = new ArrayList<>(states.length);
        for (String state : states) {
            results.add(service.getPermintaansOfState('"' + state + '"')
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
                    .observeOn(AndroidSchedulers.mainThread()));
        }
        return Observable.merge(results);
    }

    /**
     * Create a new permintaan.
     *
     * @param permintaan The permintaan model to be created.
     * @return The permintaan model that was added.
     */
    public Observable<Permintaan> createPermintaan(Permintaan permintaan) {
        return service.createPermintaan(permintaan)
                .flatMap(new Func1<PostResponse, Observable<Permintaan>>() {
                    @Override
                    public Observable<Permintaan> call(PostResponse response) {
                        return service.getPermintaan(response.id);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Update a permintaan.
     *
     * @param permintaan The permintaan model to be created.
     * @return The permintaan model that was added.
     */
    public Observable<Boolean> updatePermintaan(Permintaan permintaan) {
        return service.updatePermintaan(permintaan._id, permintaan)
                .map(new Func1<PutResponse, Boolean>() {
                    @Override
                    public Boolean call(PutResponse response) {
                        return response.ok;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Get all the permintaans between the 2 input times.
     *
     * @param start When you want to start getting permintaans.
     * @param end   When you want to end getting permintaan
     * @return Observable on the permintaans.
     */
    public Observable<List<Permintaan>> getPermintaansOfTime(Date start, Date end) {
        return service.getPermintaansofTime(start, end)
                .flatMap(new Func1<AllResponse<Permintaan>, Observable<List<Permintaan>>>() {
                    @Override
                    public Observable<List<Permintaan>> call(AllResponse<Permintaan> response) {
                        List<Permintaan> toReturn = new ArrayList<>(response.total_rows);
                        for (AllResponse<Permintaan>.AllResult<Permintaan> i : response.rows) {
                            toReturn.add(i.doc);
                        }
                        return Observable.just(toReturn);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
}
