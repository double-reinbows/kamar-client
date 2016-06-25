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
 * The restaurant confirmation array adapter class is a recycler view to hold the items for the
 *  restaurant confirmation activity.
 */
public class RestaurantConfirmationArrayAdapter
        extends RecyclerView.Adapter<RestaurantConfirmationArrayAdapter.RestaurantConfirmationViewHolder> {

    private List<String> mRestaurantTextItems;
    private List<String> mRestaurantUnitPriceItems;
    private List<String> mRestaurantSubPriceItems;
    private List<String> mRestaurantQuantityItems;

    public class RestaurantConfirmationViewHolder extends RecyclerView.ViewHolder {
        public TextView restaurantTextView;
        public TextView restaurantUnitPriceView;
        public TextView restaurantSubPriceTextView;
        public TextView restaurantQuantityTextView;

        public RestaurantConfirmationViewHolder(View restaurantConfirmationView) {
            super(restaurantConfirmationView);
            restaurantTextView = (TextView) restaurantConfirmationView.findViewById(R.id.restaurant_confirmation_text);
            restaurantUnitPriceView = (TextView) restaurantConfirmationView.findViewById(R.id.restaurant_confirmation_unit_price);
            restaurantSubPriceTextView = (TextView) restaurantConfirmationView.findViewById(R.id.restaurant_confirmation_sub_price);
            restaurantQuantityTextView = (TextView) restaurantConfirmationView.findViewById(R.id.restaurant_confirmation_quantity);
        }
    }

    public RestaurantConfirmationArrayAdapter(List<String> restaurantTextItems, List<String> restaurantUnitPriceItems,
                                              List<String> restaurantSubPriceItems, List<String> restaurantQuantityItems) {

        mRestaurantTextItems = restaurantTextItems;
        mRestaurantQuantityItems = restaurantQuantityItems;
        mRestaurantUnitPriceItems = restaurantUnitPriceItems;
        mRestaurantSubPriceItems = restaurantSubPriceItems;
    }

    @Override
    public int getItemCount() {
        return mRestaurantTextItems.size();
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
        restaurantConfirmationViewHolder.restaurantTextView.setText(mRestaurantTextItems.get(i));
        restaurantConfirmationViewHolder.restaurantQuantityTextView.setText(mRestaurantQuantityItems.get(i));
        restaurantConfirmationViewHolder.restaurantUnitPriceView.setText(mRestaurantUnitPriceItems.get(i));
        restaurantConfirmationViewHolder.restaurantSubPriceTextView.setText(mRestaurantSubPriceItems.get(i));

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}