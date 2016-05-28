package com.martabak.kamar.activity.guest;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ExpandableListView;

import com.martabak.kamar.R;
import com.martabak.kamar.activity.guest.GuestExpandableListAdapter;
import com.martabak.kamar.activity.staff.StaffPermintaanFragment;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.service.PermintaanServer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import rx.Observer;
import rx.functions.Func1;

/**
 * This fragment creates the permintaan activity from the guest homescreen.
 */
public class GuestPermintaanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_permintaan);
        doGetPermintaansOfStateAndCreateExpList();
    }

    protected void createExpandableList(List<Permintaan> permintaans) {
        GuestExpandableListAdapter listAdapter;
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
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // Set up headers (states)
        List<String> new_permintaan = new ArrayList<>();
        List<String> inprogress_permintaan = new ArrayList<>();
        List<String> indelivery_permintaan = new ArrayList<>();
        List<String> completed_permintaan = new ArrayList<>();

        // Set up child data
        for (Permintaan permintaan : permintaans) {
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
                    Log.e(GuestPermintaanActivity.class.getCanonicalName(), "Unknown state for " + permintaan);
            }
        }

        listDataChild.put(listDataHeader.get(0), new_permintaan); // Header, Child data
        listDataChild.put(listDataHeader.get(1), inprogress_permintaan);
        listDataChild.put(listDataHeader.get(2), indelivery_permintaan);
        listDataChild.put(listDataHeader.get(3), completed_permintaan);

        //create expandable list
        listAdapter = new GuestExpandableListAdapter(this, listDataHeader, listDataChild, listDataChildString);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    /**
     * Pulls the permintaans on the server based on specified states and, if successful,
     * creates the expandable list.
     */
    private void doGetPermintaansOfStateAndCreateExpList() {
        Log.d(StaffPermintaanFragment.class.getCanonicalName(), "Doing get permintaans of state");

        final String roomNumber = getSharedPreferences("roomSettings", Context.MODE_PRIVATE).getString("roomNumber", "none");
        PermintaanServer.getInstance(this)
                .getPermintaansOfState(
                        Permintaan.STATE_NEW,
                        Permintaan.STATE_INPROGRESS,
                        Permintaan.STATE_INDELIVERY,
                        Permintaan.STATE_COMPLETED)
                .filter(new Func1<Permintaan, Boolean>() {
                    @Override public Boolean call(Permintaan permintaan) {
                        return permintaan.roomNumber.equals(roomNumber);
                    }
                })
                .subscribe(new Observer<Permintaan>() {
            List<Permintaan> permintaans = new ArrayList<>();
            @Override public void onCompleted() {
                Log.d(GuestPermintaanActivity.class.getCanonicalName(), "getPermintaansOfState() On completed");
                createExpandableList(permintaans);
            }
            @Override public void onError(Throwable e) {
                Log.d(GuestPermintaanActivity.class.getCanonicalName(), "getPermintaansOfState() On error");
                e.printStackTrace();
            }
            @Override public void onNext(Permintaan result) {
                Log.d(GuestPermintaanActivity.class.getCanonicalName(), "getPermintaansOfState() On next " + result);
                permintaans.add(result);
            }
        });
    }
}