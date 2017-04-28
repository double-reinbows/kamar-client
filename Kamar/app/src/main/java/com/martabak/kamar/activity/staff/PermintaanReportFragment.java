package com.martabak.kamar.activity.staff;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
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
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.service.GuestServer;
import com.martabak.kamar.service.PermintaanServer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

/**
 * A simple {@link Fragment} subclass.
 */
public class PermintaanReportFragment extends Fragment  {

    private String roomNumber;
    private List<String> roomNumbers;
    private Guest guest;
    private ArrayAdapter rooms;
    private static Integer millisecondsInADay = 24 * 60 * 60 * 1000;
    private long selectedDate;

    public PermintaanReportFragment() {
    }

    public static PermintaanReportFragment newInstance() {
        return new PermintaanReportFragment();
    }

    // bind views here
//    @BindView(R.id.guest_info) TextView guestInfoText;
//    @BindView(R.id.guest_spinner_checkout) Spinner spinner;
//    @BindView(R.id.check_guest_out_submit) Button submitButton;
    @BindView(R.id.permintaan_report_calendar) CalendarView simpleCalendarView;
    @BindView(R.id.permintaan_report_submit) Button submitButton;

    // on click listener
    @OnClick(R.id.permintaan_report_submit)
    void onSubmit() {
        Log.v("DICK", "CHEESE");
//        CalendarView simpleCalendarView = (CalendarView) findViewById(R.id.permintaan_report_calendar); // get the reference of CalendarView
//        long selectedDate = simpleCalendarView.getDate(); // get selected date in milliseconds
        PermintaanServer.getInstance(getActivity())
                .getPermintaansOfTime(new Date(selectedDate), new Date(selectedDate+millisecondsInADay))
                .subscribe(new Observer<List<Permintaan>>() {
                    List<Permintaan> permintaans = new ArrayList<>();
                    @Override
                    public void onCompleted() {
                        for (Permintaan p: permintaans) {
                            Log.v("DICK", p.created.toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onNext(List<Permintaan> result) {
                        permintaans = result;
                    }
                });
//        if (guest != null) {
//            checkGuestOut(guest);
//        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View parentView =  inflater.inflate(R.layout.fragment_perminaan_report, container, false);
        ButterKnife.bind(this,parentView);

//        roomNumbers = new ArrayList<>();
//        roomNumbers.add(0, getString(R.string.room_select));
//        rooms = new ArrayAdapter(getActivity().getBaseContext(),
//                R.layout.spinner_item, roomNumbers);


        CalendarView calendarView = (CalendarView) parentView.findViewById(R.id.permintaan_report_calendar); // get the reference of CalendarView
        calendarView.setFirstDayOfWeek(2); // set Monday as the first day of the week

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Log.v("CUNT", (String.valueOf(System.currentTimeMillis())));
        Log.v("CUNT", (String.valueOf(c.getTimeInMillis())));
        long startOfDay = System.currentTimeMillis() - (System.currentTimeMillis() - c.getTimeInMillis()); //start of day
        Date date = new Date();
        Log.v("CUNT", (String.valueOf(startOfDay)));
        long prevDay = System.currentTimeMillis() - millisecondsInADay;
//        simpleCalendarView.setDate(startDay);
        selectedDate = startOfDay - millisecondsInADay;
        calendarView.setMaxDate(prevDay);

//        selectedDate = simpleCalendarView.getDate(); // get selected date in milliseconds

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                Log.v("BOOM", String.valueOf(calendar.getTimeInMillis()));
                selectedDate = (calendar.getTimeInMillis()); //start of selected day

//                formatedDate = sdf.format(selectedDateInMillis);
//                dateDialog.setTitle(formatedDate);
//                dateInMillis = selectedDateInMillis;
            }
        });
//        rooms.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//        getRoomNumbersWithGuests();
//        spinner.setAdapter(rooms);
//        spinner.setSelection(0);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position > 0) {
//                    roomNumber = roomNumbers.get(position);
//                    Log.v(PermintaanReportFragment.class.getCanonicalName(), "Room number selected is " + roomNumber);
//                    getGuestInRoomNumber(roomNumber);
//                    submitButton.setEnabled(true);
//                } else {
//                    submitButton.setEnabled(false);
//                }
//            }
//            @Override public void onNothingSelected(AdapterView<?> parent) {
//                submitButton.setEnabled(false);
//            }
//        });

        return parentView;
    }

    /**
     * Gets a list of room numbers with guests checked in.
     */
//    private void getRoomNumbersWithGuests() {
//        roomNumbers.set(0, getString(R.string.loading));
//        spinner.setEnabled(false);
//        rooms.notifyDataSetChanged();
//        RoomManager.getInstance().getRoomsWithGuests(getActivity().getBaseContext()).subscribe(new Observer<Room>() {
//            @Override public void onCompleted() {
//                roomNumbers.set(0, getString(R.string.room_select));
//                spinner.setEnabled(true);
//                rooms.notifyDataSetChanged();
//            }
//            @Override public void onError(Throwable e) {
//                Log.v(PermintaanReportFragment.class.getCanonicalName(), "getRoomNumbersWithoutGuests() Error");
//                e.printStackTrace();
//            }
//            @Override public void onNext(Room room) {
//                if (room != null) {
//                    roomNumbers.add(room.number);
//                    Log.v(CheckGuestInFragment.class.getCanonicalName(), "Found room: " + room.number);
//                }
//            }
//        });
//    }

    /**
     * Get the guest currently checked in to the room number.
     * @param roomNumber The room number.
     * @return The guest.
     */
//    private void getGuestInRoomNumber(String roomNumber) {
//        GuestServer.getInstance(getActivity().getBaseContext()).getGuestInRoom(
//                roomNumber).subscribe(new Observer<Guest>() {
//            @Override public void onCompleted() {
//                if (guest != null) {
//                    guestInfoText.setText(guest.firstName + " " + guest.lastName);
//                }
//                Log.d(CheckGuestInFragment.class.getCanonicalName(), "On completed");
//            }
//            @Override public void onError(Throwable e) {
//                Log.d(CheckGuestInFragment.class.getCanonicalName(), "On error");
//                e.printStackTrace();
//            }
//            @Override public void onNext(Guest result) {
//                guest = result;
//                Log.d(CheckGuestInFragment.class.getCanonicalName(), "On next guest " + result);
//            }
//        });
//    }
//
//    /**
//     * Check the guest out.
//     */
//    private void checkGuestOut(Guest guest) {
//        Calendar c = Calendar.getInstance();
//        c.add(Calendar.MINUTE, -1); // little hack here
//        Date currentDate = c.getTime();
//        Guest updateGuest;
//
//        updateGuest = new Guest(guest._id, guest._rev, guest.firstName, guest.lastName,
//                guest.phone, guest.email, guest.checkIn, currentDate, guest.roomNumber,
//                guest.welcomeMessage, guest.promoImgId);
//        GuestServer.getInstance(getActivity().getBaseContext()).updateGuest(updateGuest)
//                .subscribe(new Observer<Boolean>() {
//                    @Override public void onCompleted() {
//                        rooms.notifyDataSetChanged();
//                        Log.d(CheckGuestInFragment.class.getCanonicalName(), "updateGuest() On completed");
//                    }
//                    @Override public void onError(Throwable e) {
//                        Log.d(CheckGuestInFragment.class.getCanonicalName(), "updateGuest() On error");
//                        e.printStackTrace();
//                        Toast.makeText(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
//                    }
//                    @Override public void onNext(Boolean result) {
//                        Log.v(CheckGuestInFragment.class.getCanonicalName(), "updateGuest() On next " + result);
//                        if (result) {
//                            Toast.makeText(getActivity(), getString(R.string.guest_checkout_message), Toast.LENGTH_LONG).show();
//                            startActivity(new Intent(getActivity(), StaffHomeActivity.class));
//                        } else {
//                            Toast.makeText(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//    }

}
