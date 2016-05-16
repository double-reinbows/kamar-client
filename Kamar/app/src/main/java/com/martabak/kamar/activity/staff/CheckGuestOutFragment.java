package com.martabak.kamar.activity.staff;


import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

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
public class CheckGuestOutFragment extends Fragment {

    String roomNumber;
    Guest guest;
    ArrayAdapter adapter;
    TextView tv;

    public CheckGuestOutFragment() {
        // Required empty public constructor
    }

    public static CheckGuestOutFragment newInstance() {
        CheckGuestOutFragment fragment = new CheckGuestOutFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View parentView =  inflater.inflate(R.layout.fragment_check_guest_out, container, false);

        final Spinner spinner = (Spinner) parentView.findViewById(R.id.guest_spinner_checkout);

        final List<String> roomNumbers = getRoomNumbersWithoutGuests();

        adapter = new ArrayAdapter(getActivity().getBaseContext(),
                R.layout.support_simple_spinner_dropdown_item, roomNumbers);
        roomNumbers.add(0, "Please Select a room");
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

        tv = (TextView)parentView.findViewById(R.id.guest_info);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    roomNumber = roomNumbers.get(position);
                    Log.v("RoomNumber is", roomNumber);
                    setGuest();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) parentView.findViewById(R.id.check_guest_out_btn);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                updateGuest();
            }

        });

        return parentView;
    }

    /*
    * return a list of Room Numbers with no guests checked in
    */
    private List<String> getRoomNumbersWithoutGuests() {

        final List <String> roomStrings = new ArrayList<String>();
        GuestServer.getInstance(getActivity().getBaseContext()).
                getRoomNumbers().subscribe(new Observer<List<Room>>() {
            @Override
            public void onCompleted() {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {
                Log.v("OnError",  "Error");
                e.printStackTrace();
            }

            @Override
            public void onNext(List<Room> rooms) {
                for (int i=0; i < rooms.size(); i++) {
                    roomStrings.add(rooms.get(i).number);
                    Log.v("Room", roomStrings.get(i));
                }
                Log.v("onNext", "Next");

            }
        });

        return roomStrings;
    }

    /*
     * set guest
     */
    private void setGuest(){
        GuestServer.getInstance(getActivity().getBaseContext()).getGuestInRoom(
                roomNumber).subscribe(new Observer<Guest>() {
            @Override
            public void onCompleted() {
                if (guest != null) {
                    tv.setText(guest.firstName + " " + guest.lastName);
                    Log.v("CompletedGuest", guest._id);
                }
                Log.d("Completed", "On completed");

            }
            @Override
            public void onError(Throwable e) {
                Log.d(CheckGuestInFragment.class.getCanonicalName(), "On error");

                e.printStackTrace();
            }
            @Override
            public void onNext(Guest result) {
                guest = result;
                Log.d("Next", "On next");
            }
        });
    }


    /*
     * update the guest with current checkout date
     */
    private boolean updateGuest() {


        Calendar c = Calendar.getInstance();
        Date currentDate = c.getTime();
        Guest updateGuest;

        if (guest != null) {
            updateGuest = new Guest(guest._id, guest._rev, guest.firstName, guest.lastName, guest.phone, guest.email, guest.checkIn,
                    currentDate, guest.roomNumber, guest.welcomeMessage);


            GuestServer.getInstance(getActivity().getBaseContext()).updateGuest(updateGuest)
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onCompleted() {
                            adapter.notifyDataSetChanged();
                            Log.d("Completed", "On completed");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d("Error", "On error");
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(Boolean result) {
                            if (result == true) {
                                Log.v("Next", "On next");
                            }
                        }
                    });
            return true;
        }
        return false;



    }

}
