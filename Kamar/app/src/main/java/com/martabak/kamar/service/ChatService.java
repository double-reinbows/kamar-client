package com.martabak.kamar.service;

import com.martabak.kamar.domain.RoomChat;

import retrofit2.http.Field;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Provides chat related functionality.
 */
public interface ChatService {

    @GET("roomChatVIEW") // FIXME need to figure out how views will look
    Observable<RoomChat> getRoomChat(@Field("roomnumber") String roomNumber);

}
