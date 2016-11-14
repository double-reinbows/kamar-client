package com.martabak.kamar.domain.managers;

import android.content.Context;
import android.util.Log;

import com.martabak.kamar.domain.options.EngineeringOption;
import com.martabak.kamar.domain.options.HousekeepingOption;
import com.martabak.kamar.domain.options.MassageOption;
import com.martabak.kamar.domain.permintaan.Engineering;
import com.martabak.kamar.domain.permintaan.Housekeeping;
import com.martabak.kamar.domain.permintaan.Massage;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.service.PermintaanServer;

import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.math.operators.OperatorMinMax;
import rx.observables.GroupedObservable;

/**
 * Manages permintaan related server calls to provide a higher level abstraction above the server.
 */
public class PermintaanManager {

    private static PermintaanManager instance;

    private PermintaanManager() {}

    public static PermintaanManager getInstance() {
        if (instance == null) {
            instance = new PermintaanManager();
        }
        return instance;
    }

    /**
     * Get the guest's ID from shared prefs.
     * @param c The context.
     * @return The guest's ID.
     */
    private String getGuestId(Context c) {
        return c.getSharedPreferences("userSettings", Context.MODE_PRIVATE).getString("guestId", "none");
    }

    /**
     * Get the state of an ongoing or previous request, if it exists. Combine all requests of that
     * type to a single status by using the most recent permintaan.
     * Note that #onError will be triggered on the {@link Observable} returned if no requests
     * exist for the given type.
     * @param c The context.
     * @param type The permintaan type.
     * @return The permintaan state.
     */
    private Observable<String> getMostRecentStatusOf(Context c, final String type) {
        return OperatorMinMax.max(PermintaanServer.getInstance(c).getPermintaansForGuest(getGuestId(c))
                        // Filter by type of permintaan
                        .filter(new Func1<Permintaan, Boolean>() {
                            @Override
                            public Boolean call(Permintaan permintaan) {
                                return permintaan.type.equals(type);
                            }
                        })
                // Get the latest permintaan by date
                , new Comparator<Permintaan>() {
                    @Override public int compare(Permintaan lhs, Permintaan rhs) {
                        return lhs.created.compareTo(rhs.created);
                    }
                })
                // Ignore any that weren't created today
                .filter(new Func1<Permintaan, Boolean>() {
                    @Override public Boolean call(Permintaan permintaan) {
                        Calendar cal = Calendar.getInstance();
                        int currYear = cal.get(Calendar.YEAR);
                        int currDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
                        cal.setTime(permintaan.created);
                        int permYear = cal.get(Calendar.YEAR);
                        int permDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
                        return permYear == currYear && permDayOfYear == currDayOfYear;
                    }
                })
                // Return its status
                .map(new Func1<Permintaan, String>() {
                    @Override public String call(Permintaan permintaan) {
                        Log.v("DICK", permintaan.state);
                        return permintaan.state;
                    }
                });
    }

    /**
     * Similarly to {@link #getMostRecentStatusOf}, but for discriminate by option as well as type.
     * Gets all permintaan states for a particular type, grouping by their option ID and taking the
     * most latest permintaan for that type and option.
     * Note that #onError will be triggered on the {@link Observable} returned if no requests
     * exist for the given type.
     * @param c The context.
     * @param type The permintaan type.
     * @return A mapping from option ID -> latest permintaan state.
     */
    private Observable<Map<String, String>> getStatusesByTypeOf(Context c, final String type) {
        Log.d(PermintaanManager.class.getCanonicalName(), "getStatusesByTypeOf");
        return PermintaanServer.getInstance(c).getPermintaansForGuest(getGuestId(c))
                // Filter by type of permintaan
                .filter(new Func1<Permintaan, Boolean>() {
                    @Override public Boolean call(Permintaan permintaan) {
                        return permintaan.type.equals(type);
                    }
                })
                // Ignore any that weren't created today
                .filter(new Func1<Permintaan, Boolean>() {
                    @Override public Boolean call(Permintaan permintaan) {
                        Calendar cal = Calendar.getInstance();
                        int currYear = cal.get(Calendar.YEAR);
                        int currDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
                        cal.setTime(permintaan.created);
                        int permYear = cal.get(Calendar.YEAR);
                        int permDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
                        return permYear == currYear && permDayOfYear == currDayOfYear;
                    }
                })
                .toList()
                .map(new Func1<List<Permintaan>, Map<String, Permintaan>>() {
                    @Override public Map<String, Permintaan> call(List<Permintaan> ps) {
                        Map<String, Permintaan> latestPs = new HashMap<>();
                        for (Permintaan p : ps) {
                            EngineeringOption o = ((Engineering)p.content).option;
                            if (!latestPs.containsKey(o._id)) {
                                latestPs.put(o._id, p);
                            } else {
                                Permintaan otherP = latestPs.get(o._id);
                                if (p.created.compareTo(otherP.created) > 0) {
                                    latestPs.put(o._id, p);
                                }
                            }
                        }
                        return latestPs;
                    }
                })
                .map(new Func1<Map<String, Permintaan>, Map<String, String>>() {
                    @Override public Map<String, String> call(Map<String, Permintaan> latestPs) {
                        Map<String, String> statuses = new HashMap<>();
                        for (String optionId : latestPs.keySet()) {
                            statuses.put(optionId, latestPs.get(optionId).state);
                        }
                        return statuses;
                    }
                });
    }

    /**
     * @return The state of the most recent massage permintaan.
     */
    public Observable<String> getMassageStatus(Context c) {
        return getMostRecentStatusOf(c, Permintaan.TYPE_MASSAGE);
    }

    /**
     * @return The state of the most recent massage permintaan.
     */
    public Observable<String> getBellboyStatus(Context c) {
        return getMostRecentStatusOf(c, Permintaan.TYPE_BELLBOY);
    }

    /**
     * @return The state of the most recent housekeeping permintaan.
     */
    public Observable<Map<String, String>> getHousekeepingStatuses(Context c) {
        final String type = Permintaan.TYPE_HOUSEKEEPING;
        Log.d(PermintaanManager.class.getCanonicalName(), "getStatusesByTypeOf");
        return PermintaanServer.getInstance(c).getPermintaansForGuest(getGuestId(c))
                // Filter by type of permintaan
                .filter(new Func1<Permintaan, Boolean>() {
                    @Override public Boolean call(Permintaan permintaan) {
                        return permintaan.type.equals(type);
                    }
                })
                // Ignore any that weren't created today
                .filter(new Func1<Permintaan, Boolean>() {
                    @Override public Boolean call(Permintaan permintaan) {
                        Calendar cal = Calendar.getInstance();
                        int currYear = cal.get(Calendar.YEAR);
                        int currDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
                        cal.setTime(permintaan.created);
                        int permYear = cal.get(Calendar.YEAR);
                        int permDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
                        return permYear == currYear && permDayOfYear == currDayOfYear;
                    }
                })
                .toList()
                .map(new Func1<List<Permintaan>, Map<String, Permintaan>>() {
                    @Override public Map<String, Permintaan> call(List<Permintaan> ps) {
                        Map<String, Permintaan> latestPs = new HashMap<>();
                        for (Permintaan p : ps) {
                            HousekeepingOption o = ((Housekeeping)p.content).option;
                            if (!latestPs.containsKey(o._id)) {
                                latestPs.put(o._id, p);
                            } else {
                                Permintaan otherP = latestPs.get(o._id);
                                if (p.created.compareTo(otherP.created) > 0) {
                                    latestPs.put(o._id, p);
                                }
                            }
                        }
                        return latestPs;
                    }
                })
                .map(new Func1<Map<String, Permintaan>, Map<String, String>>() {
                    @Override public Map<String, String> call(Map<String, Permintaan> latestPs) {
                        Map<String, String> statuses = new HashMap<>();
                        for (String optionId : latestPs.keySet()) {
                            statuses.put(optionId, latestPs.get(optionId).state);
                        }
                        return statuses;
                    }
                });
    }

    public Observable<Map<String, String>> getMassageStatuses(Context c) {
        Log.d(PermintaanManager.class.getCanonicalName(), "getMassageStatuses");
        final String type = Permintaan.TYPE_MASSAGE;
        return PermintaanServer.getInstance(c).getPermintaansForGuest(getGuestId(c))
                // Filter by type of permintaan
                .filter(new Func1<Permintaan, Boolean>() {
                    @Override public Boolean call(Permintaan permintaan) {
                        return permintaan.type.equals(type);
                    }
                })
                // Ignore any that weren't created today
                .filter(new Func1<Permintaan, Boolean>() {
                    @Override public Boolean call(Permintaan permintaan) {
                        Calendar cal = Calendar.getInstance();
                        int currYear = cal.get(Calendar.YEAR);
                        int currDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
                        cal.setTime(permintaan.created);
                        int permYear = cal.get(Calendar.YEAR);
                        int permDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
                        return permYear == currYear && permDayOfYear == currDayOfYear;
                    }
                })
                .toList()
                .map(new Func1<List<Permintaan>, Map<String, Permintaan>>() {
                    @Override public Map<String, Permintaan> call(List<Permintaan> ps) {
                        Map<String, Permintaan> latestPs = new HashMap<>();
                        for (Permintaan p : ps) {
                            MassageOption o = ((Massage)p.content).option;
                            if (!latestPs.containsKey(o._id)) {
                                latestPs.put(o._id, p);
                            } else {
                                Permintaan otherP = latestPs.get(o._id);
                                if (p.created.compareTo(otherP.created) > 0) {
                                    latestPs.put(o._id, p);
                                }
                            }
                        }
                        return latestPs;
                    }
                })
                .map(new Func1<Map<String, Permintaan>, Map<String, String>>() {
                    @Override public Map<String, String> call(Map<String, Permintaan> latestPs) {
                        Map<String, String> statuses = new HashMap<>();
                        for (String optionId : latestPs.keySet()) {
                            statuses.put(optionId, latestPs.get(optionId).state);
                        }
                        return statuses;
                    }
                });
    }

    /**
     * @return The state of all possible engineering option permintaans.
     */
    public Observable<Map<String, String>> getEngineeringStatuses(Context c) {
        return getStatusesByTypeOf(c, Permintaan.TYPE_ENGINEERING);
    }

    /**
     * @return The state of the most recent laundry permintaan.
     */
    public Observable<String> getLaundryStatus(Context c) {
        return getMostRecentStatusOf(c, Permintaan.TYPE_LAUNDRY);
    }

    /**
     * @return The state of the most recent laundry permintaan.
     */
    public Observable<String> getRestaurantStatus(Context c) {
        return getMostRecentStatusOf(c, Permintaan.TYPE_RESTAURANT);
    }


}