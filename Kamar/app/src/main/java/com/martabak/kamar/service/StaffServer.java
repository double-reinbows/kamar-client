package com.martabak.kamar.service;

import android.content.Context;


import com.martabak.kamar.domain.Staff;
import com.martabak.kamar.domain.options.EngineeringOption;
import com.martabak.kamar.domain.options.HousekeepingOption;
import com.martabak.kamar.domain.options.MassageOption;
import com.martabak.kamar.domain.options.LaundryOption;
import com.martabak.kamar.service.response.AllResponse;
import com.martabak.kamar.service.response.ViewResponse;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Exposes {@link StaffService}.
 */
public class StaffServer extends Server {

    /**
     * The singleton instance.
     */
    private static StaffServer instance;

    /**
     * The service api conf.
     */
    private StaffService service;

    /**
     * Constructor.
     */
    private StaffServer(Context c) {
        super(c);
        service = createService(StaffService.class);
    }

    /**
     * Obtains singleton instance.
     * @return The singleton instance.
     */
    public static StaffServer getInstance(Context c) {
        if (instance == null)
            instance = new StaffServer(c);
        return instance;
    }

    /**
     * Attempt to login as a staff member using a password.
     * @param password The staff password.
     * @return The staff member's sub-type or null if unsuccessful.
     */
    public Observable<String> login(String password) {
        return service.login('"' + password + '"')
                .flatMap(new Func1<ViewResponse<String>, Observable<String>>() {
                    @Override public Observable<String> call(ViewResponse<String> response) {
                        for (ViewResponse<String>.ViewResult<String> i : response.rows) {
                            return Observable.just(i.value);
                        }
                        return Observable.just(null);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Get all the staff that match the given division.
     * I.E. {@code getStaffOfDivision("MASSAGE", "ENGINEERING")}
     * @param divisions The divisions to match on.
     * @return Observable on the staff.
     */
    public Observable<Staff> getStaffOfDivision(String... divisions) {
        List<Observable<Staff>> results = new ArrayList<>(divisions.length);
        for (String div : divisions) {
            results.add(service.getStaffOfDivision('"' + div + '"')
                    .flatMap(new Func1<ViewResponse<Staff>, Observable<Staff>>() {
                        @Override
                        public Observable<Staff> call(ViewResponse<Staff> response) {
                            List<Staff> staff = new ArrayList<>(response.total_rows);
                            for (ViewResponse<Staff>.ViewResult<Staff> i : response.rows) {
                                staff.add(i.value);
                            }
                            return Observable.from(staff);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()));
        }
        return Observable.merge(results);
    }

    /**
     * @return All the massage options that are available.
     */
    public Observable<List<MassageOption>> getMassageOptions() {
        return service.getMassageOptions()
                .flatMap(new Func1<AllResponse<MassageOption>, Observable<List<MassageOption>>>() {
                    @Override public Observable<List<MassageOption>> call(AllResponse<MassageOption> response) {
                        List<MassageOption> toReturn = new ArrayList<>(response.total_rows);
                        for (AllResponse<MassageOption>.AllResult<MassageOption> i : response.rows) {
                            toReturn.add(i.doc);
                        }
                        return Observable.just(toReturn);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @return All the ic_engineering options that are available.
     */
    public Observable<List<EngineeringOption>> getEngineeringOptions() {
        return service.getEngineeringOptions()
                .flatMap(new Func1<AllResponse<EngineeringOption>, Observable<List<EngineeringOption>>>() {
                    @Override public Observable<List<EngineeringOption>> call(AllResponse<EngineeringOption> response) {
                        List<EngineeringOption> toReturn = new ArrayList<>(response.total_rows);
                        for (AllResponse<EngineeringOption>.AllResult<EngineeringOption> i : response.rows) {
                            toReturn.add(i.doc);
                        }
                        return Observable.just(toReturn);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @return All the housekeeping options that are available.
     */
    public Observable<List<HousekeepingOption>> getHousekeepingOptions() {
        return service.getHousekeepingOptions()
                .flatMap(new Func1<AllResponse<HousekeepingOption>, Observable<List<HousekeepingOption>>>() {
                    @Override public Observable<List<HousekeepingOption>> call(AllResponse<HousekeepingOption> response) {
                        List<HousekeepingOption> toReturn = new ArrayList<>(response.total_rows);
                        for (AllResponse<HousekeepingOption>.AllResult<HousekeepingOption> i : response.rows) {
                            toReturn.add(i.doc);
                        }
                        return Observable.just(toReturn);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @return All the laundry options that are available.
     */
    public Observable<List<LaundryOption>> getLaundryOptions() {
        return service.getLaundryOptions()
                .flatMap(new Func1<AllResponse<LaundryOption>, Observable<List<LaundryOption>>>() {
                    @Override public Observable<List<LaundryOption>> call(AllResponse<LaundryOption> response) {
                        List<LaundryOption> toReturn = new ArrayList<>(response.total_rows);
                        for (AllResponse<LaundryOption>.AllResult<LaundryOption> i : response.rows) {
                            toReturn.add(i.doc);
                        }
                        return Observable.just(toReturn);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
