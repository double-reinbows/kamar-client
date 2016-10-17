package com.martabak.kamar.activity.guest;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.managers.PermintaanManager;
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
    private String status;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.dialog_bellboy, null);
        final Button confirmButton = (Button) view.findViewById(R.id.bellboy_confirm);
        final Button cancelButton = (Button) view.findViewById(R.id.bellboy_cancel);
        status = null;

        final AlertDialog dialog= new AlertDialog.Builder(getActivity()).create();

        PermintaanManager.getInstance().getBellboyStatus(getActivity()).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(String s) {
                status = s;
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status == null || status.equals(Permintaan.STATE_COMPLETED)) {
                    sendBellboyRequest("");
                } else {
                    Toast.makeText(
                            BellboyDialogFragment.this.getActivity().getApplicationContext(),
                            R.string.existing_request,
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                permintaanDialogListener.onDialogNegativeClick(BellboyDialogFragment.this);
            }
        });

        dialog.setView(view);

        return dialog;
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
        if (!guestId.equals("none") && !roomNumber.equals("none")) {
            PermintaanServer.getInstance(getActivity().getBaseContext()).createPermintaan(new Permintaan(
                    owner,
                    type,
                    roomNumber,
                    guestId,
                    state,
                    currentDate,
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
