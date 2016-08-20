package com.martabak.kamar.activity.guest;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.IntegerRes;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.Event;
import com.martabak.kamar.service.EventServer;
import com.martabak.kamar.service.Server;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.Observer;

/**
 * Created by adarsh on 10/08/16.
 */
public class GuestEventActivity extends AppCompatActivity {

    String eventTypeSelected;
    List<String> imageUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_event);

        imageUrls = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });

        TextView roomNumberTextView = (TextView) findViewById(R.id.toolbar_roomnumber);
        String roomNumber = getSharedPreferences("userSettings", MODE_PRIVATE)
                .getString("roomNumber", "none");
        // set room number text
        roomNumberTextView.setText(getString(R.string.room_number) + ": " + roomNumber);

        //initialize tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Bali events"));
        tabLayout.addTab(tabLayout.newTab().setText("Hotel events"));


        eventTypeSelected = "Bali events";
        setPromoImgsForType(eventTypeSelected);
        //Log.v("HOTEL IMAGE", Integer.toString(imageUrls.size()));
        //updateImageView();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.v("tab", tab.getText().toString());
                eventTypeSelected = tab.getText().toString();
                setPromoImgsForType(eventTypeSelected);
                //updateImageView();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //get imageurls based on event





    }

    private void updateImageView() {
        /*Update image view*/

        LinearLayout layout = (LinearLayout) findViewById(R.id.event_linear);
        layout.removeAllViews();
        for (int i = 0; i < imageUrls.size(); i++) {
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            Server.picasso(GuestEventActivity.this)
                    .load(imageUrls.get(i))
                    .placeholder(R.drawable.loading_batik)
                    .error(R.drawable.error)
                    .into(imageView);
            layout.addView(imageView);
            imageView.getLayoutParams().height = 1000;
            imageView.getLayoutParams().width = 1000;
        }

    }



    private void setPromoImgsForType(final String type) {
        /*
         *Get promo imgs for type
         *@Param String type
         */
        //final List<String> promoImgs = new ArrayList<String>();
        imageUrls.clear();
        EventServer.getInstance(getBaseContext()).getCurrentEventsByType(type)
                .subscribe(new Observer<Event>() {
                    @Override
                    public void onCompleted() {
                        //Log.v("HOTEL IMAGE BERORE", Integer.toString(imageUrls.size()));
                        Log.d(GuestEventActivity.class.getCanonicalName(), "Completed setting promo img");
                        updateImageView();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(GuestEventActivity.class.getCanonicalName(), "On error");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Event result) {
                        Log.d(GuestEventActivity.class.getCanonicalName(), "getEvent On next");
                        if (result != null) {
                            imageUrls.add(result.getImageUrl() + result.name + ".jpg");
                            Log.d("Promo Image: ", result.name);

                        }
                    }
                });
        Log.v("HOTEL IMAGE AFTER", Integer.toString(imageUrls.size()));
        //return promoImgs;

    }
}
