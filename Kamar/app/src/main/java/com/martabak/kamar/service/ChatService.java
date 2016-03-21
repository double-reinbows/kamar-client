package com.martabak.kamar.service;

import com.martabak.kamar.domain.RoomChat;

import retrofit2.http.Field;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Provides chat related functionality.
 */
public interface ChatService {

    /**
     * The base server url.
     */
    String BASE_URL = "http://192.168.178.24:5984";

    @GET("roomChatVIEW") // FIXME need to figure out how views will look
    Observable<RoomChat> getRoomChat(@Field("roomnumber") String roomnumber);

}
