package com.martabak.kamar.activity.restaurant;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.martabak.kamar.R;
import com.martabak.kamar.service.Server;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * The ic_restaurant confirmation array adapter class is a recycler view to hold the items for the
 *  ic_restaurant confirmation activity.
 */
public class RestaurantConfirmationArrayAdapter
        extends RecyclerView.Adapter<RestaurantConfirmationArrayAdapter.RestaurantConfirmationViewHolder> {

    private List<String> mRestaurantTextItems;
    private List<String> mRestaurantSubPriceItems;
    private List<String> mRestaurantQuantityItems;
    private List<String> mRestaurantNoteItems;
    private List<String> mRestaurantImgUrls;
    private Context context;

    public class RestaurantConfirmationViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.restaurant_confirmation_sub_price) TextView restaurantSubPriceTextView;
        @BindView(R.id.restaurant_confirmation_text) TextView restaurantTextView;
        @BindView(R.id.restaurant_confirmation_quantity) TextView restaurantQuantityTextView;
        @BindView(R.id.item_note) TextView restaurantNoteTextView;
        @BindView(R.id.item_img) ImageView itemImg;

        public RestaurantConfirmationViewHolder(View restaurantConfirmationView) {
            super(restaurantConfirmationView);
            ButterKnife.bind(this, restaurantConfirmationView);
        }
    }

    public RestaurantConfirmationArrayAdapter(Context context,
                                              List<String> restaurantTextItems,
                                              List<String> restaurantSubPriceItems,
                                              List<String> restaurantQuantityItems,
                                              List<String> restaurantNoteItems,
                                              List<String> restaurantImgUrls) {

        this.context = context;
        mRestaurantTextItems = restaurantTextItems;
        mRestaurantQuantityItems = restaurantQuantityItems;
        mRestaurantSubPriceItems = restaurantSubPriceItems;
        mRestaurantNoteItems = restaurantNoteItems;
        mRestaurantImgUrls = restaurantImgUrls;
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
        restaurantConfirmationViewHolder.restaurantQuantityTextView.setText(context.getString(R.string.quantity)+" : "+mRestaurantQuantityItems.get(i));
        restaurantConfirmationViewHolder.restaurantSubPriceTextView.setText(mRestaurantSubPriceItems.get(i));
        restaurantConfirmationViewHolder.restaurantNoteTextView.setText(mRestaurantNoteItems.get(i));

//        Set up item image
//        Log.d(RestaurantExpListAdapter.class.getCanonicalName(), "Loading image " + currConsumable.getImageUrl() + " into " + itemImg);
        Server.picasso(context)
                .load(mRestaurantImgUrls.get(i))
                .placeholder(R.drawable.loading_batik)
                .error(R.drawable.error)
                .into(restaurantConfirmationViewHolder.itemImg);

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}