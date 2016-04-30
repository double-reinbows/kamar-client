package com.martabak.kamar.activity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.SurveyQuestion;
import com.martabak.kamar.service.FeedbackServer;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;

/**
 * Created by adarsh on 18/04/16.
 */
public class SurveyArrayAdapter extends ArrayAdapter{

    private Context mContext;
    private int mResource;
    private List<String> mSurveyQuestions;

    public SurveyArrayAdapter(Context c, int resource, List<String> surveyQuestions) {
        super(c, resource, surveyQuestions);
        mContext = c;
        mResource = resource;
        mSurveyQuestions = surveyQuestions;
    }

    public int getCount() {
        return 0;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        View surveyView;
        TextView textView;
        EditText editText;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            LayoutInflater inflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            surveyView = inflater.inflate(R.layout.dialog_survey_listitem, null);
        } else {
            surveyView = (View) convertView;
        }

        //set textview and edit text of each list item
        textView = (TextView)  surveyView.findViewById(R.id.survey_question);
        editText = (EditText) surveyView.findViewById(R.id.survey_answer);

        surveyView.setLayoutParams(new ListView.LayoutParams(150, 150));
        textView.setText(mSurveyQuestions.get(position));


        return surveyView;
    }





}
