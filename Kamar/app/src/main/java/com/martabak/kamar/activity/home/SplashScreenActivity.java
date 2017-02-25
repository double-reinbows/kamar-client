package com.martabak.kamar.activity.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.RelativeLayout;

import com.martabak.kamar.R;
import com.martabak.kamar.activity.AbstractCustomFontActivity;

public class SplashScreenActivity extends AbstractCustomFontActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        RelativeLayout layout = (RelativeLayout)findViewById(R.id.splash_screen_layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //terms and conditions dialog
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SplashScreenActivity.this)
                        .setTitle(R.string.terms_and_conditions)
                        .setMessage(R.string.terms_and_conditions_message)
                        .setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton(android.R.string.yes,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(SplashScreenActivity.this, SelectLanguageActivity.class);
                                        startActivity(intent);
                                    }
                                }).setNegativeButton(android.R.string.no, null);
                alertDialogBuilder.show();
            }
        });
    }
}
