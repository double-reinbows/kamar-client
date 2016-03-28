package com.martabak.kamar.service;


import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Provides staff related functionality.
 */
public interface StaffService {

    @FormUrlEncoded
    @POST("staff/_design/staff/_view/login")
    Observable<Boolean> login(@Field("password") String password);

}