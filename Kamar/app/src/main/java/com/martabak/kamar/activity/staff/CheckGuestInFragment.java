package com.martabak.kamar.activity.staff;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;


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
 * Use the {@link CheckGuestInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckGuestInFragment extends Fragment implements View.OnClickListener {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String roomNumber;
    private DatePickerDialog datePickerDialog;
    private Date checkOutDate;


    public CheckGuestInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment CheckGuestInFragment.
     */
    public static CheckGuestInFragment newInstance() {
        CheckGuestInFragment fragment = new CheckGuestInFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_check_guest_in, container, false);

        final Spinner spinner = (Spinner) view.findViewById(R.id.guest_spinner);

        final EditText editDateCheckOut = (EditText)view.findViewById(R.id.guest_date_check_out);
        editDateCheckOut.setOnClickListener(this);
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this.getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                checkOutDate = newDate.getTime();
                editDateCheckOut.setText(newDate.getTime().toString());

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        final List<String> roomNumbers = getRoomNumbersWithoutGuests();

        
        ArrayAdapter adapter = new ArrayAdapter(getActivity().getBaseContext(),
                R.layout.support_simple_spinner_dropdown_item, roomNumbers);
        roomNumbers.add(0, "Please Select a room");
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        adapter.notifyDataSetChanged();
        spinner.setAdapter(adapter);
        spinner.setSelection(0);


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.check_guest_in_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editTextFirstName = (EditText) getView().findViewById(R.id.guest_first_name);
                firstName = editTextFirstName.getText().toString();

                EditText editTextLastName = (EditText) getView().findViewById(R.id.guest_last_name);
                lastName = editTextLastName.getText().toString();

                EditText editTextPhoneNumber = (EditText) getView().findViewById(R.id.guest_phone);
                phoneNumber = editTextPhoneNumber.getText().toString();

                EditText editTextEmail = (EditText) getView().findViewById(R.id.guest_email);
                email = editTextEmail.getText().toString();

                roomNumber = roomNumbers.get((int)spinner.getSelectedItemId()).toString();

                sendGuestRequest();


                Snackbar.make(view, "Guest successfully checked in!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        return view;
    }

    /*
     * return a list of Room Numbers with no guests checked in
     */
    private List<String> getRoomNumbersWithoutGuests() {

        final List <String> roomStrings = new ArrayList<String>();
        GuestServer.getInstance(getActivity().getBaseContext()).getRoomNumbers()
                .subscribe(new Observer<List<Room>>() {
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

    /*
     * Send create new guest request to the server
     */
    private void sendGuestRequest() {

        Calendar c = Calendar.getInstance();
        Date currentDate = c.getTime();

        Log.v("CheckOutDate", checkOutDate.toString());

        String welcomeMessage = "Hi " + firstName + "!";

        GuestServer.getInstance(getActivity().getBaseContext()).createGuest(new Guest(
                firstName,
                lastName,
                phoneNumber,
                email,
                currentDate,
                checkOutDate,
                roomNumber,
                welcomeMessage)
        ).subscribe(new Observer<Guest>() {
            @Override
            public void onCompleted() {
                Log.d("Completed", "On completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("Error", "On error");
                e.printStackTrace();
            }

            @Override
            public void onNext(Guest guest) {
                Log.d("Next", "On next");
                Log.v("GUEST", guest.toString());
            }

        });


    }

    @Override
    public void onClick(View view) {
        datePickerDialog.show();
    }
}
