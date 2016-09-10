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
import android.widget.TextView;
import android.widget.Toast;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.SurveyQuestion;

import java.util.List;

/**
 *
 */
public class SurveyRecyclerAdapter
        extends RecyclerView.Adapter<SurveyRecyclerAdapter.ViewHolder> {

    protected int selectedPos = -1;

    private final List<SurveyQuestion> mValues;
    private final RadioGroup.OnCheckedChangeListener l;

    public SurveyRecyclerAdapter(List<SurveyQuestion> items, RadioGroup.OnCheckedChangeListener l) {
        mValues = items;
        this.l = l;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.survey_list_row, parent, false);
//        final ViewHolder holder = new ViewHolder(view);
//
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int itemPosition = holder.getAdapterPosition();
//                Log.v("Clicked on: ",mValues.get(itemPosition).getQuestion());
//            }
//        });
//        return holder;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.itemView.setSelected(selectedPos == position);
        holder.item = mValues.get(position);
        holder.mainText.setText(holder.item.getQuestion());
        holder.radioGroup.setOnCheckedChangeListener(l);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public SurveyQuestion item;
        public final View rootView;
        public final TextView mainText;
        public final RadioGroup radioGroup;
        public final RadioButton checkedRadioButton;

        public ViewHolder(View view) {
            super(view);
            rootView = view;
            radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
            checkedRadioButton = (RadioButton) view.findViewById(radioGroup.getCheckedRadioButtonId());
            mainText = (TextView) view.findViewById(R.id.survey_row_text);
        }

        @Override
        public String toString() {
            return super.toString() + " " + mainText.getText();
        }
    }
}