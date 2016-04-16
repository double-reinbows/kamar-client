package com.martabak.kamar.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.Permintaan;
import com.martabak.kamar.domain.Permintaan.Housekeeping;
import com.martabak.kamar.service.PermintaanServer;

import rx.Observer;

/**
 * Housekeeping Dialog Fragment
 */
public class HousekeepingDialogFragment extends DialogFragment {

    String housekeepingMessage;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.dialog_housekeeping, null);
        builder.setView(view)
                .setPositiveButton(R.string.positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        EditText editTransportMessage = (EditText)
                                view.findViewById(R.id.housekeeping_message_edit_text);
                        housekeepingMessage = editTransportMessage.getText().toString();

                        sendHousekeepingRequest();

                        GuestHomeActivity guestHomeActivity = (GuestHomeActivity) getActivity();
                        guestHomeActivity.makeToast("Housekeeping is on its way!");

                    }
                })
                .setNegativeButton(R.string.negative, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        return builder.create();
    }

    public void sendHousekeepingRequest() {

        Permintaan<Housekeeping> permintaan = new Permintaan<Housekeeping>();

        Housekeeping housekeeping = permintaan.new Housekeeping(housekeepingMessage);

        String owner = "HOUSEKEEPING";
        String roomNumber = getActivity().getSharedPreferences("roomSettings", getActivity().MODE_PRIVATE)
                .getString("roomNumber", "none");

        PermintaanServer.getInstance(getActivity().getBaseContext()).createPermintaan(new Permintaan(
                        owner,
                        roomNumber,
                        housekeeping)
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
}
