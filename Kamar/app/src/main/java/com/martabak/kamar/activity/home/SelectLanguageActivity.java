package com.martabak.kamar.activity.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.martabak.kamar.R;
import com.martabak.kamar.activity.guest.GuestHomeActivity;
import com.martabak.kamar.domain.Event;
import com.martabak.kamar.domain.Guest;
import com.martabak.kamar.service.EventServer;
import com.martabak.kamar.service.GuestServer;
import com.martabak.kamar.service.Server;

import java.util.Locale;

import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

/**
 * Presents 4 language option buttons: English, Bahasa, Russian and ZH.
 * Upon language selection, the room is checked for a guest and sets the guest's ID to
 * SharedPreferences, otherwise it's set to "none"/
 * If there is a guest, then a welcome + promo dialog will display.
 */
public class SelectLanguageActivity extends AppCompatActivity {

    private String welcomeMessage;
    private String promoImgId;
    private String roomNumber;

    // the listener bindings for the language buttons
    @OnClick(R.id.language_english)
    public void onEnglishClick() {
        setLocale("en");
        Log.v("LOCALE",Locale.getDefault().getLanguage());
        Toast.makeText(
                SelectLanguageActivity.this,
                getString(R.string.language_set_to),
                Toast.LENGTH_LONG
        ).show();
        Log.d(SelectLanguageActivity.class.getCanonicalName(), "Set locale to English");
        setGuestId(roomNumber);
    }

    @OnClick(R.id.language_bahasa)
    public void onBahasaClick() {
        setLocale("in");
        Log.v("LOCALE",Locale.getDefault().getLanguage());
        Toast.makeText(
                SelectLanguageActivity.this,
                getString(R.string.language_set_to),
                Toast.LENGTH_LONG
        ).show();
        Log.d(SelectLanguageActivity.class.getCanonicalName(), "Set locale to Indonesian");
        setGuestId(roomNumber);
    }

    @OnClick(R.id.language_russian)
    public void onRussianClick() {
        setLocale("ru");
        Log.v("LOCALE",Locale.getDefault().getLanguage());
        Toast.makeText(
                SelectLanguageActivity.this,
                getString(R.string.language_set_to),
                Toast.LENGTH_LONG
        ).show();
        Log.d(SelectLanguageActivity.class.getCanonicalName(), "Set locale to Russian");
        setGuestId(roomNumber);
    }

    @OnClick(R.id.language_zh)
    public void onChineseClick() {
        setLocale("zh");
        Log.v("LOCALE",Locale.getDefault().getLanguage());
        Toast.makeText(
                SelectLanguageActivity.this,
                getString(R.string.language_set_to),
                Toast.LENGTH_LONG
        ).show();
        Log.d(SelectLanguageActivity.class.getCanonicalName(), "Set locale to Chinese");
        setGuestId(roomNumber);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_language);

        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get the room number
        roomNumber = getSharedPreferences("userSettings", MODE_PRIVATE)
                .getString("roomNumber", "none");
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
     *
     * @param roomNumber The room number.
     */
    private void setGuestId(final String roomNumber) {
        GuestServer.getInstance(getBaseContext()).getGuestInRoom(
                roomNumber).subscribe(new Observer<Guest>() {
            @Override
            public void onCompleted() {
                Log.d(SelectLanguageActivity.class.getCanonicalName(), "Completed setting guest ID");
                //if there is a guest in the room then show promo + welcome msg
                if (!(getSharedPreferences("userSettings", MODE_PRIVATE).
                        getString("guestId", "none")).equals("none")) {
                    PromoWelcomeDialog();
                } else { //skip welcome + promo dialog
                    startActivity(new Intent(SelectLanguageActivity.this, GuestHomeActivity.class));
                    finish();
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d(SelectLanguageActivity.class.getCanonicalName(), "On error");
                e.printStackTrace();
            }

            @Override
            public void onNext(Guest result) {
                // Store the guest id in shared preferences
                SharedPreferences pref = getSharedPreferences("userSettings", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                Log.v(SelectLanguageActivity.class.getCanonicalName(), "Room Number : " + roomNumber);
                if (result == null) {
                    editor.putString("guestId", "none");
                } else {
                    //set guestId in Shared Preferences
                    editor.putString("guestId", result._id);
                    welcomeMessage = result.welcomeMessage;
                    promoImgId = result.promoImgId;
                    Log.v(SelectLanguageActivity.class
                            .getCanonicalName(), "Setting guest ID to " + result._id);
                }
                Log.v(SelectLanguageActivity.class.getCanonicalName(), "Guest ID " + getSharedPreferences("userSettings", MODE_PRIVATE)
                        .getString("guestId", "none"));
                editor.commit();
            }
        });
    }

    private void PromoWelcomeDialog() {
        final AlertDialog welcomeDialog = new AlertDialog.Builder(SelectLanguageActivity.this).create();
        //set up the view and clickListener
        final View view = SelectLanguageActivity.this.getLayoutInflater().inflate(R.layout.dialog_welcome_message, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                welcomeDialog.dismiss();
                startActivity(new Intent(SelectLanguageActivity.this, GuestHomeActivity.class));
                finish();
            }
        });

        //set up promo image
        ImageView promoImg = ButterKnife.findById(view, R.id.guest_welcome_image);
        setPromoImg(promoImgId, promoImg);

        //set up welcome text and show dialog
        final TextView textView = ButterKnife.findById(view, R.id.guest_welcome_text);
        textView.setText(welcomeMessage);
        welcomeDialog.setView(view);
        welcomeDialog.show();
    }

    private void setPromoImg(final String promoImgId, final ImageView promoImg) {
        EventServer.getInstance(getBaseContext()).getEvent(
                promoImgId).subscribe(new Observer<Event>() {
            @Override
            public void onCompleted() {
                Log.d(SelectLanguageActivity.class.getCanonicalName(), "Completed setting promo img");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(SelectLanguageActivity.class.getCanonicalName(), "On error");
                e.printStackTrace();
            }

            @Override
            public void onNext(Event result) {
                Log.d(SelectLanguageActivity.class.getCanonicalName(), "getEvent On next");
                if (result._id.equals(promoImgId)) {
                    String menuImgPath = result.getImageUrl();
                    Server.picasso(SelectLanguageActivity.this)
                            .load(menuImgPath)
                            .placeholder(R.drawable.loading_batik)
                            .error(R.drawable.error)
                            .into(promoImg);
                }
            }
        });
    }
}
