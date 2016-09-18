package com.martabak.kamar.activity.guest;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.domain.permintaan.Transport;
import com.martabak.kamar.service.PermintaanServer;

import java.util.Calendar;
import java.util.Date;

import rx.Observer;

public class TransportActivity extends AppCompatActivity implements TextWatcher {

    private EditText editTransportDestination;
    private EditText editTransportPassengers;
    private EditText editTransportMessage;
    private String departureIn;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport);

        // Get the ActionBar here to configure the way it behaves.
        Toolbar toolbar = (Toolbar) findViewById(R.id.guest_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });

        // Set room number text.
        TextView roomNumberTextView = (TextView)findViewById(R.id.toolbar_roomnumber);
        String roomNumber = getSharedPreferences("userSettings", MODE_PRIVATE)
                .getString("roomNumber", "none");
        roomNumberTextView.setText(getString(R.string.room_number) + ": " + roomNumber);

        editTransportDestination = (EditText) findViewById(R.id.transport_destination_edit_text);
        editTransportPassengers = (EditText) findViewById(R.id.transport_passengers_edit_text);
        editTransportMessage = (EditText) findViewById(R.id.transport_message_edit_text);
        editTransportDestination.addTextChangedListener(this);
        editTransportPassengers.addTextChangedListener(this);
        editTransportMessage.addTextChangedListener(this);

        Spinner spinner = (Spinner) findViewById(R.id.transport_departure_spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.departure_increments_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                departureIn = (String)parent.getItemAtPosition(position);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        submitButton = (Button) findViewById(R.id.transport_submit);
        if (submitButton != null) {
            submitButton.setEnabled(false);
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String destination = editTransportDestination.getText().toString();
                    int passengers = Integer.parseInt(editTransportPassengers.getText().toString());
                    String message = editTransportMessage.getText().toString();
                    sendTransportRequest(destination, passengers, departureIn, message);
                }
            });
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        Log.d(TransportActivity.class.getCanonicalName(), "Validating form");
        validate();
    }

    /**
     * Validate the fields.
     */
    private void validate() {
        boolean invalid = false;
        if (editTransportDestination.getText().toString().trim().equalsIgnoreCase("")) {
            Log.d(TransportActivity.class.getCanonicalName(), "Transport field empty");
            invalid = true;
            editTransportDestination.setError(getString(R.string.required));
        }
        try {
            String passengers = editTransportPassengers.getText().toString().trim();
            if (passengers.equalsIgnoreCase("")) {
                Log.d(TransportActivity.class.getCanonicalName(), "Passenger field empty");
                invalid = true;
                editTransportPassengers.setError(getString(R.string.required));
            } else {
                if (Integer.parseInt(passengers) <= 0) {
                    Log.d(TransportActivity.class.getCanonicalName(), "Passenger number not greater than 0");
                    invalid = true;
                    editTransportPassengers.setError(getString(R.string.required));
                }
            }
        } catch (NumberFormatException e) {
            Log.d(TransportActivity.class.getCanonicalName(), "Number format exception");
            invalid = true;
            editTransportPassengers.setError(getString(R.string.required));
        }
        Log.d(TransportActivity.class.getCanonicalName(), "Setting submit button to " + !invalid);
        submitButton.setEnabled(!invalid);
    }

    /**
     * Send a transport request.
     */
    private void sendTransportRequest(String destination, int passengers, String departureIn,
                                      String message) {
        Transport transport = new Transport(message, passengers,
                departureIn, destination);
        Log.d(TransportActivity.class.getCanonicalName(), "sendTransportRequest() to " +
                destination + " in " + departureIn + " for " + passengers + " passengers with message " + message);

        String owner = Permintaan.OWNER_FRONTDESK;
        String type = Permintaan.TYPE_TRANSPORT;
        String roomNumber = getSharedPreferences("userSettings", MODE_PRIVATE)
                .getString("roomNumber", "none");
        String guestId = getSharedPreferences("userSettings", MODE_PRIVATE)
                .getString("guestId", "none");
        String state = Permintaan.STATE_NEW;
        Date currentDate = Calendar.getInstance().getTime();

        if (!guestId.equals("none") && !roomNumber.equals("none")) {
            PermintaanServer.getInstance(this.getBaseContext()).createPermintaan(new Permintaan(
                    owner,
                    type,
                    roomNumber,
                    guestId,
                    state,
                    currentDate,
                    transport)
            ).subscribe(new Observer<Permintaan>() {
                @Override public void onCompleted() {
                    Log.d(TransportActivity.class.getCanonicalName(), "createPermintaan() On completed");
                    finish();
                }
                @Override public void onError(Throwable e) {
                    Log.d(TransportActivity.class.getCanonicalName(), "createPermintaan() On error");
                    e.printStackTrace();
                    Toast.makeText(
                            TransportActivity.this,
                            getString(R.string.something_went_wrong),
                            Toast.LENGTH_LONG
                    ).show();
                    finish();
                }
                @Override public void onNext(Permintaan permintaan) {
                    Log.d(TransportActivity.class.getCanonicalName(), "createPermintaan() On next" + permintaan);
                    if (permintaan != null) {
                        Toast.makeText(
                                TransportActivity.this,
                                getString(R.string.transport_result),
                                Toast.LENGTH_LONG
                        ).show();
                    } else {
                        Toast.makeText(
                                TransportActivity.this,
                                getString(R.string.something_went_wrong),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
            });
        }
        else {
            Log.d(TransportActivity.class.getCanonicalName(), "Guest ID is none");
            Toast.makeText(
                    TransportActivity.this,
                    getString(R.string.something_went_wrong),
                    Toast.LENGTH_LONG
            ).show();
            finish();
        }

    }

}
