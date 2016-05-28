package com.martabak.kamar.activity.restaurant;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.martabak.kamar.R;
import com.martabak.kamar.activity.restaurant.RestaurantExpandableListAdapter;
import com.martabak.kamar.domain.Consumable;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.service.MenuServer;
import com.martabak.kamar.service.PermintaanServer;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observer;

public class RestaurantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Food"));
        tabLayout.addTab(tabLayout.newTab().setText("Beverages"));
        tabLayout.addTab(tabLayout.newTab().setText("Desserts"));

        doGetConsumablesOfSectionAndCreateExpList("FOOD");
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //TextView textView = (TextView) findViewById(R.id.restaurant_text);
                //textView.setText(tab.getText().toString());
                Log.v("tab", tab.getText().toString());
                doGetConsumablesOfSectionAndCreateExpList(tab.getText().toString().toUpperCase());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //doGetConsumablesOfSectionAndCreateExpList(tab.toString());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                doGetConsumablesOfSectionAndCreateExpList(tab.getText().toString().toUpperCase());
            }
        });
    }
    protected void createExpandableList(List<Consumable> consumables) {
        RestaurantExpandableListAdapter listAdapter;
        ExpandableListView expListView;
        List<String> listDataHeader; //list of states
        HashMap<String, List<String>> listDataChild; //mapping of states to a list of permintaan strings
        HashMap<String, Consumable> listDataChildString; //mapping of permintaan strings to their permintaans

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        listDataChildString = new HashMap<String, Consumable>();

        /* Set up header titles
        listDataHeader.add(this.getString(R.string.new_permintaan));
        listDataHeader.add(this.getString(R.string.inprogress_permintaan));
        listDataHeader.add(this.getString(R.string.indelivery_permintaan));
        listDataHeader.add(this.getString(R.string.completed_permintaan));
*/
        for (Consumable consumable : consumables) {
            if (!listDataHeader.contains(consumable.subsection)) {
                listDataHeader.add(consumable.subsection);
                listDataChild.put(consumable.subsection, null);
            } else {
                List<String> currList = listDataChild.get(consumable.subsection);
                if (currList != null) {
                    currList.add(consumable.name);
                } else {
                    currList = new ArrayList<String>();
                    currList.add(consumable.name);
                }

            }

            listDataChildString.put(consumable.name, consumable);
        }

        /* Set up headers (states)
        List<String> new_permintaan = new ArrayList<String>();
        List<String> processing_permintaan = new ArrayList<String>();
        List<String> in_delivery_permintaan = new ArrayList<String>();
        List<String> complete_permintaan = new ArrayList<String>();

        // Set up child data
        for (Consumable consumable : consumables) {
            if (consumable.state.equals("NEW")) {
            /*    Log.v("Pulled permintaan", "state: "+permintaan.state+
                        ", owner: "+permintaan.owner+
                        ", type: "+permintaan.type+
                        ", roomNumber: "+permintaan.roomNumber);
                String permintaanString = consumable.type+" - Room No. "+permintaan.roomNumber+" - ID: "+permintaan.guestId
                        +" - Owner: "+consumable.owner;
                new_permintaan.add(permintaanString);
                listDataChildString.put(permintaanString, permintaan);
            } else if (consumable.state.equals("PROCESSING")) {
                String permintaanString = permintaan.type+" - Room No. "+permintaan.roomNumber+" - ID: "+permintaan.guestId
                        +" - Owner: "+permintaan.owner;
                processing_permintaan.add(permintaanString);
                listDataChildString.put(permintaanString, permintaan);
            } else if (permintaan.state.equals("IN DELIVERY")) {
                String permintaanString = permintaan.type+" - Room No. "+permintaan.roomNumber+" - ID: "+permintaan.guestId
                        +" - Owner: "+permintaan.owner;
                in_delivery_permintaan.add(permintaanString);
                listDataChildString.put(permintaanString, permintaan);
            } else if (permintaan.state.equals("COMPLETE")) {
                String permintaanString = permintaan.type+" - Room No. "+permintaan.roomNumber+" - ID: "+permintaan.guestId
                        +" - Owner: "+permintaan.owner;
                complete_permintaan.add(permintaanString);
                listDataChildString.put(permintaanString, permintaan);
            }
        }

        listDataChild.put(listDataHeader.get(0), new_permintaan); // Header, Child data
        listDataChild.put(listDataHeader.get(1), processing_permintaan);
        listDataChild.put(listDataHeader.get(2), in_delivery_permintaan);
        listDataChild.put(listDataHeader.get(3), complete_permintaan);
*/
        //create expandable list
        listAdapter = new RestaurantExpandableListAdapter(this, listDataHeader, listDataChild, listDataChildString);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    /**
     * Pulls the permintaans on the server based on specified states and, if successful,
     * creates the expandable list.
     */
    private void doGetConsumablesOfSectionAndCreateExpList(final String section) {
        Log.d(RestaurantActivity.class.getCanonicalName(), "Doing get consumables of section");


        MenuServer.getInstance(this).getMenuBySection(section)
                .subscribe(new Observer<Consumable>() {
                    List<Consumable> consumables = new ArrayList<>();
                    String roomNumber = getSharedPreferences("roomSettings", Context.MODE_PRIVATE).getString("roomNumber", "none");
                    @Override
                    public void onCompleted() {
                        Log.d(RestaurantActivity.class.getCanonicalName(), "On completed");
                        createExpandableList(consumables);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(RestaurantActivity.class.getCanonicalName(), "On error");
                        //TextView textView = (TextView) findViewById(R.id.doSomethingText);
                        //textView.setText(e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Consumable result) {
                        Log.d(RestaurantActivity.class.getCanonicalName(), "On next");
                        consumables.add(result);
                    }
                });
        Log.d("Test:", "test");
    }

}
