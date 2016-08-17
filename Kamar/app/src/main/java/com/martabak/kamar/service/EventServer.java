package com.martabak.kamar.service;

import android.content.Context;

import com.martabak.kamar.domain.Event;
import com.martabak.kamar.service.response.AllResponse;
//import com.martabak.kamar.service.response.PostResponse;
//import com.martabak.kamar.service.response.PutResponse;
//import com.martabak.kamar.service.response.ViewResponse;

import java.util.ArrayList;
//import java.util.List;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.functions.Func1;

/**
 * Exposes {@link EventService}.
 */
public class EventServer extends Server {

    /**
     * The singleton instance.
     */
    private static EventServer instance;

    /**
     * The service api conf.
     */
    private EventService service;

    /**
     * Constructor.
     */
    private EventServer(Context c) {
        super(c);
        service = createService(EventService.class);
    }

    /**
     * Obtains singleton instance.
     * @return The singleton instance.
     */
    public static EventServer getInstance(Context c) {
        if (instance == null)
            instance = new EventServer(c);
        return instance;
    }

    /**
     * Get an Event, given its id.
     * @param id The Event's id.
     * @return The Event.
     */
    public Observable<Event> getEvent(String id) {
        return service.getEvent(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @return All events in the database.
     */
    public Observable<List<Event>> getAllEvents() {
        return service.getAllEvents()
                .flatMap(new Func1<AllResponse<Event>, Observable<List<Event>>>() {
                    @Override public Observable<List<Event>> call(AllResponse<Event> response) {
                        List<Event> toReturn = new ArrayList<>(response.total_rows);
                        for (AllResponse<Event>.AllResult<Event> i : response.rows) {
                            toReturn.add(i.doc);
                        }
                        return Observable.just(toReturn);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
