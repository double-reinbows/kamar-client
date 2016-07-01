package com.martabak.kamar.service;


import android.content.Context;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.martabak.kamar.domain.converters.DateConverter;
import com.martabak.kamar.domain.converters.PermintaanConverter;
import com.martabak.kamar.domain.permintaan.Permintaan;

import java.util.Date;

import okhttp3.OkHttpClient;
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
     * The pattern used for representing dates in a String.
     */
    private static final String datePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    /**
     * Construct the Retrofit server.
     */
    protected Server(Context c) {
        retrofit = new Retrofit.Builder()
                .client(new OkHttpClient.Builder()
                        .addInterceptor(new AuthorizationInterceptor(c))
                        .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                        .build())
                .baseUrl(getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .registerTypeAdapter(Permintaan.class, new PermintaanConverter(datePattern))
                        .registerTypeAdapter(Date.class, new DateConverter(datePattern))
                        .setDateFormat(datePattern)
                        .create()))
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
     * @param <T> The service type.
     * @return The service.
     */
    protected final <T> T createService(Class<T> service) {
        return retrofit.create(service);
    }

    /**
     * @return The server's base URL.
     */
    public static String getBaseUrl() {
        return "http://192.168.0.11:5984";
    }

}
