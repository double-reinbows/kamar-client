package com.martabak.kamar.activity.staff;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.martabak.kamar.R;

/**
 * PromotionalImageFragment {@link Fragment} subclass.
 */
public class PromotionalImageFragment extends Fragment {


    public PromotionalImageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_promotional_image, container, false);
    }

}
