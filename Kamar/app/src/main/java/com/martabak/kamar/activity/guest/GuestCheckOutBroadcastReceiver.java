package com.martabak.kamar.activity.guest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.martabak.kamar.R;
import com.martabak.kamar.activity.home.SplashScreenActivity;
import com.martabak.kamar.activity.staff.CheckGuestInFragment;
import com.martabak.kamar.util.Constants;

import static android.content.Context.MODE_PRIVATE;

/**
 * The Guest Check out broadcast reciever that'll return the app to the splash screen
 * when received a guest check out broadcast intent
 */

public class GuestCheckOutBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(GuestCheckOutBroadcastReceiver.this.toString(), "GOT SOMETHING");
        if (intent.getAction().equals(Constants.BROADCAST_GUEST_CHECKOUT_ACTION)) {
            Log.v(GuestCheckOutBroadcastReceiver.this.toString(), "Returning to splash screen");
            Intent i = new Intent(context, SplashScreenActivity.class);
            i.addFlags(intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS| Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            //set guest to null
            Log.d(GuestCheckOutBroadcastReceiver.this.toString(), "updateGuest");
            context.getSharedPreferences("userSettings", MODE_PRIVATE).edit().
                    putString("guestId", "none").commit();
            Toast.makeText(
                    context,
                    context.getString(R.string.logout_result),
                    Toast.LENGTH_LONG
            ).show();
        }
    }
}
