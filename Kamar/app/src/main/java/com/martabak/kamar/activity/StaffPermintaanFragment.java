package com.martabak.kamar.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.domain.permintaan.Transport;
import com.martabak.kamar.service.PermintaanServer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import rx.Observer;

/**
 * This fragment creates the requests/permintaan section of the staff homescreen.
 */
public  class StaffPermintaanFragment extends Fragment {

    //public static Permintaan permintaan;

    public StaffPermintaanFragment() {
        // Required empty public constructor
    }

    /**
     */

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

        //add dummy permintaan to the list and create list
/*
        Permintaan permintaan = new Permintaan("Front Desk", "TRANSPORT", "705", "PADOOL", "NEW",
                new Date(), null, new Transport("cabs are here", 4, null, "tebet"));
        createExpandableList(view, permintaan);
*/
        //get the permintaans on the server and then create the expandable list
        doGetPermintaansOfStateAndCreateExpList();





        return view;
     }

    protected void createExpandableList(View view, List<Permintaan> permintaans) {
        ExpandableListAdapter listAdapter;
        ExpandableListView expListView;
        List<String> listDataHeader; //list of states
        HashMap<String, List<String>> listDataChild; //mapping of states to a list of permintaan strings
        HashMap<String, Permintaan> listDataChildString; //mapping of permintaan strings to their permintaans

        // get the listview
        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        listDataChildString = new HashMap<String, Permintaan>();

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
        for (Permintaan permintaan : permintaans) {
            if (permintaan.state.equals("NEW")) {
            /*    Log.v("Pulled permintaan", "state: "+permintaan.state+
                        ", owner: "+permintaan.owner+
                        ", type: "+permintaan.type+
                        ", roomNumber: "+permintaan.roomNumber);*/
                String permintaanString = permintaan.type+" - Room No. "+permintaan.roomNumber+" - ID: "+permintaan.guestId
                        +" - Owner: "+permintaan.owner;
                new_permintaan.add(permintaanString);
                //new_permintaan.add(permintaan);
                listDataChildString.put(permintaanString, permintaan);
            } else if (permintaan.state.equals("PROCESSING")) {
                String permintaanString = permintaan.type+" - Room No. "+permintaan.roomNumber+" - ID: "+permintaan.guestId
                        +" - Owner: "+permintaan.owner;
                processing_permintaan.add(permintaanString);
                //processing_permintaan.add(permintaan);
                listDataChildString.put(permintaanString, permintaan);
            } else if (permintaan.state.equals("IN DELIVERY")) {
                String permintaanString = permintaan.type+" - Room No. "+permintaan.roomNumber+" - ID: "+permintaan.guestId
                        +" - Owner: "+permintaan.owner;
                in_delivery_permintaan.add(permintaanString);
                //in_delivery_permintaan.add(permintaan);
                listDataChildString.put(permintaanString, permintaan);
            } else if (permintaan.state.equals("COMPLETE")) {
                String permintaanString = permintaan.type+" - Room No. "+permintaan.roomNumber+" - ID: "+permintaan.guestId
                        +" - Owner: "+permintaan.owner;
                complete_permintaan.add(permintaanString);
                //complete_permintaan.add(permintaan);
                listDataChildString.put(permintaanString, permintaan);
            }
        }

        listDataChild.put(listDataHeader.get(0), new_permintaan); // Header, Child data
        listDataChild.put(listDataHeader.get(1), processing_permintaan);
        listDataChild.put(listDataHeader.get(2), in_delivery_permintaan);
        listDataChild.put(listDataHeader.get(3), complete_permintaan);

        //create expandable list
        listAdapter = new ExpandableListAdapter(this.getActivity(), listDataHeader, listDataChild, listDataChildString);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    /**
     * Pulls the permintaans on the server based on specified states and, if successful,
     * creates the expandable list.
     */
    private void doGetPermintaansOfStateAndCreateExpList() {
        Log.d(StaffPermintaanFragment.class.getCanonicalName(), "Done get permintaans of state");


        PermintaanServer.getInstance(getActivity()).getPermintaansOfState("NEW", "PROCESSING", "IN DELIVERY", "COMPLETE")
                                                                            .subscribe(new Observer<Permintaan>() {
            List<Permintaan> permintaans = new ArrayList<>();

            @Override
            public void onCompleted() {
                Log.d(StaffPermintaanFragment.class.getCanonicalName(), "On completed");
                createExpandableList(getView(), permintaans);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(StaffPermintaanFragment.class.getCanonicalName(), "On error");
                //TextView textView = (TextView) findViewById(R.id.doSomethingText);
                //textView.setText(e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onNext(Permintaan result) {
                Log.d(StaffPermintaanFragment.class.getCanonicalName(), "On next");
                //TextView textView = (TextView) findViewById(R.id.doSomethingText);
                //textView.setText(result.toString() + " for " + result.guestId + " of type " + result.content.getType());
                Log.d("GuestID from server", result.guestId);
                permintaans.add(result);
                //createExpandableList(getView(), result);
            }
        });
        Log.d("Test:", "test");
    }
}