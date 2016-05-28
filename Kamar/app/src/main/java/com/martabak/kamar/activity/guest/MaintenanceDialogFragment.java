package com.martabak.kamar.activity.guest;

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

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.dialog_maintenance, null);
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(R.string.positive, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        EditText editMaintenanceMessage = (EditText)
                                view.findViewById(R.id.maintenance_message_edit_text);
                        String maintenanceMessage = editMaintenanceMessage.getText().toString();
                        sendMaintenanceRequest(maintenanceMessage);
                    }
                })
                .setNegativeButton(R.string.negative, new DialogInterface.OnClickListener(){
                    @Override public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
    }

    /**
     * Send a maintenance request.
     */
    private void sendMaintenanceRequest(String maintenanceMessage) {
        Maintenance maintenance = new Maintenance(maintenanceMessage);

        String owner = Permintaan.OWNER_FRONTDESK;
        String type = Permintaan.TYPE_MAINTENANCE;
        String roomNumber = getActivity().getSharedPreferences("userSettings", getActivity().MODE_PRIVATE)
                .getString("roomNumber", null);
        String guestId= getActivity().getSharedPreferences("userSettings", getActivity().MODE_PRIVATE)
                .getString("guestId", null);
        String state = Permintaan.STATE_NEW;
        Date currentDate = Calendar.getInstance().getTime();

        if (guestId != null) {
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
                }

                @Override
                public void onError(Throwable e) {
                    Log.d(MaintenanceDialogFragment.class.getCanonicalName(), "On error");
                    e.printStackTrace();
                }

                @Override
                public void onNext(Permintaan permintaan) {
                    Log.d(MaintenanceDialogFragment.class.getCanonicalName(), "createPermintaan() On next" + permintaan);
                    if (permintaan != null) {
                        Toast.makeText(
                                MaintenanceDialogFragment.this.getActivity(),
                                getString(R.string.maintenance_result),
                                Toast.LENGTH_LONG
                        ).show();
                    } else {
                        Toast.makeText(
                                MaintenanceDialogFragment.this.getActivity(),
                                getString(R.string.something_went_wrong),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
            });
        }
    }
}
