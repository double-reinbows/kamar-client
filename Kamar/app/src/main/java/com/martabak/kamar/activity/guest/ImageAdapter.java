package com.martabak.kamar.activity.guest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.permintaan.Permintaan;


/**
 * Image adapter to show the permintaan image types in guest home.
 */
public class ImageAdapter extends BaseAdapter {

    /**
     * References to our images.
     */
    private static final Integer[] IMAGES = {
            R.drawable.ic_myrequests,
            R.drawable.ic_restaurant,
            R.drawable.ic_housekeeping,
            R.drawable.ic_housekeeping,
            R.drawable.ic_bellboy,
            R.drawable.ic_maintenance,
            R.drawable.ic_transport,
            R.drawable.ic_chat,
            R.drawable.ic_checkout,
            R.drawable.ic_chat
    };

    /**
     * Text descriptions.
     */
    private static final String[] TEXT = {
            "MY REQUESTS",
            Permintaan.TYPE_RESTAURANT,
            Permintaan.TYPE_MASSAGE,
            Permintaan.TYPE_HOUSEKEEPING,
            Permintaan.TYPE_BELLBOY,
            Permintaan.TYPE_ENGINEERING,
            Permintaan.TYPE_TRANSPORT,
            Permintaan.TYPE_SURVEY,
            Permintaan.TYPE_CHECKOUT,
            Permintaan.TYPE_CHAT
    };

    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return IMAGES.length;
    }

    public Object getItem(int position) {
        return TEXT[position];
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
        textView.setText(TEXT[position]);
        imageView.setImageResource(IMAGES[position]);

        //grid.setPadding(8, 8, 8, 8);
        return grid;
    }
}
