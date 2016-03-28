package com.martabak.kamar.service;

import com.martabak.kamar.domain.RoomChat;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Provides chat related functionality.
 */
public interface ChatService {

    @FormUrlEncoded
    @GET("chat/_design/chat/_view/room")
    Observable<RoomChat> getRoomChat(@Field("roomnumber") String roomNumber);

}
