package com.martabak.kamar.service;

import com.martabak.kamar.domain.Consumable;
import com.martabak.kamar.service.response.AllResponse;
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

    @GET("menu/_all_docs?include_docs=true")
    Observable<AllResponse<Consumable>> getMenu();

    @PUT("menu/{id}")
    Observable<PutResponse> updateMenu(@Path("id") String id, @Body Consumable consumable);

}