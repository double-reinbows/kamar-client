package com.martabak.kamar.activity.guest;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.martabak.kamar.R;
import com.martabak.kamar.activity.chat.GuestChatActivity;
import com.martabak.kamar.activity.chat.GuestChatService;
import com.martabak.kamar.activity.home.SelectLanguageActivity;
import com.martabak.kamar.activity.restaurant.RestaurantActivity;
import com.martabak.kamar.activity.staff.StaffHomeActivity;
import com.martabak.kamar.domain.Guest;
import com.martabak.kamar.domain.User;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.service.GuestServer;
import com.martabak.kamar.activity.staff.CheckGuestInFragment;

import java.util.Calendar;
import java.util.Date;

import rx.Observer;

public class GuestHomeActivity extends AppCompatActivity
        implements
        PermintaanDialogListener,
        ChangeRoomNumberDialogFragment.ChangeRoomDialogListener,
        LogoutDialogFragment.LogoutDialogListener {

    private String option;
    private TextView roomNumberTextView;
    private String welcomeMessage;
    private Guest guest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.guest_toolbar);
        setSupportActionBar(toolbar);

        final ImageAdapter imgAdapter = new ImageAdapter(this);
        final GridView gridView = (GridView) findViewById(R.id.guestgridview);
        //View passwordIconView = findViewById(R.id.passwordChangeIcon);
        View logoutView = findViewById(R.id.logoutIcon);
        guest = new Guest();

        roomNumberTextView = (TextView)findViewById(R.id.toolbar_roomnumber);
        final String roomNumber = getSharedPreferences("userSettings", MODE_PRIVATE)
                .getString("roomNumber", "none");
        //setGuestId(roomNumber);

        // Start any guest services.
        startGuestServices(getSharedPreferences("userSettings", MODE_PRIVATE).getString("guestId", "none"));

        // set room number text
        roomNumberTextView.setText(getString(R.string.room_number) + ": " + roomNumber);
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
/*
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
*/
        // logout guest
        if (logoutView != null) {
            logoutView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment logoutDialogFragment = new LogoutDialogFragment();
                    logoutDialogFragment.show(getFragmentManager(), "logout");

                    /*
                    new AlertDialog.Builder(GuestHomeActivity.this)
                            .setMessage(getString(R.string.logout_options))
                            .setCancelable(false)

                            .setNegativeButton(getString(R.string.logout_change_room_number), new DialogInterface.OnClickListener() {
                                @Override public void onClick(DialogInterface dialog, int which) {
                                    DialogFragment changeRoomNumberFragment = new ChangeRoomNumberDialogFragment();
                                    changeRoomNumberFragment.show(getFragmentManager(), "changeRoomNumber");
                                }
                            })

                            .setNeutralButton(getString(R.string.logout_staff), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    SharedPreferences.Editor editor = GuestHomeActivity.this
                                            .getSharedPreferences("userSettings", MODE_PRIVATE)
                                            .edit();
                                    editor.putString("subUserType", User.TYPE_STAFF_FRONTDESK)
                                            .commit();

                                    //Log.v(SelectUserTypeActivity.class.getCanonicalName(), "userType is " + getActivity().getSharedPreferences("userSettings", MODE_PRIVATE).getString("userType", "none"));
                                    //Log.v(SelectUserTypeActivity.class.getCanonicalName(), "subUserType is " + getActivity().getSharedPreferences("userSettings", MODE_PRIVATE).getString("subUserType", "none"));

                                    startActivity(new Intent(GuestHomeActivity.this, StaffHomeActivity.class));
                                    GuestHomeActivity.this.finish();
                                }
                            })

                            .setPositiveButton(getString(R.string.logout_reset_tablet), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.v("App Action", "Resetting tablet");
                                    checkGuestOut(roomNumber);
                                }
                            })

                    .create().show();

*/
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
            case "CHAT":
                startActivity(new Intent(this, GuestChatActivity.class));
            default:
                break;
        }
    }

    /**
     * Positive click.
     */
    @Override
    public void onDialogPositiveClick(DialogFragment dialog, Boolean success) {
        dialog.dismiss();
        if (success) {
            switch(option) {
                case Permintaan.TYPE_HOUSEKEEPING:
                    Toast.makeText(
                            this,
                            getString(R.string.housekeeping_result),
                            Toast.LENGTH_LONG
                    ).show();
                    break;
                case Permintaan.TYPE_BELLBOY:
                    Toast.makeText(
                            this,
                            getString(R.string.bellboy_result),
                            Toast.LENGTH_LONG
                    ).show();
                    break;
                case Permintaan.TYPE_MAINTENANCE:
                    Toast.makeText(
                            this,
                            getString(R.string.maintenance_result),
                            Toast.LENGTH_LONG
                    ).show();
                    break;
                case "TELL US":
                    Toast.makeText(
                            this,
                            getString(R.string.tellus_result),
                            Toast.LENGTH_LONG
                    ).show();
                    break;
                case Permintaan.TYPE_CHECKOUT:
                    startCheckout(getString(R.string.bellboy_result));
                    break;
                default:
                    break;
            }
        }
        else {
            Toast.makeText(
                    this,
                    getString(R.string.something_went_wrong),
                    Toast.LENGTH_LONG
            ).show();
        }


    }

    /**
     * Negative click.
     */
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
        if (option.equals("CHECKOUT")) {
            startCheckout(getString(R.string.report_to_frontdesk));
        }
    }

    /**
     * Positive click for change room number dialog
     * @param dialog The dialog fragment.
     */
    @Override
    public void onChangeRoomDialogPositiveClick(DialogFragment dialog, String roomNumber, boolean success, String reason) {
        if (success) {
            dialog.dismiss();
            getSharedPreferences("userSettings", MODE_PRIVATE)
                    .edit().putString("roomNumber", roomNumber)
                    .commit();
            Toast.makeText(
                    this,
                    getString(R.string.room_number_changed),
                    Toast.LENGTH_LONG
            ).show();
            roomNumberTextView.setText(getString(R.string.room_number) + " " + roomNumber);

            //String guestId = getSharedPreferences("userSettings", MODE_PRIVATE)
              //      .getString("guestId", "none");

            setGuestId(roomNumber);

        } else {
            Toast.makeText(
                    this,
                    reason,
                    Toast.LENGTH_SHORT
            ).show();
        }
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
     * Positive click for logout dialog
     * @param dialog The dialog fragment.
     * @param success The outcome of the server request.
     */
    @Override
    public void onLogoutDialogPositiveClick(DialogFragment dialog, Boolean success, String reason) {
        dialog.dismiss();
        if (success)
        {
            final String roomNumber = getSharedPreferences("userSettings", MODE_PRIVATE)
                    .getString("roomNumber", "none");
            new AlertDialog.Builder(GuestHomeActivity.this)
                    .setMessage(getString(R.string.logout_options))
                    .setCancelable(true)

                    .setNegativeButton(getString(R.string.logout_change_room_number), new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialog, int which) {
                            DialogFragment changeRoomNumberFragment = new ChangeRoomNumberDialogFragment();
                            changeRoomNumberFragment.show(getFragmentManager(), "changeRoomNumber");
                        }
                    })

                    .setNeutralButton(getString(R.string.logout_staff), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            SharedPreferences.Editor editor = GuestHomeActivity.this
                                    .getSharedPreferences("userSettings", MODE_PRIVATE)
                                    .edit();
                            editor.putString("subUserType", User.TYPE_STAFF_FRONTDESK)
                                    .commit();

                            //Log.v(SelectUserTypeActivity.class.getCanonicalName(), "userType is " + getActivity().getSharedPreferences("userSettings", MODE_PRIVATE).getString("userType", "none"));
                            //Log.v(SelectUserTypeActivity.class.getCanonicalName(), "subUserType is " + getActivity().getSharedPreferences("userSettings", MODE_PRIVATE).getString("subUserType", "none"));

                            startActivity(new Intent(GuestHomeActivity.this, StaffHomeActivity.class));
                            GuestHomeActivity.this.finish();
                        }
                    })

                    .setPositiveButton(getString(R.string.logout_reset_tablet), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.v("App Action", "Resetting tablet");
                            checkGuestOut(roomNumber);
                            GuestHomeActivity.this.getSharedPreferences("userSettings", GuestHomeActivity.this.MODE_PRIVATE)
                                    .edit()
                                    .putString("guestId", "none")
                                    .commit();
                            Toast.makeText(
                                    GuestHomeActivity.this,
                                    getString(R.string.logout_result),
                                    Toast.LENGTH_LONG
                            ).show();
                            Intent intent = new Intent(GuestHomeActivity.this, SelectLanguageActivity.class);
                            startActivity(intent);
                        }
                    })

                    .create().show();
        }
        else {
            Toast.makeText(
                    this,
                    reason,
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    /**
     * Negative click for logout dialog.
     * @param dialog The dialog fragment.
     */
    @Override
    public void onLogoutDialogNegativeClick(DialogFragment dialog) {
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
    private void setGuestId(final String roomNumber) {
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

                Log.v(GuestHomeActivity.class.getCanonicalName(), "Room Number : " + roomNumber);
                if (result == null) {
                    editor.putString("guestId", "none");
                }
                else {
                    editor.putString("guestId", result._id);
                    welcomeMessage = result.welcomeMessage;
                    Log.v(GuestHomeActivity.class
                            .getCanonicalName(), "Setting guest ID to " + result._id);
                }
                Log.v(GuestHomeActivity.class.getCanonicalName(), "Guest ID " + getSharedPreferences("userSettings", MODE_PRIVATE)
                        .getString("guestId", "none"));
                editor.commit();
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

    /**
     * Check the guest out.
     */
    private void checkGuestOut2(Guest guest) {
        Calendar c = Calendar.getInstance();
        Date currentDate = c.getTime();
        //getGuestInRoomNumber(roomNumber); //get Guest from
        Guest updatedGuest;
        //Log.v("roomNo", roomNumber);
        Log.v("Guest", guest._rev);
        updatedGuest = new Guest(guest._id, guest._rev, guest.firstName, guest.lastName,
                guest.phone, guest.email, guest.checkIn, currentDate, guest.roomNumber,
                guest.roomNumber, guest.promoImgId);
        GuestServer.getInstance(GuestHomeActivity.this.getBaseContext()).updateGuest(updatedGuest)
                .subscribe(new Observer<Boolean>() {
                    @Override public void onCompleted() {
                        //rooms.notifyDataSetChanged();
                        Log.d(CheckGuestInFragment.class.getCanonicalName(), "updateGuest() On completed");
                        /*getSharedPreferences("userSettings", MODE_PRIVATE)
                                .edit().putString("roomNumber", "none")
                                .commit();*/
                        }
                    @Override public void onError(Throwable e) {
                        Log.d(CheckGuestInFragment.class.getCanonicalName(), "updateGuest() On error");
                        e.printStackTrace();
                        Toast.makeText(GuestHomeActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                    @Override public void onNext(Boolean result) {
                        Log.v(CheckGuestInFragment.class.getCanonicalName(), "updateGuest() On next " + result);
                        if (result) {
                            Toast.makeText(GuestHomeActivity.this, getString(R.string.guest_checkout_message), Toast.LENGTH_LONG).show();
                            //startActivity(new Intent(GuestHomeActivity.this, StaffHomeActivity.class));
                        } else {
                            Toast.makeText(GuestHomeActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /**
     * Get the guest currently checked in to the room number.
     * @param roomNumber The room number.
     * @return The guest.
     */
    private void checkGuestOut(final String roomNumber) {
        //final Guest guest = null;
        GuestServer.getInstance(GuestHomeActivity.this.getBaseContext()).getGuestInRoom(
                roomNumber).subscribe(new Observer<Guest>() {
            Guest guest = null;
            @Override public void onCompleted() {
                if (guest != null) {
                    checkGuestOut2(guest);
                }
                //Log.d(CheckGuestInFragment.class.getCanonicalName(), "On completed");
            }
            @Override public void onError(Throwable e) {
                //Log.d(CheckGuestInFragment.class.getCanonicalName(), "On error");
                e.printStackTrace();
            }
            @Override public void onNext(Guest result) {
                guest = result;
                //Log.d(CheckGuestInFragment.class.getCanonicalName(), "On next guest " + result);
            }
        });
    }

}