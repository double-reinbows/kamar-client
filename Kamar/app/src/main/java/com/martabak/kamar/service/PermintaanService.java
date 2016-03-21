package com.martabak.kamar.service;

import com.martabak.kamar.domain.Permintaan;

import retrofit2.Response;
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

    /**
     * The base server url.
     */
    String BASE_URL = "http://192.168.178.24:5984";

    @GET("permintaan/{id}")
    Observable<Permintaan> getPermintaan(@Path("id") int id);

    @POST("permintaan")
    Observable<Response> createPermintaan(@Body Permintaan permintaan);

    @PUT("permintaan/{id}")
    Observable<Response> updatePermintaan(@Path("id") int id, @Body Permintaan permintaan);

}