package com.martabak.kamar.service;


import com.martabak.kamar.domain.Staff;
import com.martabak.kamar.domain.options.EngineeringOption;
import com.martabak.kamar.domain.options.HousekeepingOption;
import com.martabak.kamar.domain.options.MassageOption;
import com.martabak.kamar.service.response.AllResponse;
import com.martabak.kamar.service.response.ViewResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Provides staff related functionality.
 */
public interface StaffService {

    @GET("password/_design/password/_view/password")
    Observable<ViewResponse<Boolean>> login(@Query("key") String password);

    @GET("staff/_design/staff/_view/responsibility")
    Observable<ViewResponse<Staff>> getStaffOfResponsibility(@Query("key") String resp);

    @GET("massage_options/_all_docs?include_docs=true")
    Observable<AllResponse<MassageOption>> getMassageOptions();

    @GET("engineering_options/_all_docs?include_docs=true")
    Observable<AllResponse<EngineeringOption>> getEngineeringOptions();

    @GET("housekeeping_options/_all_docs?include_docs=true")
    Observable<AllResponse<HousekeepingOption>> getHousekeepingOptions();

}