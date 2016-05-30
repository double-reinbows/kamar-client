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
import java.util.LinkedHashMap;
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
        HashMap<String, Integer> quantityDict;

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        listDataChildString = new HashMap<String, Consumable>();
        quantityDict = new HashMap<String, Integer>();

        for (Consumable consumable : consumables) {
            if (!listDataHeader.contains(consumable.subsection)) {
                listDataHeader.add(consumable.subsection);
                List<String> currList = new ArrayList<>();
                currList.add(consumable.name);
                listDataChild.put(consumable.subsection, currList);
            } else { //subsection already exists
                List<String> currList = listDataChild.get(consumable.subsection);
                currList.add(consumable.name);
                listDataChild.put(consumable.subsection, currList);
            }
            listDataChildString.put(consumable.name, consumable);
            quantityDict.put(consumable.name, 0);

        }

        //create expandable list
        listAdapter = new RestaurantExpandableListAdapter(this, listDataHeader, listDataChild,
                                                            listDataChildString, quantityDict);

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
                    String roomNumber = getSharedPreferences("roomSettings", Context.MODE_PRIVATE)
                                                                .getString("roomNumber", "none");
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
    }

}
