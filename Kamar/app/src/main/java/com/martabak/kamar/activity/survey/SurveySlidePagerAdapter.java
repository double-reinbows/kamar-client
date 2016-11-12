package com.martabak.kamar.activity.survey;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.martabak.kamar.R;
import com.martabak.kamar.activity.guest.GuestHomeActivity;
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
    private HashMap<String, List<SurveyQuestion>> sectionMappings;

    public SurveySlidePagerAdapter(FragmentManager fm) {
        super(fm);
        this.sectionMappings = SurveyManager.getInstance().getMappings();
        this.sections = SurveyManager.getInstance().getSections();
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        args.putBoolean("flag", Boolean.FALSE);
        args.putString("currSection", sections.get(position));
        if (position == getCount()-1) {//set "last slide" flag
            args.putBoolean("flag", Boolean.TRUE);
        }
        ScreenSlidePageFragment f = new ScreenSlidePageFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public int getCount() {
        return sections.size();
    }

    public static class ScreenSlidePageFragment extends Fragment implements RatingBar.OnRatingBarChangeListener {
        public ScreenSlidePageFragment() {};
        private RecyclerView recyclerView;
        private HashMap<String, Integer> idToRating;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final String currSection;
            final HashMap<String, List<SurveyQuestion>> sectionMappings;
            final Boolean flag;
            Bundle args = getArguments();
            if (args != null) {
                sectionMappings = SurveyManager.getInstance().getMappings();
                idToRating = SurveyManager.getInstance().getRatings();
                flag = args.getBoolean("flag");
                currSection = args.getString("currSection");
            } else {
                return null;
            }
            ViewGroup rootView = (ViewGroup) inflater.inflate(
                    R.layout.survey_fragment_slide, container, false);
            recyclerView = (RecyclerView)rootView.findViewById(R.id.survey_list);
            TextView sectionText = (TextView)rootView.findViewById(R.id.survey_section_text);

            List<SurveyQuestion> questionList = sectionMappings.get(currSection);
            sectionText.setText(questionList.get(0).getSection());
            SurveyRecyclerAdapter recyclerAdapter = new SurveyRecyclerAdapter(questionList, ScreenSlidePageFragment.this);
            recyclerView.setAdapter(recyclerAdapter);

            Button submitButton = (Button)rootView.findViewById(R.id.survey_submit);
            if (flag) {//last slide
                submitButton.setAlpha(0.9f);
                submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String guestId = getActivity().getSharedPreferences("userSettings", getActivity().MODE_PRIVATE)
                            .getString("guestId", "none");
                    if (guestId.equals("none")) {
                        new AlertDialog.Builder(getContext())
                                .setTitle("Oh No!")
                                .setMessage(R.string.no_guest_in_room)
                                .show();
                        return;
                    } if (SurveyManager.getInstance().getPrevSubmission().equals(Boolean.TRUE)) {
                        new AlertDialog.Builder(getContext())
                                .setTitle("Oh No!")
                                .setMessage(R.string.survey_previously_submitted)
                                .show();
                        return;
                    }
                    List<SurveyAnswer> list = new ArrayList<>();
                    for (String section : sectionMappings.keySet()) {//double for loop to go thru all questions
                        for (SurveyQuestion q : sectionMappings.get(section)) {
                            SurveyAnswer answer = new SurveyAnswer(q._id, q.sectionEn, q.questionEn, idToRating.get(q._id));
                            list.add(answer);
                        }
                    }
                    String guestID = getActivity().getSharedPreferences("userSettings", Context.MODE_PRIVATE).getString("guestId", "none");
                    SurveyAnswers answers = new SurveyAnswers(guestID, list);
                    SurveyServer.getInstance(ScreenSlidePageFragment.this.getContext()).createSurveyAnswers(answers)
                            .subscribe(new Observer<Boolean>() {
                                @Override
                                public void onCompleted() {
                                    SurveyManager.getInstance().setPrevSubmission(Boolean.TRUE);
                                    //new dialog
                                    AlertDialog dialog = new AlertDialog.Builder(getContext())
                                            .setTitle(R.string.survey_thank_you_title)
                                            .setMessage(R.string.survey_thank_you)
                                            .show();

                                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialogInterface) {
                                            startActivity(new Intent(getActivity().getBaseContext(), GuestHomeActivity.class));
                                            getActivity().finish();
                                        }
                                    });
                                }
                                @Override
                                public void onError(Throwable e) {
                                    Log.d(SurveySlidePagerAdapter.class.getCanonicalName(), "getSurveyQuestions() On error");
                                    e.printStackTrace();
                                }
                                @Override
                                public void onNext(Boolean result) {
                                    if (result) {
//                                        Log.v("WWW", "answers created");
                                    }
                                }
                            });
                    }
                });
            }

            return rootView;
        }


        @Override
        public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
            final SurveyRecyclerAdapter.ViewHolder holder =
                    (SurveyRecyclerAdapter.ViewHolder)recyclerView.getChildViewHolder((View)ratingBar.getParent());
            final SurveyQuestion question = holder.item;

            idToRating.put(question._id, (int)v);
        }
    }

}
