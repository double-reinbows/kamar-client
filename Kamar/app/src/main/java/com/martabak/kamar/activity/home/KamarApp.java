package com.martabak.kamar.activity.home;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by rei on 7/12/16.
 */
public class KamarApp extends Application {

    private static KamarApp singleton;

    public static KamarApp getInstance(){
        return singleton;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        AppInitialization();
        singleton = this;
    }

    private void AppInitialization() {
        defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(_unCaughtExceptionHandler);
    }

    private Thread.UncaughtExceptionHandler defaultUEH;

    // handler listener
    private Thread.UncaughtExceptionHandler _unCaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            ex.printStackTrace();
            // TODO handle exception here
            sendLogcatMail();
        }
    };


    public void sendLogcatMail(){

        // save logcat in file
        File outputFile = new File(Environment.getExternalStorageDirectory(),
                "logcat.txt");
        Uri uri = Uri.fromFile(outputFile);
        try {
            Runtime.getRuntime().exec(
                    "logcat -f " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //send file using email
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        emailIntent.setData(Uri.parse("mailto:"));
        // Set type to "email"
        emailIntent.setType("vnd.android.cursor.dir/email");
        String to[] = {"dynamicfrogman@gmail.com"};
        emailIntent .putExtra(Intent.EXTRA_EMAIL, to);
        // the attachment
        emailIntent .putExtra(Intent.EXTRA_STREAM, uri);
        // the mail subject
        emailIntent .putExtra(Intent.EXTRA_SUBJECT, "Subject");
        Intent newIntent = Intent.createChooser(emailIntent, "Share via");
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(newIntent);
    }
}
