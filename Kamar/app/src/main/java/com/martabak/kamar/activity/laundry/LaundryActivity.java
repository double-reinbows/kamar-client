package com.martabak.kamar.activity.laundry;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.martabak.kamar.R;
import com.martabak.kamar.activity.guest.AbstractGuestBarsActivity;
import com.martabak.kamar.activity.guest.SimpleDividerItemDecoration;
import com.martabak.kamar.activity.guest.GuestHomeActivity;
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
 * Provides laundry options for the guest.
 */
public class LaundryActivity extends AbstractGuestBarsActivity {

    private RecyclerView recyclerView;
    private List<LaundryOption> laundryOptions;
    private List<LaundryOrderItem> laundryOrderItems;


    protected Options getOptions() {
        return new Options()
                .withBaseLayout(R.layout.activity_laundry)
                .withToolbarLabel(getString(R.string.laundry_label))
                .showTabLayout(false)
                .showLogoutIcon(false)
                .enableChatIcon(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View laundryView = findViewById(android.R.id.content);
        recyclerView = (RecyclerView)findViewById(R.id.laundry_recycleview);
        laundryOptions = new ArrayList<>();
        laundryOrderItems = new ArrayList<>();
        final LaundryRecyclerViewAdapter recyclerViewAdapter = new LaundryRecyclerViewAdapter(laundryOptions, laundryView);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(LaundryActivity.this));
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        Button submitButton = (Button) findViewById(R.id.laundry_submit);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size =  laundryOptions.size();

                for (int i=0; i < recyclerView.getChildCount(); i++)
                {
                    View laundryView = recyclerView.getChildAt(i);

                    TextView quantityView = (TextView) laundryView.findViewById(R.id.quantity_text);


                    //TextView priceView = (TextView) laundryView.findViewById(R.id.laundry_item_price_total);

                    CheckBox laundryCheckButton = (CheckBox) laundryView.findViewById(R.id.laundry_price_option);
                    CheckBox pressingCheckButton = (CheckBox) laundryView.findViewById(R.id.pressing_price_option);

                    CheckBox onHangerCheckButton = (CheckBox) laundryView.findViewById(R.id.on_hanger_option);
                    CheckBox noIroningCheckButton = (CheckBox) laundryView.findViewById(R.id.no_ironing_option);
                    CheckBox foldedCheckButton = (CheckBox) laundryView.findViewById(R.id.folded_option);

                    EditText notesEditText = (EditText) laundryView.findViewById(R.id.laundry_notes);

                    LaundryOption laundryOption= laundryOptions.get(i);
                    int quantity = Integer.parseInt(quantityView.getText().toString());
                    List<String> laundryExtras;
                    if ((laundryCheckButton.isChecked() || pressingCheckButton.isChecked()) && (quantity > 0))
                    {

                        int itemPrice = 0;

                        if (laundryCheckButton.isChecked())
                        {
                            itemPrice = itemPrice + Integer.parseInt(laundryCheckButton.getText().toString());
                        }

                        if (pressingCheckButton.isChecked())
                        {
                            itemPrice = itemPrice + Integer.parseInt(pressingCheckButton.getText().toString());
                        }

                        itemPrice = itemPrice * quantity;

                        laundryExtras = setLaundryExtras(onHangerCheckButton, noIroningCheckButton, foldedCheckButton);
                        String stringLaundryExtras = TextUtils.join(", ",laundryExtras);

                        LaundryOrderItem laundryOrderItem = new LaundryOrderItem(quantity, itemPrice,
                                laundryCheckButton.isChecked(), pressingCheckButton.isChecked(), laundryOption,
                                stringLaundryExtras, notesEditText.getText().toString());

                        laundryOrderItems.add(laundryOrderItem);
                    }
                }

                TextView laundryTextView = (TextView) findViewById(R.id.laundry_total_price);

                int totalPrice = Integer.parseInt(laundryTextView.getText().toString());


                if (laundryOrderItems.size() > 0)
                {
                    LaundryOrder laundryOrder = new LaundryOrder("", laundryOrderItems, totalPrice);
                    sendLaundryRequest(laundryOrder);                                            ;
                }
            }
        });

        startFragment();

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

    /*add the extras to the list*/
    private List<String> setLaundryExtras(CheckBox noIroning, CheckBox onHanger, CheckBox folded)
    {
        List<String> laundryExtras = new ArrayList<>();
        if (noIroning.isChecked())
        {
            laundryExtras.add(noIroning.getText().toString());
        }
        if (onHanger.isChecked())
        {
            laundryExtras.add(onHanger.getText().toString());
        }
        if (folded.isChecked())
        {
            laundryExtras.add(folded.getText().toString());
        }
        return laundryExtras;
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
                    laundryOrder)
            ).subscribe(new Observer<Permintaan>() {
                @Override public void onCompleted() {
                    Log.d(LaundryActivity.class.getCanonicalName(), "createPermintaan() On completed");
                    Toast.makeText(
                            LaundryActivity.this.getApplicationContext(),
                            R.string.laundry_result,
                            Toast.LENGTH_SHORT
                    ).show();
                    startActivity(new Intent(getBaseContext(), GuestHomeActivity.class));
                    finish();
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


        } else {
            Toast.makeText(
                    LaundryActivity.this.getApplicationContext(),
                    R.string.no_guest_in_room,
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

    }

    public void startFragment() {
        LaundryPermintaanFragment f = new LaundryPermintaanFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f);
        ft.commit();
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
                        holder.onHangerCheckButton.setEnabled(true);
                        holder.noIroningCheckButton.setEnabled(true);
                        holder.foldedCheckButton.setEnabled(true);
                    }

                    Integer prevItemTotal = 0;
                    if (holder.itemPriceTotal != 0)
                    {
                        prevItemTotal = holder.itemPriceTotal;
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
                    holder.itemPriceTotal = newItemTotal;

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
                        holder.onHangerCheckButton.setEnabled(false);
                        holder.noIroningCheckButton.setEnabled(false);
                        holder.foldedCheckButton.setEnabled(false);
                    }
                    int laundryPrice = 0;
                    int pressingPrice = 0;

                    if (newQuantity >= 0) {
                        Integer prevItemTotal = holder.itemPriceTotal;

                        if (holder.laundryCheckButton.isChecked()) {
                            laundryPrice = Integer.parseInt(holder.laundryCheckButton.getText().toString());
                        }
                        if (holder.pressingCheckButton.isChecked()) {
                            pressingPrice = Integer.parseInt(holder.pressingCheckButton.getText().toString());
                        }


                        Integer newItemTotal = calculateItemPrice(laundryPrice, pressingPrice, newQuantity);

                        holder.itemPriceTotal = newItemTotal;
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
                    .resize(150,150)
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

                        int prevItemTotal = 0;
                        if (holder.itemPriceTotal != prevItemTotal)
                        {
                            prevItemTotal = holder.itemPriceTotal;
                        }

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

                            int newTotalPrice = (tempTotalPrice - prevItemTotal) + currentPrice;
                            holder.itemPriceTotal = currentPrice;
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
                            int currentPrice = calculateItemPrice(laundryPrice, pressingPrice, newQuantity);

                            int tempTotalPrice = Integer.parseInt(laundryTextView.getText().toString());

                            int newTotalPrice = tempTotalPrice - (prevItemTotal - currentPrice);
                            holder.itemPriceTotal = currentPrice;
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

                        int prevItemTotal = 0;
                        if (holder.itemPriceTotal != prevItemTotal)
                        {
                            prevItemTotal = holder.itemPriceTotal;
                        }

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

                            int newTotalPrice = (tempTotalPrice - prevItemTotal) + currentPrice;
                            holder.itemPriceTotal = currentPrice;
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


                            int currentPrice = calculateItemPrice(laundryPrice, pressingPrice, newQuantity);

                            int tempTotalPrice = Integer.parseInt(laundryTextView.getText().toString());

                            int newTotalPrice = tempTotalPrice - (prevItemTotal - currentPrice);
                            holder.itemPriceTotal = currentPrice;
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
            public final CheckBox noIroningCheckButton;
            public final CheckBox onHangerCheckButton;
            public final CheckBox foldedCheckButton;
            public int itemPriceTotal;
            //public final TextView itemPriceTotal;
            public final EditText notesEditText;

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
                itemPriceTotal = 0;
                noIroningCheckButton = (CheckBox) view.findViewById(R.id.no_ironing_option);
                onHangerCheckButton = (CheckBox) view.findViewById(R.id.on_hanger_option);
                foldedCheckButton = (CheckBox) view.findViewById(R.id.folded_option);
                notesEditText = (EditText) view.findViewById(R.id.laundry_notes);
            }


            @Override
            public String toString() {
                return super.toString() + " " + nameView.getText();
            }
        }
    }


}
