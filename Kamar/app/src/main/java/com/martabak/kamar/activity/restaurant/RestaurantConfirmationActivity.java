package com.martabak.kamar.activity.restaurant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.widget.Toolbar;

import com.martabak.kamar.R;
import com.martabak.kamar.activity.guest.SurveyArrayAdapter;
import com.martabak.kamar.domain.Consumable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class RestaurantConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        HashMap<String,Consumable> consumableHashMap = (HashMap<String, Consumable>) intent.getSerializableExtra("consumableMap");
        HashMap<String,Integer> quantityHashMap = (HashMap<String, Integer>) intent.getSerializableExtra("consumableQuantityMap");




        LayoutInflater layoutInflater = getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.activity_survey, null);
        setContentView(view);
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        final RecyclerView rv = (RecyclerView) view.findViewById(R.id.survey_recycleview);
        final LinearLayoutManager llm = new LinearLayoutManager(getBaseContext());
        rv.setLayoutManager(llm);

        final List<String> restaurantTextItems = new ArrayList<>();
        final List<String> restaurantUnitPriceItems = new ArrayList<>();
        final List<String> restaurantSubPriceItems = new ArrayList<>();
        final List<String> restaurantQuantityItems = new ArrayList<>();

        Iterator it = consumableHashMap.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            Consumable c = (Consumable)pair.getValue();
            restaurantTextItems.add(pair.getKey().toString());
            restaurantUnitPriceItems.add(c.price.toString());
            restaurantSubPriceItems.add(c.price.toString());
            restaurantQuantityItems.add(quantityHashMap.get(pair.getKey()).toString());
        }

        final RestaurantConfirmationArrayAdapter restaurantConfirmationArrayAdapter = new
                RestaurantConfirmationArrayAdapter(restaurantTextItems, restaurantUnitPriceItems, restaurantSubPriceItems,
                restaurantQuantityItems);
        rv.setAdapter(restaurantConfirmationArrayAdapter);

    }
}
