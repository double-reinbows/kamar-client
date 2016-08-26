package com.martabak.kamar.activity.survey;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.martabak.kamar.R;
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

        SurveyServer.getInstance(this).getSurveyQuestions()
                .subscribe(new Observer<List<SurveyQuestion>>() {
                    List<SurveyQuestion> surveyQuestions;
                    @Override public void onCompleted() {
                        Log.d(SurveyActivity.class.getCanonicalName(), String.valueOf(surveyQuestions.size()));
                        Log.d(SurveyActivity.class.getCanonicalName(), "getSurveyQuestions() On completed");
                        for (int i=0; i<surveyQuestions.size(); i++) {
                            SurveyQuestion sq = surveyQuestions.get(i);
                            String currSection = sq.getSection();
                            //ArrayList<SurveyQuestion> sql = secToQuestions.get(currSection);
                            //sql.add(sq);
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
                        pagerAdapter = new SurveySlidePagerAdapter(getSupportFragmentManager(), sections, secToQuestions);
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

    public void getQuestions() {
        //TODO: call server and build the data for the adapter
        /* Retrieve all questions */

        SurveyServer.getInstance(this).getSurveyQuestions()
                .subscribe(new Observer<List<SurveyQuestion>>() {
                    List<SurveyQuestion> surveyQuestions;
                    @Override public void onCompleted() {
                        Log.d(SurveyActivity.class.getCanonicalName(), String.valueOf(surveyQuestions.size()));
                        Log.d(SurveyActivity.class.getCanonicalName(), "getSurveyQuestions() On completed");
                        for (int i=0; i<surveyQuestions.size(); i++) {
                            SurveyQuestion sq = surveyQuestions.get(i);
                            String currSection = sq.getSection();
                            //ArrayList<SurveyQuestion> sql = secToQuestions.get(currSection);
                            //sql.add(sq);
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
//                        surveyArrayAdapter.notifyDataSetChanged();
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
    }


}