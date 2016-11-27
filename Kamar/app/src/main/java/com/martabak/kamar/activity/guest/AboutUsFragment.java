package com.martabak.kamar.activity.guest;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.martabak.kamar.R;

/**
 * About Us Fragment which consists of the text
 */
public class AboutUsFragment extends Fragment {

    public AboutUsFragment() {
    }

    public static AboutUsFragment newInstance() {
        return new AboutUsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guest_aboutus, container, false);
        return view;
    }
}
