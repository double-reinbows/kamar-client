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
import com.martabak.kamar.activity.chat.GuestChatService;
import com.martabak.kamar.activity.chat.StaffChatService;
import com.martabak.kamar.activity.restaurant.RestaurantActivity;
import com.martabak.kamar.domain.Guest;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.service.GuestServer;

import rx.Observer;

public class GuestHomeActivity extends AppCompatActivity
        implements BellboyDialogFragment.BellboyDialogListener,
        ChangeRoomNumberDialogFragment.ChangeRoomDialogListener {

    private String option;
    private TextView roomNumberTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_home);

        final ImageAdapter imgAdapter = new ImageAdapter(this);
        final GridView gridView = (GridView) findViewById(R.id.guestgridview);
        View passwordIconView = findViewById(R.id.passwordChangeIcon);
        View logoutView = findViewById(R.id.logoutIcon);

        roomNumberTextView = (TextView)findViewById(R.id.room_number_display);
        String roomNumber = getSharedPreferences("userSettings", MODE_PRIVATE)
                .getString("roomNumber", null);
        setGuestId(roomNumber);

        // Start any guest services.
        startGuestServices(getSharedPreferences("userSettings", MODE_PRIVATE).getString("guestId", "none"));

        // set room number text
        roomNumberTextView.setText(getString(R.string.room_number) + " " + roomNumber);
        gridView.setAdapter(imgAdapter);

        // display feature text on each item click
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // perform action for each individual feature
                option = imgAdapter.getItem(position).toString();
                createAction();
            }
        });

        // open the change room number as a fragment
        if (passwordIconView != null) {
            passwordIconView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment changeRoomNumberFragment = new ChangeRoomNumberDialogFragment();
                    changeRoomNumberFragment.show(getFragmentManager(), "changeRoomNumber");
                }
            });
        }

        // logout guest
        if (logoutView != null) {
            logoutView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment logoutDialogFragment = new LogoutDialogFragment();
                    logoutDialogFragment.show(getFragmentManager(), "logout");
                }
            });
        }
    }

    @Override
    public void onStop() {
        stopGuestServices();
        super.onStop();
    }

    /*
     * Actions for each individual feature on the grid.
     */
    private void createAction() {
        switch(option) {
            case "MY REQUESTS":
                startActivity(new Intent(this, GuestPermintaanActivity.class));
                break;
            case Permintaan.TYPE_RESTAURANT:
                startActivity(new Intent(this, RestaurantActivity.class));
                break;
            case Permintaan.TYPE_TRANSPORT:
                startActivity(new Intent(this, TransportActivity.class));
                break;
            case Permintaan.TYPE_HOUSEKEEPING:
                new HousekeepingDialogFragment().show(getFragmentManager(), "housekeeping");
                break;
            case Permintaan.TYPE_BELLBOY:
                new BellboyDialogFragment().show(getFragmentManager(), "bellboy");
                break;
            case Permintaan.TYPE_MAINTENANCE:
                new MaintenanceDialogFragment().show(getFragmentManager(), "maintenance");
                break;
            case "TELL US":
                new TellUsDialogFragment().show(getFragmentManager(), "tellus");
                break;
            case Permintaan.TYPE_CHECKOUT:
                new BellboyDialogFragment().show(getFragmentManager(), "bellboy");
                break;
            default:
                break;
        }
    }

    /**
     * Positive click.
     */
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        dialog.dismiss();
        if (option == "CHECKOUT") {
            startCheckout(getString(R.string.bellboy_result));
        }
    }

    /**
     * Negative click.
     */
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
        if (option == "CHECKOUT") {
            startCheckout(getString(R.string.report_to_frontdesk));
        }
    }

    /**
     * Positive click for change room number dialog
     * @param dialog The dialog fragment.
     */
    @Override
    public void onChangeRoomDialogPositiveClick(DialogFragment dialog, String roomNumber) {
        dialog.dismiss();
        roomNumberTextView.setText(getString(R.string.room_number) + " " + roomNumber);
        Toast.makeText(
                this,
                getString(R.string.room_number_changed),
                Toast.LENGTH_LONG
        ).show();
    }

    /**
     * Negative click for change room number dialog.
     * @param dialog The dialog fragment.
     */
    @Override
    public void onChangeRoomDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    /**
     * Start the checkout process by prompting the user to enter a survey.
     * @param completionMessage The message to show on completion of the survey.
     */
    private void startCheckout(String completionMessage) {
        Intent intent = new Intent(this, SurveyActivity.class);
        intent.putExtra("completionMessage", completionMessage);
        startActivity(intent);
    }

    /**
     * Set guest id on shared preferences.
     * @param roomNumber The room number.
     */
    private void setGuestId(String roomNumber) {
        GuestServer.getInstance(getBaseContext()).getGuestInRoom(
                roomNumber).subscribe(new Observer<Guest>() {
            @Override public void onCompleted() {
                Log.d(GuestHomeActivity.class.getCanonicalName(), "Completed setting guest ID");
            }
            @Override public void onError(Throwable e) {
                Log.d(GuestHomeActivity.class.getCanonicalName(), "On error");
                e.printStackTrace();
            }
            @Override public void onNext(Guest result) {
                // Store the guest id in shared preferences
                SharedPreferences pref = getSharedPreferences("userSettings", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                if (result == null) {
                    editor.putString("guestId", null);
                }
                else {
                    editor.putString("guestId", result._id);
                }
                editor.commit();
                Log.d(GuestHomeActivity.class.getCanonicalName(), "Setting guest ID to " + result._id);
            }
        });
    }

    /**
     * Start any relevant guest services.
     * @param guestId The guest's ID.
     */
    private void startGuestServices(String guestId) {
        if (!guestId.equals("none")) {
            Log.v(GuestHomeActivity.class.getCanonicalName(), "Starting " + GuestPermintaanService.class.getCanonicalName() + " as " + guestId);
            startService(new Intent(this, GuestPermintaanService.class)
                    .putExtra("guestId", guestId));
            Log.v(GuestHomeActivity.class.getCanonicalName(), "Starting " + GuestChatService.class.getCanonicalName() + " as " + guestId);
            startService(new Intent(this, GuestChatService.class)
                    .putExtra("guestId", guestId));
        }
    }

    /**
     * Stop any relevant guest services.
     */
    private void stopGuestServices() {
        Log.v(GuestHomeActivity.class.getCanonicalName(), "Stopping " + GuestPermintaanService.class.getCanonicalName());
        stopService(new Intent(this, GuestPermintaanService.class));
        Log.v(GuestHomeActivity.class.getCanonicalName(), "Stopping " + GuestChatService.class.getCanonicalName());
        stopService(new Intent(this, GuestChatService.class));
    }

}