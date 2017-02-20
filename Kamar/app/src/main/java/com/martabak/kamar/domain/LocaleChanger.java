package com.martabak.kamar.domain;

import android.app.Activity;
import android.content.res.Configuration;

/**
 * Created by rei on 25/01/17.
 */
public class LocaleChanger {

    /**
     * Set the locale of the app.
     *
     * @param lang The 2-digit language code.
     */
    public void setLocale(Activity activity, String lang) {
        java.util.Locale locale = new java.util.Locale(lang);
        java.util.Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getBaseContext().getResources().updateConfiguration(config,
                activity.getBaseContext().getResources().getDisplayMetrics());
    }
}
