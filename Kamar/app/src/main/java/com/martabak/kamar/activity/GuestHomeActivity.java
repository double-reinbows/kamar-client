package com.martabak.kamar.activity;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.Guest;
import com.martabak.kamar.domain.SurveyQuestion;
import com.martabak.kamar.service.FeedbackServer;
import com.martabak.kamar.service.GuestServer;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;

public class GuestHomeActivity extends AppCompatActivity
        implements BellboyDialogFragment.BellboyDialogListener{

    String option;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_home);
        final ImageAdapter imgAdapter = new ImageAdapter(this);
        final GridView gridView = (GridView) findViewById(R.id.guestgridview);
        View passwordIconView = findViewById(R.id.passwordChangeIcon);

        gridView.setAdapter(imgAdapter);

        //display feature text on each item click
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //perform action for each individual feature
                option = imgAdapter.getItem(position).toString();
                createAction();
            }
        });

        //open the change room number as a fragment
        passwordIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment changeRoomNumberFragment = new ChangeRoomNumberDialogFragment();
                changeRoomNumberFragment.show(getFragmentManager(), "changeRoomNumber");

            }
        });
    }

    /*Actions for each individual feature on the grid */
    public void createAction() {
        switch(option)
        {
            case "TRANSPORT":
                Intent intent = new Intent(this, TransportActivity.class);
                startActivity(intent);
                break;
            case "HOUSEKEEPING":
                DialogFragment housekeepingFragment = new HousekeepingDialogFragment();
                housekeepingFragment.show(getFragmentManager(), "housekeeping");
                break;
            case "BELLBOY":
                DialogFragment bellboyFragment = new BellboyDialogFragment();
                bellboyFragment.show(getFragmentManager(), "bellboy");
                break;
            case "MAINTENANCE":
                DialogFragment maintenanceFragment = new MaintenanceDialogFragment();
                maintenanceFragment.show(getFragmentManager(), "maintenance");
                break;
            case "TELL US":
                DialogFragment fragment = new TellusDialogFragment();
                fragment.show(getFragmentManager(), "tellus");
                break;
            case "CHECKOUT":
                DialogFragment checkoutBellboyFragment = new BellboyDialogFragment();
                checkoutBellboyFragment.show(getFragmentManager(), "bellboy");
                break;
            default:
                break;
        }
    }

    /*
    * Display toast message at the bottom of the screen
    */
    public void makeToast(String selectedValue) {
        Toast.makeText(getBaseContext(), ""+ selectedValue, Toast.LENGTH_LONG).show();
    }


    /*
    * Positive click
    */
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        dialog.dismiss();
        if (option == "CHECKOUT") {
            startCheckout();
        }
    }

    /*
    * Negative click
    */
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
        if (option == "CHECKOUT") {
            startCheckout();
        }
    }

    /*
     * Show checkout fragment
     */
    public void startCheckout() {
        Intent intent = new Intent(this, SurveyActivity.class);
        startActivity(intent);
    }

}

