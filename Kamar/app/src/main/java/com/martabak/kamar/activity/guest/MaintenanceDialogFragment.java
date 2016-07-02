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
import com.martabak.kamar.domain.permintaan.Maintenance;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.service.PermintaanServer;

import java.util.Calendar;
import java.util.Date;

import rx.Observer;

/**
 * Maintenance Dialog Fragment
 */
public class MaintenanceDialogFragment extends DialogFragment {

    private PermintaanDialogListener permintaanDialogListener;
    private Boolean success = false;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.dialog_maintenance, null);
        final Button confirmButton = (Button) view.findViewById(R.id.maintenance_confirm);
        final Button cancelButton = (Button) view.findViewById(R.id.maintenance_cancel);

        final AlertDialog dialog= new AlertDialog.Builder(getActivity()).create();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editMaintenanceMessage = (EditText)
                        view.findViewById(R.id.maintenance_message_edit_text);
                String maintenanceMessage = editMaintenanceMessage.getText().toString();
                sendMaintenanceRequest(maintenanceMessage);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                permintaanDialogListener.onDialogNegativeClick(MaintenanceDialogFragment.this);
            }
        });

        dialog.setView(view);

        return dialog;
    }

    /**
     * Send a maintenance request.
     */
    private void sendMaintenanceRequest(String maintenanceMessage) {
        Maintenance maintenance = new Maintenance(maintenanceMessage);

        String owner = Permintaan.OWNER_FRONTDESK;
        String type = Permintaan.TYPE_MAINTENANCE;
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
                    maintenance)
            ).subscribe(new Observer<Permintaan>() {
                @Override
                public void onCompleted() {
                    Log.d(MaintenanceDialogFragment.class.getCanonicalName(), "On completed");
                    permintaanDialogListener.onDialogPositiveClick(MaintenanceDialogFragment.this, success);
                }

                @Override
                public void onError(Throwable e) {
                    Log.d(MaintenanceDialogFragment.class.getCanonicalName(), "On error");
                    e.printStackTrace();
                    success = false;
                    permintaanDialogListener.onDialogPositiveClick(MaintenanceDialogFragment.this, success);
                }

                @Override
                public void onNext(Permintaan permintaan) {
                    Log.d(MaintenanceDialogFragment.class.getCanonicalName(), "createPermintaan() On next" + permintaan);
                    if (permintaan != null) {
                        success = true;
                    } else {
                        success = false;
                    }
                }
            });
        }
        else {
            permintaanDialogListener.onDialogPositiveClick(MaintenanceDialogFragment.this, success);
        }
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        permintaanDialogListener = (PermintaanDialogListener) activity;
    }
}
