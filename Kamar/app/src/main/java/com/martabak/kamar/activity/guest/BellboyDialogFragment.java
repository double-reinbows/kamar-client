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

    private BellboyDialogListener bellboyDialogListener;

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
                        bellboyDialogListener.onDialogPositiveClick(BellboyDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.negative, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        bellboyDialogListener.onDialogNegativeClick(BellboyDialogFragment.this);
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
                .getString("roomNumber", null);
        String guestId= getActivity().getSharedPreferences("userSettings", getActivity().MODE_PRIVATE)
                .getString("guestId", null);
        String state = Permintaan.STATE_NEW;
        Date currentDate = Calendar.getInstance().getTime();

        if (guestId != "none") {
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
                    Log.d(BellboyDialogFragment.class.getCanonicalName(), "On completed");
                }
                @Override
                public void onError(Throwable e) {
                    Log.d(BellboyDialogFragment.class.getCanonicalName(), "On error");
                    e.printStackTrace();
                    Toast.makeText(
                            getActivity(),
                            getString(R.string.something_went_wrong),
                            Toast.LENGTH_SHORT
                    ).show();
                }
                @Override
                public void onNext(Permintaan permintaan) {
                    Log.d(BellboyDialogFragment.class.getCanonicalName(), "On next");
                    if (permintaan != null) {
                        Toast.makeText(
                                BellboyDialogFragment.this.getActivity(),
                                getString(R.string.bellboy_result),
                                Toast.LENGTH_LONG
                        ).show();
                    } else {
                        Toast.makeText(
                                BellboyDialogFragment.this.getActivity(),
                                getString(R.string.something_went_wrong),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
            });
        }
    }

    public interface BellboyDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        bellboyDialogListener = (BellboyDialogListener) activity;
    }

}
