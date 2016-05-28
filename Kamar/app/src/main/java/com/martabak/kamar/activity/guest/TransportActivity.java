package com.martabak.kamar.activity.guest;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                EditText editTransportDestination = (EditText)
                        findViewById(R.id.transport_destination_edit_text);
                transportDestination = editTransportDestination.getText().toString();

                EditText editTransportPassengers = (EditText)
                        findViewById(R.id.transport_passengers_edit_text);
                transportPassengers = Integer.parseInt(editTransportPassengers.getText().toString());

                final EditText editTransportDepartureDate = (EditText)
                        findViewById(R.id.transport_depature_date_edit_text);
                editTransportDepartureDate.setOnClickListener(this);

                Calendar newCalendar = Calendar.getInstance();
                datePickerDialog = new DatePickerDialog(getBaseContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        transportDepartureDate = newDate.getTime();
                        editTransportDepartureDate.setText(newDate.getTime().toString());

                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


                EditText editTransportMessage = (EditText)
                        findViewById(R.id.transport_message_edit_text);
                transportMessage = editTransportMessage.getText().toString();

                sendTransportRequest();
            }
        });
    }

    /* Send Transport Request */
    private void sendTransportRequest()
    {

        Transport transport = new Transport(transportMessage, transportPassengers,
                transportDepartureDate, transportMessage);

        String owner = "FRONT DESK";
        String type = "TRANSPORT";
        String roomNumber = getSharedPreferences("roomSettings", MODE_PRIVATE)
                .getString("roomNumber", "none");
        String guestId= getSharedPreferences("userSettings", MODE_PRIVATE)
                .getString("guestId", "none");
        String state = "NEW";

        Calendar c = Calendar.getInstance();
        Date currentDate = c.getTime();

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
            public void onNext(Permintaan permintaan) {
                Log.d("Next", "On next");
            }
        });

    }

    @Override
    public void onClick(View view) {
        datePickerDialog.show();
    }

}
