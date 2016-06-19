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
 * Use the {@link CheckGuestInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckGuestInFragment extends Fragment {

    private Date checkOutDate;

    public CheckGuestInFragment() {
    }

    /**
     * @return A new instance of fragment CheckGuestInFragment.
     */
    public static CheckGuestInFragment newInstance() {
        return new CheckGuestInFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_check_guest_in, container, false);
        final Spinner spinner = (Spinner) view.findViewById(R.id.guest_spinner);
        final EditText editDateCheckOut = (EditText) view.findViewById(R.id.guest_date_check_out);

        DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                checkOutDate = newDate.getTime();
                editDateCheckOut.setText(newDate.getTime().toString());
            }
        };
        Calendar newCalendar = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = new DatePickerDialog(
                this.getActivity(),
                dateListener,
                newCalendar.get(Calendar.YEAR),
                newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));

        editDateCheckOut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        final List<String> roomNumbers = getRoomNumbersWithoutGuests();

        ArrayAdapter adapter = new ArrayAdapter(getActivity().getBaseContext(),
                R.layout.support_simple_spinner_dropdown_item, roomNumbers);
        roomNumbers.add(0, getString(R.string.room_select));
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        adapter.notifyDataSetChanged();
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.check_guest_in_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editTextFirstName = (EditText) getView().findViewById(R.id.guest_first_name);
                String firstName = editTextFirstName.getText().toString();

                EditText editTextLastName = (EditText) getView().findViewById(R.id.guest_last_name);
                String lastName = editTextLastName.getText().toString();

                EditText editTextPhoneNumber = (EditText) getView().findViewById(R.id.guest_phone);
                String phoneNumber = editTextPhoneNumber.getText().toString();

                EditText editTextEmail = (EditText) getView().findViewById(R.id.guest_email);
                String email = editTextEmail.getText().toString();

                String roomNumber = roomNumbers.get((int)spinner.getSelectedItemId()).toString();

                // TODO this needs to be input by staff
                String welcome = "Welcome to Indoluxe Hotel!";

                sendGuestRequest(firstName, lastName, phoneNumber, email, roomNumber, checkOutDate,
                        welcome);
            }
        });
        return view;
    }

    /**
     * @return A list of room number strings without guests currently checked in.
     */
    private List<String> getRoomNumbersWithoutGuests() {
        final List<String> roomStrings = new ArrayList<String>();
        // TODO is this the correct call to getRoomNumbersWithoutGuests?
        GuestServer.getInstance(getActivity().getBaseContext()).getRoomNumbersWithoutGuests()
                .subscribe(new Observer<Room>() {
            @Override public void onCompleted() {
            }
            @Override public void onError(Throwable e) {
                Log.v(CheckGuestInFragment.class.getCanonicalName(),  "getRoomNumbersWithoutGuests() onError");
                e.printStackTrace();
            }
            @Override public void onNext(Room room) {
                roomStrings.add(room.number);
                Log.v(CheckGuestInFragment.class.getCanonicalName(), "getRoomNumbersWithoutGuests() onNext");
            }
        });
        return roomStrings;
    }

    private void sendGuestRequest(String firstName, String lastName, String phoneNumber,
                                  String email, String roomNumber, Date checkOutDate, String welcome) {
        Calendar c = Calendar.getInstance();
        Date currentDate = c.getTime();
        String welcomeMessage = "Hi " + firstName + "!";

        GuestServer.getInstance(getActivity().getBaseContext()).createGuest(new Guest(
                firstName,
                lastName,
                phoneNumber,
                email,
                currentDate,
                checkOutDate,
                roomNumber,
                welcome)
        ).subscribe(new Observer<Guest>() {
            @Override public void onCompleted() {
                Log.d(CheckGuestInFragment.class.getCanonicalName(), "createGuest() On completed");
            }
            @Override public void onError(Throwable e) {
                Log.d(CheckGuestInFragment.class.getCanonicalName(), "On error");
                e.printStackTrace();
            }
            @Override public void onNext(Guest guest) {
                Log.v(CheckGuestInFragment.class.getCanonicalName(), "createGuest() " + guest.toString());
                if (guest != null) {
                    Toast.makeText(
                            getActivity(),
                            R.string.guest_checkin_message,
                            Toast.LENGTH_LONG
                    ).show();
                } else {
                    Toast.makeText(
                            getActivity(),
                            R.string.something_went_wrong,
                            Toast.LENGTH_LONG
                    ).show();
                }
            }
        });
    }
}
