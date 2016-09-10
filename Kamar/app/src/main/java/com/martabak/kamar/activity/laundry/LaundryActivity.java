package com.martabak.kamar.activity.laundry;

import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.options.LaundryOption;
import com.martabak.kamar.domain.permintaan.LaundryOrderItem;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.domain.permintaan.LaundryOrder;
import com.martabak.kamar.service.PermintaanServer;
import com.martabak.kamar.service.Server;
import com.martabak.kamar.service.StaffServer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.zip.Inflater;

import rx.Observer;

/**
 * Created by adarsh on 21/08/16.
 */
public class LaundryActivity extends AppCompatActivity  {

    private RecyclerView recyclerView;
    private List<LaundryOption> laundryOptions;
    private List<LaundryOrderItem> laundryOrderItems;
    private List<String> laundryInstructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_laundry);

        final View laundryView = this.findViewById(android.R.id.content);
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
        laundryOrderItems = new ArrayList<>();
        laundryInstructions = new ArrayList<>();
        final LaundryRecyclerViewAdapter recyclerViewAdapter = new LaundryRecyclerViewAdapter(laundryOptions, laundryView);
        recyclerView.setAdapter(recyclerViewAdapter);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //if the radio buttons are toggled, add it to the laundry instruction list here
        final RadioButton noIroning = (RadioButton) findViewById(R.id.no_ironing);
        final RadioButton onHanger = (RadioButton) findViewById(R.id.on_hanger);
        final RadioButton folded = (RadioButton) findViewById(R.id.folded);

        noIroning.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {

                    laundryInstructions.add(noIroning.getText().toString());
                }
            }
        });

        onHanger.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    laundryInstructions.add(onHanger.getText().toString());
                }
            }
        });

        folded.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    laundryInstructions.add(folded.getText().toString());
                }
            }
        });
        Button submitButton = (Button) findViewById(R.id.laundry_submit);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size =  laundryOptions.size();

                for (int i=0; i < laundryOptions.size(); i++)
                {
                    View laundryView = layoutManager.findViewByPosition(i);
                    TextView quantityView = (TextView) laundryView.findViewById(R.id.quantity_text);
                    TextView priceView = (TextView) laundryView.findViewById(R.id.laundry_item_price_total);
                    CheckBox laundryCheckButton = (CheckBox) laundryView.findViewById(R.id.laundry_price_option);
                    CheckBox pressingCheckButton = (CheckBox) laundryView.findViewById(R.id.pressing_price_option);

                    LaundryOption laundryOption= laundryOptions.get(i);
                    int quantity = Integer.parseInt(quantityView.getText().toString());


                    if ((laundryCheckButton.isChecked() || pressingCheckButton.isChecked()) && (quantity > 0))
                    {
                        int itemPrice = Integer.parseInt(priceView.getText().toString());
                        LaundryOrderItem laundryOrderItem = new LaundryOrderItem(quantity, itemPrice, laundryOption);

                        laundryOrderItems.add(laundryOrderItem);
                    }
                }

                TextView laundryTextView = (TextView) findViewById(R.id.laundry_total_price);

                int totalPrice = Integer.parseInt(laundryTextView.getText().toString());


                if (laundryOrderItems.size() > 0)
                {
                    String stringLaundryInstructions = TextUtils.join(", ",laundryInstructions);
                    LaundryOrder laundryOrder = new LaundryOrder(stringLaundryInstructions, laundryOrderItems, totalPrice);
                    sendLaundryRequest(laundryOrder);                                            ;
                }
            }
        });


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

    /*Send laundry request*/
    public void sendLaundryRequest(LaundryOrder laundryOrder) {

        String owner = Permintaan.OWNER_FRONTDESK;
        String type = Permintaan.TYPE_LAUNDRY;
        String roomNumber = getSharedPreferences("userSettings", MODE_PRIVATE)
                .getString("roomNumber", null);
        String guestId = getSharedPreferences("userSettings", MODE_PRIVATE)
                .getString("guestId", null);
        String state = Permintaan.STATE_NEW;
        Date currentDate = Calendar.getInstance().getTime();



        /* create permintaan*/
        if (!guestId.equals("none") && !roomNumber.equals("none")) {
            PermintaanServer.getInstance(this.getBaseContext()).createPermintaan(new Permintaan(
                    owner,
                    type,
                    roomNumber,
                    guestId,
                    state,
                    currentDate,
                    null,
                    null,
                    laundryOrder)
            ).subscribe(new Observer<Permintaan>() {
                @Override public void onCompleted() {
                    Log.d(LaundryActivity.class.getCanonicalName(), "createPermintaan() On completed");
                    Toast.makeText(
                            LaundryActivity.this.getApplicationContext(),
                            R.string.laundry_result,
                            Toast.LENGTH_SHORT
                    ).show();
                    //finish();
                }
                @Override public void onError(Throwable e) {
                    Log.d(LaundryActivity.class.getCanonicalName(), "createPermintaan() On error");
                    e.printStackTrace();
                }
                @Override public void onNext(Permintaan permintaan) {
                    Log.d(LaundryActivity.class.getCanonicalName(), "createPermintaan() On next" + permintaan);
                    if (permintaan == null) {
                        Toast.makeText(
                                LaundryActivity.this,
                                getString(R.string.restaurant_error),
                                Toast.LENGTH_LONG
                        ).show();
                        finish();
                    }
                }
            });


        }

    }


    public class LaundryRecyclerViewAdapter
            extends RecyclerView.Adapter<LaundryRecyclerViewAdapter.ViewHolder> {

        protected int selectedPos = -1;

        private final List<LaundryOption> mValues;

        private final View mParentView;

        public LaundryRecyclerViewAdapter(List<LaundryOption> items, View parentView) {
            mValues = items;
            mParentView = parentView;
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

            final int minQuantity = 0;

            final TextView laundryTextView = (TextView) mParentView.findViewById(R.id.laundry_total_price);

            holder.quantityText.setText(String.valueOf(minQuantity));

            //add  button
            holder.addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer newQuantity = Integer.parseInt(holder.quantityText.getText().toString()) + 1;
                    holder.quantityText.setText(newQuantity.toString());
                    if (newQuantity > minQuantity)
                    {
                        holder.laundryCheckButton.setEnabled(true);
                        holder.pressingCheckButton.setEnabled(true);

                    }

                    Integer prevItemTotal = 0;
                    if (!holder.itemPriceTotal.getText().toString().equals(""))
                    {
                        prevItemTotal = Integer.parseInt(holder.itemPriceTotal.getText().toString());
                    }


                    int laundryPrice = 0;
                    int pressingPrice = 0;
                    if (holder.laundryCheckButton.isChecked())
                    {
                        laundryPrice = Integer.parseInt(holder.laundryCheckButton.getText().toString());
                    }
                    if (holder.pressingCheckButton.isChecked())
                    {
                        pressingPrice = Integer.parseInt(holder.pressingCheckButton.getText().toString());
                    }

                    Integer newItemTotal = calculateItemPrice(laundryPrice, pressingPrice, newQuantity);
                    holder.itemPriceTotal.setText(
                            String.valueOf(calculateItemPrice(laundryPrice, pressingPrice, newQuantity)));

                    Integer difference = newItemTotal - prevItemTotal;

                    //add the new price total to the tally
                    Integer currentTotal = Integer.parseInt(laundryTextView.getText().toString()) + difference;
                    laundryTextView.setText(String.valueOf(currentTotal));

                }
            });

            //minus button
            holder.minusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer newQuantity = Integer.parseInt(holder.quantityText.getText().toString()) - 1;
                    if (newQuantity >= minQuantity)
                    {
                        holder.quantityText.setText(newQuantity.toString());
                    }
                    if (newQuantity == minQuantity)
                    {
                        holder.laundryCheckButton.setEnabled(false);
                        holder.pressingCheckButton.setEnabled(false);
                    }
                    int laundryPrice = 0;
                    int pressingPrice = 0;

                    if (newQuantity >= 0) {
                        Integer prevItemTotal = Integer.parseInt(holder.itemPriceTotal.getText().toString());

                        if (holder.laundryCheckButton.isChecked()) {
                            laundryPrice = Integer.parseInt(holder.laundryCheckButton.getText().toString());
                        }
                        if (holder.pressingCheckButton.isChecked()) {
                            pressingPrice = Integer.parseInt(holder.pressingCheckButton.getText().toString());
                        }


                        Integer newItemTotal = calculateItemPrice(laundryPrice, pressingPrice, newQuantity);

                        holder.itemPriceTotal.setText(
                                String.valueOf(newItemTotal));
                        Integer difference = prevItemTotal - newItemTotal;

                        //subtract the new price total to the tally
                        Integer currentTotal = Integer.parseInt(laundryTextView.getText().toString()) - difference;
                        laundryTextView.setText(String.valueOf(currentTotal));
                    }
                }
            });


            Log.d(LaundryActivity.class.getCanonicalName(), "Loading image " + holder.item.getImageUrl() + " into " + holder.imageView);
            Server.picasso(LaundryActivity.this)
                    .load(holder.item.getImageUrl())
                    .placeholder(R.drawable.loading_batik)
                    .error(R.drawable.error)
                    .into(holder.imageView);
            Log.v(holder.item.getName(), LaundryActivity.this.toString());

            holder.nameView.setText(holder.item.getName());

            if (holder.item.laundryPrice != null) {
                holder.laundryCheckButton.setText(holder.item.laundryPrice.toString());
                holder.laundryCheckButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {

                            int pressingPrice = 0;
                            int laundryPrice = Integer.parseInt(holder.laundryCheckButton.getText().toString());
                            Integer newQuantity = Integer.parseInt(holder.quantityText.getText().toString());

                            if (holder.pressingCheckButton.isChecked())
                            {
                                pressingPrice = Integer.parseInt(holder.pressingCheckButton.getText().toString());
                            }
                            int currentPrice = calculateItemPrice(laundryPrice, pressingPrice, newQuantity);

                            int tempTotalPrice = Integer.parseInt(laundryTextView.getText().toString());

                            int newTotalPrice = tempTotalPrice + currentPrice;
                            holder.itemPriceTotal.setText(String.valueOf(currentPrice));
                            laundryTextView.setText(String.valueOf(newTotalPrice));
                        }
                        else
                        {
                            int pressingPrice = 0;
                            int laundryPrice = 0;
                            Integer newQuantity = Integer.parseInt(holder.quantityText.getText().toString());

                            if (holder.pressingCheckButton.isChecked())
                            {
                                pressingPrice = Integer.parseInt(holder.pressingCheckButton.getText().toString());
                            }


                            Integer prevPrice = 0;
                            if (!holder.itemPriceTotal.getText().toString().equals(""))
                            {
                                prevPrice = Integer.parseInt(holder.itemPriceTotal.getText().toString());
                            }

                            int currentPrice = calculateItemPrice(laundryPrice, pressingPrice, newQuantity);

                            int tempTotalPrice = Integer.parseInt(laundryTextView.getText().toString());

                            int newTotalPrice = tempTotalPrice - (prevPrice - currentPrice);
                            holder.itemPriceTotal.setText(String.valueOf(currentPrice));
                            laundryTextView.setText(String.valueOf(newTotalPrice));
                        }
                    }
                });
            }

            if (holder.item.pressingPrice != null) {
                holder.pressingCheckButton.setText(holder.item.pressingPrice.toString());
                holder.pressingCheckButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            int pressingPrice = Integer.parseInt(holder.pressingCheckButton.getText().toString());
                            int laundryPrice = 0;
                            Integer newQuantity = Integer.parseInt(holder.quantityText.getText().toString());

                            if (holder.laundryCheckButton.isChecked())
                            {
                                laundryPrice = Integer.parseInt(holder.laundryCheckButton.getText().toString());
                            }
                            int currentPrice = calculateItemPrice(laundryPrice, pressingPrice, newQuantity);

                            int tempTotalPrice = Integer.parseInt(laundryTextView.getText().toString());

                            int newTotalPrice = tempTotalPrice + currentPrice;
                            holder.itemPriceTotal.setText(String.valueOf(currentPrice));
                            laundryTextView.setText(String.valueOf(newTotalPrice));
                        }
                        else
                        {
                            int pressingPrice = 0;
                            int laundryPrice = 0;
                            Integer newQuantity = Integer.parseInt(holder.quantityText.getText().toString());

                            if (holder.laundryCheckButton.isChecked())
                            {
                                laundryPrice = Integer.parseInt(holder.laundryCheckButton.getText().toString());
                            }

                            Integer prevPrice = 0;
                            if (!holder.itemPriceTotal.getText().toString().equals(""))
                            {
                                prevPrice = Integer.parseInt(holder.itemPriceTotal.getText().toString());
                            }

                            int currentPrice = calculateItemPrice(laundryPrice, pressingPrice, newQuantity);

                            int tempTotalPrice = Integer.parseInt(laundryTextView.getText().toString());

                            int newTotalPrice = tempTotalPrice - (prevPrice - currentPrice);
                            holder.itemPriceTotal.setText(String.valueOf(currentPrice));
                            laundryTextView.setText(String.valueOf(newTotalPrice));
                        }
                    }
                });
            }

        }

        private int calculateItemPrice(int laundryPrice, int pressingPrice, int quantity)
        /*Calculate price for each individual item*/
        {
            int totalItemPrice = (laundryPrice + pressingPrice) * quantity;
            return totalItemPrice;
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public LaundryOption item;
            public final View rootView;
            public final ImageButton addButton;
            public final TextView quantityText;
            public final ImageButton minusButton;
            public final ImageView imageView;
            public final TextView nameView;
            public final CheckBox laundryCheckButton;
            public final CheckBox pressingCheckButton;
            public final TextView itemPriceTotal;

            public ViewHolder(View view) {
                super(view);
                rootView = view;
                addButton = (ImageButton) view.findViewById(R.id.add_button);
                quantityText = (TextView) view.findViewById(R.id.quantity_text);
                minusButton = (ImageButton) view.findViewById(R.id.minus_button);
                imageView = (ImageView) view.findViewById(R.id.laundry_option_image);
                nameView = (TextView) view.findViewById(R.id.laundry_option_text);
                laundryCheckButton = (CheckBox) view.findViewById(R.id.laundry_price_option);
                pressingCheckButton = (CheckBox) view.findViewById(R.id.pressing_price_option);
                itemPriceTotal = (TextView) view.findViewById(R.id.laundry_item_price_total);
            }


            @Override
            public String toString() {
                return super.toString() + " " + nameView.getText();
            }
        }
    }




}
