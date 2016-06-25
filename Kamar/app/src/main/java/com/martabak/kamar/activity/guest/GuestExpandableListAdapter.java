package com.martabak.kamar.activity.guest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private HashMap<String, Permintaan> _listDataChildString;

    public GuestExpandableListAdapter(Context context, List<String> listDataHeader,
                                      HashMap<String, List<String>> listDataChild, HashMap<String,
                                    Permintaan> listDataChildString) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listDataChild;
        this._listDataChildString = listDataChildString;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.guest_permintaan_item, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.permintaan_list_item);

        ImageView permintaanInfoButton = (ImageView) convertView.findViewById(R.id.permintaan_info_button);

        permintaanInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the selected permintaan into currPermintaan
                Permintaan currPermintaan;
                List<String> currPermintaans = _listDataChild.get(_listDataHeader.get(groupPosition));
                currPermintaan = _listDataChildString.get(currPermintaans.get(childPosition));

                DisplayMetrics displayMetrics = new DisplayMetrics();
                WindowManager manager = (WindowManager) _context.getSystemService(Activity.WINDOW_SERVICE);
                manager.getDefaultDisplay().getMetrics(displayMetrics);
                int width, height;
                //WindowManager.LayoutParams;
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
                    Log.d("herp", "derp");
                    //width = manager.getDefaultDisplay().getMetrics(displayMetrics);
                    //height = manager.getDefaultDisplay().getHeight();
                    width = displayMetrics.widthPixels;
                    height = displayMetrics.heightPixels;
                } else {
                    Point point = new Point();
                    manager.getDefaultDisplay().getSize(point);
                    width = point.x;
                    height = point.y;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                //build the AlertDialog's content
                String simpleUpdated = new SimpleDateFormat("hh:mm a").format(currPermintaan.updated);
                String simpleCreated = new SimpleDateFormat("hh:mm a").format(currPermintaan.updated);

                long lastStateChange = (new Date().getTime() - currPermintaan.updated.getTime())/1000;

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

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
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
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.guest_permintaan_state, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.list_state);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
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