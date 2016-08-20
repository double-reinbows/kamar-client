package com.martabak.kamar.activity.guest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.service.PermintaanServer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import rx.Observer;

class HousekeepingListAdapter extends BaseAdapter {

    private Context context;
    private List<String> states; // list of states
    // dictionary of lists of IDs, with the state as keys
    private HashMap<String, List<String>> stateToPermIds;
    // dictionary of permintaans with their IDs as keys
    private HashMap<String, Permintaan> idToPermintaan;
    private HashMap<String, Integer> hkDict;
    private Integer limit;
    private LayoutInflater inflater = null;

    public HousekeepingListAdapter(Context context, HashMap<String, Integer> hkDict,
                                   int size) {
        this.context = context;
        this.hkDict = hkDict;
        this.limit = size;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    /**
     * Returns a permintaan from the expandable list.

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

//        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.guest_permintaan_item, null);
//        }

        //Set up the "x" or permintaan cancel button
        ImageView cancelPermintaanButton = (ImageView) convertView.findViewById(R.id.permintaan_cancel_button);

        //if chosen child is NEW, then provide a cancel button
        if (getChild(groupPosition, childPosition).state.equals("NEW")) {

            cancelPermintaanButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Permintaan currPermintaan = getChild(groupPosition, childPosition);
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(context.getApplicationContext().getString(R.string.permintaan_cancel_confirmation));
                    builder.setCancelable(false);
                    builder.setPositiveButton(context.getApplicationContext().getString(R.string.positive), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        PermintaanServer.getInstance(context).getPermintaansOfState(currPermintaan.state)
                                .subscribe(new Observer<Permintaan>() {
                                    Permintaan tempPermintaan = new Permintaan();

                                    @Override
                                    public void onCompleted() {
                                        Log.d(HousekeepingListAdapter.class.getCanonicalName(), "doGetAndUpdatePermintaan() On completed");
                                        final Permintaan updatedPermintaan = new Permintaan(tempPermintaan._id, tempPermintaan._rev, tempPermintaan.owner, tempPermintaan.type,
                                                tempPermintaan.roomNumber, tempPermintaan.guestId, "CANCELLED",
                                                tempPermintaan.created, new Date(), null, tempPermintaan.content);
                                        PermintaanServer.getInstance(context).updatePermintaan(updatedPermintaan)
                                                .subscribe(new Observer<Boolean>() {
                                                    @Override
                                                    public void onCompleted() {
                                                        Log.d(HousekeepingListAdapter.class.getCanonicalName(), "updatePermintaan() On completed");
                                                        //remove currPermintaan's ID from the list of states.
                                                        stateToPermIds.get(currPermintaan.state).remove(currPermintaan._id);
                                                        idToPermintaan.put(currPermintaan._id, updatedPermintaan);
                                                        notifyDataSetChanged();
                                                    }

                                                    @Override
                                                    public void onError(Throwable e) {
                                                        Log.d(HousekeepingListAdapter.class.getCanonicalName(), "updatePermintaan() On error");
                                                        e.printStackTrace();
                                                    }

                                                    @Override
                                                    public void onNext(Boolean result) {
                                                        Log.d(HousekeepingListAdapter.class.getCanonicalName(), "updatePermintaan() staff permintaan update " + result);
                                                    }
                                                });
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.d(HousekeepingListAdapter.class.getCanonicalName(), "getPermintaansOfState() On error");
                                    }

                                    @Override
                                    public void onNext(Permintaan result) {
                                        Log.d(HousekeepingListAdapter.class.getCanonicalName(), "getPermintaansOfState() On next" + result._id + " " + currPermintaan._id);
                                        if (result._id.equals(currPermintaan._id)) {
                                            tempPermintaan = result;
                                            Log.v("Id", tempPermintaan._id);
                                        }
                                    }
                                });
                            }

                    });

                    builder.setNegativeButton(context.getApplicationContext().getString(R.string.negative), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
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

                /*
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
                */
/*
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View view = layoutInflater.inflate(R.layout.dialog_request_info, null);
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
                String createdString = null;
                try {
                    parsed = dateFormat.parse(currPermintaan.created.toString());
                    createdString = parsed.toString();
                } catch (ParseException pe) {
                    Log.v("ERROR", "created date parse error");
                }

                TextView requestInfoDetails = (TextView)
                        view.findViewById(R.id.request_content);
                requestInfoDetails.setText("Status: "+currPermintaan.state+"\n"+
                        "Message: "+currPermintaan.content.message+"\n"+
                        "Order lodged at: "+createdString+"\n"+
                        "Last Status change at "+updatedString+"\n"+
                        "Time since latest Status change: "+lastStateChange/60+" minutes ago" + "\n");
                dialog.setView(view);
                dialog.show();
            }
        });

        final String childText =  getChild(groupPosition, childPosition).type;
        TextView txtListChild = (TextView) convertView.findViewById(R.id.permintaan_list_item);
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/century-gothic.ttf");
        txtListChild.setTypeface(customFont);
        txtListChild.setText(childText);
        return convertView;
    }
*/

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View view = convertView;
        view = inflater.inflate(R.layout.housekeeping_list_item, null);


        return view;
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
        return false;
    }
}