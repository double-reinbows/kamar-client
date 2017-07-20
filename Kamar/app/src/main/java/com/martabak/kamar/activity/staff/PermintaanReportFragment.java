package com.martabak.kamar.activity.staff;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.Guest;
import com.martabak.kamar.domain.Room;
import com.martabak.kamar.domain.managers.RoomManager;
import com.martabak.kamar.domain.permintaan.Engineering;
import com.martabak.kamar.domain.permintaan.Housekeeping;
import com.martabak.kamar.domain.permintaan.Massage;
import com.martabak.kamar.domain.permintaan.OrderItem;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.domain.permintaan.RestaurantOrder;
import com.martabak.kamar.domain.permintaan.Transport;
import com.martabak.kamar.service.GuestServer;
import com.martabak.kamar.service.PermintaanServer;
import com.martabak.kamar.util.EmailSender;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import au.com.bytecode.opencsv.CSVWriter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

/**
 * A simple {@link Fragment} subclass.
 */
public class PermintaanReportFragment extends Fragment  {

    private static Integer millisecondsInADay = 24 * 60 * 60 * 1000;
    private long selectedDate;
    Calendar c;

    public PermintaanReportFragment() {
    }

    public static PermintaanReportFragment newInstance() {
        return new PermintaanReportFragment();
    }

    // bind views here
    @BindView(R.id.permintaan_report_calendar) CalendarView calendarView;
    @BindView(R.id.permintaan_report_submit) Button submitButton;

    // on click listener
    @OnClick(R.id.permintaan_report_submit)
    void onSubmit() {
        PermintaanServer.getInstance(getActivity())
                .getPermintaansOfTime(new Date(selectedDate), new Date(selectedDate+millisecondsInADay))
                .subscribe(new Observer<List<Permintaan>>() {
                    List<Permintaan> permintaans = new ArrayList<>();
                    @Override
                    public void onCompleted() {

                        for (Permintaan p: permintaans) {
                            Log.v("HERP", p.created.toString()+" - "+p.roomNumber+" "+p.content.getType());
                            if (p.content.getType().equals(Permintaan.TYPE_RESTAURANT)) {
                                RestaurantOrder restoOrder = (RestaurantOrder) p.content;
                                for (OrderItem o : restoOrder.items) {
                                    Log.v("HERP", o.quantity + " " + o.name + " Rp. " + o.price);
                                }
                            }

                        }

                        buildCSV(permintaans);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onNext(List<Permintaan> result) {
                        permintaans = result;
                    }
                });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View parentView =  inflater.inflate(R.layout.fragment_perminaan_report, container, false);
        ButterKnife.bind(this,parentView);

        c = Calendar.getInstance(TimeZone.getDefault());
        c.setTime(new Date());
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        selectedDate = c.getTimeInMillis() - millisecondsInADay;
        calendarView.setDate(selectedDate);
        calendarView.setMaxDate(selectedDate);
        calendarView.setFirstDayOfWeek(2); // set Monday as the first day of the week

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                c.set(year, month, dayOfMonth);
                Date date = new Date();
                c.set(Calendar.HOUR_OF_DAY, 0);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MILLISECOND, 0);
                selectedDate = c.getTimeInMillis();
            }
        });
        return parentView;
    }

    private void buildCSV(List<Permintaan> permintaans) {

        //Check and/or request storage permissions
        final int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    getActivity(),
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }

        String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        if (permintaans.size() != 0) {
            Date date = permintaans.get(0).created;
            String fileName = "PermintaanData-" + date.toString() + ".csv";
            String filePath = baseDir + File.separator + fileName;
            File f = new File(filePath);
            CSVWriter writer = null;
            // File exist

            if (f.exists() && !f.isDirectory()) {
                FileWriter mFileWriter = null;
                try {
                    mFileWriter = new FileWriter(filePath, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                writer = new CSVWriter(mFileWriter);
            } else {
                try {
                    writer = new CSVWriter(new FileWriter(filePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            String[] data = new String[]{"created", "room number", "type", "assignee", "language"};
            writer.writeNext(data);
            for (Permintaan p : permintaans) {
                data = new String[]{p.created.toString(), p.roomNumber.toString(), p.content.getType(), p.assignee, p.countryCode};
                writer.writeNext(data);
                if (p.content.getType().equals(Permintaan.TYPE_RESTAURANT)) {
                    RestaurantOrder restoOrder = (RestaurantOrder) p.content;
                    for (OrderItem o : restoOrder.items) {
                        data = new String[]{o.quantity.toString(), o.name, "Rp. " + o.price.toString()};
                        writer.writeNext(data);
                    }
                    data = new String[]{"Total Price", "Rp. " + restoOrder.totalPrice.toString()};
                    writer.writeNext(data);
                } else if (p.content.getType().equals(Permintaan.TYPE_HOUSEKEEPING)) {
                    Housekeeping hkOrder = (Housekeeping) p.content;
                    data = new String[]{hkOrder.quantity.toString(), hkOrder.option.nameEn};
                    writer.writeNext(data);
                } else if (p.content.getType().equals(Permintaan.TYPE_ENGINEERING)) {
                    Engineering engOrder = (Engineering) p.content;
                    data = new String[]{engOrder.option.nameEn};
                    writer.writeNext(data);
                } else if (p.content.getType().equals(Permintaan.TYPE_MASSAGE)) {
                    Massage massageOrder = (Massage) p.content;
                    data = new String[]{massageOrder.option.nameEn};
                    writer.writeNext(data);
                }
            }

            try {
                writer.close();
                date = new Date();
                EmailSender.getInstance(getActivity().getBaseContext()).sendEmail(
                        "PermintaanReport" + date.toString(), f.toString(), "dynamicfrogman@gmail.com");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}