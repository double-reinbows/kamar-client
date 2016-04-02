package com.martabak.kamar.service;

import com.martabak.kamar.domain.Guest;
import com.martabak.kamar.domain.PostResponse;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Provides guest related functionality.
 */
public interface GuestService {

    /**
     * The base server url.
     */
    String BASE_URL = "http://192.168.0.8:5984";


    @GET("guest/{id}")
    Observable<Guest> getGuest(@Path("id") String id);

    @POST("guest")
    Observable<PostResponse> createGuest(@Body Guest guest);

    @GET("roomGuestVIEW") // FIXME need to figure out how views will look
    Observable<Guest> getGuestInRoom(@Field("roomnumber") String roomNumber);

}