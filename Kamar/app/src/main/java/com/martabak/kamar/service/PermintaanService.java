package com.martabak.kamar.service;

import com.martabak.kamar.domain.permintaan.Permintaan;
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
 * Provides permintaan (request) related functionality.
 */
public interface PermintaanService {

    @GET("permintaan/{id}")
    Observable<Permintaan> getPermintaan(@Path("id") String id);

    @GET("permintaan/_design/permintaan/_view/guest")
    Observable<ViewResponse<Permintaan>> getPermintaansForGuest(@Query("key") String guestId);

    @GET("permintaan/_design/permintaan/_view/state")

    Observable<ViewResponse<Permintaan>> getPermintaansOfState(@Query("key") String state);

    @POST("permintaan")
    Observable<PostResponse> createPermintaan(@Body Permintaan permintaan);

    @PUT("permintaan/{id}")
    Observable<PutResponse> updatePermintaan(@Path("id") String id, @Body Permintaan permintaan);

}