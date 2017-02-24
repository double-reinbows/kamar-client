package com.martabak.kamar.service;

import com.martabak.kamar.domain.Event;
import com.martabak.kamar.service.response.AllResponse;
import com.martabak.kamar.service.response.PostResponse;
import com.martabak.kamar.service.response.PutResponse;
import com.martabak.kamar.service.response.ViewResponse;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Provides event (request) related functionality
 */
public interface EventService {

    /**
     * The sub-path to request for the consumable's image attachment.
     */
    String EVENT_IMAGE_PATH = ".jpg";

    @GET("event/{id}")
    Observable<Event> getEvent(@Path("id") String id);

    @GET("event/_all_docs?include_docs=true")
    Observable<AllResponse<Event>> getAllEvents();

    @GET("event/_design/event/_view/event")
    Observable<ViewResponse<Event>> getCurrentEventsByType(@Query("key") String type);
}
