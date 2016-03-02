package com.martabak.kamar.service;

import com.martabak.kamar.domain.Guest;

import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.GET;
import retrofit.http.POST;
import rx.Observable;

/**
 * Provides guest related functionality.
 */
public interface GuestService extends Service {

    @POST("guest")
    Observable<Guest> createGuest(@Body Guest guest);

    @GET("roomGuestVIEW") // FIXME need to figure out how views will look
    Observable<Guest> getGuestInRoom(@Field("roomnumber") String roomnumber);

}