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

import com.martabak.kamar.R;
import com.martabak.kamar.domain.Consumable;
import com.martabak.kamar.service.MenuServer;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observer;

/**
 * This activity generates the menu which users can order food, beverages and desserts
 * from. It pulls menu data from the server and then displays it in an interactive
 * menu.
 */
public class RestaurantActivity extends AppCompatActivity {

    //quantity dictionary with consumable.name keys
    private HashMap<String, Integer> itemQuantityDict;
    //lists of sections
    private List<Consumable> food;
    private List<Consumable> beverages;
    private List<Consumable> desserts;

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

        //initialize constant variables
        itemQuantityDict = new HashMap<String, Integer>();
        food = new ArrayList<>();
        beverages = new ArrayList<>();
        desserts = new ArrayList<>();

        doGetConsumablesOfSectionAndCreateExpList("FOOD", food); //since 1st tab is always food
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.v("tab", tab.getText().toString());
                String selectedTab = tab.getText().toString();
                if (selectedTab.equals("Food")) {
                    doGetConsumablesOfSectionAndCreateExpList(selectedTab.toUpperCase(), food);
                } else if (selectedTab.equals("Beverages")) {
                    doGetConsumablesOfSectionAndCreateExpList(selectedTab.toUpperCase(), beverages);
                } else if (selectedTab.equals("Desserts")) {
                    doGetConsumablesOfSectionAndCreateExpList(selectedTab.toUpperCase(), desserts);
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    /**
     * Creates an exp list then sets the restaurant exp list onto it
     */
    protected void createExpandableList(List<Consumable> consumables) {
        RestaurantExpandableListAdapter listAdapter;
        ExpandableListView expListView;
        List<String> subsectionHeaders; //header text
        //a list of each item's main text with header text as keys
        HashMap<String, List<String>> itemTextDict;
        //consumable dictionary with consumable.name keys
        HashMap<String, Consumable> itemObjectDict;

        //find where to inflate the exp list
        expListView = (ExpandableListView) findViewById(R.id.restaurant_exp_list);

        subsectionHeaders = new ArrayList<String>();
        itemTextDict = new HashMap<String, List<String>>();
        itemObjectDict = new HashMap<String, Consumable>();

        //iterate over the consumables for the current tab/section
        for (Consumable consumable : consumables) {
            if (!subsectionHeaders.contains(consumable.subsection)) {//subsection doesn't exist yet
                subsectionHeaders.add(consumable.subsection); //add subsection
                List<String> currList = new ArrayList<>();
                currList.add(consumable.name); //add displayed text (food's name) to list
                itemTextDict.put(consumable.subsection, currList); //add list to subsection
            } else {//subsection already added
                List<String> currList = itemTextDict.get(consumable.subsection);
                currList.add(consumable.name);
                itemTextDict.put(consumable.subsection, currList); //overwrite list with new one
            }
            itemObjectDict.put(consumable.name, consumable); //set consumable's object
            if (!itemQuantityDict.containsKey(consumable.name)) {
                itemQuantityDict.put(consumable.name, 0); //set quantity to 0
            }
        }

        //set up restaurant expandable list adapter
        listAdapter = new RestaurantExpandableListAdapter(this, subsectionHeaders, itemTextDict,
                                                            itemObjectDict, itemQuantityDict);

        //set list adapter onto exp list
        expListView.setAdapter(listAdapter);
    }

    /**
     * Pulls the consumables on the server based on the selected section (tab) and, if successful,
     * calls createExpandableList().
     */
    private void doGetConsumablesOfSectionAndCreateExpList(final String section,
                                                            final List<Consumable> consumables) {
        Log.d(RestaurantActivity.class.getCanonicalName(), "Doing get consumables of section");

        if (consumables.isEmpty()) { //if we haven't pulled the section's consumables yet...
            MenuServer.getInstance(this).getMenuBySection(section)
                    .subscribe(new Observer<Consumable>() {
                        //List<Consumable> consumables = new ArrayList<>();

                        @Override
                        public void onCompleted() {
                            Log.d(RestaurantActivity.class.getCanonicalName(), "On completed");
                            createExpandableList(consumables);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(RestaurantActivity.class.getCanonicalName(), "On error");
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(Consumable result) {
                            Log.d(RestaurantActivity.class.getCanonicalName(), "On next");
                            consumables.add(result);
                        }
                    });
        } else { //skip pulling consumables from server
            createExpandableList(consumables);
        }
    }

}
