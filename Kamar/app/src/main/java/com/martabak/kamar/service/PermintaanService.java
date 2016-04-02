package com.martabak.kamar.service;

import com.martabak.kamar.domain.Permintaan;
import com.martabak.kamar.domain.PostResponse;
import com.martabak.kamar.domain.PutResponse;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Provides permintaan (request) related functionality.
 */
public interface PermintaanService {

    @GET("permintaan/{id}")
    Observable<Permintaan> getPermintaan(@Path("id") String id);

    @POST("permintaan")
    Observable<PostResponse> createPermintaan(@Body Permintaan permintaan);

    @PUT("permintaan/{id}")
    Observable<PutResponse> updatePermintaan(@Path("id") String id, @Body Permintaan permintaan);

}