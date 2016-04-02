package com.martabak.kamar.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import com.martabak.kamar.R;
import com.martabak.kamar.domain.Guest;
import com.martabak.kamar.service.GuestServer;

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
public  class CheckGuestInFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;

    private OnFragmentInteractionListener mListener;

    public CheckGuestInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CheckGuestInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckGuestInFragment newInstance(String param1, String param2) {
        CheckGuestInFragment fragment = new CheckGuestInFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view =  inflater.inflate(R.layout.fragment_check_guest_in, container, false);
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

                sendGuestRequest();


                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        return view;
    }


    private void sendGuestRequest() {

        GuestServer.getInstance(getActivity().getBaseContext()).createGuest(new Guest(
                firstName,
                lastName,
                phoneNumber,
                email,
                null,
                null,
                "1")
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
