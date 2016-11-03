package com.martabak.kamar.activity.event;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.Event;
import com.martabak.kamar.service.EventServer;
import com.martabak.kamar.service.Server;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;

/**
 * Created by adarsh on 10/08/16.
 */
public class GuestEventActivity extends AppCompatActivity {

    String eventTypeSelected;
    List<String> imageUrls;

    private ImageButton leftBtn;
    private ImageButton rightBtn;
    private ViewPager viewPager;
    private GuestEventViewPagerAdapter guestEventViewPagerAdapter;

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
        viewPager = (ViewPager) findViewById(R.id.event_view_pager);


        guestEventViewPagerAdapter = new GuestEventViewPagerAdapter(getSupportFragmentManager(), imageUrls);
        viewPager.setAdapter(guestEventViewPagerAdapter);
        eventTypeSelected = "Bali events";

        //Log.v("HOTEL IMAGE", Integer.toString(imageUrls.size()));
        //updateImageView();

        //find view by id


        leftBtn = (ImageButton) findViewById(R.id.button_previous);
        rightBtn = (ImageButton) findViewById(R.id.button_next);

        leftBtn.setOnClickListener(OnClickListener(0));
        rightBtn.setOnClickListener(OnClickListener(1));

        setPromoImgsForType(eventTypeSelected);
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



        //updateImageView();
    }

    private View.OnClickListener OnClickListener(final int i)
    {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i > 0) {
                    //next page
                    Log.v("Next Page", String.valueOf(viewPager.getAdapter().getCount()));
                    Log.v("NEEEXT PAGGe", String.valueOf(viewPager.getCurrentItem() + 1));
                    if (viewPager.getCurrentItem() < viewPager.getAdapter().getCount() - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    }
                }
                else {
                    //previous page
                    if (viewPager.getCurrentItem() > 0) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                    }
                }
            }
        };
    }



    //private void updateImageView() {
        /*Update image view*/

        /*View imageLayout = getLayoutInflater().inflate(R.layout.event_item_image, null);


        for (int i = 0; i < imageUrls.size(); i++)
        {
            ImageView eventImageView = (ImageView) imageLayout.findViewById(R.id.event_image);
            eventImageView.setOnClickListener(onChangePageClickListener(i));
            Server.picasso(GuestEventActivity.this)
                    .load(imageUrls.get(i))
                    .placeholder(R.drawable.loading_batik)
                    .error(R.drawable.error)
                    .into(eventImageView);
            eventImageView.getLayoutParams().height = 1000;
            eventImageView.getLayoutParams().width = 1000;
        }

    } */

    private View.OnClickListener onChangePageClickListener(final int i) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(i);
            }
        };
    }



    private void setPromoImgsForType(final String type) {
        /*
         *Get promo imgs for type
         *@Param String type
         */
        //final List<String> promoImgs = new ArrayList<String>oup();
        /*
        if (imageUrls.size() > 0)
        {
            final ViewGroup viewGroup = (ViewGroup) this.findViewById(R.id.event_view_pager);
            guestEventViewPagerAdapter.destroyItem(viewGroup, 0, (Object)viewPager.getChildAt(0));
            guestEventViewPagerAdapter.notifyDataSetChanged();
        }*/

        imageUrls.clear();

        EventServer.getInstance(getBaseContext()).getCurrentEventsByType(type)
                .subscribe(new Observer<Event>() {
                    @Override
                    public void onCompleted() {
                        Log.d(GuestEventActivity.class.getCanonicalName(), "Completed setting promo img");
                        guestEventViewPagerAdapter.notifyDataSetChanged();
                        //updateImageView();
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
                            imageUrls.add(result.getImageUrl());
                            Log.d("Promo Image: ", result.name);

                        }
                    }
                });
        Log.v("HOTEL IMAGE AFTER", Integer.toString(imageUrls.size()));
        //return promoImgs;

    }
}
