package com.martabak.kamar.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.SurveyAnswer;
import com.martabak.kamar.domain.SurveyQuestion;
import com.martabak.kamar.service.FeedbackServer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import rx.Observer;

public class SurveyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_survey);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        LayoutInflater layoutInflater = getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.activity_survey, null);
        setContentView(view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final List<SurveyQuestion> surveyQuestions = new ArrayList<SurveyQuestion>();
        final List<String> viewSurveyQuestions = new ArrayList<String>();

        /* Retrieve all questions */
        FeedbackServer.getInstance(this).getSurveyQuestions()
                .subscribe(new Observer<List<SurveyQuestion>>() {
                    @Override
                    public void onCompleted() {
                        Log.d("size", String.valueOf(surveyQuestions.size()));
                        Log.d("Completed", "On completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("Error", "On error");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<SurveyQuestion> results) {

                        for (int i=0; i < results.size(); i++)
                        {
                            surveyQuestions.add(results.get(i));
                            viewSurveyQuestions.add(surveyQuestions.get(i).question);
                        }

                        Log.d("Next", "On next");

                    }
                });




        final RecyclerView rv = (RecyclerView) view.findViewById(R.id.survey_recycleview);

        final LinearLayoutManager llm = new LinearLayoutManager(getBaseContext());
        rv.setLayoutManager(llm);

        final SurveyArrayAdapter surveyArrayAdapter = new SurveyArrayAdapter(viewSurveyQuestions);
        rv.setAdapter(surveyArrayAdapter);

        final List<SurveyAnswer> surveyAnswers = new ArrayList<SurveyAnswer>();
        FloatingActionButton button = (FloatingActionButton) view.findViewById(R.id.survey_answer_add);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Adding all survey answers to the server */
                for (int i = 0; i < surveyQuestions.size(); i++) {
                    View surveyView = llm.findViewByPosition(i);
                    EditText editText = (EditText)surveyView.findViewById(R.id.survey_answer);
                    if (editText.getText() != null) {
                        SurveyAnswer surveyAnswer = new SurveyAnswer(surveyQuestions.get(i)._id
                                , editText.getText().toString());
                        surveyAnswers.add(surveyAnswer);
                    }
                }

                FeedbackServer.getInstance(getBaseContext()).createSurveyAnswers(surveyAnswers)
                        .subscribe(new Observer<Boolean>() {
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
                            public void onNext(Boolean b) {
                                Log.d("Next", "On next");
                            }
                        });




            }
        });
    }


}
