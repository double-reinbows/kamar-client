package com.martabak.kamar.activity.guest;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.domain.permintaan.Transport;
import com.martabak.kamar.service.PermintaanServer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import rx.Observer;

public class TransportActivity extends AppCompatActivity implements View.OnClickListener{

    private String transportDestination;
    private Integer transportPassengers;
    private String transportMessage;
    private Date transportDepartureDate;
    private DatePickerDialog datePickerDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport);
        Toolbar toolbar = (Toolbar) findViewById(R.id.guest_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        TextView roomNumberTextView = (TextView)findViewById(R.id.toolbar_roomnumber);
        String roomNumber = getSharedPreferences("userSettings", MODE_PRIVATE)
                .getString("roomNumber", "none");


        // set room number text
        roomNumberTextView.setText(getString(R.string.room_number) + " " + roomNumber);

        final EditText editTransportDepartureDate = (EditText)
                findViewById(R.id.transport_depature_date_edit_text);
        editTransportDepartureDate.setOnClickListener(this);
        Calendar newCalendar = Calendar.getInstance();


        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                transportDepartureDate = newDate.getTime();
                editTransportDepartureDate.setText(newDate.getTime().toString());
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText editTransportDestination = (EditText)
                            findViewById(R.id.transport_destination_edit_text);
                    transportDestination = editTransportDestination.getText().toString();

                    EditText editTransportPassengers = (EditText)
                            findViewById(R.id.transport_passengers_edit_text);
                    transportPassengers = Integer.parseInt(editTransportPassengers.getText().toString());


                    EditText editTransportMessage = (EditText)
                            findViewById(R.id.transport_message_edit_text);
                    transportMessage = editTransportMessage.getText().toString();

                    sendTransportRequest();
                }
            });
        }

    }

    /**
     * Send a transport request.
     */
    private void sendTransportRequest() {
        Transport transport = new Transport(transportMessage, transportPassengers,
                transportDepartureDate, transportDestination);

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

    @Override
    public void onClick(View view) {
        datePickerDialog.show();
    }

}
