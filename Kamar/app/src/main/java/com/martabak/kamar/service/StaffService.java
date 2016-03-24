package com.martabak.kamar.service;


import retrofit2.http.Field;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Provides staff related functionality.
 */
public interface StaffService {

    @POST("staffLoginVIEW") // FIXME need to figure out how views will look
    Observable<Boolean> login(@Field("password") String password);

}