package com.martabak.kamar.activity.restaurant;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.martabak.kamar.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by adarsh on 30/05/16.
 */
public class RestaurantConfirmationArrayAdapter
        extends RecyclerView.Adapter<RestaurantConfirmationArrayAdapter.RestaurantConfirmationViewHolder> {

    private HashMap<String,Double> restaurantItems;

    public class RestaurantConfirmationViewHolder extends RecyclerView.ViewHolder {
        public TextView restaurantTextView;
        public TextView restaurantPriceView;

        public RestaurantConfirmationViewHolder(View restaurantConfirmationView) {
            super(restaurantConfirmationView);
            restaurantTextView = (TextView) restaurantConfirmationView.findViewById(R.id.restaurant_confirmation_text);
            restaurantPriceView = (TextView) restaurantConfirmationView.findViewById(R.id.restaurant_confirmation_price);
        }
    }

    public RestaurantConfirmationArrayAdapter(HashMap<String,Double> restaurantItems) {
        restaurantItems= restaurantItems;
    }

    @Override
    public int getItemCount() {
        return restaurantItems.size();
    }

    @Override
    public RestaurantConfirmationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.restaurant_confirmation_item, viewGroup, false);
        RestaurantConfirmationViewHolder rc = new RestaurantConfirmationViewHolder(v);
        return rc;
    }

    @Override
    public void onBindViewHolder(RestaurantConfirmationViewHolder restaurantConfirmationViewHolder, int i) {
        //restaurantConfirmationViewHolder.restaurantTextView.setText(restaurantItems.get(i.toString()));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}