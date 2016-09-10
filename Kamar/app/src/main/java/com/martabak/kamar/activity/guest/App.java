package com.martabak.kamar.activity.guest;

import android.app.Application;
import android.content.Context;

/**
 * This class was created to access local (Kamar's) resources in classes other than
 * activities/fragments. Is referenced in the AndroidManifest.
 */
public class App extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }
}
