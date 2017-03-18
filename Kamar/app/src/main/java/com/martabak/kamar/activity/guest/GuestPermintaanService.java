package com.martabak.kamar.activity.guest;

import android.app.IntentService;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import rx.functions.Action1;
import rx.functions.Func1;

/**
 * A permintaan service that routinely checks for new updates on a guest's permintaans.
 * <p>
 *     Expects intent extra: "guestId" mapping to the guest's ID.
 * </p>
 */
public class GuestPermintaanService extends IntentService {

    private static final int POLL_EVERY_SECONDS_AMOUNT = 20;

    private static final Class RESULT_ACTIVITY = GuestHomeActivity.class;

    /**
     * Construct a guest permintaan service.
     */
    public GuestPermintaanService() {
        super("GuestPermintaanService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String guestId = intent.getExtras().getString("guestId");
        final Map<String, Permintaan> permintaans = new HashMap<>();

        while (true) {
            Log.d(GuestPermintaanService.class.getCanonicalName(), "Checking for guest permintaan status updates for " + guestId);
            // Get the guest's permintaans.
            PermintaanServer.getInstance(this).getPermintaansForGuest(guestId)
                    // Filter for only permintaans that have had their status updated
                    .filter(new Func1<Permintaan, Boolean>() {
                        @Override public Boolean call(Permintaan curr) {
                            Permintaan prev = permintaans.get(curr._id);
                            permintaans.put(curr._id, curr);
                            return prev != null && !prev.state.equals(curr.state);
                        }
                    }).subscribe(new Action1<Permintaan>() {
                        @Override public void call(Permintaan permintaan) {
                            Log.d(GuestPermintaanService.class.getCanonicalName(), "Guest permintaan has been updated with new status");
                            List<String> permIds = new ArrayList<String>(permintaans.keySet());
                            Collections.sort(permIds);
                            createNotification(permIds.indexOf(permintaan._id), permintaan);
                        }
                    });

            try {
                Log.d(GuestPermintaanService.class.getCanonicalName(), "Going to sleep for " + POLL_EVERY_SECONDS_AMOUNT + " seconds");
                Thread.sleep(POLL_EVERY_SECONDS_AMOUNT * 1000);
            } catch (InterruptedException e) {
            }
        }
    }

    private void createNotification(int nId, Permintaan permintaan) {
        String eta = "";
        if (permintaan.shouldShowEta()) {
            eta = String.format(Locale.getDefault(), ", %s %d %s", getString(R.string.estimate), permintaan.eta, getString(R.string.minutes));
        }
        NotificationCompat.Builder mBuilder =new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_menu_manage)
                .setContentTitle(permintaan.type + " " + getString(R.string.permintaan))
                .setContentText(getString(R.string.permintaan_status_is_now) + " " + permintaan.state + eta);
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
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // nId allows you to update the notification later on.
        mNotificationManager.notify("permintaan", nId, mBuilder.build());
    }

}
