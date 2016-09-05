package com.martabak.kamar.activity.survey;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.SurveyAnswer;
import com.martabak.kamar.domain.SurveyAnswers;
import com.martabak.kamar.domain.SurveyQuestion;
import com.martabak.kamar.service.SurveyServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observer;

/**
 * A pager adapter that creates a fragment for each survey section.
 * Each fragment creates a recycler view.
 */
class SurveySlidePagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<String> sections;
    private HashMap<String, ArrayList<SurveyQuestion>> secToQuestions;
    private HashMap<String, Integer> idToRating;

    public SurveySlidePagerAdapter(FragmentManager fm, ArrayList<String> s,
                                   HashMap<String, ArrayList<SurveyQuestion>> sToQ,
                                   HashMap<String, Integer> idToRating) {
        super(fm);
        this.sections = s;
        this.secToQuestions = sToQ;
        this.idToRating = idToRating;
    }

    @Override
    public Fragment getItem(int position) {
        //TODO: set up the fragment (page)
        Bundle args = new Bundle();
        //args.putString("currSection", sections.get(position));

        args.putString("currSection", sections.get(position));
        args.putSerializable("dict", secToQuestions);
        args.putSerializable("ratings", idToRating);
        ScreenSlidePageFragment f = new ScreenSlidePageFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public int getCount() {
        //returns no. of sections
        return sections.size();
    }

    public static class ScreenSlidePageFragment extends Fragment {
        public ScreenSlidePageFragment() {};

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final String currSection;
            final HashMap<String, ArrayList<SurveyQuestion>> secToQuestions;
            HashMap<String, Integer> idToRating;
            Bundle args = getArguments();
            if (args != null) {
                currSection = args.getString("currSection");
                secToQuestions = (HashMap)args.getSerializable("dict");
                idToRating = (HashMap)args.getSerializable("ratings");
            } else {
                return null;
            }
            ViewGroup rootView = (ViewGroup) inflater.inflate(
                    R.layout.survey_fragment_slide, container, false);
//            TextView questionText = (TextView) rootView.findViewById(R.id.survey_row_text);
            RecyclerView recyclerView = (RecyclerView)rootView.findViewById(R.id.survey_list);
//            Log.v("DDD", currSection);
//            Log.v("ZZZ", secToQuestions.get("room").get(0).getQuestion());
//
            TextView test = (TextView)rootView.findViewById(R.id.survey_section_text);
            test.setText(currSection);
//            test.setText(secToQuestions.get(currSection).get(0).getQuestion());

            SurveyRecyclerAdapter recyclerAdapter = new SurveyRecyclerAdapter(secToQuestions.get(currSection));
            recyclerView.setAdapter(recyclerAdapter);


            return rootView;
        }
    }

}
