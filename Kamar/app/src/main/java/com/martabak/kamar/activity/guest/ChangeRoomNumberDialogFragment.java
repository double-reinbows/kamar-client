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
import rx.functions.Func1;

/**
 * Change Room Number Fragment.
 */
public class ChangeRoomNumberDialogFragment extends DialogFragment {

    private ArrayAdapter adapter;

    private ChangeRoomDialogListener changeRoomDialogListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.dialog_change_room_number, null);
        final Spinner spinner = (Spinner) view.findViewById(R.id.change_room_number_spinner);
        final List<String> roomNumbers = getRoomNumbers();

        adapter = new ArrayAdapter(getActivity().getBaseContext(),
                R.layout.support_simple_spinner_dropdown_item, roomNumbers);
        roomNumbers.add(0, getString(R.string.room_select));
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        adapter.notifyDataSetChanged();
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

        final EditText passwordEditText = (EditText)
                view.findViewById(R.id.password_edit_text);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {;
                        String roomNumber = roomNumbers.get((int)spinner.getSelectedItemId()).toString();
                        String password = passwordEditText.getText().toString();
                        changeRoomNumber(roomNumber, password);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        changeRoomDialogListener.onChangeRoomDialogNegativeClick(ChangeRoomNumberDialogFragment.this);
                    }
                })
                .create();
    }

    /**
     * Change the room number if the password is correct.
     * @param roomNumber The room number.
     * @param password The password string.
     */
    public void changeRoomNumber(final String roomNumber, String password) {
        StaffServer.getInstance(getActivity()).login(password).subscribe(new Observer<Boolean>() {
            boolean success = false;
            String reason;
            @Override public void onCompleted() {
                Log.v(ChangeRoomNumberDialogFragment.class.getCanonicalName(), "Success: " + success);
                changeRoomDialogListener.onChangeRoomDialogPositiveClick(ChangeRoomNumberDialogFragment.this, roomNumber, success, reason);
                Log.d(ChangeRoomNumberDialogFragment.class.getCanonicalName(), "On completed");
            }
            @Override public void onError(Throwable e) {
                Log.d(ChangeRoomNumberDialogFragment.class.getCanonicalName(), "On error");
                e.printStackTrace();
                reason = "Something went wrong";
                changeRoomDialogListener.onChangeRoomDialogPositiveClick(ChangeRoomNumberDialogFragment.this, roomNumber, success, reason);
            }
            @Override public void onNext(Boolean loginResponse) {
                Log.v(ChangeRoomNumberDialogFragment.class.getCanonicalName(), "Login response: " + loginResponse.toString());
                success = loginResponse;
                if (!loginResponse) {
                    reason = "Incorrect Password";
                }

            }
        });
    }

    /**
     * @return The list of room numbers.
     */
    private List<String> getRoomNumbers() {
        final List<String> roomStrings = new ArrayList<>();
        GuestServer.getInstance(getActivity().getBaseContext()).getRoomNumbers()
                .subscribe(new Observer<List<Room>>() {
                    @Override public void onCompleted() {}
                    @Override public void onError(Throwable e) { e.printStackTrace(); }
                    @Override public void onNext(List<Room> rooms) {
                        for (Room room : rooms) {
                            roomStrings.add(room.number);
                        }
                    }
                });
        return roomStrings;
    }

    public interface ChangeRoomDialogListener {
        void onChangeRoomDialogPositiveClick(DialogFragment dialog, String roomNumber, boolean success, String reason);
        void onChangeRoomDialogNegativeClick(DialogFragment dialog);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        changeRoomDialogListener = (ChangeRoomDialogListener) activity;
    }

}
