package com.martabak.kamar.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.permintaan.Bellboy;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.service.PermintaanServer;

import java.util.Calendar;
import java.util.Date;

import rx.Observer;

/**
 * Bellboy Dialog Fragment
 */
public class BellboyDialogFragment extends DialogFragment {


    String bellboyMessage;
    BellboyDialogListener bellboyDialogListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.dialog_bellboy, null);
        builder.setView(view)
                .setPositiveButton(R.string.positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        EditText editBellboyMessage = (EditText)
                                view.findViewById(R.id.bellboy_message_edit_text);
                        bellboyMessage = editBellboyMessage.getText().toString();
                        sendBellboyRequest();
                        bellboyDialogListener.onDialogPositiveClick(BellboyDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.negative, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        bellboyDialogListener.onDialogNegativeClick(BellboyDialogFragment.this);

                    }
                });
        return builder.create();
    }

    /*
     * Send bellboy request to the server
     */
    public void sendBellboyRequest() {

        Bellboy bellboy = new Bellboy(bellboyMessage);

        String owner = "FRONT DESK";
        String type = "BELLBOY";
        String roomNumber = getActivity().getSharedPreferences("roomSettings", getActivity().MODE_PRIVATE)
                .getString("roomNumber", "none");
        String guestId= getActivity().getSharedPreferences("userSettings", getActivity().MODE_PRIVATE)
                .getString("guestId", "none");
        String state = "NEW";

        Calendar c = Calendar.getInstance();
        Date currentDate = c.getTime();

        PermintaanServer.getInstance(getActivity().getBaseContext()).createPermintaan(new Permintaan(
                owner,
                type,
                roomNumber,
                guestId,
                state,
                currentDate,
                currentDate,
                bellboy)
        ).subscribe(new Observer<Permintaan>() {
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
            public void onNext(Permintaan permintaan) {
                Log.d("Next", "On next");
            }
        });

    }

    public interface BellboyDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            bellboyDialogListener = (BellboyDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + "must implement BellboyDalogListener");
        }

    }


}
