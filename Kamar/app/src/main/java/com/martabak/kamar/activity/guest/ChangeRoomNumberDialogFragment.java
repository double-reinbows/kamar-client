package com.martabak.kamar.activity.guest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.martabak.kamar.R;
import com.martabak.kamar.activity.home.SelectUserTypeActivity;
import com.martabak.kamar.domain.Room;
import com.martabak.kamar.service.GuestServer;
import com.martabak.kamar.service.StaffServer;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;

/**
 * Change Room Number Fragment.
 */
public class ChangeRoomNumberDialogFragment extends DialogFragment {

    private String roomNumber;
    private String passwordString;
    private ArrayAdapter adapter;
    private ChangeRoomDialogListener changeRoomDialogListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        roomNumber = getActivity().getSharedPreferences("roomSettings", getActivity().MODE_PRIVATE)
                .getString("roomNumber", "none");
        final View view = layoutInflater.inflate(R.layout.dialog_change_room_number, null);
        final Spinner spinner = (Spinner) view.findViewById(R.id.change_room_number_spinner);
        final List<String> roomNumbers = getRoomNumbersWithoutGuests();

        adapter = new ArrayAdapter(getActivity().getBaseContext(),
                R.layout.support_simple_spinner_dropdown_item, roomNumbers);
        roomNumbers.add(0, getString(R.string.room_select));
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        adapter.notifyDataSetChanged();
        spinner.setAdapter(adapter);

        //spinner.setSelection(Integer.valueOf(roomNumber));
        final EditText passwordEditText = (EditText)
                view.findViewById(R.id.password_edit_text);

        builder.setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {;
                        roomNumber = roomNumbers.get((int)spinner.getSelectedItemId()).toString();
                        passwordString = passwordEditText.getText().toString();
                        changeRoomNumber();
                        changeRoomDialogListener.onChangeRoomDialogPositiveClick(ChangeRoomNumberDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        changeRoomDialogListener.onChangeRoomDialogNegativeClick(ChangeRoomNumberDialogFragment.this);
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
        final SharedPreferences.Editor editor = languagePref.edit().putString("roomNumber", roomNumber);

        StaffServer.getInstance(getActivity()).login(passwordString).subscribe(new Observer<Boolean>() {
            @Override public void onCompleted() {
                Log.d(ChangeRoomNumberDialogFragment.class.getCanonicalName(), "On completed");
            }
            @Override public void onError(Throwable e) {
                Log.d(ChangeRoomNumberDialogFragment.class.getCanonicalName(), "On error");
                e.printStackTrace();
            }
            @Override public void onNext(Boolean loginResponse) {
                Log.d(ChangeRoomNumberDialogFragment.class.getCanonicalName(), "On next");
                Log.v(ChangeRoomNumberDialogFragment.class.getCanonicalName(), "Login response: " + loginResponse.toString());
                if (loginResponse) {
                    editor.commit();
                } else {
                    String text = getString(R.string.incorrect_password) + " ";
                    Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*
     * Return a list of room numbers with no guests checked in.
     */
    private List<String> getRoomNumbersWithoutGuests() {
        final List <String> roomStrings = new ArrayList<String>();
        GuestServer.getInstance(getActivity().getBaseContext()).
                getRoomNumbers().subscribe(new Observer<List<Room>>() {
            @Override public void onCompleted() {
            }
            @Override public void onError(Throwable e) {
                Log.v(ChangeRoomNumberDialogFragment.class.getCanonicalName(),  "Error");
                e.printStackTrace();
            }
            @Override public void onNext(List<Room> rooms) {
                Log.v(ChangeRoomNumberDialogFragment.class.getCanonicalName(), "Next");
                for (int i=0; i < rooms.size(); i++) {
                    roomStrings.add(rooms.get(i).number);
                    Log.v(ChangeRoomNumberDialogFragment.class.getCanonicalName(),
                            "Room " + roomStrings.get(i));
                }

            }
        });
        return roomStrings;
    }

    /*
     * Get room number text.
     */
    public String getUpdatedRoomNumberText() {
        return roomNumber;
    }

    public interface ChangeRoomDialogListener {
        void onChangeRoomDialogPositiveClick(DialogFragment dialog);
        void onChangeRoomDialogNegativeClick(DialogFragment dialog);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        changeRoomDialogListener = (ChangeRoomDialogListener) activity;
    }

}
