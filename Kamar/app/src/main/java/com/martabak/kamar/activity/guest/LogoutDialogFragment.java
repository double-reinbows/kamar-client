package com.martabak.kamar.activity.guest;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.martabak.kamar.R;
import com.martabak.kamar.activity.home.SelectLanguageActivity;
import com.martabak.kamar.activity.home.SelectUserTypeActivity;
import com.martabak.kamar.service.StaffServer;

import rx.Observer;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogoutDialogFragment extends DialogFragment {

    String passwordString;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        final View view = layoutInflater.inflate(R.layout.dialog_logout, null);


        final EditText passwordEditText = (EditText)
                view.findViewById(R.id.password_edit_text);

        builder.setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {;
                        passwordString = passwordEditText.getText().toString();
                        sendLogoutRequest();

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        return builder.create();
    }

    /* Send logout request */
    private void sendLogoutRequest() {

        StaffServer.getInstance(getActivity()).login(passwordString).subscribe(new Observer<Boolean>() {
            @Override
            public void onCompleted() {
                Log.d(SelectUserTypeActivity.class.getCanonicalName(), "On completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(SelectUserTypeActivity.class.getCanonicalName(), "On error");
                e.printStackTrace();
            }

            @Override
            public void onNext(Boolean loginResponse) {
                Log.d(SelectUserTypeActivity.class.getCanonicalName(), "On next");
                Log.v("loginResponse", loginResponse.toString());
                if (loginResponse) {
                    logout();


                } else {
                    Context context = getActivity().getApplicationContext();
                    String text = getResources().getString(R.string.incorrect_password) + " ";
                    Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    /* Logout */
    private void logout() {

        Activity activity = getActivity();
        if (isAdded() && activity != null) {
            SharedPreferences pref = getActivity().getSharedPreferences("userSettings",
                    getActivity().MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("guestId", null);
            editor.commit();
            Intent intent = new Intent(getActivity(), SelectLanguageActivity.class);
            startActivity(intent);
        }


    }


}
