package com.martabak.kamar.activity.staff;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.service.PermintaanServer;
import com.martabak.kamar.util.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.view.WindowManager;
import android.widget.ExpandableListView;

import rx.Observer;

/**
 * This fragment responsible for the staff permintaans view.
 */
public  class StaffPermintaanFragment extends Fragment {

    public StaffPermintaanFragment() {
    }

    public static StaffPermintaanFragment newInstance() {
        return new StaffPermintaanFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staff_permintaan, container, false);
        doGetPermintaansOfStateAndCreateExpList();

        return view;
     }

    protected void createExpandableList(View view, List<Permintaan> permintaans) {
        StaffExpandableListAdapter listAdapter;
        ExpandableListView expListView;
        List<String> states = Arrays.asList(
                Permintaan.STATE_NEW,
                Permintaan.STATE_INPROGRESS,
                Permintaan.STATE_COMPLETED
        );
        HashMap<String, List<String>> stateToPermintaanIds = new HashMap<>(); //mapping of states to a list of permintaan strings
        HashMap<String, Permintaan> permintaanIdToPermintaan = new HashMap<>(); //mapping of permintaan strings to their permintaans

        // get the listview
        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);

        // Set up headers (states)
        List<String> new_permintaan = new ArrayList<>();
        List<String> inprogress_permintaan = new ArrayList<>();
        List<String> completed_permintaan = new ArrayList<>();

        // Set up child data
        for (Permintaan permintaan : permintaans) {
            permintaanIdToPermintaan.put(permintaan._id, permintaan);
            switch (permintaan.state) {
                case Permintaan.STATE_NEW:
                    new_permintaan.add(permintaan._id);
                    break;
                case Permintaan.STATE_INPROGRESS:
                    inprogress_permintaan.add(permintaan._id);
                    break;
                //case Permintaan.STATE_INDELIVERY:
                  //  indelivery_permintaan.add(permintaan._id);
                    //break;
                case Permintaan.STATE_COMPLETED:
                    completed_permintaan.add(permintaan._id);
                    break;
                //case Permintaan.STATE_CANCELLED:
                  //  cancelled_permintaan.add(permintaan._id);
                default:
                    Log.e(StaffPermintaanFragment.class.getCanonicalName(), "Unknown state for " + permintaan);
            }
        }

        stateToPermintaanIds.put(states.get(0), new_permintaan); // Header, Child data
        stateToPermintaanIds.put(states.get(1), inprogress_permintaan);
        stateToPermintaanIds.put(states.get(2), completed_permintaan);

        // create expandable list
        listAdapter = new StaffExpandableListAdapter(this.getActivity(), states, stateToPermintaanIds, permintaanIdToPermintaan, this);

        // setting list rooms
        expListView.setAdapter(listAdapter);

        // expanding all expandable groups
        for (int i=0; i<stateToPermintaanIds.keySet().size(); i++) {
            expListView.expandGroup(i);
        }

    }

    /**
     * Pulls the permintaans on the server that are within 3 days in the past then
     * creates an expandable list.
     */
    private void doGetPermintaansOfStateAndCreateExpList() {
        Log.d(StaffPermintaanFragment.class.getCanonicalName(), "Doing get permintaans of state");

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, Constants.PERMINTAAN_VIEW_WINDOW_FOR_STAFF_IN_DAYS); //current day -3 days
        PermintaanServer.getInstance(getActivity())
                .getPermintaansOfTime(new Date(c.getTimeInMillis()), new Date())
                .subscribe(new Observer<List<Permintaan>>() {
            List<Permintaan> permintaans = new ArrayList<>();
            @Override
            public void onCompleted() {
                createExpandableList(getView(), permintaans);
            }

            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onNext(List<Permintaan> result) {
                permintaans = result;
            }
        });
    }

    /**
     * Disables the entire screen from user input
     */
    public void disableUserInput() {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    /**
     * Enables the entire screen for user input
     */
    public void enableUserInput() {
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}