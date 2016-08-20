package com.martabak.kamar.service;

import com.martabak.kamar.domain.Consumable;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.service.response.PostResponse;
import com.martabak.kamar.service.response.PutResponse;
import com.martabak.kamar.service.response.ViewResponse;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Provides menu related functionality.
 */
public interface MenuService {

    @GET("menu/_design/menu/_view/section")
    Observable<ViewResponse<Consumable>> getMenuBySection(@Query("key") String section2);

}