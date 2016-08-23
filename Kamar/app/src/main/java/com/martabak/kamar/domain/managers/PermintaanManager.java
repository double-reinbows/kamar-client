package com.martabak.kamar.domain.managers;

import android.content.Context;
import android.util.Log;

import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.service.PermintaanServer;

import java.util.Comparator;

import rx.Observable;
import rx.functions.Func1;
import rx.math.operators.OperatorMinMax;

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
     * Get the state of an ongoing or previous request, if it exists.
     * Note that #onError will be triggered on the {@link Observable} returned if no requests
     * exist for the given type.
     * @param c The context.
     * @param type The permintaan type.
     * @return The permintaan state.
     */
    private Observable<String> getStatusOf(Context c, final String type) {
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
                // Return its status
                .map(new Func1<Permintaan, String>() {
                    @Override public String call(Permintaan permintaan) {
                        return permintaan.state;
                    }
                });
    }

    /**
     * @return The state of the most recent massage permintaan.
     */
    public Observable<String> getMassageStatus(Context c) {
        return getStatusOf(c, Permintaan.TYPE_MASSAGE);
    }

    /**
     * @return The state of the most recent housekeeping permintaan.
     */
    public Observable<String> getHousekeepingStatus(Context c) {
        return getStatusOf(c, Permintaan.TYPE_HOUSEKEEPING);
    }

    /**
     * @return The state of the most recent engineering permintaan.
     */
    public Observable<String> getEngineeringStatus(Context c) {
        return getStatusOf(c, Permintaan.TYPE_ENGINEERING);
    }

    /**
     * @return The state of the most recent laundry permintaan.
     */
    public Observable<String> getLaundryStatus(Context c) {
        return getStatusOf(c, Permintaan.TYPE_LAUNDRY);
    }

}
