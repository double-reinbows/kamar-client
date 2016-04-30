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
import android.widget.EditText;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.Feedback;
import com.martabak.kamar.service.FeedbackServer;

import rx.Observer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TellusDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TellusDialogFragment extends DialogFragment {
    String tellusMessage;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.dialog_tellus, null);
        builder.setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        EditText editBellboyMessage = (EditText)
                                view.findViewById(R.id.tellus_message_edit_text);
                        tellusMessage = editBellboyMessage.getText().toString();

                        sendTellUs();

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        return builder.create();
    }

    /*
    Send the tell us message to the server.
    */
    public void sendTellUs() {

        FeedbackServer.getInstance(getActivity().getBaseContext()).createFeedback(new Feedback(
                        tellusMessage)
        ).subscribe(new Observer<Boolean>() {
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
            public void onNext(Boolean b) {
                Log.d("Next", "On next");
            }
        });

    }
}
