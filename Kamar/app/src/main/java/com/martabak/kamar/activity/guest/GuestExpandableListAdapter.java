package com.martabak.kamar.activity.guest;

import android.annotation.SuppressLint;
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
import com.martabak.kamar.domain.permintaan.Bellboy;
import com.martabak.kamar.domain.permintaan.Engineering;
import com.martabak.kamar.domain.permintaan.Housekeeping;
import com.martabak.kamar.domain.permintaan.LaundryOrder;
import com.martabak.kamar.domain.permintaan.Massage;
import com.martabak.kamar.domain.permintaan.OrderItem;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.domain.permintaan.RestaurantOrder;
import com.martabak.kamar.domain.permintaan.Transport;
import com.martabak.kamar.service.PermintaanServer;

import org.w3c.dom.Text;

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

        LayoutInflater inflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.guest_permintaan_item, null);
        // Set up the "i"/info button
        //ImageView permintaanInfoButton = (ImageView) convertView.findViewById(R.id.permintaan_info_button);
        ImageView permintaanCancelButton = (ImageView) convertView.findViewById(R.id.permintaan_cancel_button);
        ImageView orderItemImg = (ImageView)convertView.findViewById(R.id.order_item_image);
        Permintaan currPermintaan = getChild(groupPosition, childPosition);
        TextView orderSent = (TextView) convertView.findViewById(R.id.permintaan_order_sent_text);
        TextView orderUpdated = (TextView) convertView.findViewById(R.id.permintaan_order_updated_text);

        String datePattern = "EEE MMM dd hh:mm a";
        SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);

        String createdString = "Order lodged at: "  + dateFormat.format(currPermintaan.created);
        orderSent.setText(createdString);


        String updatedString;
        long lastStateChange;

        if (currPermintaan.updated != null) {
            updatedString = dateFormat.format(currPermintaan.updated);
            lastStateChange = (new Date().getTime() - currPermintaan.updated.getTime())/1000;
        } else {
            updatedString = "never";
            lastStateChange = 0;
        }
        updatedString = "Time since last Status change: "+lastStateChange/60+" minutes ago";
        orderUpdated.setText(updatedString);


        if (currPermintaan.content.getType().equals(Permintaan.TYPE_RESTAURANT)) {
            orderItemImg.setImageResource(R.drawable.ic_restaurant);
            /*RestaurantOrder restoOrder = (RestaurantOrder) currPermintaan.content;
            for (OrderItem o : restoOrder.items) {
                contentString += o.quantity + " " + o.name + " Rp. " + o.price+"\n";
                if (o.note != "") {
                    contentString += "Note: " + o.note + "\n";
                }
            }
            contentString += "\n\nTotal: Rp. " + restoOrder.totalPrice;*/
        } else if (currPermintaan.content.getType().equals(Permintaan.TYPE_TRANSPORT)) {
            orderItemImg.setImageResource(R.drawable.ic_transport);
            /*Transport transportOrder = (Transport) currPermintaan.content;
            contentString += "\nDeparting in: " + transportOrder.departingIn +
                    "\nDestination: " + transportOrder.destination +
                    "\nNumber of passengers: " + transportOrder.passengers;*/
        } else if (currPermintaan.content.getType().equals(Permintaan.TYPE_HOUSEKEEPING)) {
            orderItemImg.setImageResource(R.drawable.ic_housekeeping);
            /*Housekeeping hkOrder = (Housekeeping) currPermintaan.content;
            contentString += "\nOrder: " + hkOrder.option.nameEn +
                    "\nQuantity: " + hkOrder.quantity;*/
        } else if (currPermintaan.content.getType().equals(Permintaan.TYPE_ENGINEERING)) {
            orderItemImg.setImageResource(R.drawable.ic_engineering);
            /*Engineering engOrder = (Engineering) currPermintaan.content;
            contentString += "\nOrder: "+engOrder.option.nameEn;*/
        } else if (currPermintaan.content.getType().equals(Permintaan.TYPE_MASSAGE)) {
            orderItemImg.setImageResource(R.drawable.ic_massage);
            /*Massage massageOrder = (Massage)currPermintaan.content;
            contentString += "\nOrder: "+massageOrder.option.nameEn;*/
        } else if (currPermintaan.content.getType().equals(Permintaan.TYPE_LAUNDRY)) {
            orderItemImg.setImageResource(R.drawable.ic_laundry);
        } else if (currPermintaan.content.getType().equals(Permintaan.TYPE_BELLBOY)) {
            orderItemImg.setImageResource(R.drawable.ic_bellboy);
        }

        /*

        permintaanInfoButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                Permintaan currPermintaan = getChild(groupPosition, childPosition);

                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View view = layoutInflater.inflate(R.layout.dialog_request_info, null);
                TextView requestInfoDetails = (TextView)view.findViewById(R.id.request_content);
                ImageView orderItemImg = (ImageView)view.findViewById(R.id.order_item_image);
                //ImageView img = (ImageView)view.findViewById(R.id.request_content_image);
                final AlertDialog dialog= new AlertDialog.Builder(context).create();

                //build the AlertDialog's content
                String datePattern = "EEE MMM dd hh:mm a";
                SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);

                long lastStateChange;
                String updatedString;
                if (currPermintaan.updated != null) {
                    updatedString = dateFormat.format(currPermintaan.updated);
                    lastStateChange = (new Date().getTime() - currPermintaan.updated.getTime())/1000;
                } else {
                    updatedString = "never";
                    lastStateChange = 0;
                }
                String createdString = dateFormat.format(currPermintaan.created);

                String contentString = "\n";
                if (currPermintaan.content.getType().equals(Permintaan.TYPE_RESTAURANT)) {
                    orderItemImg.setImageResource(R.drawable.ic_restaurant);
                    RestaurantOrder restoOrder = (RestaurantOrder) currPermintaan.content;
                    for (OrderItem o : restoOrder.items) {
                        contentString += o.quantity + " " + o.name + " Rp. " + o.price+"\n";
                        if (o.note != "") {
                            contentString += "Note: " + o.note + "\n";
                        }
                    }
                    contentString += "\n\nTotal: Rp. " + restoOrder.totalPrice;
                } else if (currPermintaan.content.getType().equals(Permintaan.TYPE_TRANSPORT)) {
                    orderItemImg.setImageResource(R.drawable.ic_transport);
                    Transport transportOrder = (Transport) currPermintaan.content;
                    contentString += "\nDeparting in: " + transportOrder.departingIn +
                            "\nDestination: " + transportOrder.destination +
                            "\nNumber of passengers: " + transportOrder.passengers;
                } else if (currPermintaan.content.getType().equals(Permintaan.TYPE_HOUSEKEEPING)) {
                    orderItemImg.setImageResource(R.drawable.ic_housekeeping);
                    Housekeeping hkOrder = (Housekeeping) currPermintaan.content;
                    contentString += "\nOrder: " + hkOrder.option.nameEn +
                            "\nQuantity: " + hkOrder.quantity;
                } else if (currPermintaan.content.getType().equals(Permintaan.TYPE_ENGINEERING)) {
                    orderItemImg.setImageResource(R.drawable.ic_engineering);
                    Engineering engOrder = (Engineering) currPermintaan.content;
                    contentString += "\nOrder: "+engOrder.option.nameEn;
                } else if (currPermintaan.content.getType().equals(Permintaan.TYPE_MASSAGE)) {
                    orderItemImg.setImageResource(R.drawable.ic_massage);
                    Massage massageOrder = (Massage)currPermintaan.content;
                    contentString += "\nOrder: "+massageOrder.option.nameEn;
                } else if (currPermintaan.content.getType().equals(Permintaan.TYPE_LAUNDRY)) {
                    orderItemImg.setImageResource(R.drawable.ic_laundry);
                } else if (currPermintaan.content.getType().equals(Permintaan.TYPE_BELLBOY)) {
                    orderItemImg.setImageResource(R.drawable.ic_bellboy);
                }

                requestInfoDetails.setText("Status: "+currPermintaan.state+"\n"+
                        "Order lodged at: "+createdString+"\n"+
                        "Last status change at "+updatedString+"\n"+
                        "Time since last Status change: "+lastStateChange/60+" minutes ago" + "\n"+
                        contentString);
                dialog.setView(view);
                dialog.show();
            }
        });
        */
        permintaanCancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialog= new AlertDialog.Builder(context);
                dialog.setMessage(context.getString(R.string.permintaan_cancel_confirmation))
                        .setPositiveButton(R.string.agree, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Permintaan currPermintaan = getChild(groupPosition, childPosition);
                                if (currPermintaan.isCancelable()) {
                                    getLatestPermintaan(currPermintaan._id);
                                }
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                dialog.show();
            }


        });

        final String childText =  getChild(groupPosition, childPosition).type;
        TextView txtListChild = (TextView) convertView.findViewById(R.id.permintaan_list_item);
        txtListChild.setText(childText);
        return convertView;
    }

    private void getLatestPermintaan(final String _id) {
        Log.d(GuestExpandableListAdapter.class.getCanonicalName(), "Doing get permintaan of state");

        final Permintaan currPermintaan = idToPermintaan.get(_id);


        //disableButtons();
        PermintaanServer.getInstance(context).getPermintaan(currPermintaan._id).subscribe(new Observer<Permintaan>() {
            String rev;
            @Override
            public void onCompleted() {
                Log.d(GuestExpandableListAdapter.class.getCanonicalName(), "Completed getting _rev");
                Permintaan updatedPermintaan = new Permintaan(currPermintaan._id, rev, currPermintaan.owner, currPermintaan.creator, currPermintaan.type,
                        currPermintaan.roomNumber, currPermintaan.guestId, Permintaan.STATE_CANCELLED,
                        currPermintaan.created, new Date(), currPermintaan.assignee, currPermintaan.eta, currPermintaan.content);
                updatePermintaan(updatedPermintaan, currPermintaan.state);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Permintaan permintaan) {
                rev = permintaan._rev;
            }
        });
    }

    /**
     * Update Permintaan
     */
    private void updatePermintaan(final Permintaan permintaan, final String prevPermintaanState) {


        PermintaanServer.getInstance(context).updatePermintaan(permintaan)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {
                        Log.d(GuestExpandableListAdapter.class.getCanonicalName(), "updatePermintaan() On completed");

                        //update Permintaan dictionary
                        idToPermintaan.remove(permintaan._id);
                        idToPermintaan.put(permintaan._id, permintaan);
                        List<String> cancelPermintaans = stateToPermIds.get(permintaan.state);
                        cancelPermintaans.add(permintaan._id);
                        stateToPermIds.put(permintaan.state, cancelPermintaans);

                        List<String> otherPermintaans = stateToPermIds.get(prevPermintaanState);
                        otherPermintaans.remove(permintaan._id);
                        stateToPermIds.put(prevPermintaanState, otherPermintaans);
                        notifyDataSetChanged();

                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.d(GuestExpandableListAdapter.class.getCanonicalName(), "updatePermintaan() On error");
                        e.printStackTrace();
                    }
                    @Override
                    public void onNext(Boolean result) {
                        Log.d(GuestExpandableListAdapter.class.getCanonicalName(), "updatePermintaan() staff permintaan update " + result);
                    }
                });
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
        } else if (headerTitle.equals(Permintaan.STATE_CANCELLED)) {
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