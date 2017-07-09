package com.martabak.kamar.service;


import android.content.Context;


import com.google.gson.GsonBuilder;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.martabak.kamar.domain.converters.DateConverter;
import com.martabak.kamar.domain.converters.PermintaanConverter;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.squareup.picasso.Picasso;

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
    private static Retrofit retrofit;

    /**
     * The OkHttpClient instance.
     */
    private static OkHttpClient client;

    /**
     * The pattern used for representing dates in a String.
     */
    private static final String datePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    /**
     * Construct the Retrofit server.
     */
    protected Server(Context c) {
        if (client == null) {
            client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthorizationInterceptor(c))
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build();
        }
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(getBaseUrl())
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                            .registerTypeAdapter(Permintaan.class, new PermintaanConverter(datePattern))
                            .registerTypeAdapter(Date.class, new DateConverter(datePattern))
                            .setDateFormat(datePattern)
                            .create()))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }
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
//        return "http://34.208.159.122:5984/"; // Rei's Amazon EC2
//        return "http://192.168.1.105:5984/"; // martabak
//        return "http://192.168.1.6:5984/"; // Yianni's laptop
        return "http://34.209.91.208:5984/"; // Yianni's Amazon EC2
//        return "http://192.168.1.5:5984/"; // Adarsh
//        return "http://110.44.171.30:5984"; // pi@segara from remote
    }

    /**
     * Fetch an instance of Picasso to load an image.
     * @param c The context.
     * @return The Picasso instance.
     */
    public static Picasso picasso(Context c) {
        Picasso p = new Picasso.Builder(c)
                .downloader(new OkHttp3Downloader(client))
                .build();
        p.setLoggingEnabled(true);
        return p;
    }

}
