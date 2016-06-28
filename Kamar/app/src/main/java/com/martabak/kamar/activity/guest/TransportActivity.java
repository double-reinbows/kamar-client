package com.martabak.kamar.activity.guest;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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

public class TransportActivity extends AppCompatActivity {

    private String departureIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport);

        // Get the ActionBar here to configure the way it behaves.
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false);
        ab.setCustomView(R.layout.actionbar_guestcustom_view);
        // Set room number text.
        TextView roomNumberTextView = (TextView)findViewById(R.id.toolbar_roomnumber);
        String roomNumber = getSharedPreferences("userSettings", MODE_PRIVATE)
                .getString("roomNumber", "none");
        roomNumberTextView.setText(getString(R.string.room_number) + ": " + roomNumber);

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

        Button submitButton = (Button) findViewById(R.id.transport_submit);
        if (submitButton != null) {
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText editTransportDestination = (EditText)
                            findViewById(R.id.transport_destination_edit_text);
                    String destination = editTransportDestination.getText().toString();

                    EditText editTransportPassengers = (EditText)
                            findViewById(R.id.transport_passengers_edit_text);
                    int passengers = Integer.parseInt(editTransportPassengers.getText().toString());

                    EditText editTransportMessage = (EditText)
                            findViewById(R.id.transport_message_edit_text);
                    String message = editTransportMessage.getText().toString();

                    sendTransportRequest(destination, passengers, departureIn, message);
                }
            });
        }
    }

    /**
     * Send a transport request.
     */
    private void sendTransportRequest(String message, int passengers, String departureIn,
                                      String destination) {
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

        if (guestId != "none") {
            PermintaanServer.getInstance(this.getBaseContext()).createPermintaan(new Permintaan(
                    owner,
                    type,
                    roomNumber,
                    guestId,
                    state,
                    currentDate,
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
