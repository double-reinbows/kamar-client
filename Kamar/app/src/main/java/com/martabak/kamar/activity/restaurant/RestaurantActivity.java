package com.martabak.kamar.activity.restaurant;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.martabak.kamar.R;
import com.martabak.kamar.activity.guest.AbstractGuestBarsActivity;
import com.martabak.kamar.domain.Consumable;
import com.martabak.kamar.domain.managers.RestaurantOrderManager;
import com.martabak.kamar.domain.permintaan.OrderItem;
import com.martabak.kamar.domain.permintaan.RestaurantOrder;
import com.martabak.kamar.service.MenuServer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import rx.Observer;

/**
 * This activity generates the menu which users can order food, beverages and desserts
 * from. It pulls menu data from the server and then displays it in an interactive
 * menu.
 */
public class RestaurantActivity extends AbstractGuestBarsActivity {

    //quantity dictionary with consumable.name keys
    private HashMap<String, Integer> idToQuantity;
    private HashMap<String, Consumable> idToConsumable;
    private HashMap<String, String> idToNote;
    //lists of sections
    private List<Consumable> food;
    private List<Consumable> beverages;
    private List<Consumable> desserts;

    RestaurantExpListAdapter listAdapter;
    private TextView subtotalText;
    private Boolean dessertFlag = false;

    //Views
    private ExpandableListView foodExpListView;
    private ExpandableListView bevExpListView;
    private ExpandableListView dessExpListView;

    protected Options getOptions() {
        return new Options()
                .withBaseLayout(R.layout.activity_restaurant)
                .withToolbarLabel(getString(R.string.restaurant_label))
                .showTabLayout(true)
                .showLogoutIcon(false)
                .enableChatIcon(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/century-gothic.ttf");

        //initialize tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Food"));
        tabLayout.addTab(tabLayout.newTab().setText("Beverages"));
        tabLayout.addTab(tabLayout.newTab().setText("Desserts"));

        //set tab text's font
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
        idToQuantity = new HashMap<>();
        idToConsumable = new HashMap<>();
        idToNote = new HashMap<>();
        food = new ArrayList<>();
        beverages = new ArrayList<>();
        desserts = new ArrayList<>();
        foodExpListView = (ExpandableListView) findViewById(R.id.restaurant_exp_list);
        bevExpListView = (ExpandableListView) findViewById(R.id.restaurant_exp_list);
        dessExpListView = (ExpandableListView) findViewById(R.id.restaurant_exp_list);
        subtotalText = (TextView) findViewById(R.id.restaurant_subtotal_text);

        doGetConsumablesOfSectionAndCreateExpList("FOOD", food, foodExpListView); //since 1st tab is always food
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.v("tab", tab.getText().toString());
                String selectedTab = tab.getText().toString();
                if (selectedTab.equals("Food")) {
                    dessertFlag = false;
                    //if (food.isEmpty()) {
                        doGetConsumablesOfSectionAndCreateExpList(selectedTab.toUpperCase(), food, foodExpListView);
                    /*} else {
                        Log.v("tab", "saved food view");
                        foodExpListView = (ExpandableListView) findViewById(R.id.restaurant_exp_list);
                        foodExpListView.setAdapter(listAdapter);
                    }
                    */
                } else if (selectedTab.equals("Beverages")) {
                    dessertFlag = false;
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
                    dessertFlag = true;
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
     * Creates an exp list then sets the ic_restaurant exp list onto it
     */
    protected void createExpandableList(List<Consumable> consumables, ExpandableListView view) {
        List<String> subsections; //list of subsections
        //a dictionary of lists of consumable IDs with subsections as keys
        HashMap<String, List<String>> subsectionToIds;

        subsections = new ArrayList<>();
        subsectionToIds = new HashMap<>();

        //iterate over the consumables for the current tab/section
        for (Consumable consumable : consumables) {
            if (!subsections.contains(consumable.subsection)) {//subsection doesn't exist yet
                subsections.add(consumable.subsection); //add subsection
                List<String> currList = new ArrayList<>();
                currList.add(consumable._id); //add ID to the subsection
                subsectionToIds.put(consumable.subsection, currList); //reinsert the subsection
            } else {//subsection previously added
                List<String> currList = subsectionToIds.get(consumable.subsection);
                currList.add(consumable._id); //add ID to the subsection
                subsectionToIds.put(consumable.subsection, currList); //reinsert the subsection
            }
            idToConsumable.put(consumable._id, consumable); //add _id:consumable
            if (!idToQuantity.containsKey(consumable._id)) {//if quantity hasn't been initialized
                idToQuantity.put(consumable._id, 0); //initialize quantity
                idToNote.put(consumable._id, ""); //initialize notes
            }
        }
        //Initialize subtotal
        if (!idToQuantity.containsKey("subtotal")) {
            idToQuantity.put("subtotal", 0);
            subtotalText.setText("Rp. "+ idToQuantity.get("subtotal").toString());
        }


        //set up ic_restaurant expandable list adapter
        listAdapter = new RestaurantExpListAdapter(this, subsections, subsectionToIds,
                idToConsumable, idToQuantity, idToNote, subtotalText);

        //set list adapter onto view
        view.setAdapter(listAdapter);
        if (dessertFlag.equals(true)) {
            view.expandGroup(0, true);
        }
        view.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);

        //set listener for the button
        ImageView restaurantButton = (ImageView) findViewById(R.id.restaurant_add);
        restaurantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // only add the ones that are greater than 0
                List<OrderItem> restaurantOrderItems = new ArrayList<>();
                Iterator it = idToQuantity.entrySet().iterator();
                while (it.hasNext()) {
                    HashMap.Entry pair = (HashMap.Entry) it.next();
//                    Log.v("CUNT", idToNote.toString());
                    if (((int)pair.getValue() > 0) && (pair.getKey().toString() != "subtotal")){
                        OrderItem orderItem = new OrderItem((int)pair.getValue(),idToConsumable.get(pair.getKey().toString()).name,
                                idToConsumable.get(pair.getKey().toString()).price, idToNote.get(pair.getKey().toString()));
                        restaurantOrderItems.add(orderItem);
                    }
                }
                if (restaurantOrderItems.size() > 0) { //if user added food

                    RestaurantOrder restaurantOrder = new RestaurantOrder("",restaurantOrderItems, idToQuantity.get("subtotal"));
                    RestaurantOrderManager restaurantOrderManager = RestaurantOrderManager.getInstance();
                    restaurantOrderManager.setOrder(restaurantOrder);

                    Intent intent = new Intent(getBaseContext(), RestaurantConfirmationActivity.class);
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
                            Log.d(RestaurantActivity.class.getCanonicalName(), "getMenuBySection: On completed");
                            createExpandableList(consumables, view);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(RestaurantActivity.class.getCanonicalName(), "On error");
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(Consumable result) {
                            Log.d(RestaurantActivity.class.getCanonicalName(), "getMenuBySection: On next: " + result.name);
                            consumables.add(result);
                        }
                    });
        } else { //skip pulling consumables from server
            createExpandableList(consumables, view);
        }
    }
}