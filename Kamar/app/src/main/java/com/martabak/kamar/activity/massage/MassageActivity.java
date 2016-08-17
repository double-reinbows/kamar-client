package com.martabak.kamar.activity.massage;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.options.MassageOption;
import com.martabak.kamar.service.StaffServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rx.Observer;

/**
 * This activity generates the list of massage options and allows the guest to request one.
 */
public class MassageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_massage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });
        TextView roomNumberTextView = (TextView)findViewById(R.id.toolbar_roomnumber);
        String roomNumber = getSharedPreferences("userSettings", MODE_PRIVATE)
                .getString("roomNumber", "none");
        roomNumberTextView.setText(getString(R.string.room_number) + ": " + roomNumber);
        // END GENERIC LAYOUT STUFF

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.massage_list);
        final List<MassageOption> massageOptions = new ArrayList<>();
        final MassageRecyclerViewAdapter recyclerViewAdapter = new MassageRecyclerViewAdapter(massageOptions);
        recyclerView.setAdapter(recyclerViewAdapter);

        StaffServer.getInstance(this).getMassageOptions().subscribe(new Observer<List<MassageOption>>() {
            @Override public void onCompleted() {
                Log.d(MassageActivity.class.getCanonicalName(), "onCompleted");
                recyclerViewAdapter.notifyDataSetChanged();
            }
            @Override public void onError(Throwable e) {
                Log.d(MassageActivity.class.getCanonicalName(), "onError", e);
                e.printStackTrace();
            }
            @Override public void onNext(final List<MassageOption> options) {
                Log.d(MassageActivity.class.getCanonicalName(), options.size() + " massage options found");
                massageOptions.addAll(options);
            }
        });
    }

    public class MassageRecyclerViewAdapter
            extends RecyclerView.Adapter<MassageRecyclerViewAdapter.ViewHolder> {

        protected int selectedPos = -1;

        private final List<MassageOption> mValues;

        public MassageRecyclerViewAdapter(List<MassageOption> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.massage_list_row, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.itemView.setSelected(selectedPos == position);
            holder.item = mValues.get(position);
            // TODO image view
            holder.lengthView.setText(holder.item.length.toString() + " mins");
            holder.priceView.setText("Rp. " + holder.item.price.toString());
            if (Locale.getDefault().getLanguage().equals(Locale.ENGLISH.getLanguage()))
            holder.nameView.setText(holder.item.getName());
            holder.descriptionView.setText(holder.item.getDescription());
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public MassageOption item;
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
                nameView = (TextView) view.findViewById(R.id.massage_name);
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

}