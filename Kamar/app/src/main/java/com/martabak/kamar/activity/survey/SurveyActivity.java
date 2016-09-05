package com.martabak.kamar.activity.survey;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.SurveyAnswer;
import com.martabak.kamar.domain.SurveyAnswers;
import com.martabak.kamar.domain.SurveyQuestion;
import com.martabak.kamar.service.SurveyServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observer;

public class SurveyActivity extends FragmentActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    //private static final int NUM_PAGES = 5;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager viewPager;
//    private final SurveySlidePagerAdapter pagerAdapter = null;
    private ArrayList<String> sections;
    private HashMap<String, ArrayList<SurveyQuestion>> secToQuestions;
    private HashMap<String, Integer> idToRating;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        // Instantiate a ViewPager and a PagerAdapter.
        viewPager = (ViewPager) findViewById(R.id.survey_pager);
        sections = new ArrayList<>();
        secToQuestions = new HashMap<>();
        idToRating = new HashMap<>();

        SurveyServer.getInstance(this).getSurveyQuestions()
                .subscribe(new Observer<List<SurveyQuestion>>() {
                    List<SurveyQuestion> surveyQuestions;
                    @Override public void onCompleted() {
                        Log.d(SurveyActivity.class.getCanonicalName(), String.valueOf(surveyQuestions.size()));
                        Log.d(SurveyActivity.class.getCanonicalName(), "getSurveyQuestions() On completed");
                        for (int i=0; i<surveyQuestions.size(); i++) {
                            SurveyQuestion sq = surveyQuestions.get(i);
                            String currSection = sq.getSection();
                            idToRating.put(sq._id, 0);
                            if (!sections.contains(currSection)) { //new section found
                                sections.add(currSection);
                                ArrayList<SurveyQuestion> sql = new ArrayList<>();
                                sql.add(sq);
                                secToQuestions.put(currSection, sql);
                            } else {
                                ArrayList<SurveyQuestion> sql = secToQuestions.get(currSection);
                                sql.add(sq);
                                secToQuestions.put(currSection, sql);
                            }
                        }
                        pagerAdapter = new SurveySlidePagerAdapter(getSupportFragmentManager(), sections, secToQuestions, idToRating);
                        viewPager.setAdapter(pagerAdapter);
                    }
                    @Override public void onError(Throwable e) {
                        Log.d(SurveyActivity.class.getCanonicalName(), "getSurveyQuestions() On error");
                        e.printStackTrace();
                    }
                    @Override public void onNext(List<SurveyQuestion> results) {
//                        for (int i=0; i < results.size(); i++) {
//                            surveyQuestions.add(results.get(i));
//                            viewSurveyQuestions.add(surveyQuestions.get(i).getQuestion());
//                        }
                        surveyQuestions = results;
                        Log.d(SurveyActivity.class.getCanonicalName(), "getSurveyQuestions() On next");
                    }
                });

        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.survey_submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dummy data
                SurveyQuestion question1 = secToQuestions.get("About our rooms...").get(0);
                SurveyQuestion question2 = secToQuestions.get("About our rooms...").get(1);
                List<SurveyAnswer> list = new ArrayList<>();
                SurveyAnswer answer1 = new SurveyAnswer(question1._id, question1.questionEn, 2, "herp");
                SurveyAnswer answer2 = new SurveyAnswer(question2._id, question2.questionEn, 3, "derp");
                list.add(answer1);
                list.add(answer2);
                SurveyAnswers answers = new SurveyAnswers("46abbef316832bf8648f4473a20ded66", list);

                SurveyServer.getInstance(SurveyActivity.this).createSurveyAnswers(answers)
                        .subscribe(new Observer<Boolean>() {
                            @Override public void onCompleted() {
                            }
                            @Override public void onError(Throwable e) {
                                Log.d(SurveySlidePagerAdapter.class.getCanonicalName(), "getSurveyQuestions() On error");
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(Boolean result) {
                                Log.v("WWW", "answers created");
                            }

                        });
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }




}