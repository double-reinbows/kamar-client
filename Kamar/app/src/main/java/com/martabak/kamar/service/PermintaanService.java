package com.martabak.kamar.service;

import com.martabak.kamar.domain.Permintaan;
import com.squareup.okhttp.Response;
import retrofit.http.*;
import rx.Observable;

/**
 * Provides permintaan (request) related functionality.
 */
public interface PermintaanService extends Service {

    @GET("permintaan/{id}")
    Observable<Permintaan> getPermintaan(@Path("id") int id);

    @POST("permintaan")
    Observable<Response> createPermintaan(@Body Permintaan permintaan);

    @PUT("permintaan/{id}")
    Observable<Response> updatePermintaan(@Path("id") int id, @Body Permintaan permintaan);

}