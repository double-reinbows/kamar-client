package com.martabak.kamar.activity.restaurant;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.martabak.kamar.R;
import com.martabak.kamar.activity.guest.SurveyArrayAdapter;
import com.martabak.kamar.domain.Consumable;
import com.martabak.kamar.domain.managers.RestaurantOrderManager;
import com.martabak.kamar.domain.permintaan.OrderItem;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.domain.permintaan.RestaurantOrder;
import com.martabak.kamar.service.PermintaanServer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import rx.Observer;

public class RestaurantConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater layoutInflater = getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.activity_restaurant_confirmation, null);
        setContentView(view);

        // Get the ActionBar here to configure the way it behaves.
        final ActionBar ab = getSupportActionBar();

        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false);

        ab.setCustomView(R.layout.actionbar_guestcustom_view);


        TextView roomNumberTextView = (TextView)findViewById(R.id.toolbar_roomnumber);
        String roomNumber = getSharedPreferences("userSettings", MODE_PRIVATE)
                .getString("roomNumber", "none");
        // set room number text
        roomNumberTextView.setText(getString(R.string.room_number) + " " + roomNumber);

        final RecyclerView rv = (RecyclerView) view.findViewById(R.id.restaurant_recycleview);
        rv.addItemDecoration(new SimpleDividerItemDecoration(this));
        final LinearLayoutManager llm = new LinearLayoutManager(getBaseContext());
        rv.setLayoutManager(llm);

        final List<String> restaurantTextItems = new ArrayList<>();
        final List<String> restaurantSubPriceItems = new ArrayList<>();
        final List<String> restaurantQuantityItems = new ArrayList<>();

        List<OrderItem> restaurantOrderItems = new ArrayList<>();

        // fill in each of the respective display lists based on the restaurant model manager
        final RestaurantOrder restaurantOrder = RestaurantOrderManager.getInstance().getOrder();
        restaurantOrderItems = restaurantOrder.items;

        for (OrderItem restaurantOrderItem : restaurantOrderItems)
        {
            Integer subPrice = restaurantOrderItem.price * restaurantOrderItem.quantity;
            restaurantTextItems.add(restaurantOrderItem.name);
            restaurantQuantityItems.add(restaurantOrderItem.quantity.toString());
            restaurantSubPriceItems.add(subPrice.toString());

        }



        final RestaurantConfirmationArrayAdapter restaurantConfirmationArrayAdapter = new
                RestaurantConfirmationArrayAdapter(restaurantTextItems, restaurantSubPriceItems,
                restaurantQuantityItems);
        rv.setAdapter(restaurantConfirmationArrayAdapter);



        //confirmation
        FloatingActionButton button = (FloatingActionButton) view.findViewById(R.id.restaurant_confirm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //restaurant submit
                sendRestaurantRequest(restaurantOrder);

            }
        });

    }

    /*Send restaurant request*/
    public void sendRestaurantRequest(RestaurantOrder restaurantOrder) {

        String owner = Permintaan.OWNER_RESTAURANT;
        String type = Permintaan.TYPE_RESTAURANT;
        String roomNumber = getSharedPreferences("userSettings", MODE_PRIVATE)
                .getString("roomNumber", null);
        String guestId = getSharedPreferences("userSettings", MODE_PRIVATE)
                .getString("guestId", null);
        String state = Permintaan.STATE_NEW;
        Date currentDate = Calendar.getInstance().getTime();

        Log.v(RestaurantConfirmationActivity.class.getCanonicalName(), restaurantOrder.toString());

        /* create permintaan*/
        if (guestId != "none") {
            PermintaanServer.getInstance(this.getBaseContext()).createPermintaan(new Permintaan(
                    owner,
                    type,
                    roomNumber,
                    guestId,
                    state,
                    currentDate,
                    null,
                    restaurantOrder)
            ).subscribe(new Observer<Permintaan>() {
                @Override public void onCompleted() {
                    Log.d(RestaurantConfirmationActivity.class.getCanonicalName(), "createPermintaan() On completed");
                    finish();
                }
                @Override public void onError(Throwable e) {
                    Log.d(RestaurantConfirmationActivity.class.getCanonicalName(), "createPermintaan() On error");
                    e.printStackTrace();
                }
                @Override public void onNext(Permintaan permintaan) {
                    Log.d(RestaurantConfirmationActivity.class.getCanonicalName(), "createPermintaan() On next" + permintaan);
                    if (permintaan != null) {
                        Toast.makeText(
                                RestaurantConfirmationActivity.this,
                                getString(R.string.restaurant_success),
                                Toast.LENGTH_LONG
                        ).show();
                    } else {
                        Toast.makeText(
                                RestaurantConfirmationActivity.this,
                                getString(R.string.restaurant_error),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
            });


        }

    }
}
