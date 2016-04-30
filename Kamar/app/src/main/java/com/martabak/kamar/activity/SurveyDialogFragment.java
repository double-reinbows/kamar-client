package com.martabak.kamar.activity;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.SurveyQuestion;
import com.martabak.kamar.service.FeedbackServer;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;

/**
 * A simple {@link Fragment} subclass.
 */
public class SurveyDialogFragment extends DialogFragment {

     // survey dialog list items

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.dialog_survey, null);
        List<String> surveyQuestions = getSurveyQuestions();
        SurveyArrayAdapter surveyArrayAdapter = new SurveyArrayAdapter(getActivity().getBaseContext(),
                R.layout.dialog_survey_listitem,surveyQuestions);

        final ListView listView = (ListView) view.findViewById(R.id.survey_listview);
        listView.setAdapter(surveyArrayAdapter);

        builder.setView(view)
                .setPositiveButton(R.string.positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                })
                .setNegativeButton(R.string.negative, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //bellboyDialogListener.onDialogNegativeClick(BellboyDialogFragment.this);

                    }
                });
        return builder.create();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_survey, container, false);
    }

    /*
     * Get list<string> for survey questions
    */
    public List<String> getSurveyQuestions() {

        final List<String> surveyQuestions = new ArrayList<String>();

        FeedbackServer.getInstance(getActivity()).getSurveyQuestions()
                .subscribe(new Observer<List<SurveyQuestion>>() {
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
                    public void onNext(List<SurveyQuestion> results) {

                        for (int i=0; i < results.size(); i++)
                        {
                            surveyQuestions.add(results.get(i).question);
                        }

                        Log.d("Next", "On next");

                    }
                });
        surveyQuestions.add("Test Question");
        return surveyQuestions;

    }

}
