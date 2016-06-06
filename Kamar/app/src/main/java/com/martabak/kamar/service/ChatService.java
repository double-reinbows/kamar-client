package com.martabak.kamar.service;

import com.martabak.kamar.domain.chat.ChatMessage;
import com.martabak.kamar.domain.chat.GuestChat;
import com.martabak.kamar.service.response.PostResponse;
import com.martabak.kamar.service.response.PutResponse;
import com.martabak.kamar.service.response.ViewResponse;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Provides chat related functionality.
 */
public interface ChatService {

    @POST("chat")
    Observable<PostResponse> sendChatMessage(@Body ChatMessage message);

    @GET("chat/_design/chat/_view/guest")
    Observable<ViewResponse<ChatMessage>> getGuestChat(@Query("key") String guestId);

    @PUT("chat/{id}")
    Observable<PutResponse> updateChatMessage(@Path("id") String id, @Body ChatMessage message);

}
