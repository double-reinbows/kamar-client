package com.martabak.kamar.activity;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.Permintaan;
import com.martabak.kamar.service.PermintaanServer;

import rx.Observer;

/**
 * Maintenance Dialog Fragment
 */
public class MaintenanceDialogFragment extends DialogFragment {

    String maintenanceMessage;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.dialog_maintenance, null);
        builder.setView(view)
                .setPositiveButton(R.string.positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        EditText editMaintenanceMessage = (EditText)
                                view.findViewById(R.id.maintenance_message_edit_text);
                        maintenanceMessage = editMaintenanceMessage.getText().toString();

                        sendMaintenanceRequest();

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

    /* Send Maintenance Request */
    public void sendMaintenanceRequest() {

        Permintaan permintaan = new Permintaan();

        Permintaan.Maintenance maintenance = permintaan.new Maintenance(maintenanceMessage);

        Log.v(maintenance.getType(), maintenance.getClass().toString());

        String owner = "MAINTENANCE";
        String roomNumber = getActivity().getSharedPreferences("roomSettings", getActivity().MODE_PRIVATE)
                .getString("roomNumber", "none");

        PermintaanServer.getInstance(getActivity().getBaseContext()).createPermintaan(new Permintaan(
                        owner,
                        roomNumber,
                        maintenance)
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
