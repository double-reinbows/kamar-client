package com.martabak.kamar.activity.guest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.permintaan.Engineering;
import com.martabak.kamar.domain.permintaan.Housekeeping;
import com.martabak.kamar.domain.permintaan.LaundryOrder;
import com.martabak.kamar.domain.permintaan.LaundryOrderItem;
import com.martabak.kamar.domain.permintaan.Massage;
import com.martabak.kamar.domain.permintaan.OrderItem;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.domain.permintaan.RestaurantOrder;
import com.martabak.kamar.domain.permintaan.Transport;
import com.martabak.kamar.service.PermintaanServer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import rx.Observer;

/**
 * Handles guest's requests list.
 */
class GuestExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> states; // list of states
    // dictionary of lists of IDs, with the state as keys
    private HashMap<String, List<String>> stateToPermIds;
    // dictionary of permintaans with their IDs as keys
    private HashMap<String, Permintaan> idToPermintaan;

    public GuestExpandableListAdapter(Context context, List<String> states,
                                      HashMap<String, List<String>> stateToPermIds, HashMap<String,
                                    Permintaan> idToPermintaan) {
        this.context = context;
        this.states = states;
        this.stateToPermIds = stateToPermIds;
        this.idToPermintaan = idToPermintaan;
    }

    /**
     * Returns a permintaan from the expandable list.
     */
    @Override
    public Permintaan getChild(int groupPosition, int childPosition) {
        return idToPermintaan.get(stateToPermIds.get(states.get(groupPosition)).get(childPosition));
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        LayoutInflater infalInflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.guest_permintaan_item, null);
        // Set up the "i"/info button
        ImageView permintaanInfoButton = (ImageView) convertView.findViewById(R.id.permintaan_info_button);


        permintaanInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permintaan currPermintaan = getChild(groupPosition, childPosition);

                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View view = layoutInflater.inflate(R.layout.dialog_request_info, null);
                TextView requestInfoDetails = (TextView)view.findViewById(R.id.request_content);
                ImageView img = (ImageView)view.findViewById(R.id.request_content_image);
                final AlertDialog dialog= new AlertDialog.Builder(context).create();

                //build the AlertDialog's content
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd hh:mm a");
                Date parsed;
                String updatedString = null;
                long lastStateChange;
                if (currPermintaan.updated != null) {
                    try {
                        parsed = dateFormat.parse(currPermintaan.updated.toString());
                        updatedString = parsed.toString();
                    } catch (ParseException pe) {
                        Log.v("ERROR", "updated date parse error");
                    }
                    lastStateChange = (new Date().getTime() - currPermintaan.updated.getTime())/1000;
                } else {
                    updatedString = "never";
                    lastStateChange = 0;
                }
//                String simpleCreated = new SimpleDateFormat("EEE MMM dd hh:mm a").format(currPermintaan.created);
                String createdString = currPermintaan.created.toString();
                try {
                    parsed = dateFormat.parse(currPermintaan.created.toString());
                    createdString = parsed.toString();
                } catch (ParseException pe) {
                    Log.v("ERROR", "created date parse error");
                }

                String contentString = "\n";
                if (currPermintaan.content.getType().equals(Permintaan.TYPE_RESTAURANT)) {
                    img.setImageResource(R.drawable.ic_restaurant);
                    RestaurantOrder restoOrder = (RestaurantOrder) currPermintaan.content;
                    for (OrderItem o : restoOrder.items) {
                        contentString += o.quantity + " " + o.name + " Rp. " + o.price+"\n";
                        if (o.note != "") {
                            contentString += "Note: " + o.note + "\n";
                        }
                    }
                    contentString += "\n\nTotal: Rp. " + restoOrder.totalPrice;
                } else if (currPermintaan.content.getType().equals(Permintaan.TYPE_TRANSPORT)) {
                    img.setImageResource(R.drawable.ic_transport);
                    Transport transportOrder = (Transport) currPermintaan.content;
                    contentString += "\nDeparting in: " + transportOrder.departingIn +
                            "\nDestination: " + transportOrder.destination +
                            "\nNumber of passengers: " + transportOrder.passengers;
                } else if (currPermintaan.content.getType().equals(Permintaan.TYPE_HOUSEKEEPING)) {
                    img.setImageResource(R.drawable.ic_housekeeping);
                    Housekeeping hkOrder = (Housekeeping) currPermintaan.content;
                    contentString += "\nOrder: " + hkOrder.option.nameEn +
                            "\nQuantity: " + hkOrder.quantity;
                } else if (currPermintaan.content.getType().equals(Permintaan.TYPE_LAUNDRY)) {
                    img.setImageResource(R.drawable.ic_laundry);
                    LaundryOrder laundryOrder = (LaundryOrder) currPermintaan.content;
                    for (LaundryOrderItem l : laundryOrder.items) {
                        String laundryString = "Tidak";
                        String pressingString = "Tidak\n";
                        if (l.laundry) { laundryString = "Iya"; }
                        if (l.pressing) { pressingString = "Iya\n"; }
                        contentString += l.quantity+" "+l.option.nameEn+": "+"\n"+l.price+"\n"
                                +"Cuci: "+laundryString+
                                "\nSeterika: "+pressingString;
                    }
                    contentString += "<i>"+laundryOrder.message;
                } else if (currPermintaan.content.getType().equals(Permintaan.TYPE_ENGINEERING)) {
                    img.setImageResource(R.drawable.ic_engineering);
                    Engineering engOrder = (Engineering) currPermintaan.content;
                    contentString += "\nOrder: "+engOrder.option.nameEn;
                } else if (currPermintaan.content.getType().equals(Permintaan.TYPE_MASSAGE)) {
                    img.setImageResource(R.drawable.ic_massage);
                    Massage massageOrder = (Massage)currPermintaan.content;
                    contentString += "\nOrder: "+massageOrder.option.nameEn;
                }



                requestInfoDetails.setText("Status: "+currPermintaan.state+"\n"+
                        "Message: "+currPermintaan.content.message+"\n"+
                        "Order lodged at: "+createdString+"\n"+
                        "Last Status change at "+updatedString+"\n"+
                        "Time since latest Status change: "+lastStateChange/60+" minutes ago" + "\n"+
                        contentString);
                dialog.setView(view);
                dialog.show();
            }
        });

        final String childText =  getChild(groupPosition, childPosition).type;
        TextView txtListChild = (TextView) convertView.findViewById(R.id.permintaan_list_item);
        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.stateToPermIds.get(this.states.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.states.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.states.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        LayoutInflater infalInflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.guest_permintaan_state, null);

        String headerTitle = (String) getGroup(groupPosition);
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.list_state);
        lblListHeader.setTypeface(null, Typeface.BOLD); //bolded text
        lblListHeader.setText(headerTitle);
        // Set different bg colour for each state
        if (headerTitle.equals(Permintaan.STATE_NEW)) {
            lblListHeader.setBackgroundColor(0xFFac0d13);
        } else if (headerTitle.equals(Permintaan.STATE_INPROGRESS)) {
            lblListHeader.setBackgroundColor(0xFFaa373a);
        } else if (headerTitle.equals(Permintaan.STATE_COMPLETED)) {
            lblListHeader.setBackgroundColor(0xFFa09091);
        }
        lblListHeader.invalidate();

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