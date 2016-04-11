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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.martabak.kamar.R;

/**
 * Change Room Number Fragment
 */
public class ChangeRoomNumberDialogFragment extends DialogFragment {

    String roomNumber;

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
        builder.setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {;
                        roomNumber = editText.getText().toString();
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
        SharedPreferences.Editor editor = languagePref.edit().
                    putString("roomNumber", roomNumber);
        editor.commit();


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
