package com.martabak.kamar.activity.home;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;

import com.martabak.kamar.R;
import com.martabak.kamar.util.Constants;
import com.martabak.kamar.util.CrashReportSender;
import com.martabak.kamar.util.EmailSender;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.ConnectException;

import rx.exceptions.OnErrorNotImplementedException;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

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

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Montserrat-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

    }

    private void AppInitialization() {

        Thread.setDefaultUncaughtExceptionHandler(
                new EmailExceptionHandler(Thread.getDefaultUncaughtExceptionHandler()));


    }

    class EmailExceptionHandler implements Thread.UncaughtExceptionHandler {

        private Thread.UncaughtExceptionHandler defaultUEH;

        public EmailExceptionHandler(Thread.UncaughtExceptionHandler defaultUEH) {
            this.defaultUEH = defaultUEH;
        }

        @Override
        public void uncaughtException(Thread thread, Throwable e) {

            e.printStackTrace();
            Log.e(EmailExceptionHandler.class.getCanonicalName(), "Handling uncaught exception");
            if (Constants.SEND_CRASH_REPORTS) {
                CrashReportSender.sendCrashReport(getApplicationContext(), e);
            }

            //defaultUEH.uncaughtException(thread, e);


        }
    }
}
