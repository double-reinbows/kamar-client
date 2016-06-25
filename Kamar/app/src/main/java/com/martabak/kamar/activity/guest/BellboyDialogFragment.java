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

    private PermintaanDialogListener permintaanDialogListener;
    private Boolean success = false;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.dialog_bellboy, null);
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(R.string.positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editBellboyMessage = (EditText)
                                view.findViewById(R.id.bellboy_message_edit_text);
                        String bellboyMessage = editBellboyMessage.getText().toString();
                        sendBellboyRequest(bellboyMessage);
                    }
                })
                .setNegativeButton(R.string.negative, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        permintaanDialogListener.onDialogNegativeClick(BellboyDialogFragment.this);
                    }
                })
                .create();
    }

    /**
     * Send bellboy request to the server.
     */
    public void sendBellboyRequest(String bellboyMessage) {
        Bellboy bellboy = new Bellboy(bellboyMessage);

        String owner = Permintaan.OWNER_FRONTDESK;
        String type = Permintaan.TYPE_BELLBOY;
        String roomNumber = getActivity().getSharedPreferences("userSettings", getActivity().MODE_PRIVATE)
                .getString("roomNumber", "none");
        String guestId= getActivity().getSharedPreferences("userSettings", getActivity().MODE_PRIVATE)
                .getString("guestId", "none");
        String state = Permintaan.STATE_NEW;
        Date currentDate = Calendar.getInstance().getTime();
        if (!guestId.equals("none")) {
            PermintaanServer.getInstance(getActivity().getBaseContext()).createPermintaan(new Permintaan(
                    owner,
                    type,
                    roomNumber,
                    guestId,
                    state,
                    currentDate,
                    null,
                    bellboy)
            ).subscribe(new Observer<Permintaan>() {
                @Override
                public void onCompleted() {
                    Log.d(BellboyDialogFragment.class.getCanonicalName(), "On completed");
                    permintaanDialogListener.onDialogPositiveClick(BellboyDialogFragment.this, success);
                }
                @Override
                public void onError(Throwable e) {
                    Log.d(BellboyDialogFragment.class.getCanonicalName(), "On error");
                    e.printStackTrace();
                    success = false;
                    permintaanDialogListener.onDialogPositiveClick(BellboyDialogFragment.this, success);
                }
                @Override
                public void onNext(Permintaan permintaan) {
                    Log.d(BellboyDialogFragment.class.getCanonicalName(), "On next");
                    if (permintaan != null) {
                        success = true;
                    } else {
                        success = false;
                    }
                }
            });
        }
        else {
            permintaanDialogListener.onDialogPositiveClick(BellboyDialogFragment.this, success);
        }
    }



    public void onAttach(Activity activity) {
        super.onAttach(activity);
        permintaanDialogListener = (PermintaanDialogListener) activity;
    }

}
