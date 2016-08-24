package com.martabak.kamar.activity.guest;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.options.HousekeepingOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class HousekeepingOptionAdapter
        extends RecyclerView.Adapter<HousekeepingOptionAdapter.ViewHolder> {

    protected int selectedPos = -1;

    private final List<HousekeepingOption> housekeepingOptions;
    private HashMap<HousekeepingOption, Integer> hkOptionDict;
    private String section;

    public HousekeepingOptionAdapter(HashMap<HousekeepingOption, Integer> items, String section) {
        this.housekeepingOptions = new ArrayList<HousekeepingOption>(items.keySet());
        Log.v("DICK", housekeepingOptions.get(0).getName());
        Log.v("DICK", housekeepingOptions.get(1).getName());
        this.hkOptionDict = items;
        this.section = section;
        Log.v("SECTION", section);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.housekeeping_option_row, parent, false);
        //view.setOnClickListener(HousekeepingActivity.this);
        Log.v("vagina", "vagina");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.itemView.setSelected(selectedPos == position);

        if (housekeepingOptions.get(position).sectionEn.equals(section)) {
            holder.item = housekeepingOptions.get(position);
            Log.v("added option", holder.item.getName());
            holder.nameView.setText(holder.item.getName());
        }
        holder.item = housekeepingOptions.get(position);
        holder.nameView.setText(housekeepingOptions.get(position).getName());
        Log.v("current_option", housekeepingOptions.get(position).getName());
        Log.v("current_item", holder.item.getName());
        /*holder.item = hkOptionDict.keySet().;
        for (HousekeepingOption hk : hkOptionDict.keySet()) {
            Log.v("hk",hk.getName());
            if (hk.sectionEn.equals(section)) {
                holder.item = hk;
                holder.nameView.setText(hk.getName());
                Log.v("options", hk.getName());
            }
        }
*/


        //Log.d(HousekeepingOptionAdapter.class.getCanonicalName(), "onBindViewHolder " + holder.item.getName());
        /* TODO image view
        holder.nameView.setText(holder.item.getName());
        if (holder.item.length != null) {
            holder.lengthView.setText(holder.item.length.toHousekeepingOption() + " mins");
        }
        if (holder.item.price != null) {
            holder.priceView.setText("Rp. " + holder.item.price.toHousekeepingOption());
        }
        holder.descriptionView.setText(holder.item.getDescription());
        */
        //holder.nameView.setText(hkOptionDict.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return housekeepingOptions.size();
        //Log.v("Sizesz", String.valueOf(housekeepingOptions.size()));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public HousekeepingOption item;
        public final View rootView;
        public final ImageView imageView;
        public final TextView nameView;
        public final TextView lengthView;
        public final TextView priceView;
        public final TextView descriptionView;

        public ViewHolder(View view) {
            super(view);
            rootView = view;
            imageView = (ImageView) view.findViewById(R.id.massage_image);
            nameView = (TextView) view.findViewById(R.id.hk_name);
            lengthView = (TextView) view.findViewById(R.id.massage_length);
            priceView = (TextView) view.findViewById(R.id.massage_price);
            descriptionView = (TextView) view.findViewById(R.id.massage_description);
        }

        @Override
        public String toString() {
            return super.toString() + " " + nameView.getText();
        }
    }
}