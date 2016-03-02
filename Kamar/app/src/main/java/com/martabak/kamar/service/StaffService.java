package com.martabak.kamar.service;

import com.squareup.okhttp.Response;

import retrofit.http.Field;
import retrofit.http.POST;
import rx.Observable;

/**
 * Provides staff related functionality.
 */
public interface StaffService extends Service {

    @POST("staffLoginVIEW") // FIXME need to figure out how views will look
    Observable<Response> login(@Field("password") String password);

}