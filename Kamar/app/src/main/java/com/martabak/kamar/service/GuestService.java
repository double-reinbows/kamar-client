package com.martabak.kamar.service;

import com.martabak.kamar.domain.Guest;
import com.martabak.kamar.domain.PostResponse;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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
    String BASE_URL = "http://192.168.43.42:5984";


    @GET("guest/{id}")
    Observable<Guest> getGuest(@Path("id") String id);

    @POST("guest")
    Observable<PostResponse> createGuest(@Body Guest guest);

    @FormUrlEncoded
    @GET("guest/_design/guest/_view/room")
    Observable<Guest> getGuestInRoom(@Field("roomnumber") String roomNumber);

}