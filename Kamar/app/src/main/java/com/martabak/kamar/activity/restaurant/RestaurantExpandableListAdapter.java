package com.martabak.kamar.activity.restaurant;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.Consumable;

import java.util.HashMap;
import java.util.List;

class RestaurantExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> subsectionHeaders; //header text
    //a list of each item's main text with header text as keys
    private HashMap<String, List<String>> itemTextDict;
    //consumable dictionary with consumable.name keys
    private HashMap<String, Consumable> itemObjectDict;
    //quantity dictionary with consumable.name keys
    private HashMap<String, Integer> itemQuantityDict;
    private TextView subtotalText;

    public RestaurantExpandableListAdapter(Context context, List<String> subsectionHeaders,
                                            HashMap<String, List<String>> listDataChild,
                                            HashMap<String, Consumable> listDataChildString,
                                            HashMap<String, Integer> quantityDict,
                                            TextView subtotalText) {
        this.context = context;
        this.subsectionHeaders = subsectionHeaders;
        this.itemTextDict = listDataChild;
        this.itemObjectDict = listDataChildString;
        this.itemQuantityDict = quantityDict;
        this.subtotalText = subtotalText;
    }

    @Override
    public Consumable getChild(int groupPosition, int childPosititon) {
        return itemObjectDict.get(this.itemTextDict.get(this.subsectionHeaders.get(groupPosition))
                .get(childPosititon));
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
            convertView = inflater.inflate(R.layout.restaurant_menu_item, null);
        }

        //Set up main text
        String childText = getChild(groupPosition, childPosition).name;
        TextView txtListChild = (TextView) convertView.findViewById(R.id.item_text);
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/century-gothic.ttf");
        txtListChild.setTypeface(customFont);
        txtListChild.setText(childText);

        //Set up info text
        //if (context.getSharedPreferences("userSettings", MODE_PRIVATE).getString("locale", null).equals("english"));
        String infoText = getChild(groupPosition, childPosition).description_en;
        TextView infoView = (TextView)convertView.findViewById(R.id.item_info);
        infoView.setText(infoText);

        //Set up quantity text
        List<String> currConsumables = itemTextDict.get(subsectionHeaders.get(groupPosition));
        Consumable currConsumable = itemObjectDict.get(currConsumables.get(childPosition));
        TextView quantity = (TextView) convertView.findViewById(R.id.item_quantity);
        quantity.setText(itemQuantityDict.get(currConsumable.name).toString());
        quantity.setBackgroundColor(0xFFac0d13);
        quantity.invalidate();
        quantity.setTextColor(Color.WHITE);
        //Set up price text
        String priceText = getChild(groupPosition, childPosition).price.toString();
        TextView priceView = (TextView)convertView.findViewById(R.id.item_price);
        priceView.setText("Rp. "+priceText+" 000");

        //Set up item image
        ImageView itemImg = (ImageView) convertView.findViewById(R.id.item_img);


        /**
         * Implement the minus button
         */
        ImageView minusQuantityButton = (ImageView) convertView.findViewById(R.id.minus_button);
        minusQuantityButton.setBackgroundColor(0xFFac0d13);
        minusQuantityButton.invalidate();
        minusQuantityButton.setColorFilter(Color.argb(255, 255, 255, 255));

        minusQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                List<String> currConsumables = itemTextDict.get(subsectionHeaders.get(groupPosition));
                Consumable currConsumable = itemObjectDict.get(currConsumables.get(childPosition));
                Integer currQuantity = itemQuantityDict.get(currConsumable.name);
                if (currQuantity - 1 > -1) { //Stop user from setting to -1 quantity
                    //Minus 1 off the quantity
                    itemQuantityDict.put(currConsumable.name, (currQuantity - 1));
                    //minus subtotal by price of item
                    itemQuantityDict.put("subtotal", itemQuantityDict.get("subtotal")-currConsumable.price);
                    subtotalText.setText("Rp. "+itemQuantityDict.get("subtotal").toString()+" 000");
                    notifyDataSetChanged();
                }
            }
        });

        /**
         * Implement the plus button
         */
        ImageView plusQuantityButton = (ImageView) convertView.findViewById(R.id.plus_button);
        plusQuantityButton.setBackgroundColor(0xFFac0d13);
        plusQuantityButton.invalidate();
        plusQuantityButton.setColorFilter(Color.argb(255, 255, 255, 255));

        plusQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                List<String> currConsumables = itemTextDict.get(subsectionHeaders.get(groupPosition));
                Consumable currConsumable = itemObjectDict.get(currConsumables.get(childPosition));
                Integer currQuantity = itemQuantityDict.get(currConsumable.name);
                //Add 1 to the quantity
                itemQuantityDict.put(currConsumable.name, (currQuantity + 1));
                //plus subtotal by price of item
                itemQuantityDict.put("subtotal", itemQuantityDict.get("subtotal") + currConsumable.price);
                subtotalText.setText("Rp. "+itemQuantityDict.get("subtotal").toString()+" 000");
                notifyDataSetChanged();
            }
        });

        //txtListChild.setText(childText);
        return convertView;
    }

   @Override
    public int getChildrenCount(int groupPosition) {
        return this.itemTextDict.get(subsectionHeaders.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.subsectionHeaders.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.subsectionHeaders.size();
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
        //lblListHeader.setTypeface(null, Typeface.BOLD);
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/century-gothic.ttf");
        Typeface boldFont = Typeface.create(customFont, Typeface.BOLD);
        lblListHeader.setTypeface(boldFont);
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