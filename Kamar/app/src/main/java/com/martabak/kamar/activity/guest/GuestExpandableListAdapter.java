package com.martabak.kamar.activity.guest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.service.PermintaanServer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import rx.Observer;

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

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.guest_permintaan_item, null);
        }

        //Set up the "x" or permintaan cancel button
        ImageView cancelPermintaanButton = (ImageView) convertView.findViewById(R.id.permintaan_cancel_button);

        //if chosen child is NEW, then provide a cancel button
        if (getChild(groupPosition, childPosition).state.equals("NEW")) {

            cancelPermintaanButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Permintaan currPermintaan = getChild(groupPosition, childPosition);
                    PermintaanServer.getInstance(context).getPermintaansOfState(currPermintaan.state)
                            .subscribe(new Observer<Permintaan>() {
                                Permintaan tempPermintaan = new Permintaan();

                                @Override
                                public void onCompleted() {
                                    Log.d(GuestExpandableListAdapter.class.getCanonicalName(), "doGetAndUpdatePermintaan() On completed");
                                    Permintaan updatedPermintaan = new Permintaan(tempPermintaan._id, tempPermintaan._rev, tempPermintaan.owner, tempPermintaan.type,
                                            tempPermintaan.roomNumber, tempPermintaan.guestId, "CANCELLED",
                                            tempPermintaan.created, new Date(), tempPermintaan.content);
                                    PermintaanServer.getInstance(context).updatePermintaan(updatedPermintaan)
                                            .subscribe(new Observer<Boolean>() {
                                                @Override
                                                public void onCompleted() {
                                                    Log.d(GuestExpandableListAdapter.class.getCanonicalName(), "updatePermintaan() On completed");
//                                                notifyDataSetChanged();
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
                                public void onError(Throwable e) {
                                    Log.d(GuestExpandableListAdapter.class.getCanonicalName(), "getPermintaansOfState() On error");
                                }

                                @Override
                                public void onNext(Permintaan result) {
                                    Log.d(GuestExpandableListAdapter.class.getCanonicalName(), "getPermintaansOfState() On next" + result._id + " " + currPermintaan._id);
                                    if (result._id.equals(currPermintaan._id)) {
                                        tempPermintaan = result;
                                        Log.v("Id", tempPermintaan._id);
                                    }
                                }
                            });
                    //remove currPermintaan's ID from the list of states.
                    stateToPermIds.get(currPermintaan.state).remove(currPermintaan._id);
                    notifyDataSetChanged();
                }
            });
        } else { //remove the cancel ImageView from the View
            cancelPermintaanButton.setVisibility(View.GONE);
        }

        // Set up the "i" or info button
        ImageView permintaanInfoButton = (ImageView) convertView.findViewById(R.id.permintaan_info_button);

        permintaanInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permintaan currPermintaan = getChild(groupPosition, childPosition); //idToPermintaan.get(currPermintaans.get(childPosition));
                DisplayMetrics displayMetrics = new DisplayMetrics();
                WindowManager manager = (WindowManager) context.getSystemService(Activity.WINDOW_SERVICE);
                manager.getDefaultDisplay().getMetrics(displayMetrics);
                int width, height;
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
                    width = displayMetrics.widthPixels;
                    height = displayMetrics.heightPixels;
                } else {
                    Point point = new Point();
                    manager.getDefaultDisplay().getSize(point);
                    width = point.x;
                    height = point.y;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                //build the AlertDialog's content
                String simpleUpdated;
                long lastStateChange;
                if (currPermintaan.updated != null) {
                    simpleUpdated = new SimpleDateFormat("hh:mm a").format(currPermintaan.updated);
                    lastStateChange = (new Date().getTime() - currPermintaan.updated.getTime())/1000;
                } else {
                    simpleUpdated = "never";
                    lastStateChange = 0;
                }
                String simpleCreated = new SimpleDateFormat("hh:mm a").format(currPermintaan.created);


                builder
                        .setTitle(currPermintaan.type + " ORDER DETAILS")
                        .setMessage("Status: "+currPermintaan.state+"\n"+
                                    "Message: "+currPermintaan.content.message+"\n"+
                                    "Order lodged at: "+simpleCreated+"\n"+
                                    "Last Status change at "+simpleUpdated+"\n"+
                                    "Time since latest Status change: "+lastStateChange/60+" minutes ago")
                        .setCancelable(true)
                        ;
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                alertDialog.getWindow().setLayout(width, (height-100));
            }
        });

        final String childText =  getChild(groupPosition, childPosition).type;
        TextView txtListChild = (TextView) convertView.findViewById(R.id.permintaan_list_item);
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/century-gothic.ttf");
        txtListChild.setTypeface(customFont);
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

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.guest_permintaan_state, null);
        }

        String headerTitle = (String) getGroup(groupPosition);
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.list_state);
        lblListHeader.setTypeface(null, Typeface.BOLD); //bolded text
        lblListHeader.setText(headerTitle);
        // Set different bg colour for each state
        if (headerTitle.equals("NEW")) {
            lblListHeader.setBackgroundColor(0xFFac0d13);
        } else if (headerTitle.equals("IN PROGRESS")) {
            lblListHeader.setBackgroundColor(0xFFaa373a);
        } else if (headerTitle.equals("IN DELIVERY")) {
            lblListHeader.setBackgroundColor(0xFFa66163);
        } else if (headerTitle.equals("COMPLETED")) {
            lblListHeader.setBackgroundColor(0xFFa09091);
        }
        lblListHeader.invalidate();

        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/century-gothic.ttf");
        Typeface boldFont = Typeface.create(customFont, Typeface.BOLD);
        lblListHeader.setTypeface(boldFont);
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