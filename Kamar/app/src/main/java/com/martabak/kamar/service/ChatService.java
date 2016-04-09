package com.martabak.kamar.service;

import com.martabak.kamar.domain.GuestChat;
import com.martabak.kamar.service.response.ViewResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Provides chat related functionality.
 */
public interface ChatService {

    @GET("chat/_design/chat/_view/guest")
    Observable<ViewResponse<GuestChat.Message>> getGuestChat(@Query("key") String guestId);

}
