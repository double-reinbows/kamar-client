package com.martabak.kamar.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;


import com.martabak.kamar.R;
import com.martabak.kamar.domain.Guest;
import com.martabak.kamar.domain.Room;
import com.martabak.kamar.service.GuestServer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Observer;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CheckGuestInFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CheckGuestInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckGuestInFragment extends Fragment {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String roomNumber;

    private OnFragmentInteractionListener mListener;

    public CheckGuestInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment CheckGuestInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckGuestInFragment newInstance() {
        CheckGuestInFragment fragment = new CheckGuestInFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_check_guest_in, container, false);

        final Spinner spinner = (Spinner) view.findViewById(R.id.guest_spinner);

        final List<String> roomNumbers = getRoomNumbersWithoutGuests();

        
        ArrayAdapter adapter = new ArrayAdapter(getActivity().getBaseContext(),
                R.layout.support_simple_spinner_dropdown_item, roomNumbers);
        roomNumbers.add(0, "Please Select a room");
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        adapter.notifyDataSetChanged();
        spinner.setAdapter(adapter);
        spinner.setSelection(0);


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.check_guest_in_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editTextFirstName = (EditText) getView().findViewById(R.id.guest_first_name);
                firstName = editTextFirstName.getText().toString();

                EditText editTextLastName = (EditText) getView().findViewById(R.id.guest_last_name);
                lastName = editTextLastName.getText().toString();

                EditText editTextPhoneNumber = (EditText) getView().findViewById(R.id.guest_phone);
                phoneNumber = editTextPhoneNumber.getText().toString();

                EditText editTextEmail = (EditText) getView().findViewById(R.id.guest_email);
                email = editTextEmail.getText().toString();

                roomNumber = roomNumbers.get((int)spinner.getSelectedItemId()).toString();

                sendGuestRequest();


                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        return view;
    }

    /*
     * return a list of Room Numbers with no guests checked in
     */
    private List<String> getRoomNumbersWithoutGuests() {

        final List <String> roomStrings = new ArrayList<String>();
        GuestServer.getInstance(getActivity().getBaseContext()).
                getRoomNumbers().subscribe(new Observer<List<Room>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.v("OnError",  "Error");
                e.printStackTrace();
            }

            @Override
            public void onNext(List<Room> rooms) {
                for (int i=0; i < rooms.size(); i++) {
                    roomStrings.add(rooms.get(i).number);
                    Log.v("Room", roomStrings.get(i));
                }
                Log.v("onNext", "Next");

            }
        });

        return roomStrings;

    }

    /*
     * Send create new guest request to the server
     */
    private void sendGuestRequest() {

        Calendar c = Calendar.getInstance();
        Date currentDate = c.getTime();

        c.add(Calendar.DAY_OF_MONTH,5);
        Date futureDate = c.getTime();

        Log.v("FutureDate", futureDate.toString());

        String welcomeMessage = "Hi " + firstName + "!";

        GuestServer.getInstance(getActivity().getBaseContext()).createGuest(new Guest(
                firstName,
                lastName,
                phoneNumber,
                email,
                currentDate,
                futureDate,
                roomNumber,
                welcomeMessage)
        ).subscribe(new Observer<Guest>() {
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
            public void onNext(Guest guest) {
                Log.d("Next", "On next");
                Log.v("GUEST", guest.toString());
            }

        });


    }




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
