package com.martabak.kamar.service;


import android.content.Context;


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
     * Construct the Retrofit server.
     */
    protected Server(Context c) {
        retrofit = new Retrofit.Builder()
                .client(new OkHttpClient.Builder()
                        .addInterceptor(new AuthorizationInterceptor(c))
                        .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                        .build())
                .baseUrl(getBaseUrl())
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

    /**
     * @return The server's base URL.
     */
    protected String getBaseUrl() {
        return "http://192.168.0.9:5984";
    }

}
