package com.martabak.kamar.activity.restaurant;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.martabak.kamar.R;
import com.martabak.kamar.activity.guest.AbstractGuestBarsActivity;
import com.martabak.kamar.activity.guest.GuestHomeActivity;
import com.martabak.kamar.activity.guest.SimpleDividerItemDecoration;
import com.martabak.kamar.domain.managers.RestaurantOrderManager;
import com.martabak.kamar.domain.permintaan.OrderItem;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.domain.permintaan.RestaurantOrder;
import com.martabak.kamar.service.PermintaanServer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

public class RestaurantConfirmationActivity extends AbstractGuestBarsActivity {

    private RestaurantOrder restaurantOrder;

    protected Options getOptions() {
        return new Options()
                .withBaseLayout(R.layout.activity_restaurant_confirmation)
                .withToolbarLabel(getString(R.string.restaurant_confirmation_label))
                .showTabLayout(false)
                .showLogoutIcon(false)
                .enableChatIcon(true);
    }

    // bind views here
    @BindView(R.id.order_total) TextView finalPriceTextView;

    //on click confirm here
    @OnClick(R.id.restaurant_confirm)
    void onSubmitClick() {
        //check there's a guest...
        if (getSharedPreferences("userSettings", MODE_PRIVATE)
                .getString("guestId", "none").equals("none")) {
            Toast.makeText(
                    RestaurantConfirmationActivity.this,
                    getString(R.string.no_guest_in_room),
                    Toast.LENGTH_LONG
            ).show();
            return;
        }
        //ic_restaurant submit
        sendRestaurantRequest(restaurantOrder);

        String creator = getSharedPreferences("userSettings", MODE_PRIVATE)
                .getString("userType", "none");
        //new dialog


        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_restaurant_confirmation);
        dialog.show();

        dialog.setOnDismissListener(new Dialog.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                startActivity(new Intent(getBaseContext(), GuestHomeActivity.class));
                finish();
            }
        });

        new CountDownTimer(11000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                dialog.dismiss();
            }
        }.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startFragment(); //permintaan status lights
        ButterKnife.bind(this);
        final RecyclerView rv = (RecyclerView) findViewById(R.id.restaurant_recycleview);
        rv.addItemDecoration(new SimpleDividerItemDecoration(this));
        final LinearLayoutManager llm = new LinearLayoutManager(getBaseContext());
        rv.setLayoutManager(llm);

        final List<String> restaurantTextItems = new ArrayList<>();
        final List<String> restaurantSubPriceItems = new ArrayList<>();
        final List<String> restaurantQuantityItems = new ArrayList<>();
        final List<String> restaurantNoteItems = new ArrayList<>();

        List<OrderItem> restaurantOrderItems;

        Integer multiplyFactor = 1000;

        //fill in each of the respective display lists based on the ic_restaurant model manager
        RestaurantOrder tempRestaurantOrder = RestaurantOrderManager.getInstance().getOrder();
        restaurantOrderItems = tempRestaurantOrder.items;

        for (OrderItem restaurantOrderItem : restaurantOrderItems)
        {
            Integer subPrice = restaurantOrderItem.price * restaurantOrderItem.quantity;
            restaurantTextItems.add(restaurantOrderItem.name);
            restaurantQuantityItems.add(restaurantOrderItem.quantity.toString());
            restaurantNoteItems.add(restaurantOrderItem.note);


            Integer newSubPriceInteger = subPrice * multiplyFactor;
            String newSubPrice = "Rp. " + newSubPriceInteger.toString();

            restaurantSubPriceItems.add(newSubPrice);

        }

        //sub price
        Integer subFinalPriceInteger = tempRestaurantOrder.totalPrice * multiplyFactor;

        //final price

        float taxPercentage = 10;
        float svcChargePercentage = 11;

        float tax = subFinalPriceInteger.floatValue() * (taxPercentage/100);
        float svcCharge = (tax + subFinalPriceInteger.floatValue()) * (svcChargePercentage/100);
        float finalPriceFloat = subFinalPriceInteger.floatValue() + tax + svcCharge;

        Integer finalPriceInteger = (int)finalPriceFloat;
        DecimalFormat df = new DecimalFormat("#,###,###");
        finalPriceTextView.setText("Rp. "+df.format(finalPriceInteger).replaceAll(",", " "));

        //+ Add More Items
        TextView addMoreText = (TextView)findViewById(R.id.order_add_more);
        addMoreText.setText(getString(R.string.add_more_items));
        addMoreText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //return to RestaurantActivity
                finish();
            }
        });

        //TODO: remove RestaurantOrder's comment (1st variable)
        restaurantOrder = new RestaurantOrder("",restaurantOrderItems, finalPriceInteger);

        final Activity activity = this;

        final RestaurantConfirmationArrayAdapter restaurantConfirmationArrayAdapter = new
                RestaurantConfirmationArrayAdapter(this, restaurantTextItems, restaurantSubPriceItems,
                restaurantQuantityItems, restaurantNoteItems, RestaurantOrderManager.getInstance().getUrls());
        rv.setAdapter(restaurantConfirmationArrayAdapter);

        //confirmation
        /*
        Button restaurantConfirmButton = (Button)findViewById(R.id.restaurant_confirm);
        restaurantConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check there's a guest...
                if (getSharedPreferences("userSettings", MODE_PRIVATE)
                        .getString("guestId", "none").equals("none")) {
                    Toast.makeText(
                            RestaurantConfirmationActivity.this,
                            getString(R.string.no_guest_in_room),
                            Toast.LENGTH_LONG
                    ).show();
                    return;
                }
                //ic_restaurant submit
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

                new CountDownTimer(11000, 1000) {
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
        */
    }

    /*Send ic_restaurant request*/
    public void sendRestaurantRequest(RestaurantOrder restaurantOrder) {
        String owner = Permintaan.OWNER_RESTAURANT;
        String type = Permintaan.TYPE_RESTAURANT;
        String creator = getSharedPreferences("userSettings", MODE_PRIVATE)
                .getString("userType", "none");
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
                    creator,
                    type,
                    roomNumber,
                    guestId,
                    state,
                    currentDate,
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

    public void startFragment() {
        RestaurantPermintaanFragment f = new RestaurantPermintaanFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f);
        ft.commit();
    }
}
