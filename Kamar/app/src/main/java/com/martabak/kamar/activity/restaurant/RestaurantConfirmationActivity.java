package com.martabak.kamar.activity.restaurant;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.martabak.kamar.R;
import com.martabak.kamar.activity.guest.GuestHomeActivity;
import com.martabak.kamar.activity.guest.GuestPermintaanActivity;
import com.martabak.kamar.activity.guest.SurveyArrayAdapter;
import com.martabak.kamar.domain.Consumable;
import com.martabak.kamar.domain.managers.RestaurantOrderManager;
import com.martabak.kamar.domain.permintaan.OrderItem;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.domain.permintaan.RestaurantOrder;
import com.martabak.kamar.service.PermintaanServer;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import rx.Observer;

public class RestaurantConfirmationActivity extends AppCompatActivity {

    RestaurantOrder restaurantOrder;
    Permintaan currentPermintaan;
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

        Integer multiplyFactor = 1000;

        // fill in each of the respective display lists based on the restaurant model manager
        RestaurantOrder tempRestaurantOrder = RestaurantOrderManager.getInstance().getOrder();
        restaurantOrderItems = tempRestaurantOrder.items;


        for (OrderItem restaurantOrderItem : restaurantOrderItems)
        {
            Integer subPrice = restaurantOrderItem.price * restaurantOrderItem.quantity;
            restaurantTextItems.add(restaurantOrderItem.name);
            restaurantQuantityItems.add(restaurantOrderItem.quantity.toString());


            Integer newSubPriceInteger = subPrice * multiplyFactor;
            String newSubPrice = "Rp. " + newSubPriceInteger.toString();

            restaurantSubPriceItems.add(newSubPrice);

        }

        //sub price
        TextView subPriceTextView = (TextView) view.findViewById(R.id.order_sub_total);
        Integer subFinalPriceInteger = tempRestaurantOrder.totalPrice * multiplyFactor;
        String subFinalPrice = "Sub Total = Rp. "  + subFinalPriceInteger.toString();
        subPriceTextView.setText(subFinalPrice);


        //final price
        TextView finalPriceTextView = (TextView) view.findViewById(R.id.order_total);
        float taxPercentage = 10;
        float svcChargePercentage = 11;

        float tax = subFinalPriceInteger.floatValue() * (taxPercentage/100);
        float svcCharge = (tax + subFinalPriceInteger.floatValue()) * (svcChargePercentage/100);
        float finalPriceFloat = subFinalPriceInteger.floatValue() + tax + svcCharge;

        Integer finalPriceInteger = (int)finalPriceFloat;
        String newFinalPrice = "Total (inc. tax and service) = Rp. "  + finalPriceInteger.toString();

        finalPriceTextView.setText(newFinalPrice);

        //comment here
        TextView commentTextView = (TextView) view.findViewById(R.id.restaurant_confirm_input);

        restaurantOrder = new RestaurantOrder(commentTextView.getText().toString(),restaurantOrderItems, finalPriceInteger);

        final Activity activity = this;

        final RestaurantConfirmationArrayAdapter restaurantConfirmationArrayAdapter = new
                RestaurantConfirmationArrayAdapter(restaurantTextItems, restaurantSubPriceItems,
                restaurantQuantityItems);
        rv.setAdapter(restaurantConfirmationArrayAdapter);

        //confirmation
        Button restaurantConfirmButton = (Button) view.findViewById(R.id.restaurant_confirm);
        restaurantConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //restaurant submit
                sendRestaurantRequest(restaurantOrder);

                //new dialog
                final Dialog dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_restaurant_confirmation);
                dialog.show();

                dialog.setOnDismissListener(new Dialog.OnDismissListener(){

                    @Override
                    public void onDismiss(DialogInterface dialogInterface){
                        startActivity(new Intent(getBaseContext(), GuestHomeActivity.class));
                        finish();
                    }
                });

                new CountDownTimer(7000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }
                    @Override
                    public void onFinish() {
                        dialog.dismiss();
                    }
                }.start();
            }
        });

        //back
        Button restaurantBackButton = (Button) view.findViewById(R.id.restaurant_back);
        restaurantBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //restaurant back;
                finish();
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
        if (!guestId.equals("none") && !roomNumber.equals("none")) {
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


                    //finish();
                }
                @Override public void onError(Throwable e) {
                    Log.d(RestaurantConfirmationActivity.class.getCanonicalName(), "createPermintaan() On error");
                    e.printStackTrace();
                }
                @Override public void onNext(Permintaan permintaan) {
                    Log.d(RestaurantConfirmationActivity.class.getCanonicalName(), "createPermintaan() On next" + permintaan);
                    if (permintaan == null) {
                        Toast.makeText(
                                RestaurantConfirmationActivity.this,
                                getString(R.string.restaurant_error),
                                Toast.LENGTH_LONG
                        ).show();
                        finish();
                    }
                }
            });


        }

    }
}
