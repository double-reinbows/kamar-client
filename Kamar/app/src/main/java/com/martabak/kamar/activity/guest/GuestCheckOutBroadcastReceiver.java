package com.martabak.kamar.activity.guest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.martabak.kamar.activity.home.SplashScreenActivity;

/**
 * Created by adarsh on 18-Jul-17.
 */

public class GuestCheckOutBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(context, SplashScreenActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
