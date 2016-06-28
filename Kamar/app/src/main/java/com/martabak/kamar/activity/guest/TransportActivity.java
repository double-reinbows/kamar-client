package com.martabak.kamar.activity.guest;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.domain.permintaan.Transport;
import com.martabak.kamar.service.PermintaanServer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import rx.Observer;

public class TransportActivity extends AppCompatActivity {

    private static final SimpleDateFormat WITHOUT_HHMM_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat HHMM_DATE_FORMAT = new SimpleDateFormat("HH:mm");
    private String transportDestination;
    private Integer transportPassengers;
    private String transportMessage;
    private Date transportDepartureDate;
    private Calendar transportDepartureCal;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timerPickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport);

        // Get the ActionBar here to configure the way it behaves.
        final ActionBar ab = getSupportActionBar();

        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false);

        ab.setCustomView(R.layout.actionbar_guestcustom_view);

        Button submitButton = (Button) findViewById(R.id.submit);

        TextView roomNumberTextView = (TextView)findViewById(R.id.toolbar_roomnumber);
        String roomNumber = getSharedPreferences("userSettings", MODE_PRIVATE)
                .getString("roomNumber", "none");

        // set room number text
        roomNumberTextView.setText(getString(R.string.room_number) + ": " + roomNumber);

        final EditText editTransportDepartureDate = (EditText)
                findViewById(R.id.transport_depature_date_edit_text);
        editTransportDepartureDate.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        final EditText editTransportDepartureTime = (EditText)
                findViewById(R.id.transport_depature_time_edit_text);
        editTransportDepartureTime.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                timerPickerDialog.show();
            }
        });

        transportDepartureCal = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                transportDepartureCal.set(year, monthOfYear, dayOfMonth);
                transportDepartureDate = transportDepartureCal.getTime();
                editTransportDepartureDate.setText(WITHOUT_HHMM_DATE_FORMAT.format(transportDepartureCal.getTime()));
            }
        }, transportDepartureCal.get(Calendar.YEAR), transportDepartureCal.get(Calendar.MONTH), transportDepartureCal.get(Calendar.DAY_OF_MONTH));
        timerPickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                transportDepartureCal.set(Calendar.HOUR, hour);
                transportDepartureCal.set(Calendar.MINUTE, minute);
                transportDepartureDate = transportDepartureCal.getTime();
                editTransportDepartureTime.setText(HHMM_DATE_FORMAT.format(transportDepartureCal.getTime()));
            }
        }, transportDepartureCal.get(Calendar.HOUR), transportDepartureCal.get(Calendar.MINUTE), true);

        if (submitButton != null) {
            submitButton.setOnClickListener(new View.OnClickListener() {
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

}
