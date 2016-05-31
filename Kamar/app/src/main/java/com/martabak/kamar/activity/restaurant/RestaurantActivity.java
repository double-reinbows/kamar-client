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
                Log.v("tab", tab.getText().toString());
                doGetConsumablesOfSectionAndCreateExpList(tab.getText().toString().toUpperCase());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                doGetConsumablesOfSectionAndCreateExpList(tab.getText().toString().toUpperCase());
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
        //quantity dictionary with consumable.name keys
        HashMap<String, Integer> itemQuantityDict;

        //find where to inflate the exp list
        expListView = (ExpandableListView) findViewById(R.id.restaurant_exp_list);

        subsectionHeaders = new ArrayList<String>();
        itemTextDict = new HashMap<String, List<String>>();
        itemObjectDict = new HashMap<String, Consumable>();
        itemQuantityDict = new HashMap<String, Integer>();

        //iterate over the consumables for the current tab/section
        for (Consumable consumable : consumables) {
            if (!subsectionHeaders.contains(consumable.subsection)) {//subsection does not yet exist
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
            itemQuantityDict.put(consumable.name, 0); //set quantity to 0
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
    private void doGetConsumablesOfSectionAndCreateExpList(final String section) {
        Log.d(RestaurantActivity.class.getCanonicalName(), "Doing get consumables of section");


        MenuServer.getInstance(this).getMenuBySection(section)
                .subscribe(new Observer<Consumable>() {
                    List<Consumable> consumables = new ArrayList<>();

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
    }

}
