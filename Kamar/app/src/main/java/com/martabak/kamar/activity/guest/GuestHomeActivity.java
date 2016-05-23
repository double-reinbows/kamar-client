package com.martabak.kamar.activity.guest;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.martabak.kamar.R;
import com.martabak.kamar.activity.restaurant.RestaurantActivity;
import com.martabak.kamar.activity.staff.CheckGuestInFragment;
import com.martabak.kamar.domain.Guest;
import com.martabak.kamar.service.GuestServer;

import rx.Observer;

public class GuestHomeActivity extends AppCompatActivity
        implements BellboyDialogFragment.BellboyDialogListener,
        ChangeRoomNumberDialogFragment.ChangeRoomDialogListener {

    String option;
    TextView roomNumberTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_home);

        final ImageAdapter imgAdapter = new ImageAdapter(this);
        final GridView gridView = (GridView) findViewById(R.id.guestgridview);
        View passwordIconView = findViewById(R.id.passwordChangeIcon);
        View logoutView = findViewById(R.id.logoutIcon);

        roomNumberTextView = (TextView)findViewById(R.id.room_number_display);
        String roomNumber = getSharedPreferences("roomSettings", MODE_PRIVATE)
                .getString("roomNumber", null);

        setGuestId(roomNumber);

        //set room number text
        roomNumberTextView.setText("RoomNumber:" + roomNumber);

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

        //logout guest
        logoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment logoutDialogFragment = new LogoutDialogFragment();
                logoutDialogFragment.show(getFragmentManager(), "logout");

            }
        });
    }

    /*Actions for each individual feature on the grid */
    public void createAction() {
        Intent intent;
        switch(option)
        {
            case "MY REQUESTS":
                intent = new Intent(this, GuestPermintaanActivity.class);
                startActivity(intent);
                break;
            case "RESTAURANT":
                intent = new Intent(this, RestaurantActivity.class);
                startActivity(intent);
                break;
            case "TRANSPORT":
                intent = new Intent(this, TransportActivity.class);
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
            startCheckout("Bellboy on the way!");
        }
    }

    /*
    * Negative click
    */
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
        if (option == "CHECKOUT") {
            startCheckout("Report to the frontdesk!");
        }
    }

    /*
    * Positive click for change room number dialog
    */
    @Override
    public void onChangeRoomDialogPositiveClick(DialogFragment dialog) {
        dialog.dismiss();
        roomNumberTextView.setText("RoomNumber:" + ((ChangeRoomNumberDialogFragment) dialog).getUpdatedRoomNumberText());
        Toast.makeText(getBaseContext(), ""+ "Room number successfully changed!", Toast.LENGTH_LONG).show();
    }

    /*
    * Negative click for change room number dialog
    */
    @Override
    public void onChangeRoomDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();

    }

    /*
     * Show checkout fragment
     */
    public void startCheckout(String bellboy) {
        Intent intent = new Intent(this, SurveyActivity.class);
        intent.putExtra("Bellboy",bellboy);
        startActivity(intent);
    }



    /*
    * Set guest id on shared preferences
    */
    public void setGuestId(String roomNumber)
    {
        GuestServer.getInstance(getBaseContext()).getGuestInRoom(
                roomNumber).subscribe(new Observer<Guest>() {
            @Override
            public void onCompleted() {
                Log.d("Completed", "On completed");

            }
            @Override public void onError(Throwable e) {
                Log.d(CheckGuestInFragment.class.getCanonicalName(), "On error");
                e.printStackTrace();
            }
            @Override public void onNext(Guest result) {
                //stroe the guest id in shared preferences
                SharedPreferences pref = getSharedPreferences("userSettings",
                        MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                if (result == null) {
                    editor.putString("guestId", null);
                }
                else {
                    editor.putString("guestId", result._id);
                }

                editor.commit();
                Log.d("Next", "On next");

            }
        });
    }

}

