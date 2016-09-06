package com.martabak.kamar.activity.housekeeping;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HousekeepingOptionAdapter
        extends RecyclerView.Adapter<HousekeepingOptionAdapter.ViewHolder> {

    protected int selectedPos = -1;

    private final List<HousekeepingOption> housekeepingOptions;
    private HashMap<String, Integer> idToQuantity;
    private Context context;
    private View.OnClickListener submitButtonListener;

    public HousekeepingOptionAdapter(List<HousekeepingOption> hkOptions, HashMap<String, Integer> items,
                                     Context context, View.OnClickListener submitButtonListener) {
        this.housekeepingOptions = hkOptions;
        this.idToQuantity = items;
        this.context = context;
        this.submitButtonListener = submitButtonListener;
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
        /*
        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO: do shit
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
*/

        holder.submitButton.setOnClickListener(submitButtonListener);
        Log.v("BBB", holder.toString());
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

        public ViewHolder(View view) {
            super(view);
            rootView = view;
            nameView = (TextView) view.findViewById(R.id.hk_name);
            submitButton = (ImageView) view.findViewById(R.id.start_fragment);
            spinner = (Spinner) view.findViewById(R.id.hk_spinner);
        }

        @Override
        public String toString() {
            return super.toString() + " " + nameView.getText();
        }
    }

    public Spinner getSpinner(ViewHolder holder ) {
        return holder.spinner; }
}