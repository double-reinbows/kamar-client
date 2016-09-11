package com.martabak.kamar.activity.guest;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.permintaan.Permintaan;

import java.util.ArrayList;
import java.util.List;


/**
 * guest home adapter to show the permintaan images and texts in guest home.
 */
public class GuestHomeAdapter extends BaseAdapter {

    /**
     * References to our images.
     */
    private  final Integer[] IMAGES = {
            R.drawable.ic_events,
            R.drawable.ic_restaurant,
            R.drawable.ic_housekeeping,
            R.drawable.ic_laundry,
            R.drawable.ic_engineering,
            R.drawable.ic_massage,
            R.drawable.ic_bellboy,
            R.drawable.ic_comments
    };

    /**
     * Text descriptions.
     */
    private Context mContext;

    private List<String> TEXT = new ArrayList<String>();


    public GuestHomeAdapter(Context c) {
        mContext = c;
        //adding all the text here
        TEXT.add(mContext.getResources().getString(R.string.event_label));
        TEXT.add(mContext.getResources().getString(R.string.restaurant_label));
        TEXT.add(mContext.getResources().getString(R.string.housekeeping_label));
        TEXT.add(mContext.getResources().getString(R.string.laundry_label));
        TEXT.add(mContext.getResources().getString(R.string.engineering_label));
        TEXT.add(mContext.getResources().getString(R.string.massage_label));
        TEXT.add(mContext.getResources().getString(R.string.bellboy_label));
        TEXT.add(mContext.getResources().getString(R.string.survey_label));

    }

    public int getCount() {
        return IMAGES.length;
    }

    public Object getItem(int position) {
        return TEXT.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    /**
     * Create a new ImageView for each item referenced by the Adapter.
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        TextView textView;
        ImageView imageView;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            LayoutInflater inflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            grid = inflater.inflate(R.layout.grid_view, null);
        } else {
            grid = convertView;
        }

        //set image and text of each item in the grid
        imageView = (ImageView) grid.findViewById(R.id.grid_image);
        textView = (TextView) grid.findViewById(R.id.grid_text);
        grid.setLayoutParams(new GridView.LayoutParams(280, 250));
        textView.setText(TEXT.get(position));
        imageView.setImageResource(IMAGES[position]);

        //grid.setPadding(8, 8, 8, 8);
        return grid;
    }
}
