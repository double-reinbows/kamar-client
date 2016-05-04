package com.martabak.kamar.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.martabak.kamar.R;

import java.util.List;

/**
 * Created by adarsh on 18/04/16.
 */
public class SurveyArrayAdapter extends RecyclerView.Adapter<SurveyArrayAdapter.SurveyViewHolder> {


    private List<String> mSurveyQuestions;


    public class SurveyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public EditText editText;

        public SurveyViewHolder(View surveyView) {
            super(surveyView);
            //set textview and edit text of each list item
            textView = (TextView) surveyView.findViewById(R.id.survey_question);
            editText = (EditText) surveyView.findViewById(R.id.survey_answer);
        }

    }

    public SurveyArrayAdapter( List<String> surveyQuestions) {
        mSurveyQuestions = surveyQuestions;
    }

    @Override
    public int getItemCount() {
        return mSurveyQuestions.size();
    }


    @Override
    public SurveyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.survey_item, viewGroup, false);
        SurveyViewHolder svh = new SurveyViewHolder(v);
        return  svh;
    }

    @Override
    public void onBindViewHolder(SurveyViewHolder surveyViewHolder, int i) {
        surveyViewHolder.textView.setText(mSurveyQuestions.get(i));

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}