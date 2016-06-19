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

    private LogoutDialogListener logoutDialogListener;
    private boolean success = false;
    private String reason;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_logout, null);
        final EditText passwordEditText = (EditText)
                view.findViewById(R.id.password_edit_text);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {;
                        String password = passwordEditText.getText().toString();
                        sendLogoutRequest(password);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logoutDialogListener.onLogoutDialogNegativeClick(LogoutDialogFragment.this);
                    }
                })
                .create();
    }

    /**
     * Send the password to login as a staff, and logout as a guest.
     * @param password The password string.
     */
    private void sendLogoutRequest(String password) {
        StaffServer.getInstance(getActivity()).login(password).subscribe(new Observer<Boolean>() {
            @Override public void onCompleted() {
                Log.d(LogoutDialogFragment.class.getCanonicalName(), "On completed");
                logoutDialogListener.onLogoutDialogPositiveClick(LogoutDialogFragment.this, success, reason);
            }
            @Override public void onError(Throwable e) {
                Log.d(LogoutDialogFragment.class.getCanonicalName(), "On error");
                e.printStackTrace();
                reason = "Something went wrong";
                logoutDialogListener.onLogoutDialogPositiveClick(LogoutDialogFragment.this, success, reason);
            }
            @Override  public void onNext(Boolean loginResponse) {
                Log.d(LogoutDialogFragment.class.getCanonicalName(), "On next " + loginResponse.toString());
                success = loginResponse;
                if (!loginResponse) {
                    reason = "Incorrect Password";
                }

            }
        });
    }

    public interface LogoutDialogListener {
        void onLogoutDialogPositiveClick(DialogFragment dialog, Boolean success, String reason);
        void onLogoutDialogNegativeClick(DialogFragment dialog);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        logoutDialogListener = (LogoutDialogListener) activity;
    }

}
