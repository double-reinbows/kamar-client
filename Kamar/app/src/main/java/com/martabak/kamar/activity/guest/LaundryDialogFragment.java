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
import com.martabak.kamar.domain.User;
import com.martabak.kamar.domain.managers.PermintaanManager;
import com.martabak.kamar.domain.permintaan.Bellboy;
import com.martabak.kamar.domain.permintaan.LaundryOrder;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.service.PermintaanServer;

import java.util.Calendar;
import java.util.Date;

import rx.Observer;



/**
 * Laundry Dialog Fragment
 */
public class LaundryDialogFragment extends DialogFragment {

    private PermintaanDialogListener permintaanDialogListener;
    private Boolean success = false;
    private String status;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.dialog_laundry, null);
        final Button confirmButton = (Button) view.findViewById(R.id.bellboy_confirm);
        final Button cancelButton = (Button) view.findViewById(R.id.bellboy_cancel);
        status = null;

        final AlertDialog dialog= new AlertDialog.Builder(getActivity()).create();

        PermintaanManager.getInstance().getBellboyStatus(getActivity()).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.d(LaundryDialogFragment.class.getCanonicalName(), "getBellboyStatus complete");
            }
            @Override
            public void onError(Throwable e) {
                Log.e(LaundryDialogFragment.class.getCanonicalName(), "Error getting bellboy status", e);
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
                    sendLaundryRequest("");
                } else {
                    Toast.makeText(
                            LaundryDialogFragment.this.getActivity().getApplicationContext(),
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
                permintaanDialogListener.onDialogNegativeClick(LaundryDialogFragment.this);
            }
        });

        dialog.setView(view);

        return dialog;
    }

    /**
     * Send bellboy request to the server.
     */
    public void sendLaundryRequest(String laundryMessage) {
        LaundryOrder laundryOrder = new LaundryOrder(laundryMessage);

        String owner = User.TYPE_STAFF_FRONTDESK;
        String type = Permintaan.TYPE_LAUNDRY;
        String creator = getActivity().getSharedPreferences("userSettings", getActivity().MODE_PRIVATE)
                .getString("userType", "none");
        String roomNumber = getActivity().getSharedPreferences("userSettings", getActivity().MODE_PRIVATE)
                .getString("roomNumber", "none");
        String guestId= getActivity().getSharedPreferences("userSettings", getActivity().MODE_PRIVATE)
                .getString("guestId", "none");
        String state = Permintaan.STATE_NEW;
        Date currentDate = Calendar.getInstance().getTime();
        if (!guestId.equals("none") && !roomNumber.equals("none")) {
            PermintaanServer.getInstance(getActivity().getBaseContext()).createPermintaan(new Permintaan(
                    owner,
                    creator,
                    type,
                    roomNumber,
                    guestId,
                    state,
                    currentDate,
                    laundryOrder)
            ).subscribe(new Observer<Permintaan>() {
                @Override
                public void onCompleted() {
                    Log.d(LaundryDialogFragment.class.getCanonicalName(), "On completed");
                    permintaanDialogListener.onDialogPositiveClick(LaundryDialogFragment.this, success);
                }
                @Override
                public void onError(Throwable e) {
                    Log.e(LaundryDialogFragment.class.getCanonicalName(), "On error", e);
                    success = false;
                    permintaanDialogListener.onDialogPositiveClick(LaundryDialogFragment.this, success);
                }
                @Override
                public void onNext(Permintaan permintaan) {
                    Log.d(LaundryDialogFragment.class.getCanonicalName(), "On next");
                    if (permintaan != null) {
                        success = true;
                    } else {
                        success = false;
                    }
                }
            });
        } else {
            permintaanDialogListener.onDialogPositiveClick(LaundryDialogFragment.this, success);
        }
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        permintaanDialogListener = (PermintaanDialogListener) activity;
    }

}
