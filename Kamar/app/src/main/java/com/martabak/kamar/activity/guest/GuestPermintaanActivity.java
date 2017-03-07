package com.martabak.kamar.activity.guest;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

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

        // Get the ActionBar here to configure the way it behaves.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });

        TextView roomNumberTextView = (TextView)findViewById(R.id.room_number);
        String roomNumber = getSharedPreferences("userSettings", MODE_PRIVATE)
                .getString("roomNumber", "none");
        // set room number text
//        roomNumberTextView.setText(getString(R.string.room_number) + ": " + roomNumber);

    }

    protected void createExpandableList(List<Permintaan> permintaans) {
        GuestExpandableListAdapter listAdapter;
        ExpandableListView expListView;
        List<String> states = Arrays.asList(
                Permintaan.STATE_NEW,
                Permintaan.STATE_INPROGRESS,
                Permintaan.STATE_COMPLETED
        );
        //mapping of states to a list of permintaan IDs
        HashMap<String, List<String>> statesToPermIds = new HashMap<>();
        //mapping of permintaan IDs to their permintaan object
        HashMap<String, Permintaan> idsToPermintaans = new HashMap<>();

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // Set up headers (states)
        List<String> new_permintaan = new ArrayList<>();
        List<String> inprogress_permintaan = new ArrayList<>();
        List<String> completed_permintaan = new ArrayList<>();

        // Set up child data
        for (Permintaan permintaan : permintaans) {
            idsToPermintaans.put(permintaan._id, permintaan);
            switch (permintaan.state) {
                case Permintaan.STATE_NEW:
                    new_permintaan.add(permintaan._id);
                    break;
                case Permintaan.STATE_INPROGRESS:
                    inprogress_permintaan.add(permintaan._id);
                    break;
                case Permintaan.STATE_COMPLETED:
                    completed_permintaan.add(permintaan._id);
                    break;
                default:
                    Log.e(GuestPermintaanActivity.class.getCanonicalName(), "Unknown state for " + permintaan);
            }
        }

        //set the state text and accompanying permintaan IDs
        statesToPermIds.put(states.get(0), new_permintaan);
        statesToPermIds.put(states.get(1), inprogress_permintaan);
        statesToPermIds.put(states.get(2), completed_permintaan);

        //create expandable list
        listAdapter = new GuestExpandableListAdapter(this, states, statesToPermIds, idsToPermintaans);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    /**
     * Pulls the permintaans on the server based on specified states and, if successful,
     * creates the expandable list.
     */
    private void doGetPermintaansOfStateAndCreateExpList() {
        final String guestId = getSharedPreferences("userSettings", Context.MODE_PRIVATE).getString("guestId", "none");
        Log.d(GuestPermintaanActivity.class.getCanonicalName(), "Doing get permintaans of state for guestId " + guestId);
        PermintaanServer.getInstance(this)
                .getPermintaansForGuest(guestId)
                .filter(new Func1<Permintaan, Boolean>() {
                    @Override public Boolean call(Permintaan permintaan) {
                        return permintaan.state.equals(Permintaan.STATE_NEW) ||
                                permintaan.state.equals(Permintaan.STATE_INPROGRESS) ||
                                permintaan.state.equals(Permintaan.STATE_COMPLETED);
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