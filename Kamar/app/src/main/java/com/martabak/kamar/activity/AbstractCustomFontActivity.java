package com.martabak.kamar.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * An abstract class that provides the context to apply a custom font across every text element
 * that exists within the extending activity.
 */
public abstract class AbstractCustomFontActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}