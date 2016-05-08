package com.martabak.kamar.activity;


import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.Room;
import com.martabak.kamar.service.GuestServer;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckGuestOutFragment extends Fragment {

    String roomNumber;

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

        View view =  inflater.inflate(R.layout.fragment_check_guest_out, container, false);

        final Spinner spinner = (Spinner) view.findViewById(R.id.guest_spinner);

        final List<String> roomNumbers = getRoomNumbersWithoutGuests();

        ArrayAdapter adapter = new ArrayAdapter(getActivity().getBaseContext(),
                R.layout.support_simple_spinner_dropdown_item, roomNumbers);
        roomNumbers.add(0, "Please Select a room");
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        adapter.notifyDataSetChanged();
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.check_guest_out_btn);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                roomNumber = roomNumbers.get((int)spinner.getSelectedItemId()).toString();
            }

        });

        return inflater.inflate(R.layout.fragment_check_guest_out, container, false);
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

}
