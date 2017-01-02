package com.martabak.kamar.activity.survey;

import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.SurveyQuestion;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class SurveyRecyclerAdapter
        extends RecyclerView.Adapter<SurveyRecyclerAdapter.ViewHolder> {

    protected int selectedPos = -1;

    private final List<SurveyQuestion> mValues;
    private final RatingBar.OnRatingBarChangeListener l;

    public SurveyRecyclerAdapter(List<SurveyQuestion> items, RatingBar.OnRatingBarChangeListener l) {
        mValues = items;
        this.l = l;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.survey_list_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.itemView.setSelected(selectedPos == position);
        holder.item = mValues.get(position);
        holder.mainText.setText(holder.item.getQuestion());
        holder.ratingBar.setOnRatingBarChangeListener(l);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public SurveyQuestion item;
        public final View rootView;
        @BindView(R.id.survey_row_text) TextView mainText;
        @BindView(R.id.survey_rating_bar) RatingBar ratingBar;

        public ViewHolder(View view) {
            super(view);
            rootView = view;
            ButterKnife.bind(this, view);
        }

        @Override
        public String toString() {
            return super.toString() + " " + mainText.getText();
        }
    }
}