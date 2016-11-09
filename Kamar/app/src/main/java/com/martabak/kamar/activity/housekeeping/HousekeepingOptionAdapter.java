package com.martabak.kamar.activity.housekeeping;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.options.HousekeepingOption;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.service.Server;

import java.util.List;
import java.util.Map;

public class HousekeepingOptionAdapter
        extends RecyclerView.Adapter<HousekeepingOptionAdapter.ViewHolder> {

    protected int selectedPos = -1;

    private final List<HousekeepingOption> housekeepingOptions;
    private Context context;
    private Map<String, String> statuses;
    private View.OnClickListener submitButtonListener;
    private final RadioGroup.OnCheckedChangeListener l;

    public HousekeepingOptionAdapter(List<HousekeepingOption> hkOptions,
                                     Context context, View.OnClickListener submitButtonListener,
                                     Map<String, String> statuses,
                                     RadioGroup.OnCheckedChangeListener l) {
        this.housekeepingOptions = hkOptions;
        this.context = context;
        this.submitButtonListener = submitButtonListener;
        this.statuses = statuses;
        this.l = l;
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
        Server.picasso(context)
                .load(holder.item.getImageUrl())
                .placeholder(R.drawable.loading_batik)
                .error(R.drawable.error)
                .into(holder.imgView);
        //set radio group and create its buttons
        holder.radioGroup.setOnCheckedChangeListener(l);
        if (holder.radioGroup.getChildCount() == 0) {//stops duplicate buttons
            for (Integer i = 0; i < holder.item.max; i++) {
                RadioButton rb = new RadioButton(context);
                Integer j = i + 1;
                rb.setText(j.toString());
                rb.setTag(j);
                float scale = context.getResources().getDisplayMetrics().density;
                int leftPad = (int) (20*scale + 0.5f);
                int rightPad = (int) (45*scale + 0.5f);
                rb.setPadding(leftPad, 0, rightPad, 0);

                holder.radioGroup.addView(rb);
            }
        }
        //set permintaan statuses
        String state = statuses.containsKey(holder.item._id) ? statuses.get(holder.item._id) : Permintaan.STATE_CANCELLED;
        Log.d(HousekeepingActivity.class.getCanonicalName(), "Status for housekeeping " + holder.item.getName() + " is " + state);
        switch (state) {
            case Permintaan.STATE_CANCELLED:
                holder.submitButton.setOnClickListener(submitButtonListener);
                break;
            case Permintaan.STATE_COMPLETED:
                holder.completedImageView.setBackground(context.getResources().getDrawable(R.drawable.circle_green));
            case Permintaan.STATE_INPROGRESS:
                holder.processedImageView.setBackground(context.getResources().getDrawable(R.drawable.circle_green));
            case Permintaan.STATE_NEW:
                holder.sentImageView.setBackground(context.getResources().getDrawable(R.drawable.circle_green));
                holder.submitButton.setColorFilter(Color.argb(150,200,200,200));
                break;
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
        public final View sentImageView;
        public final View processedImageView;
        public final View completedImageView;
        public final ImageView imgView;
        public final RadioGroup radioGroup;
        public final RadioButton checkedRadioButton;

        public ViewHolder(View view) {
            super(view);
            rootView = view;
            nameView = (TextView) view.findViewById(R.id.hk_name);
            submitButton = (ImageView) view.findViewById(R.id.hk_submit);
            sentImageView = view.findViewById(R.id.sent_image);
            processedImageView = view.findViewById(R.id.processed_image);
            completedImageView = view.findViewById(R.id.completed_image);
            imgView = (ImageView) view.findViewById(R.id.hk_img);
            radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
            checkedRadioButton = (RadioButton) view.findViewById(radioGroup.getCheckedRadioButtonId());
        }

        @Override
        public String toString() {
            return super.toString() + " " + nameView.getText();
        }
    }
}