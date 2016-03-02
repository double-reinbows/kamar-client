package com.martabak.kamar.service;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

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
    protected Server(String baseUrl) {
        retrofit = new Retrofit.Builder()
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
