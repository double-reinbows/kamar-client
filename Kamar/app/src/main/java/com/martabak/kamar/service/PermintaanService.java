package com.martabak.kamar.service;

import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.service.response.AllResponse;
import com.martabak.kamar.service.response.PostResponse;
import com.martabak.kamar.service.response.PutResponse;
import com.martabak.kamar.service.response.ViewResponse;

import java.util.Date;

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

    @GET("permintaan/_design/permintaan/_view/time")
    Observable<ViewResponse<Permintaan>> getPermintaansofTime(@Query("startKey") Date start, @Query("endKey") Date end);

    @GET("permintaan/_all_docs?include_docs=true&descending=true&skip=1\"")
    Observable<AllResponse<Permintaan>> getAllPermintaans();

    @POST("permintaan")
    Observable<PostResponse> createPermintaan(@Body Permintaan permintaan);

    @PUT("permintaan/{id}")
    Observable<PutResponse> updatePermintaan(@Path("id") String id, @Body Permintaan permintaan);


}