package com.martabak.kamar.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.martabak.kamar.R;
import com.martabak.kamar.service.StaffServer;

import rx.Observer;

/**
 * Change Room Number Fragment
 */
public class ChangeRoomNumberDialogFragment extends DialogFragment {

    String roomNumber;
    String passwordString;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        roomNumber = getActivity().getSharedPreferences("roomSettings", getActivity().MODE_PRIVATE)
                    .getString("roomNumber", "none");
        final View view = layoutInflater.inflate(R.layout.dialog_change_room_number, null);
        final EditText editText = (EditText)
                view.findViewById(R.id.room_number_edit_text);
        editText.setText(roomNumber);

        final EditText passwordEditText = (EditText)
                view.findViewById(R.id.password_edit_text);

        builder.setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {;
                        roomNumber = editText.getText().toString();
                        passwordString = passwordEditText.getText().toString();
                        changeRoomNumber();

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



    /*
     * Change room number
     */
    public void changeRoomNumber() {

        SharedPreferences languagePref = getActivity().getSharedPreferences("roomSettings",
                getActivity().MODE_PRIVATE);
        final SharedPreferences.Editor editor = languagePref.edit().
                    putString("roomNumber", roomNumber);


        StaffServer.getInstance(getActivity()).login(passwordString).subscribe(new Observer<Boolean>() {
            @Override
            public void onCompleted() {
                Log.d(SelectUserTypeActivity.class.getCanonicalName(), "On completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(SelectUserTypeActivity.class.getCanonicalName(), "On error");
                //TextView textView = (TextView) findViewById(R.id.doSomethingText);
                //textView.setText(e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onNext(Boolean loginResponse) {
                Log.d(SelectUserTypeActivity.class.getCanonicalName(), "On next");
                Log.v("loginResponse", loginResponse.toString());
                if (loginResponse) {
                    editor.commit();

                } else {
                    Context context = getActivity().getApplicationContext();
                    String text = getResources().getString(R.string.incorrect_password) + " ";
                    Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });


    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
