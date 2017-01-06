package com.martabak.kamar.activity.guest;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.martabak.kamar.R;
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

        final Button confirmButton = (Button) view.findViewById(R.id.logout_confirm);
        final Button cancelButton = (Button) view.findViewById(R.id.logout_cancel);

        final AlertDialog dialog= new AlertDialog.Builder(getActivity()).create();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordEditText.getText().toString();
                sendLogoutRequest(password);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialogListener.onLogoutDialogNegativeClick(LogoutDialogFragment.this);
            }
        });

        dialog.setView(view);

        return dialog;
    }

    /**
     * Send the password to login as a staff (does NOT logout the guest).
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
