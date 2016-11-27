package com.martabak.kamar.activity.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.martabak.kamar.R;

/**
 * Created by adarsh on 26/11/16.
 */
public class SplashScreenActivity extends Activity {

    //splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, SelectLanguageActivity.class);
                startActivity(intent);

                //end this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}
