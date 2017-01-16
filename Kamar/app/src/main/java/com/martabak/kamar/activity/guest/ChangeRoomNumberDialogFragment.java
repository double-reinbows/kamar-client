package com.martabak.kamar.activity.guest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.Room;
import com.martabak.kamar.service.GuestServer;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;

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


        final Button confirmButton = (Button) view.findViewById(R.id.change_room_number_confirm);
        final Button cancelButton = (Button) view.findViewById(R.id.change_room_number_cancel);

        final AlertDialog dialog= new AlertDialog.Builder(getActivity()).create();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String roomNumber = roomNumbers.get((int)spinner.getSelectedItemId()).toString();
                changeRoomNumber(roomNumber);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                changeRoomDialogListener.onChangeRoomDialogNegativeClick(
                        ChangeRoomNumberDialogFragment.this);
            }
        });

        dialog.setView(view);

        return dialog;
    }


    /**
     * Change the room number.
     * @param roomNumber The room number.
     */
    public void changeRoomNumber(final String roomNumber) {
        changeRoomDialogListener.onChangeRoomDialogPositiveClick(
                ChangeRoomNumberDialogFragment.this, roomNumber, true, null);
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
        void onChangeRoomDialogPositiveClick(DialogFragment dialog, String roomNumber,
                                             boolean success, String reason);
        void onChangeRoomDialogNegativeClick(DialogFragment dialog);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        changeRoomDialogListener = (ChangeRoomDialogListener) activity;
    }

}
