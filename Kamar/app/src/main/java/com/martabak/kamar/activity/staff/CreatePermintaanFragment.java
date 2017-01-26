package com.martabak.kamar.activity.staff;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.martabak.kamar.R;
import com.martabak.kamar.activity.engineering.EngineeringActivity;
import com.martabak.kamar.activity.guest.BellboyDialogFragment;
import com.martabak.kamar.activity.housekeeping.HousekeepingActivity;
import com.martabak.kamar.activity.laundry.LaundryActivity;
import com.martabak.kamar.activity.massage.MassageActivity;
import com.martabak.kamar.activity.restaurant.RestaurantActivity;
import com.martabak.kamar.domain.Guest;
import com.martabak.kamar.domain.Room;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.service.GuestServer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.Observer;

/**
 * Allows staff to create permintaans on behalf of a guest.
 */
public class CreatePermintaanFragment extends Fragment  {

    private Guest guest;
    private String roomNumber;
    private List<String> roomNumbers;
    private ArrayAdapter rooms;
    private String permintaan;
    private List<String> permintaanOptions;
    private ArrayAdapter permintaans;
    private Spinner guestSpinner;
    private TextView guestInfoText;
    private Spinner permintaanSpinner;
    private Button submitButton;

    public CreatePermintaanFragment() {
    }

    public static CreatePermintaanFragment newInstance() {
        return new CreatePermintaanFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        roomNumbers = new ArrayList<>();
        permintaanOptions = new ArrayList<>();

        final View parentView = inflater.inflate(R.layout.fragment_create_permintaan, container, false);
        guestInfoText = (TextView) parentView.findViewById(R.id.guest_info);
        guestSpinner = (Spinner) parentView.findViewById(R.id.guest_spinner);
        permintaanSpinner = (Spinner) parentView.findViewById(R.id.permintaan_spinner);
        submitButton = (Button) parentView.findViewById(R.id.create_permintaan_submit);

        setupGuestSpinner();
        setupPermintaanSpinner();
        setupButtons(parentView.getContext());

        getRoomNumbersWithGuests();
        getPermintaanOptions();

        return parentView;
    }

    private void setupGuestSpinner() {
        rooms = new ArrayAdapter(getActivity().getBaseContext(),
                R.layout.support_simple_spinner_dropdown_item, roomNumbers);
        roomNumbers.add(0, getString(R.string.room_select));
        rooms.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        guestSpinner.setAdapter(rooms);
        guestSpinner.setSelection(0);
        guestSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    roomNumber = roomNumbers.get(position);
                    Log.v(CreatePermintaanFragment.class.getCanonicalName(), "Room number selected is " + roomNumber);
                    getGuestInRoomNumber(roomNumber);
                } else {
                    roomNumber = null;
                }
                validate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                validate();
            }
        });
    }

    private void setupPermintaanSpinner() {
        permintaans = new ArrayAdapter(getActivity().getBaseContext(),
                R.layout.support_simple_spinner_dropdown_item, permintaanOptions);
        permintaanOptions.add(0, getString(R.string.permintaan_select));
        permintaans.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        permintaanSpinner.setAdapter(permintaans);
        permintaanSpinner.setSelection(0);
        permintaanSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    permintaan = permintaanOptions.get(position);
                    Log.v(CreatePermintaanFragment.class.getCanonicalName(), "Permintaan selected is " + permintaan);
                } else {
                    permintaan = null;
                }
                validate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                validate();
            }
        });
    }

    private void setupButtons(final Context context) {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                setRoomNumberAndGuestId(context);
                launchPermintaanView(context);

            }
        });
    }

    private void validate() {
        if (roomNumber != null && permintaan != null) {
            submitButton.setEnabled(true);
        } else {
            submitButton.setEnabled(false);
        }
    }

    private void setRoomNumberAndGuestId(Context context) {
        context.getSharedPreferences("userSettings", Context.MODE_PRIVATE).edit()
                .putString("guestId", guest._id)
                .putString("roomNumber", guest.roomNumber)
                .commit();
        Log.i(CreatePermintaanFragment.class.getCanonicalName(), "Setting userSettings guestId to " + guest._id + " and roomNumber to " + guest.roomNumber);
    }

    private void launchPermintaanView(Context context) {
        switch (permintaan) {
            case Permintaan.TYPE_BELLBOY:
                launchPorterDialog();
                break;
            case Permintaan.TYPE_ENGINEERING:
                launchActivity(EngineeringActivity.class, context);
                break;
            case Permintaan.TYPE_HOUSEKEEPING:
                launchActivity(HousekeepingActivity.class, context);
                break;
            case Permintaan.TYPE_LAUNDRY:
                launchActivity(LaundryActivity.class, context);
                break;
            case Permintaan.TYPE_MASSAGE:
                launchActivity(MassageActivity.class, context);
                break;
            case Permintaan.TYPE_RESTAURANT:
                launchActivity(RestaurantActivity.class, context);
                break;
        }
    }

    private void launchPorterDialog() {
        new BellboyDialogFragment().show(getFragmentManager(), "bellboy");
    }

    private void launchActivity(Class activity, Context context) {
        startActivityForResult(new Intent(context, activity), Permintaan.SUCCESS);
    }

    /**
     * Show Snackbar based on the success of the permintaan
     * @param requestCode
     * @param resultCode
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //successful in creating permintaan
        if (resultCode == Permintaan.SUCCESS) {

            //replace fragment here
            getFragmentManager().beginTransaction()
                    .replace(R.id.staff_container, StaffPermintaanFragment.newInstance())
                    .addToBackStack(null)
                    .commit();

            // create snackbar
            Snackbar.make(this.getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content),
                    R.string.request_success,Snackbar.LENGTH_LONG).
                    setAction(R.string.positive, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getFragmentManager().beginTransaction()
                                    .replace(R.id.staff_container, CreatePermintaanFragment.newInstance())
                                    .addToBackStack(null)
                                    .commit();
                        }
                    }).show();
        }
    }

    /**
     * Gets a list of permintaan options that the staff may select.
     */
    private void getPermintaanOptions() {
        permintaanOptions.addAll(Arrays.asList(
                Permintaan.TYPE_BELLBOY,
                Permintaan.TYPE_ENGINEERING,
                Permintaan.TYPE_HOUSEKEEPING,
                Permintaan.TYPE_LAUNDRY,
                Permintaan.TYPE_MASSAGE,
                Permintaan.TYPE_RESTAURANT
        ));
    }

    /**
     * Gets a list of room numbers with guests checked in.
     */
    private void getRoomNumbersWithGuests() {
        roomNumbers.set(0, getString(R.string.loading));
        guestSpinner.setEnabled(false);
        rooms.notifyDataSetChanged();
        GuestServer.getInstance(getActivity().getBaseContext()).getRoomNumbersWithGuests().subscribe(new Observer<Room>() {
            @Override public void onCompleted() {
                roomNumbers.set(0, getString(R.string.room_select));
                guestSpinner.setEnabled(true);
                rooms.notifyDataSetChanged();
            }
            @Override public void onError(Throwable e) {
                Log.v(CreatePermintaanFragment.class.getCanonicalName(), "getRoomNumbersWithoutGuests() Error");
                e.printStackTrace();
            }
            @Override public void onNext(Room room) {
                if (room != null) {
                    roomNumbers.add(room.number);
                    Log.v(CreatePermintaanFragment.class.getCanonicalName(), "Found room: " + room.number);
                }
            }
        });
    }

    /**
     * Get the guest currently checked in to the room number.
     * @param roomNumber The room number.
     * @return The guest.
     */
    private void getGuestInRoomNumber(String roomNumber) {
        GuestServer.getInstance(getActivity().getBaseContext()).getGuestInRoom(
                roomNumber).subscribe(new Observer<Guest>() {
            @Override public void onCompleted() {
                if (guest != null) {
                    guestInfoText.setText(guest.firstName + " " + guest.lastName);
                }
                Log.d(CreatePermintaanFragment.class.getCanonicalName(), "On completed");
            }
            @Override public void onError(Throwable e) {
                Log.d(CreatePermintaanFragment.class.getCanonicalName(), "On error");
                e.printStackTrace();
            }
            @Override public void onNext(Guest result) {
                guest = result;
                Log.d(CreatePermintaanFragment.class.getCanonicalName(), "On next guest " + result);
            }
        });
    }

}
