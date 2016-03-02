package com.martabak.kamar.service;

import com.martabak.kamar.domain.RoomChat;

import retrofit.http.Field;
import retrofit.http.GET;
import rx.Observable;

/**
 * Provides chat related functionality.
 */
public interface ChatService extends Service {

    @GET("roomChatVIEW") // FIXME need to figure out how views will look
    Observable<RoomChat> getRoomChat(@Field("roomnumber") String roomnumber);

}
