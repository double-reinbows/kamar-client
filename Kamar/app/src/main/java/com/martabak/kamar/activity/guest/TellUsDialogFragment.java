package com.martabak.kamar.activity.guest;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.Feedback;
import com.martabak.kamar.service.FeedbackServer;

import rx.Observer;

/**
 * The feedback fragment.
 */
public class TellUsDialogFragment extends DialogFragment {

    private PermintaanDialogListener permintaanDialogListener;
    private Boolean success = false;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.dialog_tellus, null);
        final Button confirmButton = (Button) view.findViewById(R.id.tellus_confirm);
        final Button cancelButton = (Button) view.findViewById(R.id.tellus_cancel);

        final AlertDialog dialog= new AlertDialog.Builder(getActivity()).create();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText tellusBellboyMessage = (EditText)
                        view.findViewById(R.id.tellus_message_edit_text);
                RatingBar rB = (RatingBar) view.findViewById(R.id.tellusRatingBar);

                String tellUsMessage = tellusBellboyMessage.getText().toString();
                sendTellUs(tellUsMessage);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permintaanDialogListener.onDialogNegativeClick(TellUsDialogFragment.this);
                dialog.dismiss();
            }
        });

        dialog.setView(view);

        return dialog;

    }

    /**
     * Send the feedback message to the server.
     * @param message The feedback message.
     */
    private void sendTellUs(String message) {
        FeedbackServer.getInstance(getActivity()).createFeedback(new Feedback(message)).subscribe(new Observer<Boolean>() {
                @Override public void onCompleted() {
                    Log.d(TellUsDialogFragment.class.getCanonicalName(), "createFeedback() On completed");
                    permintaanDialogListener.onDialogPositiveClick(TellUsDialogFragment.this, success);
                }
                @Override
                public void onError(Throwable e) {
                    Log.d(TellUsDialogFragment.class.getCanonicalName(), "createFeedback() On error");
                    e.printStackTrace();
                    permintaanDialogListener.onDialogPositiveClick(TellUsDialogFragment.this, success);
                }
                @Override public void onNext(Boolean b) {
                    Log.d(TellUsDialogFragment.class.getCanonicalName(), "createFeedback() On next " + b);
                    if (b) {
                        success = true;
                    } else {
                        success = false;
                    }
                }
        });
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        permintaanDialogListener = (PermintaanDialogListener) activity;
    }

}
