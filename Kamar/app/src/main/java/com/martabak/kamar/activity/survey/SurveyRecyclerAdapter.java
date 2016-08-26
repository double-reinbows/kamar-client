package com.martabak.kamar.activity.survey;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.SurveyQuestion;

import java.util.List;

/**
 * Created by rei on 25/08/16.
 */
public class SurveyRecyclerAdapter
        extends RecyclerView.Adapter<SurveyRecyclerAdapter.ViewHolder> {

    protected int selectedPos = -1;

    private final List<SurveyQuestion> mValues;

    public SurveyRecyclerAdapter(List<SurveyQuestion> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.survey_list_row, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemPosition = holder.getAdapterPosition();
                Log.v("Clicked on: ",mValues.get(itemPosition).getQuestion());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.itemView.setSelected(selectedPos == position);
        holder.item = mValues.get(position);

//        Log.d(MassageActivity.class.getCanonicalName(), "onBindViewHolder " + holder.item.getName());
        // TODO image view
        holder.mainText.setText(holder.item.getQuestion());

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public SurveyQuestion item;
        public final View rootView;
        //        public final ImageView imageView;
        public final TextView mainText;
//        public final TextView lengthView;
//        public final TextView priceView;
//        public final TextView descriptionView;

        public ViewHolder(View view) {
            super(view);
            rootView = view;
//            imageView = (ImageView) view.findViewById(R.id.massage_image);
//            nameView = (TextView) view.findViewById(R.id.massage_name);
//            lengthView = (TextView) view.findViewById(R.id.massage_length);
//            priceView = (TextView) view.findViewById(R.id.massage_price);
//            descriptionView = (TextView) view.findViewById(R.id.massage_description);
            mainText = (TextView) view.findViewById(R.id.survey_row_text);

        }

        @Override
        public String toString() {
            return super.toString() + " " + mainText.getText();
        }
    }
}