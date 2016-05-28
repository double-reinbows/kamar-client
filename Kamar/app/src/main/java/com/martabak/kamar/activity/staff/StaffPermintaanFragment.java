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
        List<String> listDataHeader = Arrays.asList(
                getString(R.string.new_permintaan),
                getString(R.string.inprogress_permintaan),
                getString(R.string.indelivery_permintaan),
                getString(R.string.completed_permintaan)
        );
        HashMap<String, List<String>> listDataChild = new HashMap<>(); //mapping of states to a list of permintaan strings
        HashMap<String, Permintaan> listDataChildString = new HashMap<>(); //mapping of permintaan strings to their permintaans

        // get the listview
        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);

        // Set up headers (states)
        List<String> new_permintaan = new ArrayList<String>();
        List<String> inprogress_permintaan = new ArrayList<String>();
        List<String> indelivery_permintaan = new ArrayList<String>();
        List<String> completed_permintaan = new ArrayList<String>();

        String subUserType = getActivity().getSharedPreferences("userSettings", Context.MODE_PRIVATE).getString("subUserType", "none");
        Log.v("subUserType", subUserType);

        // Set up child data
        for (Permintaan permintaan : permintaans) {
            if (subUserType.equals(permintaan.owner)) {
                listDataChildString.put(permintaan.toString(), permintaan);

                switch (permintaan.state) {
                    case Permintaan.STATE_NEW:
                        new_permintaan.add(permintaan.toString());
                        break;
                    case Permintaan.STATE_INPROGRESS:
                        inprogress_permintaan.add(permintaan.toString());
                        break;
                    case Permintaan.STATE_INDELIVERY:
                        indelivery_permintaan.add(permintaan.toString());
                        break;
                    case Permintaan.STATE_COMPLETED:
                        completed_permintaan.add(permintaan.toString());
                        break;
                    default:
                        Log.e(StaffPermintaanFragment.class.getCanonicalName(), "Unknown state for " + permintaan);
                }
            }
        }

        listDataChild.put(listDataHeader.get(0), new_permintaan); // Header, Child data
        listDataChild.put(listDataHeader.get(1), inprogress_permintaan);
        listDataChild.put(listDataHeader.get(2), indelivery_permintaan);
        listDataChild.put(listDataHeader.get(3), completed_permintaan);

        // create expandable list
        listAdapter = new StaffExpandableListAdapter(this.getActivity(), listDataHeader, listDataChild, listDataChildString);

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
                        Permintaan.STATE_COMPLETED)
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