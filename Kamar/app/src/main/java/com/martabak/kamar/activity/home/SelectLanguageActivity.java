package com.martabak.kamar.activity.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.martabak.kamar.R;
import com.martabak.kamar.activity.guest.GuestHomeActivity;
import com.martabak.kamar.domain.Guest;
import com.martabak.kamar.service.GuestServer;

import java.util.Locale;

import rx.Observer;


public class SelectLanguageActivity extends AppCompatActivity {

    private String welcomeMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_language);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ImageButton englishButton = (ImageButton) findViewById(R.id.language_english);
        final ImageButton indonesianButton = (ImageButton) findViewById(R.id.language_bahasa);

        final String roomNumber = getSharedPreferences("userSettings", MODE_PRIVATE)
                .getString("roomNumber", "none");
        setGuestId(roomNumber);
        String guestId = getSharedPreferences("userSettings", MODE_PRIVATE).
                getString("guestId", "none");
        Log.v("guestId Shared Pref", guestId);

        if (englishButton != null) {
            englishButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setLocale("en");

                    Toast.makeText(
                            SelectLanguageActivity.this,
                            getString(R.string.language_set_to),
                            Toast.LENGTH_LONG
                    ).show();
                    Log.d(SelectLanguageActivity.class.getCanonicalName(), "Set locale to English");

                    //startActivity(new Intent(SelectLanguageActivity.this, SelectUserTypeActivity.class));
                    if (!(getSharedPreferences("userSettings", MODE_PRIVATE).
                            getString("guestId", "none")).equals("none")) {

                        final AlertDialog welcomeDialog = new AlertDialog.Builder(SelectLanguageActivity.this).create();
                        final View view = SelectLanguageActivity.this.getLayoutInflater().inflate(R.layout.dialog_welcome_message, null);
                        final TextView textView = (TextView) view.findViewById(R.id.guest_welcome_text);
                        textView.setText(welcomeMessage);
                        welcomeDialog.setView(view);
                        welcomeDialog.show();
                    }
                    startActivity(new Intent(SelectLanguageActivity.this, GuestHomeActivity.class));
                    finish();
                }
            });
        }
        if (indonesianButton != null) {
            indonesianButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setLocale("in");

                    Toast.makeText(
                            SelectLanguageActivity.this,
                            getString(R.string.language_set_to),
                            Toast.LENGTH_LONG
                    ).show();
                    Log.d(SelectLanguageActivity.class.getCanonicalName(), "Set locale to Indonesian");

                    //startActivity(new Intent(SelectLanguageActivity.this, SelectUserTypeActivity.class));
                    if (!(getSharedPreferences("userSettings", MODE_PRIVATE).
                            getString("guestId", "none")).equals("none")) {

                        final AlertDialog welcomeDialog = new AlertDialog.Builder(SelectLanguageActivity.this).create();
                        final View view = SelectLanguageActivity.this.getLayoutInflater().inflate(R.layout.dialog_welcome_message, null);
                        final TextView textView = (TextView) view.findViewById(R.id.guest_welcome_text);
                        textView.setText(welcomeMessage);
                        welcomeDialog.setView(view);
                        welcomeDialog.show();
                    }
                    startActivity(new Intent(SelectLanguageActivity.this, GuestHomeActivity.class));
                    finish();
                }
            });
        }
    }

    /**
     * Set the locale of the app.
     *
     * @param lang The 2-digit language code.
     */
    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    /**
     * Set guest id on shared preferences.
     * @param roomNumber The room number.
     */
    private void setGuestId(final String roomNumber) {
        GuestServer.getInstance(getBaseContext()).getGuestInRoom(
                roomNumber).subscribe(new Observer<Guest>() {
            @Override public void onCompleted() {
                Log.d(GuestHomeActivity.class.getCanonicalName(), "Completed setting guest ID");
            }
            @Override public void onError(Throwable e) {
                Log.d(GuestHomeActivity.class.getCanonicalName(), "On error");
                e.printStackTrace();
            }
            @Override public void onNext(Guest result) {
                // Store the guest id in shared preferences
                SharedPreferences pref = getSharedPreferences("userSettings", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                Log.v(GuestHomeActivity.class.getCanonicalName(), "Room Number : " + roomNumber);
                if (result == null) {
                    editor.putString("guestId", "none");
                }
                else {
                    editor.putString("guestId", result._id);
                    welcomeMessage = result.welcomeMessage;
                    Log.v(GuestHomeActivity.class
                            .getCanonicalName(), "Setting guest ID to " + result._id);
                }
                Log.v(GuestHomeActivity.class.getCanonicalName(), "Guest ID " + getSharedPreferences("userSettings", MODE_PRIVATE)
                        .getString("guestId", "none"));
                editor.commit();
            }
        });
    }

}
