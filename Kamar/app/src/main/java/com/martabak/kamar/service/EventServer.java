package com.martabak.kamar.service;

import android.content.Context;
import android.util.Log;

import com.martabak.kamar.domain.Event;
import com.martabak.kamar.service.response.AllResponse;
import com.martabak.kamar.service.response.ViewResponse;
//import com.martabak.kamar.service.response.PostResponse;
//import com.martabak.kamar.service.response.PutResponse;
//import com.martabak.kamar.service.response.ViewResponse;

import java.util.ArrayList;
//import java.util.List;

import java.util.Calendar;
import java.util.Date;
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

    /**
     * @return All event names in the database.
     */
    public Observable<List<String>> getAllEventNames() {
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
                .map(new Func1<List<Event>, List<String>>() {
                    @Override
                    public List<String> call(List<Event> events) {
                        List<String> names = new ArrayList<>();
                        for (Event e : events) {
                            names.add(e.name);
                        }
                        return names;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @return Event based on the type of event, which should also be greater than today's date.
     * @param type The Event's type.
     */
    public Observable<Event> getCurrentEventsByType(String type) {
        return service.getCurrentEventsByType('"' + type + '"')
                .flatMap(new Func1<ViewResponse<Event>, Observable<Event>>() {
                    @Override public Observable<Event> call(ViewResponse<Event> response) {
                        List<Event> toReturn = new ArrayList<>();
                        for (ViewResponse<Event>.ViewResult<Event> i : response.rows) {
                            toReturn.add(i.value);
                            Log.v("Event Response", i.value.name + i.value.type);
                        }
                        return Observable.from(toReturn);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


    }
}
