package com.martabak.kamar.activity.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.martabak.kamar.R;

/**
 * Created by adarsh on 26/11/16.
 */
public class SplashScreenActivity extends Activity {

    //splash screen timer
    private static int SPLASH_TIME_OUT = 9999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        RelativeLayout layout = (RelativeLayout)findViewById(R.id.splash_screen_layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashScreenActivity.this, SelectLanguageActivity.class);
                startActivity(intent);
            }
        });
    }

}
