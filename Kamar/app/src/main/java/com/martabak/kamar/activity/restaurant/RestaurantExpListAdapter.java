package com.martabak.kamar.activity.restaurant;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.annotations.SerializedName;
import com.martabak.kamar.R;
import com.martabak.kamar.domain.Consumable;
import com.martabak.kamar.service.MenuServer;
import com.martabak.kamar.service.Server;

import java.util.HashMap;
import java.util.List;

import rx.Observer;

public class RestaurantExpListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> subsections; //header text
    //a list of each item's main text with header text as keys
    private HashMap<String, List<String>> subsectionToIds;
    //consumable dictionary with consumable.name keys
    private HashMap<String, Consumable> idToConsumable;
    //quantity dictionary with consumable.name keys
    private HashMap<String, Integer> idToQuantity;
    private HashMap<String, String> idToNote;
    private TextView subtotalText;
    private String type;

    public static final String TYPE_ORDER = "ORDER";
    public static final String TYPE_EDIT = "EDIT";

    public RestaurantExpListAdapter(Context context, List<String> subsections,
                                    HashMap<String, List<String>> subsectionToIds,
                                    HashMap<String, Consumable> idToConsumable,
                                    HashMap<String, Integer> idToQuantity,
                                    HashMap<String, String> idToNote,
                                    TextView subtotalText, String type) {
        this.context = context;
        this.subsections = subsections;
        this.subsectionToIds = subsectionToIds;
        this.idToConsumable = idToConsumable;
        this.idToQuantity = idToQuantity;
        this.subtotalText = subtotalText;
        this.idToNote = idToNote;
        this.type = type;
    }

    @Override
    public Consumable getChild(int groupPosition, int childPosition) {
        return idToConsumable.get(subsectionToIds.get(subsections.get(groupPosition)).get(childPosition));
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (type.equals(TYPE_ORDER)) {

                convertView = inflater.inflate(R.layout.restaurant_menu_item, null);
            } else if (type.equals(TYPE_EDIT)) {
                convertView = inflater.inflate(R.layout.restaurant_menu_item_edit, null);
            }
        }

        final Consumable currConsumable = getChild(groupPosition, childPosition);


            //Set up main text
            String itemName = currConsumable.getName();
            final TextView txtListChild = (TextView) convertView.findViewById(R.id.item_text);
            txtListChild.setText(itemName);

            //Set up price text
            String priceText = currConsumable.price.toString();
            TextView priceView = (TextView) convertView.findViewById(R.id.item_price);
            priceView.setText("Rp. " + priceText + " 000");

        if (type.equals(TYPE_ORDER)) {

            final TextView quantity = (TextView) convertView.findViewById(R.id.item_quantity);
            quantity.setText(idToQuantity.get(currConsumable._id).toString());
            quantity.invalidate();

            //Set up item image1
            ImageView itemImg = (ImageView) convertView.findViewById(R.id.item_img);
            Log.d(RestaurantExpListAdapter.class.getCanonicalName(), "Loading image " + currConsumable.getImageUrl() + " into " + itemImg);
            Server.picasso(context).setIndicatorsEnabled(true);
            Server.picasso(context)
                    .load(currConsumable.getImageUrl())
                    .resize(250, 125)
                    .placeholder(R.drawable.loading_batik)
                    .error(R.drawable.error)
                    .into(itemImg);

            /**
             * Implement the minus button
             */
            ImageView minusQuantityButton = (ImageView) convertView.findViewById(R.id.minus_button);

            minusQuantityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Integer currQuantity = idToQuantity.get(currConsumable._id);
                    if (currQuantity - 1 > -1) { //Stop user from setting to -1 quantity
                        //Minus 1 off the quantity
                        idToQuantity.put(currConsumable._id, (currQuantity - 1));
                        //minus subtotal by price of item
                        idToQuantity.put("subtotal", idToQuantity.get("subtotal") - currConsumable.price);
                        subtotalText.setText("Rp. " + idToQuantity.get("subtotal").toString() + " 000");
                        notifyDataSetChanged();
                    }
                }
            });

            /**
             * Implement the plus button
             */
            ImageView plusQuantityButton = (ImageView) convertView.findViewById(R.id.plus_button);

            plusQuantityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Integer currQuantity = idToQuantity.get(currConsumable._id);
                    if (currQuantity + 1 < 11) {
                        //Add 1 to the quantity
                        idToQuantity.put(currConsumable._id, (currQuantity + 1));
                        //plus subtotal by price of item
                        idToQuantity.put("subtotal", idToQuantity.get("subtotal") + currConsumable.price);
                        subtotalText.setText("Rp. " + idToQuantity.get("subtotal").toString() + " 000");
                        notifyDataSetChanged();
                    }
                }
            });

            final EditText note = (EditText) convertView.findViewById(R.id.item_note);
            note.setText(idToNote.get(currConsumable._id));

            note.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        idToNote.put(currConsumable._id, note.getText().toString());
                        Log.d(RestaurantExpListAdapter.class.getCanonicalName(), "Adding note " + note.getText().toString() + " for " + currConsumable.nameEn);
                    }
                }
            });
        /**
         * Below code is for staff members disabling/enabling menu items
         */
        } else if(type.equals(TYPE_EDIT)) {
            final RadioButton activeRadio = (RadioButton) convertView.findViewById(R.id.item_active);
            final RadioButton inactiveRadio = (RadioButton) convertView.findViewById(R.id.item_inactive);
            RadioGroup restaurantRadioGroup = (RadioGroup) convertView.findViewById(R.id.item_active_radiogroup);

            //default is set to active so set radiogroup to inactive if menu item is inactive
            if (currConsumable.active == false) {
                inactiveRadio.setChecked(true);
                activeRadio.setChecked(false);
            }

            restaurantRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(final RadioGroup radioGroup, final int checkedId) {
                    ((RestaurantActivity)context).disableUserInput();
                    //decide new state
                    final Boolean newState;
                    if (currConsumable.active) {
                        newState = false;
                    } else {
                        newState = true;
                    }
                    //update consumable's state on the server...
                    final Consumable updatedConsumable = new Consumable(currConsumable._id, currConsumable._rev,
                            currConsumable.nameEn, currConsumable.nameIn, currConsumable.nameRu,
                            currConsumable.nameZh, currConsumable.descriptionEn, currConsumable.descriptionIn, currConsumable.descriptionZh,
                            currConsumable.descriptionRu, currConsumable.sectionEn, currConsumable.sectionIn, currConsumable.sectionRu,
                            currConsumable.sectionZh, currConsumable.subsectionEn, currConsumable.subsectionIn, currConsumable.subsectionRu,
                            currConsumable.subsectionZh, currConsumable.order, currConsumable.price, currConsumable.attachmentName, newState);
                    MenuServer.getInstance(context).updateMenu(updatedConsumable).subscribe(new Observer<Boolean>() {
                        @Override
                        public void onCompleted() {
                            Log.d(RestaurantExpListAdapter.class.getCanonicalName(), "updateConsumable() On completed");
//                            idToConsumable.put(currConsumable._id, updatedConsumable);
                            activeRadio.setEnabled(false);
                            inactiveRadio.setEnabled(false);
                            ((RestaurantActivity)context).enableUserInput();
                            Toast.makeText(
                                    context,
                                    R.string.edit_menu_result,
                                    Toast.LENGTH_SHORT
                            ).show();
//                            notifyDataSetChanged();
                        }
                        @Override
                        public void onError(Throwable e) {
                            Log.d(RestaurantExpListAdapter.class.getCanonicalName(), "updateConsumable() On error");
                        }
                        @Override
                        public void onNext(Boolean aBoolean) {
                            Log.d(RestaurantExpListAdapter.class.getCanonicalName(), "updateConsumable() result " + aBoolean);
                        }
                    });
                }
            });
        }

        return convertView;
    }

   @Override
    public int getChildrenCount(int groupPosition) {
        return this.subsectionToIds.get(subsections.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.subsections.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.subsections.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.restaurant_menu_subsection, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.restaurant_subsection);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}