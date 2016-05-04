package com.martabak.kamar.service;


import com.martabak.kamar.service.response.ViewResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Provides staff related functionality.
 */
public interface StaffService {

    @GET("password/_design/password/_view/password")
    Observable<ViewResponse<Boolean>> login(@Query("key") String password);

}