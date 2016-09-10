package com.martabak.kamar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.martabak.kamar.R;
import com.martabak.kamar.activity.chat.GuestChatService;
import com.martabak.kamar.activity.chat.StaffChatService;
import com.martabak.kamar.activity.guest.GuestPermintaanService;
import com.martabak.kamar.activity.staff.StaffPermintaanService;
import com.martabak.kamar.domain.Guest;
import com.martabak.kamar.domain.Staff;
import com.martabak.kamar.domain.SurveyAnswer;
import com.martabak.kamar.domain.SurveyAnswers;
import com.martabak.kamar.domain.chat.GuestChat;
import com.martabak.kamar.domain.Room;
import com.martabak.kamar.domain.managers.PermintaanManager;
import com.martabak.kamar.domain.options.EngineeringOption;
import com.martabak.kamar.domain.options.HousekeepingOption;
import com.martabak.kamar.domain.options.MassageOption;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.domain.SurveyQuestion;
import com.martabak.kamar.service.ChatServer;
import com.martabak.kamar.service.SurveyServer;
import com.martabak.kamar.service.GuestServer;
import com.martabak.kamar.service.PermintaanServer;
import com.martabak.kamar.service.StaffServer;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;


public class YiannisTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yiannis_test);

        final Button doSomething1Button = (Button) findViewById(R.id.doSomething1);
        final Button doSomething2Button = (Button) findViewById(R.id.doSomething2);
        final Button doSomething3Button = (Button) findViewById(R.id.doSomething3);

        doSomething1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doGetStaff();
            }
        });
        doSomething2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doGetEngineeringOptions();
            }
        });
        doSomething3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //doGetHousekeepingOptions();
                doGetPermintaanManager();
            }
        });
    }

    private void doGetStaff() {
        Log.d(YiannisTestActivity.class.getCanonicalName(), "Done get staff of division");
        StaffServer.getInstance(getBaseContext()).getStaffOfDivision(Staff.RESP_MASSAGE).subscribe(new Observer<Staff>() {
            List<Staff> staff = new ArrayList<>();
            @Override
            public void onCompleted() {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On completed");
                TextView textView = (TextView) findViewById(R.id.doSomethingText);
                textView.setText(staff.toString());
            }
            @Override
            public void onError(Throwable e) {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On error");
                TextView textView = (TextView) findViewById(R.id.doSomethingText);
                textView.setText(e.getMessage());
                e.printStackTrace();
            }
            @Override
            public void onNext(Staff result) {
                Log.i(YiannisTestActivity.class.getCanonicalName(), "On next " + result.firstName);
                staff.add(result);
            }
        });
    }

    private void doGetMassageOptions() {
        Log.d(YiannisTestActivity.class.getCanonicalName(), "Done get massage options");
        StaffServer.getInstance(getBaseContext()).getMassageOptions().subscribe(new Observer<List<MassageOption>>() {
            @Override
            public void onCompleted() {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On completed");
            }
            @Override
            public void onError(Throwable e) {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On error");
                TextView textView = (TextView) findViewById(R.id.doSomethingText);
                textView.setText(e.getMessage());
                e.printStackTrace();
            }
            @Override
            public void onNext(List<MassageOption> result) {
                Log.i(YiannisTestActivity.class.getCanonicalName(), "On next " + result.size());
                TextView textView = (TextView) findViewById(R.id.doSomethingText);
                textView.setText(result.toString());
            }
        });
    }

    private void doGetEngineeringOptions() {
        Log.d(YiannisTestActivity.class.getCanonicalName(), "Done get engineering options");
        StaffServer.getInstance(getBaseContext()).getEngineeringOptions().subscribe(new Observer<List<EngineeringOption>>() {
            @Override
            public void onCompleted() {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On completed");
            }
            @Override
            public void onError(Throwable e) {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On error");
                TextView textView = (TextView) findViewById(R.id.doSomethingText);
                textView.setText(e.getMessage());
                e.printStackTrace();
            }
            @Override
            public void onNext(List<EngineeringOption> result) {
                Log.i(YiannisTestActivity.class.getCanonicalName(), "On next " + result.size());
                TextView textView = (TextView) findViewById(R.id.doSomethingText);
                textView.setText(result.toString());
            }
        });
    }

    private void doGetHousekeepingOptions() {
        Log.d(YiannisTestActivity.class.getCanonicalName(), "Done get massage options");
        StaffServer.getInstance(getBaseContext()).getHousekeepingOptions().subscribe(new Observer<List<HousekeepingOption>>() {
            @Override
            public void onCompleted() {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On completed");
            }
            @Override
            public void onError(Throwable e) {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On error");
                TextView textView = (TextView) findViewById(R.id.doSomethingText);
                textView.setText(e.getMessage());
                e.printStackTrace();
            }
            @Override
            public void onNext(List<HousekeepingOption> result) {
                Log.i(YiannisTestActivity.class.getCanonicalName(), "On next " + result.size());
                TextView textView = (TextView) findViewById(R.id.doSomethingText);
                textView.setText(result.toString());
            }
        });
    }

    private void doGetRoomNumbersWithoutGuests() {
        Log.d(YiannisTestActivity.class.getCanonicalName(), "Done get room numbers");
        GuestServer.getInstance(getBaseContext()).getRoomNumbersWithoutGuests().subscribe(new Observer<Room>() {
            List<Room> rooms = new ArrayList<>();
            @Override
            public void onCompleted() {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On completed");
                TextView textView = (TextView) findViewById(R.id.doSomethingText);
                textView.setText(rooms.toString());
            }

            @Override
            public void onError(Throwable e) {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On error");
                TextView textView = (TextView) findViewById(R.id.doSomethingText);
                textView.setText(e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onNext(Room result) {
                Log.i(YiannisTestActivity.class.getCanonicalName(), "On next another room " + result.number);
                rooms.add(result);
            }
        });
    }

    private void doGetRoomNumbersWithGuests() {
        Log.d(YiannisTestActivity.class.getCanonicalName(), "Done get room numbers");
        GuestServer.getInstance(getBaseContext()).getRoomNumbersWithGuests().subscribe(new Observer<Room>() {
            List<Room> rooms = new ArrayList<>();
            @Override
            public void onCompleted() {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On completed");
                TextView textView = (TextView) findViewById(R.id.doSomethingText);
                textView.setText(rooms.toString());
            }

            @Override
            public void onError(Throwable e) {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On error");
                TextView textView = (TextView) findViewById(R.id.doSomethingText);
                textView.setText(e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onNext(Room result) {
                Log.i(YiannisTestActivity.class.getCanonicalName(), "On next another room " + result.number);
                rooms.add(result);
            }
        });
    }

    private void doStartStaffPermintaanService() {
        Intent intent = new Intent(this, StaffPermintaanService.class);
        intent.putExtra("guestId", "yianni");
        startService(intent);
    }

    private void doStartGuestPermintaanService() {
        Intent intent = new Intent(this, GuestPermintaanService.class);
        intent.putExtra("guestId", "yianni");
        startService(intent);
    }

    private void doStartStaffChatService() {
        Intent intent = new Intent(this, StaffChatService.class);
        startService(intent);
    }

    private void doStartGuestChatService() {
        Intent intent = new Intent(this, GuestChatService.class);
        intent.putExtra("guestId", "yianni");
        startService(intent);
    }

    private void doGetRoomNumbers() {
        Log.d(YiannisTestActivity.class.getCanonicalName(), "Done get room numbers");
        GuestServer.getInstance(getBaseContext()).getRoomNumbers().subscribe(new Observer<List<Room>>() {
            @Override
            public void onCompleted() {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On error");
                TextView textView = (TextView) findViewById(R.id.doSomethingText);
                textView.setText(e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onNext(List<Room> result) {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On next");
                TextView textView = (TextView) findViewById(R.id.doSomethingText);
                textView.setText(result.toString());
            }
        });
    }

    private void doGetPermintaansOfState() {
        Log.d(YiannisTestActivity.class.getCanonicalName(), "Done get permintaans of state");
        PermintaanServer.getInstance(getBaseContext()).getPermintaansOfState("NEW", "INPROGRESS").subscribe(new Observer<Permintaan>() {
            @Override
            public void onCompleted() {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On error");
                TextView textView = (TextView) findViewById(R.id.doSomethingText);
                textView.setText(e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onNext(Permintaan result) {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On next");
                TextView textView = (TextView) findViewById(R.id.doSomethingText);
                textView.setText(result.toString() + " for " + result.guestId + " of type " + result.content.getType());
            }
        });
    }

    private void doGetPermintaansForGuest() {
        Log.d(YiannisTestActivity.class.getCanonicalName(), "Done get permintaans for guest");
        PermintaanServer.getInstance(getBaseContext()).getPermintaansForGuest("yianni").subscribe(new Observer<Permintaan>() {
            @Override
            public void onCompleted() {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On error");
                TextView textView = (TextView) findViewById(R.id.doSomethingText);
                textView.setText(e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onNext(Permintaan result) {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On next");
                TextView textView = (TextView) findViewById(R.id.doSomethingText);
                textView.setText(result.toString() + " for " + result.guestId + " of type " + result.content.getType());
            }
        });
    }

    private void doGetGuestChat() {
        Log.d(YiannisTestActivity.class.getCanonicalName(), "Done get guest in room");
        ChatServer.getInstance(getBaseContext()).getGuestChat("yianni").subscribe(new Observer<GuestChat>() {
            @Override
            public void onCompleted() {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On error");
                TextView textView = (TextView) findViewById(R.id.doSomethingText);
                textView.setText(e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onNext(GuestChat result) {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On next");
                TextView textView = (TextView) findViewById(R.id.doSomethingText);
                textView.setText(result.toString() + " contains " + result.messages.size() + " messages");
            }
        });
    }

    private void doGetGuestInRoom() {
        Log.d(YiannisTestActivity.class.getCanonicalName(), "Done get guest in room");
        GuestServer.getInstance(getBaseContext()).getGuestInRoom("4").subscribe(new Observer<Guest>() {
            @Override
            public void onCompleted() {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On error");
                TextView textView = (TextView) findViewById(R.id.doSomethingText);
                textView.setText(e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onNext(Guest result) {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On next");
                TextView textView = (TextView) findViewById(R.id.doSomethingText);
                textView.setText(result.toString() + "checked in at " + result.checkIn + "checked out at " + result.checkOut);
            }
        });
    }

    private void doLogin() {
        Log.d(YiannisTestActivity.class.getCanonicalName(), "Done login");
        StaffServer.getInstance(getBaseContext()).login("bleh123").subscribe(new Observer<Boolean>() {
            @Override
            public void onCompleted() {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On error");
                TextView textView = (TextView) findViewById(R.id.doSomethingText);
                textView.setText(e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onNext(Boolean result) {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On next");
                TextView textView = (TextView) findViewById(R.id.doSomethingText);
                textView.setText(result.toString());
            }
        });
    }

    private void doGetSurveyQuestions() {
        Log.d(YiannisTestActivity.class.getCanonicalName(), "Done get survey questions");
        SurveyServer.getInstance(getBaseContext()).getSurveyQuestions().subscribe(new Observer<List<SurveyQuestion>>() {
            @Override
            public void onCompleted() {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On error");
                TextView textView = (TextView) findViewById(R.id.doSomethingText);
                textView.setText(e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onNext(List<SurveyQuestion> result) {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On next");
                TextView textView = (TextView) findViewById(R.id.doSomethingText);
                textView.setText(result.toString());
            }
        });
    }

    private void doCreateGuest() {
        Log.d(YiannisTestActivity.class.getCanonicalName(), "Done create guest");
        GuestServer.getInstance(getBaseContext()).createGuest(new Guest(
                "Adarsh",
                "Jegadeesan",
                "0430230239103",
                "adarshj@gmail.com",
                null,
                null,
                "69",
                "Welcome.",
                "TBA")
        ).subscribe(new Observer<Guest>() {
            @Override
            public void onCompleted() {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On error");
                TextView textView = (TextView)findViewById(R.id.doSomethingText);
                textView.setText(e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onNext(Guest guest) {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On next");
                TextView textView = (TextView)findViewById(R.id.doSomethingText);
                textView.setText(guest.firstName + " " + guest.lastName);
            }
        });
    }

    private void doGetPermintaanManager() {
        Log.d(YiannisTestActivity.class.getCanonicalName(), "Done create guest");
        PermintaanManager.getInstance().getMassageStatus(getBaseContext()
        ).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On error");
                TextView textView = (TextView) findViewById(R.id.doSomethingText);
                textView.setText("RED RED");
                e.printStackTrace();
            }

            @Override
            public void onNext(String result) {
                Log.d(YiannisTestActivity.class.getCanonicalName(), "On next");
                TextView textView = (TextView) findViewById(R.id.doSomethingText);
                switch (result) {
                    case Permintaan.STATE_NEW:
                        textView.setText("GREEN RED");
                        break;
                    case Permintaan.STATE_INPROGRESS:
                        textView.setText("GREEN GREEN");
                        break;
                    case Permintaan.STATE_COMPLETED:
                        textView.setText("RED RED");
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void doCreateSurveyAnswers() {

        final List<SurveyAnswer> surveyAnswers = new ArrayList<SurveyAnswer>();
        final List<SurveyQuestion> surveyQuestions = new ArrayList<>();

        surveyAnswers.add(new SurveyAnswer("46abbef316832bf8648f4473a207efca", "About our rooms...","what ya think?", 3));
        surveyAnswers.add(new SurveyAnswer("46abbef316832bf8648f4473a207f26b", "About our cafe...", "suck mah dick?", 2));

        final SurveyAnswers surveyAnswer = new SurveyAnswers("GUEST_ID", surveyAnswers);
            SurveyServer.getInstance(this).createSurveyAnswers(surveyAnswer)
                .subscribe(new Observer<Boolean>() {
                    @Override public void onCompleted() {
                        Log.d(YiannisTestActivity.class.getCanonicalName(), "createSurveyAnswers() On completed");
                    }
                    @Override public void onError(Throwable e) {
                        Log.d(YiannisTestActivity.class.getCanonicalName(), "createSurveyAnswers() On error");
                        e.printStackTrace();
                    }
                    @Override public void onNext(Boolean b) {
                        Log.d(YiannisTestActivity.class.getCanonicalName(), "createSurveyAnswers() On next");
                    }
                });
    }
}
