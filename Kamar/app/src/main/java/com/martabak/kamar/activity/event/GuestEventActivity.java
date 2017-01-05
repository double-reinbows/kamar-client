package com.martabak.kamar.activity.event;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import com.martabak.kamar.R;
import com.martabak.kamar.activity.guest.AbstractGuestBarsActivity;
import com.martabak.kamar.domain.Event;
import com.martabak.kamar.service.EventServer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observer;

/**
 * Guest Event Activity that is utilising view pager to display events
 */
public class GuestEventActivity extends AbstractGuestBarsActivity {

    String eventTypeSelected;
    List<String> imageUrls;
    private GuestEventViewPagerAdapter guestEventViewPagerAdapter;

    @Override
    protected Options getOptions() {
        return new Options()
                .withBaseLayout(R.layout.activity_guest_event)
                .withToolbarLabel(getString(R.string.event_label))
                .showTabLayout(true)
                .showLogoutIcon(false)
                .enableChatIcon(true);
    }

    //binding views here
    @BindView(R.id.button_previous) ImageButton leftBtn;
    @BindView(R.id.button_next) ImageButton rightBtn;
    @BindView(R.id.event_view_pager) ViewPager viewPager;

    //on previous click
    @OnClick(R.id.button_previous)
    void onPreviousClick() {
        OnClickListener(0);
    }

    //on next click
    @OnClick(R.id.button_next)
    void onNextClick(){
        OnClickListener(1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageUrls = new ArrayList<>();

        //initialize tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Bali events"));
        tabLayout.addTab(tabLayout.newTab().setText("Hotel events"));



        guestEventViewPagerAdapter = new GuestEventViewPagerAdapter(getSupportFragmentManager(), imageUrls);
        viewPager.setAdapter(guestEventViewPagerAdapter);
        eventTypeSelected = "Bali events";

        //Log.v("HOTEL IMAGE", Integer.toString(imageUrls.size()));
        //updateImageView();

        //find view by id

        //leftBtn.setOnClickListener(OnClickListener(0));
        //rightBtn.setOnClickListener(OnClickListener(1));

        setPromoImgsForType(eventTypeSelected);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.v("tab", tab.getText().toString());
                eventTypeSelected = tab.getText().toString();
                setPromoImgsForType(eventTypeSelected);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    // method used to set the current page
    private View.OnClickListener OnClickListener(final int i)
    {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i > 0) {
                    //next page
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

        // clear any previous promo images
        imageUrls.clear();
        guestEventViewPagerAdapter.notifyDataSetChanged();

        EventServer.getInstance(getBaseContext()).getCurrentEventsByType(type)
                .subscribe(new Observer<Event>() {
                    @Override
                    public void onCompleted() {
                        Log.d(GuestEventActivity.class.getCanonicalName(), "Completed setting promo img");
                        guestEventViewPagerAdapter.notifyDataSetChanged();
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

    }
}
