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
import android.widget.Toast;

import com.martabak.kamar.R;
import com.martabak.kamar.activity.guest.GuestHomeActivity;
import com.martabak.kamar.domain.permintaan.Housekeeping;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.service.PermintaanServer;

import java.util.Date;
import java.util.Calendar;

import rx.Observer;

/**
 * Housekeeping Dialog Fragment
 */
public class HousekeepingDialogFragment extends DialogFragment {

    private PermintaanDialogListener permintaanDialogListener;
    private Boolean success = false;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.dialog_housekeeping, null);
        final Button confirmButton = (Button) view.findViewById(R.id.housekeeping_confirm);
        final Button cancelButton = (Button) view.findViewById(R.id.housekeeping_cancel);

        final AlertDialog dialog= new AlertDialog.Builder(getActivity()).create();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTransportMessage = (EditText)
                        view.findViewById(R.id.housekeeping_message_edit_text);
                String housekeepingMessage = editTransportMessage.getText().toString();
                sendHousekeepingRequest(housekeepingMessage);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                permintaanDialogListener.onDialogNegativeClick(HousekeepingDialogFragment.this);
            }
        });

        dialog.setView(view);

        return dialog;


    }

    /**
     * Send a housekeeping request.
     */
    private void sendHousekeepingRequest(String housekeepingMessage) {
        Housekeeping housekeeping = new Housekeeping(housekeepingMessage);

        String owner = Permintaan.OWNER_FRONTDESK;
        String type = Permintaan.TYPE_HOUSEKEEPING;
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
                            currentDate,
                            housekeeping
                    )
            ).subscribe(new Observer<Permintaan>() {
                @Override public void onCompleted() {
                    permintaanDialogListener.onDialogPositiveClick(HousekeepingDialogFragment.this, success);
                    Log.d(HousekeepingDialogFragment.class.getCanonicalName(), "On completed");
                }
                @Override public void onError(Throwable e) {
                    Log.d(HousekeepingDialogFragment.class.getCanonicalName(), "On error");
                    e.printStackTrace();
                    success = false;
                }
                @Override public void onNext(Permintaan permintaan) {
                    Log.d(HousekeepingDialogFragment.class.getCanonicalName(), "On next");
                    if (permintaan != null) {
                        success = true;
                    } else {
                        success = false;
                    }
                }
            });
        }
        else {
            permintaanDialogListener.onDialogPositiveClick(HousekeepingDialogFragment.this, success);
        }
    }



    public void onAttach(Activity activity) {
        super.onAttach(activity);
        permintaanDialogListener = (PermintaanDialogListener) activity;
    }


}
