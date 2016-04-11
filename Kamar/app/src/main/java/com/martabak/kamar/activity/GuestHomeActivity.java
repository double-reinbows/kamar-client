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

public class GuestHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_home);
        final ImageAdapter imgAdapter = new ImageAdapter(this);
        final GridView gridView = (GridView) findViewById(R.id.guestgridview);
        View passwordIconView = findViewById(R.id.passwordChangeIcon);
        gridView.setAdapter(imgAdapter);

        //check if a staff is logged in
        String userType = getSharedPreferences("userSettings", MODE_PRIVATE).
                getString("userType", "none");

        /*if (userType == "GUEST") {
            passwordIconView.setVisibility(View.GONE);

        }*/

        //display feature text on each item click
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //perform action for each individual feature
                createAction(imgAdapter.getItem(position).toString());
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
    public void createAction(String option) {
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
            default:
                break;
        }
    }



}

