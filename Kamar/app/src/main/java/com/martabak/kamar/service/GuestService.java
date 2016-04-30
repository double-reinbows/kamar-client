package com.martabak.kamar.service;

import com.martabak.kamar.domain.Guest;
import com.martabak.kamar.domain.Room;
import com.martabak.kamar.service.response.AllResponse;
import com.martabak.kamar.service.response.PostResponse;
import com.martabak.kamar.service.response.ViewResponse;

import java.util.List;

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

    @GET("room/_all_docs?include_docs=true")
    Observable<AllResponse<Room>> getRooms();

}