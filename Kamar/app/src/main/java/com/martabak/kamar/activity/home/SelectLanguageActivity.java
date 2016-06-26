package com.martabak.kamar.activity.home;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.martabak.kamar.R;

import java.util.Locale;


public class SelectLanguageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);

        // Get the ActionBar here to configure the way it behaves.
        final ActionBar ab = getSupportActionBar();

        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)

        ab.setCustomView(R.layout.actionbar_custom_view);

        final ImageButton englishButton = (ImageButton) findViewById(R.id.language_english);
        final ImageButton indonesianButton = (ImageButton) findViewById(R.id.language_bahasa);

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

                    startActivity(new Intent(SelectLanguageActivity.this, SelectUserTypeActivity.class));
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

                    startActivity(new Intent(SelectLanguageActivity.this, SelectUserTypeActivity.class));
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

}
