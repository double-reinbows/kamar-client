package com.martabak.kamar.activity.survey;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.SurveyQuestion;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
 * sequence.
 */
class SurveySlidePagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<String> sections;
    private HashMap<String, ArrayList<SurveyQuestion>> secToQuestions;

    public SurveySlidePagerAdapter(FragmentManager fm, ArrayList<String> s,
                                   HashMap<String, ArrayList<SurveyQuestion>> sToQ) {
        super(fm);
        this.sections = s;
        this.secToQuestions = sToQ;
    }

    @Override
    public Fragment getItem(int position) {
        //TODO: set up the fragment (page)
        Bundle args = new Bundle();
        //args.putString("currSection", sections.get(position));

        args.putString("currSection", sections.get(position));
        args.putSerializable("dict", secToQuestions);
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
            String currSection;
            HashMap<String, ArrayList<SurveyQuestion>> secToQuestions;
            Bundle args = getArguments();
            if (args != null) {
                currSection = args.getString("currSection");
                secToQuestions = (HashMap)args.getSerializable("dict");
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
