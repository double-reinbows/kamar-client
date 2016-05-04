package com.martabak.kamar.activity;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.service.PermintaanServer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.functions.Action1;
import rx.functions.Func1;

/**
 * A service that routinely checks for new permintaans from guests.
 */
public class StaffPermintaanService extends IntentService {

    private static final int POLL_EVERY_SECONDS_AMOUNT = 10;

    private static final Class RESULT_ACTIVITY = StaffHomeActivity.class;

    /**
     * Construct a staff permintaan service.
     */
    public StaffPermintaanService() {
        super("StaffPermintaanService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final Set<String> permintaanIds = new HashSet<>();

        while (true) {
            Log.d(StaffPermintaanService.class.getCanonicalName(), "Checking for new permintaans");
            // Get all permintaans in the NEW state
            PermintaanServer.getInstance(this).getPermintaansOfState("NEW")
                    // Filter for only permintaans that have not been seen by this service and not been updated
                    .filter(new Func1<Permintaan, Boolean>() {
                        @Override public Boolean call(Permintaan permintaan) {
                            boolean seen = permintaanIds.contains(permintaan._id);
                            permintaanIds.add(permintaan._id);
                            return !seen && permintaan.updated == null;
                        }
                    }).subscribe(new Action1<Permintaan>() {
                        @Override public void call(Permintaan permintaan) {
                            Log.d(StaffPermintaanService.class.getCanonicalName(), "New permintaan has been found");
                            List<String> permintaanIdsList = new ArrayList<String>(permintaanIds);
                            Collections.sort(permintaanIdsList);
                            createNotification(permintaanIdsList.indexOf(permintaan._id), permintaan);
                        }
                    });

            try {
                Log.d(StaffPermintaanService.class.getCanonicalName(), "Going to sleep for " + POLL_EVERY_SECONDS_AMOUNT + " seconds");
                Thread.sleep(POLL_EVERY_SECONDS_AMOUNT * 1000);
            } catch (InterruptedException e) {
            }
        }
    }

    private void createNotification(int nId, Permintaan permintaan) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_menu_manage)
                        .setContentTitle(permintaan.type + " REQUEST")
                        .setContentText("New " + permintaan.type + " REQUEST " + permintaan.roomNumber);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, RESULT_ACTIVITY);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(RESULT_ACTIVITY);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // nId allows you to update the notification later on.
        mNotificationManager.notify("permintaan", nId, mBuilder.build());
    }
}
