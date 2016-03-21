package com.martabak.kamar.service;


import retrofit2.http.Field;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Provides staff related functionality.
 */
public interface StaffService {

    /**
     * The base server url.
     */
    String BASE_URL = "http://192.168.178.24:5984";

    @POST("staffLoginVIEW") // FIXME need to figure out how views will look
    Observable<Boolean> login(@Field("password") String password);

}