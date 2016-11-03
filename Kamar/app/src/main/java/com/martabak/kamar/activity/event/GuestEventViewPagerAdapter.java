package com.martabak.kamar.activity.event;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.util.List;

/**
 * Created by adarsh on 1/11/16.
 */
public class GuestEventViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<String> images;

    public GuestEventViewPagerAdapter(FragmentManager fm, List<String> imagesList)
    {
        super(fm);
        this.images = imagesList;
    }

    @Override
    public Fragment getItem(int position) {
        return GuestEventFragment.getInstance(images.get(position));
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager
        ((ViewPager) container).removeView((FrameLayout) object);
    }



    /*@Override
    public int getItemPosition(Object object) {
        if (images.contains((View) object)) {
            return images.indexOf((View) object);
        } else {
            return POSITION_NONE;
        }
    }*/

    @Override
    public int getCount() {
        return images.size();
    }
}
