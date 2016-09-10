package com.martabak.kamar.activity.guest;

import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.Guest;

import java.util.Locale;

/**
 * Duplicate of SelectLanguageActivity because this is a fragment instead of activity.
 * Does not have the welcome/promoImg code either
 */
public class SelectLanguageFragment extends Fragment {

    public SelectLanguageFragment() {
    }

    public static SelectLanguageFragment newInstance() {
        return new SelectLanguageFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_select_language, container, false);

        final ImageButton englishButton = (ImageButton) view.findViewById(R.id.language_english);
        final ImageButton indonesianButton = (ImageButton) view.findViewById(R.id.language_bahasa);
        final ImageButton russianButton = (ImageButton) view.findViewById(R.id.language_russian);
        final ImageButton zhButton = (ImageButton) view.findViewById(R.id.language_zh);


        if (englishButton != null) {
            englishButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    setLocale("en");
                    Toast.makeText(
                            getActivity(),
                            getString(R.string.language_set_to),
                            Toast.LENGTH_LONG
                    ).show();
                    getFragmentManager().beginTransaction()
                            .replace(R.id.guest_container, GuestHomeFragment.newInstance())
                            .commit();
                }
            });
        }

        if (indonesianButton != null) {
            indonesianButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setLocale("in");

                    Toast.makeText(
                            getActivity(),
                            getString(R.string.language_set_to),
                            Toast.LENGTH_LONG
                    ).show();
                    getFragmentManager().beginTransaction()
                            .replace(R.id.guest_container, GuestHomeFragment.newInstance())
                            .commit();
                }
            });
        }

        if (russianButton != null) {
            russianButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setLocale("ru");

                    Toast.makeText(
                            getActivity(),
                            getString(R.string.language_set_to),
                            Toast.LENGTH_LONG
                    ).show();
                    getFragmentManager().beginTransaction()
                            .replace(R.id.guest_container, GuestHomeFragment.newInstance())
                            .commit();
                }
            });
        }
        if (zhButton != null) {
            zhButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setLocale("zh");

                    Toast.makeText(
                            getActivity(),
                            getString(R.string.language_set_to),
                            Toast.LENGTH_LONG
                    ).show();
                    getFragmentManager().beginTransaction()
                            .replace(R.id.guest_container, GuestHomeFragment.newInstance())
                            .commit();
                }
            });
        }
        return view;

    }

    /*
    * Set the locale of the app.
    *
    * @param lang The 2-digit language code.
    */
    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(config,
                getActivity().getBaseContext().getResources().getDisplayMetrics());
    }
}