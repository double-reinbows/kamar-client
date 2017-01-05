package com.martabak.kamar.activity.staff;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
    private Button submitButton;

    public CreatePermintaanFragment() {
    }

    public static CreatePermintaanFragment newInstance() {
        return new CreatePermintaanFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View parentView = inflater.inflate(R.layout.fragment_check_guest_out, container, false);
        guestInfoText = (TextView) parentView.findViewById(R.id.guest_info);
        guestSpinner = (Spinner) parentView.findViewById(R.id.guest_spinner_checkout);
        roomNumbers = getRoomNumbersWithGuests();
        permintaanOptions = getPermintaanOptions();
        submitButton = (Button) parentView.findViewById(R.id.check_guest_out_submit);

        setupGuestSpinner();
        setupPermintaanSpinner();
        setupButtons(parentView.getContext());

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
        guestSpinner.setAdapter(permintaans);
        guestSpinner.setSelection(0);
        guestSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    permintaan = permintaanOptions.get(position);
                    Log.v(CreatePermintaanFragment.class.getCanonicalName(), "Permintaan selected is " + permintaan);
                    getGuestInRoomNumber(permintaan);
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
        });
    }

    private void validate() {
        if (roomNumber != null && permintaan != null) {
            submitButton.setEnabled(true);
        } else {
            submitButton.setEnabled(false);
        }
    }

    private void launchPorterDialog() {
        // TODO launch the
    }

    private void launchActivity(Class activity, Context context) {
        startActivity(new Intent(context, activity));
    }

    /**
     * @return A list of permintaan options that the staff may select.
     */
    private List<String> getPermintaanOptions() {
        return Arrays.asList(
                Permintaan.TYPE_BELLBOY,
                Permintaan.TYPE_ENGINEERING,
                Permintaan.TYPE_HOUSEKEEPING,
                Permintaan.TYPE_LAUNDRY,
                Permintaan.TYPE_MASSAGE,
                Permintaan.TYPE_RESTAURANT
        );
    }

    /**
     * @return A list of room numbers with guests checked in.
     */
    private List<String> getRoomNumbersWithGuests() {
        final List <String> roomStrings = new ArrayList<String>();
        GuestServer.getInstance(getActivity().getBaseContext()).getRoomNumbersWithGuests().subscribe(new Observer<Room>() {
            @Override public void onCompleted() {
                rooms.notifyDataSetChanged();
            }
            @Override public void onError(Throwable e) {
                Log.v(CreatePermintaanFragment.class.getCanonicalName(), "getRoomNumbersWithoutGuests() Error");
                e.printStackTrace();
            }
            @Override public void onNext(Room room) {
                if (room != null) {
                    roomStrings.add(room.number);
                    Log.v(CheckGuestInFragment.class.getCanonicalName(), "Found room: " + room.number);
                }
            }
        });
        return roomStrings;
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
                Log.d(CheckGuestInFragment.class.getCanonicalName(), "On completed");
            }
            @Override public void onError(Throwable e) {
                Log.d(CheckGuestInFragment.class.getCanonicalName(), "On error");
                e.printStackTrace();
            }
            @Override public void onNext(Guest result) {
                guest = result;
                Log.d(CheckGuestInFragment.class.getCanonicalName(), "On next guest " + result);
            }
        });
    }


}
