package com.martabak.kamar.activity.guest;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.martabak.kamar.R;
import java.util.ArrayList;


public class GuestHomeSliderAdapter extends PagerAdapter {

    private LayoutInflater inflater;
    private Context context;

    private  final Integer[] images = {
            R.drawable.banner,
            R.drawable.splash_screen
    };


    public GuestHomeSliderAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View imageViewLayout = inflater.inflate(R.layout.guest_home_image_slider,container,false);
        ImageView view = (ImageView)imageViewLayout.findViewById(R.id.image_banner);
        view.setImageResource(images[position]);
        container.addView(imageViewLayout, 0);
        return imageViewLayout;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
