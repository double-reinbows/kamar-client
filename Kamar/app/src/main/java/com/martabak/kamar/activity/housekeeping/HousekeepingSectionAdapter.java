package com.martabak.kamar.activity.housekeeping;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.martabak.kamar.R;

import java.util.List;

public class HousekeepingSectionAdapter
        extends RecyclerView.Adapter<HousekeepingSectionAdapter.ViewHolder> {

    protected int selectedPos = -1;

    private final List<String> housekeepingSections;
    private HousekeepingActivity activity;

    public HousekeepingSectionAdapter(HousekeepingActivity activity, List<String> items) {
        this.activity = activity;
        this.housekeepingSections = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.housekeeping_section_row, parent, false);
        view.setOnClickListener(activity);

        /*ImageView startFragmentButton = (ImageView) view.findViewById(R.id.start_fragment);
        startFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(final View v) {
                //activi
            }
        });
        */
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.itemView.setSelected(selectedPos == position);
        holder.item = housekeepingSections.get(position);

        Log.d(HousekeepingActivity.class.getCanonicalName(), "onBindViewHolder " + holder.item);
        /* TODO image view
        holder.nameView.setText(holder.item.getName());
        if (holder.item.length != null) {
            holder.lengthView.setText(holder.item.length.toString() + " mins");
        }
        if (holder.item.price != null) {
            holder.priceView.setText("Rp. " + holder.item.price.toString());
        }
        holder.descriptionView.setText(holder.item.getDescription());
        */
        holder.nameView.setText(housekeepingSections.get(position));
    }

    @Override
    public int getItemCount() {
        if (housekeepingSections == null) {
            return 0;
        }
        return housekeepingSections.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public String item;
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