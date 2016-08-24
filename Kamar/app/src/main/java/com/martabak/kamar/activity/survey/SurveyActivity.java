package com.martabak.kamar.activity.survey;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.SurveyAnswer;
import com.martabak.kamar.domain.SurveyAnswers;
import com.martabak.kamar.domain.SurveyQuestion;
import com.martabak.kamar.service.SurveyServer;

import java.util.ArrayList;
import java.util.List;

import android.widget.TextView;
import android.widget.Toast;

import rx.Observer;

public class SurveyActivity extends AppCompatActivity {

    private String completionMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            completionMessage = extras.getString("completionMessage");
        }

        LayoutInflater layoutInflater = getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.activity_survey, null);
        setContentView(view);

        // Get the ActionBar here to configure the way it behaves.
        Toolbar toolbar = (Toolbar) findViewById(R.id.guest_toolbar);
        setSupportActionBar(toolbar);

        // Get the ActionBar here to configure the way it behaves.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });

        TextView roomNumberTextView = (TextView)findViewById(R.id.toolbar_roomnumber);
        String roomNumber = getSharedPreferences("userSettings", MODE_PRIVATE)
                .getString("roomNumber", "none");
        // set room number text
        roomNumberTextView.setText(getString(R.string.room_number) + " " + roomNumber);

        final RecyclerView rv = (RecyclerView) view.findViewById(R.id.survey_recycleview);
        final LinearLayoutManager llm = new LinearLayoutManager(getBaseContext());
        rv.setLayoutManager(llm);

        final List<SurveyQuestion> surveyQuestions = new ArrayList<>();
        final List<String> viewSurveyQuestions = new ArrayList<>();
        final SurveyArrayAdapter surveyArrayAdapter = new SurveyArrayAdapter(viewSurveyQuestions);
        rv.setAdapter(surveyArrayAdapter);

        /* Retrieve all questions */
        SurveyServer.getInstance(this).getSurveyQuestions()
                .subscribe(new Observer<List<SurveyQuestion>>() {
                    @Override public void onCompleted() {
                        Log.d(SurveyActivity.class.getCanonicalName(), String.valueOf(surveyQuestions.size()));
                        Log.d(SurveyActivity.class.getCanonicalName(), "getSurveyQuestions() On completed");
                        surveyArrayAdapter.notifyDataSetChanged();
                    }
                    @Override public void onError(Throwable e) {
                        Log.d(SurveyActivity.class.getCanonicalName(), "getSurveyQuestions() On error");
                        e.printStackTrace();
                    }
                    @Override public void onNext(List<SurveyQuestion> results) {
                        for (int i=0; i < results.size(); i++) {
                            surveyQuestions.add(results.get(i));
                            viewSurveyQuestions.add(surveyQuestions.get(i).getQuestion());
                        }
                        Log.d(SurveyActivity.class.getCanonicalName(), "getSurveyQuestions() On next");
                    }
        });

        final List<SurveyAnswer> surveyAnswers = new ArrayList<SurveyAnswer>();
        Button button = (Button) view.findViewById(R.id.survey_submit);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Adding all survey answers to the server */
                for (int i = 0; i < surveyQuestions.size(); i++) {
                    View surveyView = llm.findViewByPosition(i);
                    EditText editText = (EditText)surveyView.findViewById(R.id.survey_comment);
                    if (editText.getText() != null) {
                        SurveyAnswer surveyAnswer = new SurveyAnswer(surveyQuestions.get(i)._id,
                                3, editText.getText().toString());
                        surveyAnswers.add(surveyAnswer);
                    }
                }

                SurveyServer.getInstance(getBaseContext()).createSurveyAnswers(new SurveyAnswers("GUEST_ID", surveyAnswers))
                        .subscribe(new Observer<Boolean>() {
                            @Override public void onCompleted() {
                                Toast.makeText(
                                        getBaseContext(),
                                        completionMessage,
                                        Toast.LENGTH_LONG
                                ).show();
                                finish();
                                Log.d(SurveyActivity.class.getCanonicalName(), "createSurveyAnswers() On completed");
                            }
                            @Override public void onError(Throwable e) {
                                Log.d(SurveyActivity.class.getCanonicalName(), "createSurveyAnswers() On error");
                                e.printStackTrace();
                            }
                            @Override public void onNext(Boolean b) {
                                Log.d(SurveyActivity.class.getCanonicalName(), "createSurveyAnswers() On next");
                            }
                        });
                }
            });
    }


}
