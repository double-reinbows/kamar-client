package com.martabak.kamar.activity;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
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
 * A simple {@link Fragment} subclass.
 */
public class BellboyDialogFragment extends DialogFragment {


    String bellboyMessage;

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

    public void sendBellboyRequest() {

        Permintaan<Permintaan.Bellboy> permintaan = new Permintaan<Permintaan.Bellboy>();

        Permintaan.Bellboy bellboy = permintaan.new Bellboy(bellboyMessage);

        String owner = "BELLBOY";
        String roomNumber = "1";

        PermintaanServer.getInstance(getActivity().getBaseContext()).createPermintaan(new Permintaan(
                        owner,
                        roomNumber,
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

}
