package com.martabak.kamar.service;

import com.martabak.kamar.domain.Guest;
import com.martabak.kamar.service.response.PostResponse;
import com.martabak.kamar.service.response.ViewResponse;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Provides guest related functionality.
 */
public interface GuestService {

    @GET("guest/{id}")
    Observable<Guest> getGuest(@Path("id") String id);

    @POST("guest")
    Observable<PostResponse> createGuest(@Body Guest guest);

    @GET("guest/_design/guest/_view/room")
    Observable<ViewResponse<Guest>> getGuestInRoom(@Query("key") String roomNumber);

}