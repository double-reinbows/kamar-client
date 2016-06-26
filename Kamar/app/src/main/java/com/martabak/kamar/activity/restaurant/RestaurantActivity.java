package com.martabak.kamar.activity.restaurant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.Consumable;
import com.martabak.kamar.service.MenuServer;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rx.Observer;

/**
 * This activity generates the menu which users can order food, beverages and desserts
 * from. It pulls menu data from the server and then displays it in an interactive
 * menu.
 */
public class RestaurantActivity extends AppCompatActivity {

    //quantity dictionary with consumable.name keys
    private HashMap<String, Integer> itemQuantityDict;
    private HashMap<String, Consumable> itemObjectDict;
    //lists of sections
    private List<Consumable> food;
    private List<Consumable> beverages;
    private List<Consumable> desserts;
    RestaurantExpandableListAdapter listAdapter;
    private ExpandableListView foodExpListView;
    private ExpandableListView bevExpListView;
    private ExpandableListView dessExpListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        Toolbar toolbar = (Toolbar) findViewById(R.id.guest_toolbar);
        setSupportActionBar(toolbar);

        TextView roomNumberTextView = (TextView)findViewById(R.id.toolbar_roomnumber);
        String roomNumber = getSharedPreferences("userSettings", MODE_PRIVATE)
                .getString("roomNumber", null);

        // set room number text
        roomNumberTextView.setText(getString(R.string.room_number) + " " + roomNumber);
        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/century-gothic.ttf");

        //initialize tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Food"));
        tabLayout.addTab(tabLayout.newTab().setText("Beverages"));
        tabLayout.addTab(tabLayout.newTab().setText("Desserts"));

        //set tab text
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int numTabs = vg.getChildCount();
        for (int j = 0; j < numTabs; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(customFont);
                }
            }
        }

        //initialize constant variables
        itemQuantityDict = new HashMap<String, Integer>();
        itemObjectDict = new HashMap<String, Consumable>();
        food = new ArrayList<>();
        beverages = new ArrayList<>();
        desserts = new ArrayList<>();
        foodExpListView = (ExpandableListView) findViewById(R.id.restaurant_exp_list);
        bevExpListView = (ExpandableListView) findViewById(R.id.restaurant_exp_list);
        dessExpListView = (ExpandableListView) findViewById(R.id.restaurant_exp_list);

        doGetConsumablesOfSectionAndCreateExpList("FOOD", food, foodExpListView); //since 1st tab is always food
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.v("tab", tab.getText().toString());
                String selectedTab = tab.getText().toString();
                if (selectedTab.equals("Food")) {
                    //if (food.isEmpty()) {
                        doGetConsumablesOfSectionAndCreateExpList(selectedTab.toUpperCase(), food, foodExpListView);
                    /*} else {
                        Log.v("tab", "saved food view");
                        foodExpListView = (ExpandableListView) findViewById(R.id.restaurant_exp_list);
                        foodExpListView.setAdapter(listAdapter);
                    }
                    */
                } else if (selectedTab.equals("Beverages")) {
                    //if (beverages.isEmpty()) {
                        doGetConsumablesOfSectionAndCreateExpList(selectedTab.toUpperCase(), beverages, bevExpListView);
                    /*} else {
                        Log.v("tab", "saved bev view");
                        bevExpListView = (ExpandableListView) findViewById(R.id.restaurant_exp_list);
                        bevExpListView.setAdapter(listAdapter);
                    }
                    */
                } else if (selectedTab.equals("Desserts")) {
                    //if (desserts.isEmpty()) {
                        doGetConsumablesOfSectionAndCreateExpList(selectedTab.toUpperCase(), desserts, dessExpListView);
                    /*} else {
                        Log.v("tab", "saved dess view");
                        dessExpListView = (ExpandableListView) findViewById(R.id.restaurant_exp_list);
                        dessExpListView.setAdapter(listAdapter);
                    }
                    */
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.v("tab", "Reselected tab");
                String selectedTab = tab.getText().toString();
                if (selectedTab.equals("Food")) {
                    foodExpListView = (ExpandableListView) findViewById(R.id.restaurant_exp_list);
                    Log.v("tab", "Reselected food");
                } else if (selectedTab.equals("Beverages")) {
                    bevExpListView = (ExpandableListView) findViewById(R.id.restaurant_exp_list);
                } else if (selectedTab.equals("Desserts")) {
                    dessExpListView = (ExpandableListView) findViewById(R.id.restaurant_exp_list);
                }
            }
        });
    }

    /**
     * Creates an exp list then sets the restaurant exp list onto it
     */
    protected void createExpandableList(List<Consumable> consumables, ExpandableListView view) {
        //RestaurantExpandableListAdapter listAdapter;
        //ExpandableListView expListView;
        List<String> subsectionHeaders; //header text
        //a list of each item's main text with header text as keys
        HashMap<String, List<String>> itemTextDict;
        //consumable dictionary with consumable.name keys
        //final HashMap<String, Consumable> itemObjectDict;

        //find where to inflate the exp list
        //expListView = (ExpandableListView) findViewById(R.id.restaurant_exp_list);

        subsectionHeaders = new ArrayList<String>();
        itemTextDict = new HashMap<String, List<String>>();
        //itemObjectDict = new HashMap<String, Consumable>();

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
        //Initialize subtotal
        if (!itemQuantityDict.containsKey("subtotal")) {
            Log.v("test", "test");
            itemQuantityDict.put("subtotal", 0);
        }

        //set up subtotal text
        TextView subtotalText = (TextView) findViewById(R.id.restaurant_subtotal_text);
        subtotalText.setText("Rp. 0");
        //subtotalText.setBackgroundColor(0xFFac0d13);
        //subtotalText.invalidate();
        //FloatingActionButton subtotalButton = (FloatingActionButton) findViewById(R.id.restaurant_subtotal_button);

        //set up restaurant expandable list adapter
        listAdapter = new RestaurantExpandableListAdapter(this, subsectionHeaders, itemTextDict,
                itemObjectDict, itemQuantityDict, subtotalText);


        //set list adapter onto view
        view.setAdapter(listAdapter);

        //set listener for the button
        FloatingActionButton restaurantButton = (FloatingActionButton) findViewById(R.id.restaurant_add);
        restaurantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // only add the ones that are greater than 0
                HashMap<String, Consumable> consumableHashMap = new HashMap<String, Consumable>();
                HashMap<String, Integer> consumableQuantityHashMap = new HashMap<String, Integer>();
                Iterator it = itemQuantityDict.entrySet().iterator();
                while (it.hasNext()) {
                    HashMap.Entry pair = (HashMap.Entry) it.next();
                    if ((int)pair.getValue() > 0){
                        consumableHashMap.put(pair.getKey().toString(),itemObjectDict.get(pair.getKey().toString()));
                        consumableQuantityHashMap.put(pair.getKey().toString(), (Integer) pair.getValue());
                    }
                }
                if (!consumableHashMap.isEmpty()) { //if user added food
                    Intent intent = new Intent(getBaseContext(), RestaurantConfirmationActivity.class);
                    intent.putExtra("consumableMap", consumableHashMap);
                    intent.putExtra("consumableQuantityMap", consumableQuantityHashMap);
                    startActivity(intent);
                } else { //all quantities are 0
                    Toast.makeText(
                            RestaurantActivity.this,
                            "You didn't add any food!",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }
        });


    }

    /**
     * Pulls the consumables on the server based on the selected section (tab) and, if successful,
     * calls createExpandableList().
     */
    private void doGetConsumablesOfSectionAndCreateExpList(final String section,
                                                            final List<Consumable> consumables,
                                                                final ExpandableListView view) {
        Log.d(RestaurantActivity.class.getCanonicalName(), "Doing get consumables of section");

        if (consumables.isEmpty()) { //if we haven't pulled the section's consumables yet...
            MenuServer.getInstance(this).getMenuBySection(section)
                    .subscribe(new Observer<Consumable>() {

                        @Override
                        public void onCompleted() {
                            Log.d(RestaurantActivity.class.getCanonicalName(), "On completed");
                            createExpandableList(consumables, view);
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
            createExpandableList(consumables, view);
        }
    }


}