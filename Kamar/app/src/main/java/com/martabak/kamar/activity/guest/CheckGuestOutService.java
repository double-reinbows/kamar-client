package com.martabak.kamar.activity.guest;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.martabak.kamar.domain.Guest;
import com.martabak.kamar.service.GuestServer;
import com.martabak.kamar.util.Constants;

import rx.Observer;

/**
 * Created by adarsh on 15-Jul-17.
 */

public class CheckGuestOutService extends IntentService {

    private static final Class PARENT_ACTIVITY = GuestHomeActivity.class;

    /**
     * Construct a handle guset check out service.
     */
    public CheckGuestOutService() {
        super("CheckGuestOutService");
    }

    @Override

    protected void onHandleIntent(Intent intent) {
        final String roomNumber = getSharedPreferences("userSettings", MODE_PRIVATE)
                .getString("roomNumber", "none");
        while (true) {
            Log.d(CheckGuestOutService.class.getCanonicalName(), "Checking for guest checkout updates");
            // Get the guest's permintaans.
            GuestServer.getInstance(getBaseContext()).getGuestInRoom(roomNumber).subscribe(new Observer<Guest>() {
                Guest guest = null;

                @Override
                public void onCompleted() {
                    if (guest == null) {
                        //need to go to splash screen activity
                        Log.d(CheckGuestOutService.class.getCanonicalName(), "Guest has been checked out");
                        handleCheckGuestOut();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onNext(Guest result) {
                    guest = result;
                }
            });
            try {
                Log.d(CheckGuestOutService.class.getCanonicalName(), "Going to sleep for " + Constants.GUEST_CHECKOUT_REFRESH_TIME_IN_SECONDS + " seconds");
                Thread.sleep(Constants.GUEST_CHECKOUT_REFRESH_TIME_IN_SECONDS * 1000);
            } catch (InterruptedException e) {
            }
        }
    }

    private void handleCheckGuestOut()
    {
        Intent broadcastCheckGuestOutIntent =
                new Intent();
        broadcastCheckGuestOutIntent.setAction(Constants.BROADCAST_GUEST_CHECKOUT_ACTION);
        // Broadcasts the Intent to receivers in this app.
        sendBroadcast(broadcastCheckGuestOutIntent);
//        this.stopService(broadcastCheckGuestOutIntent);
    }

}