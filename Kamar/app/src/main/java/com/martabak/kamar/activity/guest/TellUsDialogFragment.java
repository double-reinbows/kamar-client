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
import android.widget.EditText;
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
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        EditText editBellboyMessage = (EditText)
                                view.findViewById(R.id.tellus_message_edit_text);
                        String tellUsMessage = editBellboyMessage.getText().toString();
                        sendTellUs(tellUsMessage);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        permintaanDialogListener.onDialogNegativeClick(TellUsDialogFragment.this);
                        dialog.dismiss();
                    }
                })
                .create();
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
