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
                        dialog.dismiss();
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
            }
            @Override public void onError(Throwable e) {
                Log.d(LogoutDialogFragment.class.getCanonicalName(), "On error");
                e.printStackTrace();
                Toast.makeText(
                        LogoutDialogFragment.this.getActivity(),
                        getString(R.string.something_went_wrong),
                        Toast.LENGTH_LONG
                ).show();
            }
            @Override  public void onNext(Boolean loginResponse) {
                Log.d(LogoutDialogFragment.class.getCanonicalName(), "On next " + loginResponse.toString());
                if (loginResponse) {
                    logout();
                } else {
                    Toast.makeText(
                            getActivity(),
                            getString(R.string.incorrect_password),
                            Toast.LENGTH_LONG
                    ).show();
                }
            }
        });
    }

    /**
     * Logout the guest.
     */
    private void logout() {
        Activity activity = getActivity();
        if (isAdded() && activity != null) {
            getActivity().getSharedPreferences("userSettings", getActivity().MODE_PRIVATE)
                    .edit()
                    .putString("guestId", null)
                    .commit();
            Toast.makeText(
                    getActivity(),
                    getString(R.string.logout_result),
                    Toast.LENGTH_LONG
            ).show();
            startActivity(new Intent(getActivity(), SelectLanguageActivity.class));
            getActivity().finish();
        }
    }

}
