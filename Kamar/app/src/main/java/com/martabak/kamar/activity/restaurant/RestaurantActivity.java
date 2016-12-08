package com.martabak.kamar.activity.restaurant;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
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
    List<String> sections; //AKA tabs
    private HashMap<String, List<Consumable>> sectionToConsumables;
    RestaurantExpListAdapter listAdapter;
    private TextView subtotalText;
    private TextView arrowText;

    //Views
    private HashMap<String, ExpandableListView> expandableListViews;


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
        permintaanFragment(); //permintaan status lights
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        idToConsumable = RestaurantOrderManager.getInstance().getConsumables();

        if (idToConsumable == null) {
            Log.v("MEMORY", "server");
            MenuServer.getInstance(this).getMenu().subscribe(new Observer<List<Consumable>>() {
                List<Consumable> consumables = new ArrayList<>();

                @Override
                public void onCompleted() {
                    //initialize constant variables
                    idToConsumable = new HashMap<>();
                    setGlobals();
                    for (Consumable c : consumables) {
//                        Log.v("HELLO", c.nameEn);
                        idToConsumable.put(c._id, c);
                        idToQuantity.put(c._id, 0);
                        idToNote.put(c._id, "");
                        if (!sections.contains(c.getSection())) {//new tab
                            sections.add(c.getSection());
                            List<Consumable> temp = new ArrayList<>();
                            temp.add(c);
                            sectionToConsumables.put(c.getSection(), temp);
                            //create tab
                            if (tabLayout.getTabCount() == 0) { //add first tab
                                tabLayout.addTab(tabLayout.newTab().setText(c.getSection()));
                            } else { //add new tab to the back
                                tabLayout.addTab(tabLayout.newTab().setText(c.getSection()), tabLayout.getTabCount() - 1);
                            }
                            ExpandableListView tempView = (ExpandableListView) findViewById(R.id.restaurant_exp_list);
                            expandableListViews.put(c.getSection(), tempView);
                        } else { //previously found tab
                            List<Consumable> temp = sectionToConsumables.get(c.getSection());
                            temp.add(c);
                            sectionToConsumables.put(c.getSection(), temp);
                        }
                    }
                    setupTabs(tabLayout);
                    RestaurantOrderManager.getInstance().saveConsumables(idToConsumable);
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onNext(List<Consumable> output) {
                    consumables = output;
                }
            });
        } else {
            Log.v("MEMORY", "memory");
            setGlobals();
            for (String id : idToConsumable.keySet()) {
                Consumable c = idToConsumable.get(id);
                idToQuantity.put(c._id, 0);
                idToNote.put(c._id, "");
                if (!sections.contains(c.getSection())) {//new tab
                    sections.add(c.getSection());
                    List<Consumable> temp = new ArrayList<>();
                    temp.add(c);
                    sectionToConsumables.put(c.getSection(), temp);
                    //create tab
                    if (tabLayout.getTabCount() == 0) { //add first tab
                        tabLayout.addTab(tabLayout.newTab().setText(c.getSection()));
                    } else { //add new tab to the back
                        tabLayout.addTab(tabLayout.newTab().setText(c.getSection()), tabLayout.getTabCount() - 1);
                    }
                    ExpandableListView tempView = (ExpandableListView) findViewById(R.id.restaurant_exp_list);
                    expandableListViews.put(c.getSection(), tempView);
                } else { //previously found tab
                    List<Consumable> temp = sectionToConsumables.get(c.getSection());
                    temp.add(c);
                    sectionToConsumables.put(c.getSection(), temp);
                }
            }
            setupTabs(tabLayout);
        }
    }

    /**
     * set global variables
     */
    private void setGlobals() {
        sectionToConsumables = new HashMap<>();
        sections = new ArrayList<>();
        expandableListViews = new HashMap<>();
        idToQuantity = new HashMap<>();
        idToQuantity.put("subtotal", 0);
        subtotalText = (TextView) findViewById(R.id.restaurant_subtotal_text);
        arrowText = (TextView) findViewById(R.id.restaurant_arrow_text);
        arrowText.setText("\u2192");
        subtotalText.setText("Rp. " + idToQuantity.get("subtotal").toString());
        idToNote = new HashMap<>();
    }

    /**
     * set custom font, tab listener and select left-most tab
     *
     * @param tabLayout
     */
    private void setupTabs(TabLayout tabLayout) {
        setCustomFont(tabLayout);
        setTabListener(tabLayout);
        tabLayout.getTabAt(0).select(); //initialize on the 1st tab
    }

    /**
     * initialize and set the tab listener onto the Tab Layout
     *
     * @param tabLayout
     */
    private void setTabListener(TabLayout tabLayout) {
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String selectedTab = tab.getText().toString();
                createExpandableList(sectionToConsumables.get(selectedTab), expandableListViews.get(selectedTab));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                tab.select();
            }
        });
    }

    /**
     * set the custom font onto the tab text
     *
     * @param tabLayout
     */
    private void setCustomFont(ViewGroup tabLayout) {
        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/century-gothic.ttf");
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
    }

    /**
     * Creates an exp list then sets the ic_restaurant exp list onto it
     *
     * @param consumables
     * @param view
     */
    protected void createExpandableList(List<Consumable> consumables, ExpandableListView view) {
        List<String> subsections; //list of subsections
        //a dictionary of lists of consumable IDs with subsections as keys
        HashMap<String, List<String>> subsectionToIds;

        subsections = new ArrayList<>();
        subsectionToIds = new HashMap<>();

        //iterate over the consumables for the current tab/section
        for (Consumable consumable : consumables) {
            if (!subsections.contains(consumable.getSubsection())) {//subsection doesn't exist yet
                subsections.add(consumable.getSubsection()); //add subsection
                List<String> currList = new ArrayList<>();
                currList.add(consumable._id); //add ID to the subsection
                subsectionToIds.put(consumable.getSubsection(), currList); //reinsert the subsection
            } else {//subsection previously added
                List<String> currList = subsectionToIds.get(consumable.getSubsection());
                currList.add(consumable._id); //add ID to the subsection
                subsectionToIds.put(consumable.getSubsection(), currList); //reinsert the subsection
            }
        }


        //set up ic_restaurant expandable list adapter
        listAdapter = new RestaurantExpListAdapter(this, subsections, subsectionToIds,
                idToConsumable, idToQuantity, idToNote, subtotalText);

        //set list adapter onto view
        view.setAdapter(listAdapter);
        //open the first expandable group if we are in the DRINKS tab
//        if (consumables.get(0).getSection().matches(Consumable.SECTION_BEVERAGES)) {
        if (consumables.get(0).isDrinks()) {
            view.expandGroup(0, true);
        }
        view.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);

        //set listener for the button
        final TextView restaurantButton = (TextView) findViewById(R.id.restaurant_arrow_text);
        if (restaurantButton != null) {
            restaurantButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // only add the ones that are greater than 0
                    List<OrderItem> restaurantOrderItems = new ArrayList<>();
                    List<String> restaurantImgUrls = new ArrayList<>();
                    Iterator it = idToQuantity.entrySet().iterator();
                    while (it.hasNext()) {
                        HashMap.Entry pair = (HashMap.Entry) it.next();
                        if (((int) pair.getValue() > 0) && (pair.getKey().toString() != "subtotal")) {
                            OrderItem orderItem = new OrderItem((int) pair.getValue(), idToConsumable.get(pair.getKey().toString()).nameEn,
                                    idToConsumable.get(pair.getKey().toString()).price, idToNote.get(pair.getKey().toString()));
                            restaurantOrderItems.add(orderItem);
                            restaurantImgUrls.add(idToConsumable.get(pair.getKey().toString()).getImageUrl());
                        }
                    }

                    if (restaurantOrderItems.size() > 0) { //if user added food
                        RestaurantOrder restaurantOrder = new RestaurantOrder("", restaurantOrderItems, idToQuantity.get("subtotal"));
                        RestaurantOrderManager restaurantOrderManager = RestaurantOrderManager.getInstance();
                        restaurantOrderManager.setOrder(restaurantOrder);
                        restaurantOrderManager.setUrls(restaurantImgUrls);

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
    }


    /**
     * Initializes the permintaan tracking lights down the bottom
     */
    public void permintaanFragment() {
        RestaurantPermintaanFragment f = new RestaurantPermintaanFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f);
        ft.commit();
    }


}