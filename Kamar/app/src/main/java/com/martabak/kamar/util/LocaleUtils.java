package com.martabak.kamar.util;

import android.app.Activity;
import android.content.res.Configuration;

import java.util.Locale;

public class LocaleUtils {

    /**
     * Set the locale of the app.
     */
    public static void setLocale(Activity activity, String languageCode, String countryCode) {
        Locale locale = new Locale(languageCode, countryCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getBaseContext().getResources().updateConfiguration(config,
                activity.getBaseContext().getResources().getDisplayMetrics());
    }

    public static String localeToEmoji(Locale locale) {
        String countryCode = locale.getCountry();
        int firstLetter = Character.codePointAt(countryCode, 0) - 0x41 + 0x1F1E6;
        int secondLetter = Character.codePointAt(countryCode, 1) - 0x41 + 0x1F1E6;
        return new String(Character.toChars(firstLetter)) + new String(Character.toChars(secondLetter));
    }

}
