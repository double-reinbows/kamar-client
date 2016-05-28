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
 * Image adapter to show the permintaan image types.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mImages.length;
    }

    public Object getItem(int position) {

        return mText[position];
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
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
            grid = (View) convertView;
        }

        //set image and text of each item in the grid
        imageView = (ImageView) grid.findViewById(R.id.grid_image);
        textView = (TextView) grid.findViewById(R.id.grid_text);
        grid.setLayoutParams(new GridView.LayoutParams(150, 150));
        textView.setText(mText[position]);
        imageView.setImageResource(mImages[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //grid.setPadding(8, 8, 8, 8);
        return grid;
    }

    // references to our images
    private Integer[] mImages = {
            R.mipmap.ic_myrequests,
            R.mipmap.ic_restaurant,
            R.mipmap.ic_housekeeping,
            R.mipmap.ic_bellboy,
            R.mipmap.ic_maintenance,
            R.mipmap.ic_transport,
            R.mipmap.ic_tellus,
            R.mipmap.ic_checkout,
    };

    //Text descriptions
    private String[] mText = {
            "MY REQUESTS",
            Permintaan.TYPE_RESTAURANT,
            Permintaan.TYPE_HOUSEKEEPING,
            Permintaan.TYPE_BELLBOY,
            Permintaan.TYPE_MAINTENANCE,
            Permintaan.TYPE_TRANSPORT,
            "TELL US",
            Permintaan.TYPE_CHECKOUT
    };
}
