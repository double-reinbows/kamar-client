package com.martabak.kamar.activity.staff;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.service.PermintaanServer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
                Permintaan.STATE_INDELIVERY,
                Permintaan.STATE_COMPLETED,
                Permintaan.STATE_CANCELLED
        );
        HashMap<String, List<String>> stateToPermintaanIds = new HashMap<>(); //mapping of states to a list of permintaan strings
        HashMap<String, Permintaan> permintaanIdToPermintaan = new HashMap<>(); //mapping of permintaan strings to their permintaans

        // get the listview
        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);

        // Set up headers (states)
        List<String> new_permintaan = new ArrayList<>();
        List<String> inprogress_permintaan = new ArrayList<>();
        List<String> indelivery_permintaan = new ArrayList<>();
        List<String> completed_permintaan = new ArrayList<>();
        List<String> cancelled_permintaan = new ArrayList<>();

        String subUserType = getActivity().getSharedPreferences("userSettings", Context.MODE_PRIVATE).getString("subUserType", "none");
        Log.v("subUserType", subUserType);

        // Set up child data
        for (Permintaan permintaan : permintaans) {
            if (subUserType.equals(permintaan.owner)) {
                permintaanIdToPermintaan.put(permintaan._id, permintaan);
                switch (permintaan.state) {
                    case Permintaan.STATE_NEW:
                        new_permintaan.add(permintaan._id);
                        break;
                    case Permintaan.STATE_INPROGRESS:
                        inprogress_permintaan.add(permintaan._id);
                        break;
                    case Permintaan.STATE_INDELIVERY:
                        indelivery_permintaan.add(permintaan._id);
                        break;
                    case Permintaan.STATE_COMPLETED:
                        completed_permintaan.add(permintaan._id);
                        break;
                    case Permintaan.STATE_CANCELLED:
                        cancelled_permintaan.add(permintaan._id);
                    default:
                        Log.e(StaffPermintaanFragment.class.getCanonicalName(), "Unknown state for " + permintaan);
                }
            }
        }

        stateToPermintaanIds.put(states.get(0), new_permintaan); // Header, Child data
        stateToPermintaanIds.put(states.get(1), inprogress_permintaan);
        stateToPermintaanIds.put(states.get(2), indelivery_permintaan);
        stateToPermintaanIds.put(states.get(3), completed_permintaan);
        stateToPermintaanIds.put(states.get(4), cancelled_permintaan);

        // create expandable list
        listAdapter = new StaffExpandableListAdapter(this.getActivity(), states, stateToPermintaanIds, permintaanIdToPermintaan);

        // setting list rooms
        expListView.setAdapter(listAdapter);
    }

    /**
     * Pulls the permintaans on the server based on specified states and, if successful,
     * creates the expandable list.
     */
    private void doGetPermintaansOfStateAndCreateExpList() {
        Log.d(StaffPermintaanFragment.class.getCanonicalName(), "Doing get permintaans of state");

        PermintaanServer.getInstance(getActivity())
                .getPermintaansOfState(
                        Permintaan.STATE_NEW,
                        Permintaan.STATE_INPROGRESS,
                        Permintaan.STATE_INDELIVERY,
                        Permintaan.STATE_COMPLETED,
                        Permintaan.STATE_CANCELLED)
                .subscribe(new Observer<Permintaan>() {
            List<Permintaan> permintaans = new ArrayList<>();

            @Override public void onCompleted() {
                Log.d(StaffPermintaanFragment.class.getCanonicalName(), "On completed");
                createExpandableList(getView(), permintaans);
            }

            @Override public void onError(Throwable e) {
                Log.d(StaffPermintaanFragment.class.getCanonicalName(), "On error");
                e.printStackTrace();
            }

            @Override public void onNext(Permintaan result) {
                Log.d(StaffPermintaanFragment.class.getCanonicalName(), "On next");
                permintaans.add(result);
            }
        });
    }
}