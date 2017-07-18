package com.martabak.kamar.activity.guest;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.martabak.kamar.R;
import com.martabak.kamar.activity.home.SplashScreenActivity;
import com.martabak.kamar.activity.staff.CheckGuestInFragment;
import com.martabak.kamar.domain.Guest;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.service.GuestServer;
import com.martabak.kamar.service.PermintaanServer;
import com.martabak.kamar.util.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observer;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by adarsh on 15-Jul-17.
 */

public class HandleCheckGuestOutService extends IntentService {

    private static final Class PARENT_ACTIVITY = GuestHomeActivity.class;

    /**
     * Construct a guest permintaan service.
     */
    public HandleCheckGuestOutService() {
        super("HandleCheckGuestOutService");
    }

    @Override

    protected void onHandleIntent(Intent intent) {
        String guestId = intent.getExtras().getString("guestId");
        //final Map<String, Permintaan> permintaans = new HashMap<>();
        final String roomNumber = getSharedPreferences("userSettings", MODE_PRIVATE)
                .getString("roomNumber", "none");
        while (true) {
            Log.d(GuestPermintaanService.class.getCanonicalName(), "Checking for guest permintaan status updates for " + guestId);
            // Get the guest's permintaans.
            GuestServer.getInstance(getBaseContext()).getGuestInRoom(roomNumber).subscribe(new Observer<Guest>() {
                Guest guest = null;

                @Override
                public void onCompleted() {
                    if (guest == null) {
                        //need to go to splash screen activity
                        handleCheckGuestOut();
                    }

                }

                @Override
                public void onError(Throwable e) {
                    Log.d(CheckGuestInFragment.class.getCanonicalName(), "On error");
                    e.printStackTrace();
                }

                @Override
                public void onNext(Guest result) {
                    guest = result;
                    Log.d(CheckGuestInFragment.class.getCanonicalName(), "On next guest " + result);
                }
            });
            try {
                Log.d(GuestPermintaanService.class.getCanonicalName(), "Going to sleep for " + Constants.GUEST_PERMINTAAN_REFRESH_TIME_IN_SECONDS + " seconds");
                Thread.sleep(Constants.GUEST_CHECKOUT_REFRESH_TIME_IN_SECONDS * 1000);
            } catch (InterruptedException e) {
            }
        }
    }

    private void handleCheckGuestOut()
    {
        Intent localIntent =
                new Intent(Constants.BROADCAST_GUEST_CHECKOUT_ACTION);
        // Broadcasts the Intent to receivers in this app.
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }
}