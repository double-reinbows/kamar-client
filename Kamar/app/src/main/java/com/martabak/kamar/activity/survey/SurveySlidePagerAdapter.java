package com.martabak.kamar.activity.survey;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.SurveyAnswer;
import com.martabak.kamar.domain.SurveyAnswers;
import com.martabak.kamar.domain.SurveyQuestion;
import com.martabak.kamar.domain.managers.SurveyManager;
import com.martabak.kamar.service.SurveyServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observer;

/**
 * A pager adapter that creates a fragment for each survey section.
 * Each fragment contains a recycler view.
 */
class SurveySlidePagerAdapter extends FragmentStatePagerAdapter {

    private List<String> sections;
    private HashMap<String, SurveyQuestion> idToQuestion;
    private HashMap<String, List<String>> sectionMappings;
    private HashMap<String, Integer> idToRating;

    public SurveySlidePagerAdapter(FragmentManager fm) {
        super(fm);
        this.idToQuestion = SurveyManager.getInstance().getQuestions();
        this.sectionMappings = SurveyManager.getInstance().getMappings();
        this.idToRating = SurveyManager.getInstance().getRatings();
        this.sections = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        args.putBoolean("flag", Boolean.FALSE);
        Log.v("SECTIONS", sections.toString());
        //for some reason this doesn't work: SurveyManager.getInstance().setCurrSection(sections.get(position));
        args.putString("currSection", sections.get(position));
        if (position == getCount()-1) {//set "last slide" flag
            args.putBoolean("flag", Boolean.TRUE);
            SurveyManager.getInstance().setFlag(Boolean.TRUE);
        }
        ScreenSlidePageFragment f = new ScreenSlidePageFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public int getCount() {
        //returns no. of sections
        if (sections.size() == 0) {
            sections.addAll(sectionMappings.keySet());
        }
        return sections.size();
    }

    public static class ScreenSlidePageFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {
        public ScreenSlidePageFragment() {};
        private RecyclerView recyclerView;
        private HashMap<String, Integer> idToRating;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final String currSection;
            final HashMap<String, List<String>> sectionMappings;
            final HashMap<String, SurveyQuestion> questions;
            final Boolean flag;
            Bundle args = getArguments();
            if (args != null) {
                sectionMappings = SurveyManager.getInstance().getMappings();
                idToRating = SurveyManager.getInstance().getRatings();
                questions = SurveyManager.getInstance().getQuestions();
                flag = args.getBoolean("flag");
                currSection = args.getString("currSection");
//                currSection = SurveyManager.getInstance().getCurrSection();
//                flag = SurveyManager.getInstance().getFlag();
            } else {
                return null;
            }
            ViewGroup rootView = (ViewGroup) inflater.inflate(
                    R.layout.survey_fragment_slide, container, false);
            recyclerView = (RecyclerView)rootView.findViewById(R.id.survey_list);
            TextView test = (TextView)rootView.findViewById(R.id.survey_section_text);
            test.setText(currSection);
            List<SurveyQuestion> questionList = new ArrayList<>();
            for (String id : sectionMappings.get(currSection)) {
                questionList.add(questions.get(id));
            }
            SurveyRecyclerAdapter recyclerAdapter = new SurveyRecyclerAdapter(questionList, ScreenSlidePageFragment.this);
            recyclerView.setAdapter(recyclerAdapter);

            ImageView submitButton = (ImageView)rootView.findViewById(R.id.survey_submit);
            if (flag) {//last slide
                submitButton.setAlpha(0.9f);
                submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<SurveyAnswer> list = new ArrayList<>();
                    for (String id : questions.keySet()) {
                        SurveyQuestion q = questions.get(id);
                        SurveyAnswer answer = new SurveyAnswer(q._id, q.sectionEn, q.questionEn, idToRating.get(q._id));
                        list.add(answer);
                    }
                    String guestID = getActivity().getSharedPreferences("userSettings", Context.MODE_PRIVATE).getString("guestId", "none");
                    SurveyAnswers answers = new SurveyAnswers(guestID, list);
                    SurveyServer.getInstance(ScreenSlidePageFragment.this.getContext()).createSurveyAnswers(answers)
                            .subscribe(new Observer<Boolean>() {
                                @Override
                                public void onCompleted() {
                                }
                                @Override
                                public void onError(Throwable e) {
                                    Log.d(SurveySlidePagerAdapter.class.getCanonicalName(), "getSurveyQuestions() On error");
                                    e.printStackTrace();
                                }
                                @Override
                                public void onNext(Boolean result) {
                                    if (result) {
                                        Log.v("WWW", "answers created");
                                    }
                                }
                            });
                    }
                });
            }

            return rootView;
        }

        public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
            final SurveyRecyclerAdapter.ViewHolder holder =
                    (SurveyRecyclerAdapter.ViewHolder)recyclerView.getChildViewHolder((View)radioGroup.getParent());
            final SurveyQuestion question = holder.item;

            RadioButton checkedRadioButton = (RadioButton)radioGroup.findViewById(checkedId);
            // This puts the value (true/false) into the variable
            boolean isChecked = checkedRadioButton.isChecked();
            // If the radio button that has changed in check state is now checked...
            if (isChecked) {
                Integer i = Integer.parseInt(checkedRadioButton.getTag().toString());
                idToRating.put(question._id, i);
            }
        }
    }

}
