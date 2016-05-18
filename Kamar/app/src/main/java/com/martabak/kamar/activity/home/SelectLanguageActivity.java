package com.martabak.kamar.activity.home;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.martabak.kamar.R;

import java.util.Locale;


public class SelectLanguageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);

        final Button englishButton = (Button) findViewById(R.id.language_english);
        final Button indonesianButton = (Button) findViewById(R.id.language_bahasa);

        englishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d(SelectLanguageActivity.class.getCanonicalName(), "Previous value:" + getSharedPreferences("userSettings", MODE_PRIVATE).getString("language", "BH"));
//                SharedPreferences languagePref = getSharedPreferences("userSettings", MODE_PRIVATE);
//                SharedPreferences.Editor editor = languagePref.edit().
//                        putString("language", "en");
//                editor.commit();
                setLocale("en");
                displayLanguageToast();
                Log.d(SelectLanguageActivity.class.getCanonicalName(), "Set locale to English");

                switchActivity();
            }
        });
        indonesianButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d(SelectLanguageActivity.class.getCanonicalName(), "Previous value:" + getSharedPreferences("userSettings", MODE_PRIVATE).getString("language", "BH"));
//                SharedPreferences languagePref = getSharedPreferences("userSettings", MODE_PRIVATE);
//                SharedPreferences.Editor editor = languagePref.edit().
//                        putString("language", "in");
//                editor.commit();
                setLocale("in");
                displayLanguageToast();
                Log.d(SelectLanguageActivity.class.getCanonicalName(), "Set locale to Indonesian");

                switchActivity();
            }
        });
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
     * Display a toast after language set.
     */
    private void displayLanguageToast() {
        Context context = getApplicationContext();
        CharSequence text = getResources().getString(R.string.language_set_to);
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /**
     * Switch activity.
     */
    private void switchActivity() {
        Intent intent = new Intent(this, SelectUserTypeActivity.class);
        startActivity(intent);
    }
}
