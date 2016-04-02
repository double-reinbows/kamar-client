package com.martabak.kamar.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.Permintaan;
import com.martabak.kamar.domain.permintaan.Content;
import com.martabak.kamar.domain.permintaan.Transport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.widget.ExpandableListView;
import java.util.Date;

/**
 * This fragment creates the requests/permintaan section of the staff homescreen.
 */
public  class StaffPermintaanFragment extends Fragment {

    public StaffPermintaanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CheckGuestInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StaffPermintaanFragment newInstance() {
        StaffPermintaanFragment fragment = new StaffPermintaanFragment();
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
        View view = inflater.inflate(R.layout.fragment_staff_permintaan, container, false);

        //add dummy permintaan to the list
        Permintaan permintaan = new Permintaan("Front Desk", "705", new Transport(
                "cabs are here", 4, null, "tebet"));

        //create the expandable list. Would like for it to not have to receive a
        //permintaan in future
        createExpandableList(view, permintaan);

        return view;
    }

    /**
     *
     */
    protected void createExpandableList(View view, Permintaan permintaan) {
        ExpandableListAdapter listAdapter;
        ExpandableListView expListView;
        List<String> listDataHeader;
        HashMap<String, List<String>> listDataChild;

        // get the listview
        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Set up header titles
        listDataHeader.add(this.getString(R.string.new_permintaan));
        listDataHeader.add(this.getString(R.string.processing_permintaan));
        listDataHeader.add(this.getString(R.string.in_delivery_permintaan));
        listDataHeader.add(this.getString(R.string.complete_permintaan));

        // Set up headers (states)
        List<String> new_permintaan = new ArrayList<String>();
        List<String> processing_permintaan = new ArrayList<String>();
        List<String> in_delivery_permintaan = new ArrayList<String>();
        List<String> complete_permintaan = new ArrayList<String>();


        // Set up child data
        if (permintaan.state == "NEW") {
            new_permintaan.add(permintaan.type+" - ROOM NUMBER "+permintaan.roomNumber+" ("+permintaan.id+")");
        } else if (permintaan.state == "PROCESSING") {
            processing_permintaan.add(permintaan.type+" - ROOM NUMBER "+permintaan.roomNumber+" ("+permintaan.id+")");
        } else if (permintaan.state == "IN DELIVERY") {
            in_delivery_permintaan.add(permintaan.type+" - ROOM NUMBER "+permintaan.roomNumber+" ("+permintaan.id+")");
        } else if (permintaan.state == "COMPLETE") {
            complete_permintaan.add(permintaan.type+" - ROOM NUMBER "+permintaan.roomNumber+" ("+permintaan.id+")");
        }

        listDataChild.put(listDataHeader.get(0), new_permintaan); // Header, Child data
        listDataChild.put(listDataHeader.get(1), processing_permintaan);
        listDataChild.put(listDataHeader.get(2), in_delivery_permintaan);
        listDataChild.put(listDataHeader.get(3), complete_permintaan);

        //create expandable list
        listAdapter = new ExpandableListAdapter(this.getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

}