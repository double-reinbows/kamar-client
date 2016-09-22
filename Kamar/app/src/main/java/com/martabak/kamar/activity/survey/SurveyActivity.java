package com.martabak.kamar.activity.survey;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.SurveyQuestion;
import com.martabak.kamar.domain.managers.SurveyManager;
import com.martabak.kamar.service.SurveyServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import rx.Observer;

public class SurveyActivity extends ActionBarActivity {
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager viewPager;
//    private List<String> sections;
    private HashMap<String, List<String>> sectionMappings;
    private HashMap<String, SurveyQuestion> idToQuestion;
    private HashMap<String, Integer> idToRating;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        Toolbar toolbar = (Toolbar) findViewById(R.id.guest_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });

        // Instantiate a ViewPager and a PagerAdapter.
        viewPager = (ViewPager) findViewById(R.id.survey_pager);


        if (SurveyManager.getInstance().getQuestions() == null) {
            idToRating = new HashMap<>();
            sectionMappings = new HashMap<>();
            idToQuestion = new HashMap<>();
            SurveyServer.getInstance(this).getSurveyQuestions()
                    .subscribe(new Observer<List<SurveyQuestion>>() {
                        List<SurveyQuestion> surveyQuestions;

                        @Override
                        public void onCompleted() {
                            Log.d(SurveyActivity.class.getCanonicalName(), String.valueOf(surveyQuestions.size()));
                            Log.d(SurveyActivity.class.getCanonicalName(), "getSurveyQuestions() On completed");
                            for (SurveyQuestion sq : surveyQuestions) {
                                String currSection = sq.getSection();
                                idToRating.put(sq._id, 0);
                                if (!sectionMappings.keySet().contains(currSection)) { //new section found
                                    List<String> ids = new ArrayList<>();
                                    ids.add(sq._id);
                                    sectionMappings.put(currSection, ids);
//                                    sections.add(currSection);
                                } else {
                                    List<String> sql = sectionMappings.get(currSection);
                                    sql.add(sq._id);
                                    sectionMappings.put(currSection, sql);
                                }
                                idToQuestion.put(sq._id, sq);
                            }
                            SurveyManager.getInstance().setQuestions(idToQuestion);
                            SurveyManager.getInstance().setRatings(idToRating);
                            SurveyManager.getInstance().setMapping(sectionMappings);
                            pagerAdapter = new SurveySlidePagerAdapter(getSupportFragmentManager());
                            viewPager.setAdapter(pagerAdapter);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(SurveyActivity.class.getCanonicalName(), "getSurveyQuestions() On error");
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(List<SurveyQuestion> results) {
                            surveyQuestions = results;
                            Log.d(SurveyActivity.class.getCanonicalName(), "getSurveyQuestions() On next");
                        }
                    });
        } else {
            pagerAdapter = new SurveySlidePagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(pagerAdapter);
        }
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

    private class SurveySection {
        /**
         * The name of the option's section, in English.
         */
        public final String sectionEn;

        /**
         * The name of the option's section, in Indonesian Bahasa.
         */
        public final String sectionIn;

        /**
         * The name of the option's section, in Chinese.
         */
        public final String sectionZh;

        /**
         * The name of the option's section, in Russian.
         */
        public final String sectionRu;

        public SurveySection(SurveyQuestion sq) {
            this.sectionEn = sq.sectionEn;
            this.sectionIn = sq.sectionIn;
            this.sectionZh = sq.sectionZh;
            this.sectionRu = sq.sectionRu;
        }

        /**
         * @return The description in the appropriate language.
         */
        public String getSection() {
            if (Locale.getDefault().getLanguage().equals(Locale.ENGLISH.getLanguage())) {
                return this.sectionEn;
            } else if (Locale.getDefault().getLanguage().equals(new Locale("in").getLanguage())) {
                return this.sectionIn;
            } else if (Locale.getDefault().getLanguage().equals(Locale.CHINESE.getLanguage())) {
                return this.sectionZh;
            } else if (Locale.getDefault().getLanguage().equals(new Locale("ru").getLanguage())) {
                return this.sectionRu;
            } else {
                Log.e(SurveyActivity.class.getCanonicalName(), "Unknown locale language: " + Locale.getDefault().getLanguage());
                return this.sectionEn;
            }
        }
    }


}