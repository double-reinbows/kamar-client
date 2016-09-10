package com.martabak.kamar.activity.housekeeping;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.options.HousekeepingOption;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.service.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HousekeepingOptionAdapter
        extends RecyclerView.Adapter<HousekeepingOptionAdapter.ViewHolder> {

    protected int selectedPos = -1;

    private final List<HousekeepingOption> housekeepingOptions;
    private HashMap<String, Integer> idToQuantity;
    private Context context;
    private Map<String, String> statuses;
    private View.OnClickListener submitButtonListener;

    public HousekeepingOptionAdapter(List<HousekeepingOption> hkOptions, HashMap<String, Integer> items,
                                     Context context, View.OnClickListener submitButtonListener,
                                     Map<String, String> statuses) {
        this.housekeepingOptions = hkOptions;
        this.idToQuantity = items;
        this.context = context;
        this.submitButtonListener = submitButtonListener;
        this.statuses = statuses;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.housekeeping_option_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.itemView.setSelected(selectedPos == position);
        holder.item = housekeepingOptions.get(position);
        holder.nameView.setText(housekeepingOptions.get(position).getName());
//        holder.nameView.post(new Runnable() {
//            @Override
//            public void run() {
////                Log.v("Child info", "child: "+currConsumable.name+" no. of lines: "+Integer.toString(txtListChild.getLineCount()));
//                if (holder.nameView.count) {//if 2 lines used for item name
//                    //reduce item name padding
//                    txtListChild.setPadding(txtListChild.getPaddingLeft(),
//                            txtListChild.getPaddingTop(),
//                            txtListChild.getPaddingRight(),
//                            20);
//                } /*else {
//                    //set item name padding to default
//                    txtListChild.setPadding(txtListChild.getPaddingLeft(),
//                            txtListChild.getPaddingTop(),
//                            txtListChild.getPaddingRight(),
//                            90);
//
////                    quantity.margin
//                }*/
//            }
//        });
        Server.picasso(context)
            .load(holder.item.getImageUrl())
            .placeholder(R.drawable.loading_batik)
            .error(R.drawable.error)
            .into(holder.imgView);

        //create list of options in the spinner
        final List<String> spinnerText = new ArrayList<>();
        for (Integer i=0; i<=holder.item.max; i++) {
            spinnerText.add(i.toString());
        }
        ArrayAdapter adapter = new ArrayAdapter(context,
                R.layout.support_simple_spinner_dropdown_item, spinnerText);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        holder.spinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        String state = statuses.containsKey(holder.item._id) ? statuses.get(holder.item._id) : Permintaan.STATE_COMPLETED;
        Log.d(HousekeepingActivity.class.getCanonicalName(), "Status for engineering " + holder.item.getName() + " is " + state);
        switch (state) {
            case Permintaan.STATE_INPROGRESS:
                holder.processedImageView.setBackground(context.getResources().getDrawable(R.drawable.circle_green));
            case Permintaan.STATE_NEW:
                holder.sentImageView.setBackground(context.getResources().getDrawable(R.drawable.circle_green));
                holder.submitButton.setColorFilter(Color.argb(150,200,200,200));
                break;
            case Permintaan.STATE_COMPLETED:
                holder.submitButton.setOnClickListener(submitButtonListener);
        }

    }

    @Override
    public int getItemCount() {
        return housekeepingOptions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public HousekeepingOption item;
        public final View rootView;
        public final TextView nameView;
        public final ImageView submitButton;
        public final Spinner spinner;
        public final View sentImageView;
        public final View processedImageView;
        public final ImageView imgView;

        public ViewHolder(View view) {
            super(view);
            rootView = view;
            nameView = (TextView) view.findViewById(R.id.hk_name);
            submitButton = (ImageView) view.findViewById(R.id.hk_submit);
            spinner = (Spinner) view.findViewById(R.id.hk_spinner);
            sentImageView = view.findViewById(R.id.sent_image);
            processedImageView = view.findViewById(R.id.processed_image);
            imgView = (ImageView) view.findViewById(R.id.hk_img);
        }

        @Override
        public String toString() {
            return super.toString() + " " + nameView.getText();
        }
    }
}