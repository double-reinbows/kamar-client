package com.martabak.kamar.activity.staff;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.service.PermintaanServer;
import com.martabak.kamar.util.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.functions.Action1;
import rx.functions.Func1;

/**
 * A service that routinely checks for new permintaans from guests.
 *
 * <p>
 *     Expects intent extra: "subUserType" mapping to the staff member's sub-user type, either
 *     {@code Permintaan.OWNER_RESTAURANT} or {@code Permintaan.OWNER_FRONTDESK}.
 * </p>
 */
public class StaffPermintaanService extends IntentService {

    private static final Class RESULT_ACTIVITY = StaffHomeActivity.class;

    /**
     * Construct a staff permintaan service.
     */
    public StaffPermintaanService() {
        super("StaffPermintaanService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final String owner = intent.getExtras().getString("subUserType");
        final Set<String> permintaanIds = new HashSet<>();

        while (true) {
            Log.d(StaffPermintaanService.class.getCanonicalName(), "Checking for new permintaans for owner " + owner);
            // Get all permintaans in the NEW state
            PermintaanServer.getInstance(this).getPermintaansOfState(Permintaan.STATE_NEW)
                    // Filter for only permintaans that are:
                    // * relevant to this owner
                    // * have not been updated
                    // * no more than 3 days old
                    .filter(new Func1<Permintaan, Boolean>() {
                        @Override public Boolean call(Permintaan permintaan) {
                            permintaanIds.add(permintaan._id);
                            return permintaan.updated == null &&
                                    permintaan.owner.equalsIgnoreCase(owner) &&
                                    !permintaan.isOlderThan(Constants.PERMINTAAN_VIEW_WINDOW_FOR_STAFF_IN_DAYS);
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
                Log.d(StaffPermintaanService.class.getCanonicalName(), "Going to sleep for " + Constants.STAFF_PERMINTAAN_REFRESH_TIME_IN_SECONDS + " seconds");
                Thread.sleep(Constants.STAFF_PERMINTAAN_REFRESH_TIME_IN_SECONDS * 1000);
            } catch (InterruptedException e) {
            }
        }
    }

    private void createNotification(int nId, Permintaan permintaan) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_menu_manage)
                .setOnlyAlertOnce(false)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(getString(R.string.new_permintaan) + " " + getString(R.string.permintaan).toUpperCase())
                .setContentText(permintaan.type + ": " + getString(R.string.room_number) + " " + permintaan.roomNumber);
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
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // nId allows you to update the notification later on.
        mNotificationManager.notify("permintaan", nId, mBuilder.build());
    }
}
