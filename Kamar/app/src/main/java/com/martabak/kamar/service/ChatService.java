package com.martabak.kamar.service;

import com.martabak.kamar.domain.RoomChat;
import com.martabak.kamar.domain.ViewResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Provides chat related functionality.
 */
public interface ChatService {

    @GET("chat/_design/chat/_view/room")
    Observable<ViewResponse<RoomChat>> getRoomChat(@Query("key") String roomNumber);

}
