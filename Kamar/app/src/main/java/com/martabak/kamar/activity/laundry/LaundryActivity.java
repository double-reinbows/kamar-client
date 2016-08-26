package com.martabak.kamar.activity.laundry;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.options.LaundryOption;
import com.martabak.kamar.service.Server;
import com.martabak.kamar.service.StaffServer;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;

/**
 * Created by adarsh on 21/08/16.
 */
public class LaundryActivity extends AppCompatActivity  {

    private RecyclerView recyclerView;
    private List<LaundryOption> laundryOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.guest_toolbar);
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


        recyclerView = (RecyclerView)findViewById(R.id.laundry_recycleview);
        laundryOptions = new ArrayList<>();
        final LaundryRecyclerViewAdapter recyclerViewAdapter = new LaundryRecyclerViewAdapter(laundryOptions);
        recyclerView.setAdapter(recyclerViewAdapter);

        StaffServer.getInstance(this).getLaundryOptions().subscribe(new Observer<List<LaundryOption>>() {
            @Override public void onCompleted() {
                Log.d(LaundryActivity.class.getCanonicalName(), "onCompleted");
                recyclerViewAdapter.notifyDataSetChanged();
            }
            @Override public void onError(Throwable e) {
                Log.d(LaundryActivity.class.getCanonicalName(), "onError", e);
                e.printStackTrace();
            }
            @Override public void onNext(final List<LaundryOption> options) {
                Log.d(LaundryActivity.class.getCanonicalName(), options.size() + " laundry options found");
                laundryOptions.addAll(options);
            }
        });
    }


    public class LaundryRecyclerViewAdapter
            extends RecyclerView.Adapter<LaundryRecyclerViewAdapter.ViewHolder> {

        protected int selectedPos = -1;

        private final List<LaundryOption> mValues;

        public LaundryRecyclerViewAdapter(List<LaundryOption> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.laundry_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.itemView.setSelected(selectedPos == position);
            holder.item = mValues.get(position);

            Log.d(LaundryActivity.class.getCanonicalName(), "Loading image " + holder.item.getImageUrl() + " into " + holder.imageView);
            Server.picasso(LaundryActivity.this)
                    .load(holder.item.getImageUrl())
                    .placeholder(R.drawable.loading_batik)
                    .error(R.drawable.error)
                    .into(holder.imageView);
            holder.nameView.setText(holder.item.getName());

            if (holder.item.laundryPrice != null) {
                holder.laundryRadioButton.setText(holder.item.laundryPrice);
            }

            if (holder.item.pressingPrice != null) {
                holder.pressingRadioButton.setText(holder.item.pressingPrice);
            }
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public LaundryOption item;
            public final View rootView;
            public final ImageView imageView;
            public final TextView nameView;
            public final RadioButton laundryRadioButton;
            public final RadioButton pressingRadioButton;

            public ViewHolder(View view) {
                super(view);
                rootView = view;
                imageView = (ImageView) view.findViewById(R.id.laundry_option_image);
                nameView = (TextView) view.findViewById(R.id.laundry_option_text);
                laundryRadioButton = (RadioButton) view.findViewById(R.id.laundry_price_option);
                pressingRadioButton = (RadioButton) view.findViewById(R.id.pressing_price_option);
            }

            @Override
            public String toString() {
                return super.toString() + " " + nameView.getText();
            }
        }
    }




}
