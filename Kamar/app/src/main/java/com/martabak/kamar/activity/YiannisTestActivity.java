package com.martabak.kamar.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.Guest;
import com.martabak.kamar.domain.GuestChat;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.domain.SurveyQuestion;
import com.martabak.kamar.service.ChatServer;
import com.martabak.kamar.service.FeedbackServer;
import com.martabak.kamar.service.GuestServer;
import com.martabak.kamar.service.PermintaanServer;
import com.martabak.kamar.service.StaffServer;

import java.util.List;

import rx.Observer;


public class YiannisTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yiannis_test);

        final Button doSomethingButton = (Button) findViewById(R.id.doSomething);

        doSomethingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doGetPermintaansForGuest();
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
        FeedbackServer.getInstance(getBaseContext()).getSurveyQuestions().subscribe(new Observer<List<SurveyQuestion>>() {
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
                "69")
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
}
