package com.martabak.kamar.activity.staff;


import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.Guest;
import com.martabak.kamar.domain.Room;
import com.martabak.kamar.service.GuestServer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.Observer;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckGuestOutFragment extends Fragment  {

    private String roomNumber;
    private Guest guest;
    private ArrayAdapter rooms;
    private TextView guestInfoText;
    private FloatingActionButton fab;

    public CheckGuestOutFragment() {
    }

    public static CheckGuestOutFragment newInstance() {
        return new CheckGuestOutFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View parentView =  inflater.inflate(R.layout.fragment_check_guest_out, container, false);
        guestInfoText = (TextView)parentView.findViewById(R.id.guest_info);
        final Spinner spinner = (Spinner) parentView.findViewById(R.id.guest_spinner_checkout);
        final List<String> roomNumbers = getRoomNumbersWithoutGuests();
        fab = (FloatingActionButton) parentView.findViewById(R.id.check_guest_out_btn);

        rooms = new ArrayAdapter(getActivity().getBaseContext(),
                R.layout.support_simple_spinner_dropdown_item, roomNumbers);
        roomNumbers.add(0, getString(R.string.room_select));
        rooms.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(rooms);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    roomNumber = roomNumbers.get(position);
                    Log.v(CheckGuestOutFragment.class.getCanonicalName(), "Room number selected is " + roomNumber);
                    getGuestInRoomNumber(roomNumber);
                    fab.setEnabled(true);
                } else {
                    fab.setEnabled(false);
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {
                fab.setEnabled(false);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (guest != null) {
                    if (checkGuestOut(guest)) {
                        Toast.makeText(getActivity(), getString(R.string.guest_checkout_message), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        return parentView;
    }

    /**
     * @return A list of Room Numbers with no guests checked in.
     */
    private List<String> getRoomNumbersWithoutGuests() {
        final List <String> roomStrings = new ArrayList<String>();
        // TODO double check that this is the correct method to call
        GuestServer.getInstance(getActivity().getBaseContext()).getRoomNumbersWithGuests().subscribe(new Observer<Room>() {
            @Override public void onCompleted() {
                rooms.notifyDataSetChanged();
            }
            @Override public void onError(Throwable e) {
                Log.v(CheckGuestOutFragment.class.getCanonicalName(), "getRoomNumbersWithoutGuests() Error");
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

    /**
     * Check the guest out.
     */
    private boolean checkGuestOut(Guest guest) {
        Calendar c = Calendar.getInstance();
        Date currentDate = c.getTime();
        Guest updateGuest;

        final List<Boolean> toReturn = new ArrayList<>(1);
        updateGuest = new Guest(guest._id, guest._rev, guest.firstName, guest.lastName,
                guest.phone, guest.email, guest.checkIn, currentDate, guest.roomNumber,
                guest.welcomeMessage);
        GuestServer.getInstance(getActivity().getBaseContext()).updateGuest(updateGuest)
                .subscribe(new Observer<Boolean>() {
                    @Override public void onCompleted() {
                        rooms.notifyDataSetChanged();
                        Log.d(CheckGuestInFragment.class.getCanonicalName(), "updateGuest() On completed");
                    }
                    @Override public void onError(Throwable e) {
                        Log.d(CheckGuestInFragment.class.getCanonicalName(), "updateGuest() On error");
                        e.printStackTrace();
                    }
                    @Override public void onNext(Boolean result) {
                        Log.v(CheckGuestInFragment.class.getCanonicalName(), "updateGuest() On next " + result);
                        toReturn.add(result);
                    }
                });
        return toReturn.get(0);
    }

}
