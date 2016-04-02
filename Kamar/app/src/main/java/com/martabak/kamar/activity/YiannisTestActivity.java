package com.martabak.kamar.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.Guest;
import com.martabak.kamar.domain.SurveyQuestion;
import com.martabak.kamar.service.FeedbackServer;
import com.martabak.kamar.service.GuestServer;
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
                doLogin();
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
