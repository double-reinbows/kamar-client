package com.martabak.kamar.service;


import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * An abstract Retrofit server.
 */
public abstract class Server {

    /**
     * The Retrofit instance.
     */
    private Retrofit retrofit;

    /**
     * Construct the Retrofit server.
     * @param baseUrl The server's base URL.
     */
    protected Server(Context c, String baseUrl) {
        retrofit = new Retrofit.Builder()
                .client(new OkHttpClient.Builder()
                        .addInterceptor(new AuthorizationInterceptor(c))
                        .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                        .build())
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    /**
     * Construct a Retrofit server given a Retrofit instance.
     * @param retrofit The retrofit instance.
     */
    protected Server(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    /**
     * Create a Retrofit service.
     * @param service The Retrofit service
     * @param <T> The service type.
     * @return The service.
     */
    protected final <T> T createService(Class<T> service) {
        return  retrofit.create(service);
    }

}
