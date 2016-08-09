package com.martabak.kamar.activity.staff;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
 * Use the {@link CheckGuestInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckGuestInFragment extends Fragment implements TextWatcher, AdapterView.OnItemSelectedListener {

    private EditText editFirstName;
    private EditText editLastName;
    private EditText editEmail;
    private EditText editPhoneNumber;
    //private Date checkOutDate;
    //private EditText editDateCheckOut;
    private Spinner spinnerRoomNumber;
    private EditText editWelcomeMessage;
    private Button submitButton;

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

        editFirstName = (EditText) view.findViewById(R.id.guest_first_name);
        editLastName = (EditText) view.findViewById(R.id.guest_last_name);
        editPhoneNumber = (EditText) view.findViewById(R.id.guest_phone);
        editEmail = (EditText) view.findViewById(R.id.guest_email);
        spinnerRoomNumber = (Spinner) view.findViewById(R.id.guest_spinner);
        //editDateCheckOut = (EditText) view.findViewById(R.id.guest_date_check_out);
        editWelcomeMessage = (EditText) view.findViewById(R.id.guest_welcome_message);
        editFirstName.addTextChangedListener(this);
        editLastName.addTextChangedListener(this);
        editPhoneNumber.addTextChangedListener(this);
        //editDateCheckOut.addTextChangedListener(this);
/*
        DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                checkOutDate = newDate.getTime();
                editDateCheckOut.setText(newDate.getTime().toString());
                validate();
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
*/
        final List<String> roomNumbers = getRoomNumbersWithoutGuests();
        ArrayAdapter adapter = new ArrayAdapter(getActivity().getBaseContext(),
                R.layout.support_simple_spinner_dropdown_item, roomNumbers);
        roomNumbers.add(0, getString(R.string.room_select));
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        adapter.notifyDataSetChanged();
        spinnerRoomNumber.setAdapter(adapter);
        spinnerRoomNumber.setSelection(0);
        spinnerRoomNumber.setOnItemSelectedListener(this);

        submitButton = (Button) view.findViewById(R.id.check_guest_in_submit);
        submitButton.setEnabled(false);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = editFirstName.getText().toString();
                String lastName = editLastName.getText().toString();
                String phoneNumber = editPhoneNumber.getText().toString();
                String email = editEmail.getText().toString();
                String roomNumber = roomNumbers.get((int)spinnerRoomNumber.getSelectedItemId()).toString();
                String welcome = editWelcomeMessage.getText().toString();
                sendCreateGuestRequest(firstName, lastName, phoneNumber, email, roomNumber,
                        null, welcome);
            }
        });
        return view;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        Log.d(CheckGuestInFragment.class.getCanonicalName(), "Validating form");
        validate();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(CheckGuestInFragment.class.getCanonicalName(), "Validating form");
        validate();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d(CheckGuestInFragment.class.getCanonicalName(), "Validating form");
        validate();
    }

    /**
     * Validate the fields.
     */
    private void validate() {
        boolean invalid = false;
        if (editFirstName.getText().toString().trim().equalsIgnoreCase("")) {
            Log.d(CheckGuestInFragment.class.getCanonicalName(), "First name field empty");
            invalid = true;
            editFirstName.setError(getString(R.string.required));
        }
        if (editLastName.getText().toString().trim().equalsIgnoreCase("")) {
            Log.d(CheckGuestInFragment.class.getCanonicalName(), "Last name field empty");
            invalid = true;
            editLastName.setError(getString(R.string.required));
        }
        if (editPhoneNumber.getText().toString().trim().equalsIgnoreCase("")) {
            Log.d(CheckGuestInFragment.class.getCanonicalName(), "Phone number field empty");
            invalid = true;
            editPhoneNumber.setError(getString(R.string.required));
        }
        /*
        if (editDateCheckOut.getText().toString().trim().equalsIgnoreCase("")) {
            Log.d(CheckGuestInFragment.class.getCanonicalName(), "Check out date field empty");
            invalid = true;
            editDateCheckOut.setError(getString(R.string.required));
        } else {
            editDateCheckOut.setError(null);
        }
        */
        if ((int)spinnerRoomNumber.getSelectedItemId() <= 0) {
            Log.d(CheckGuestInFragment.class.getCanonicalName(), "Valid room number required");
            invalid = true;
            TextView spinnerErrorText = (TextView)spinnerRoomNumber.getSelectedView();
            spinnerErrorText.setError("");
            spinnerErrorText.setTextColor(Color.RED);
            spinnerErrorText.setText(getString(R.string.required));
        }
        Log.d(CheckGuestInFragment.class.getCanonicalName(), "Setting submit button to " + !invalid);
        submitButton.setEnabled(!invalid);
    }

    /**
     * @return A list of room number strings without guests currently checked in.
     */
    private List<String> getRoomNumbersWithoutGuests() {
        final List<String> roomStrings = new ArrayList<String>();
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

    /**
     * Send a request to create a new guest, given their details.
     * @param firstName Guest's first name.
     * @param lastName Guest's last name.
     * @param phoneNumber Guest's phone number.
     * @param email Guest's email.
     * @param roomNumber Guest's room number.
     * @param checkOutDate Guest's expected check-out date.
     * @param welcome A welcome message for the guest.
     */
    public void sendCreateGuestRequest(String firstName, String lastName, String phoneNumber,
                                        String email, String roomNumber, Date checkOutDate, String welcome) {
        Calendar c = Calendar.getInstance();
        Date currentDate = c.getTime();

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
                Toast.makeText(
                        getActivity(),
                        R.string.something_went_wrong,
                        Toast.LENGTH_LONG
                ).show();
            }
            @Override public void onNext(Guest guest) {
                Log.v(CheckGuestInFragment.class.getCanonicalName(), "createGuest() " + guest.toString());
                if (guest != null) {
                    Toast.makeText(
                            getActivity(),
                            R.string.guest_checkin_message,
                            Toast.LENGTH_LONG
                    ).show();
                    startActivity(new Intent(getActivity(), StaffHomeActivity.class));
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
