package com.martabak.kamar.activity;

import java.util.HashMap;
import java.util.List;
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
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.service.PermintaanServer;

import rx.Observer;

class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private HashMap<String, Permintaan> _listDataChildString;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
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
            convertView = infalInflater.inflate(R.layout.staff_permintaan_item, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.permintaan_list_item);

        //TODO: Change button picture to a down arrow
        ImageView progressPermintaanButton = (ImageView) convertView.findViewById(R.id.progress_permintaan_button);

        progressPermintaanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                builder.setMessage("Are you sure you want to progress this permintaan?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<String> currPermintaans = _listDataChild.get(_listDataHeader.get(groupPosition));
                        //Log.d("id", _listDataChildString.get(currPermintaans.get(childPosition)).id);
                        Permintaan currPermintaan = _listDataChildString.get(currPermintaans.get(childPosition));
                        Log.d("curr permintaan id", currPermintaan.guestId);

                        //Log.d("selected child state:", _listDataHeader.get(groupPosition));

                        //Get the list of permintaans in the next state
                        List<String> nextPermintaans = _listDataChild.get(_listDataHeader.get(groupPosition + 1));
                        //Create new Permintaan with updated state
                        Permintaan updatedPermintaan = new Permintaan(currPermintaan.owner, currPermintaan.type,
                                            currPermintaan.roomNumber, currPermintaan.guestId, _listDataHeader.get(groupPosition+1),
                                            currPermintaan.updated, currPermintaan.created, currPermintaan.content);
                        //Add child to the next state
                        nextPermintaans.add(currPermintaans.get(childPosition)); //print permintaans mapped to current string
                        //Remove the child from the current state
                        currPermintaans.remove(childPosition);
                        //TODO: I'm not actually removing the child from _listDataChild hashmap? or am I?



                        //TODO: change "new Permintaan()" to a permintaan that the staff has changed
                        PermintaanServer.getInstance(_context).updatePermintaan(updatedPermintaan)
                                                                .subscribe(new Observer<Boolean>() {
                            @Override
                            public void onCompleted() {
                                Log.d(StaffPermintaanFragment.class.getCanonicalName(), "On completed");
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(StaffPermintaanFragment.class.getCanonicalName(), "On error");
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(Boolean result) {
                                Log.d(StaffPermintaanFragment.class.getCanonicalName(), "staff permintaan update " + result);
                            }
                        });
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        });


        ImageView regressPermintaanButton = (ImageView) convertView.findViewById(R.id.regress_permintaan_button);

        regressPermintaanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                builder.setMessage("Are you sure you want to regress this permintaan?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<String> currPermintaans = _listDataChild.get(_listDataHeader.get(groupPosition));
                        //Log.d("id", _listDataChildString.get(currPermintaans.get(childPosition)).id);
                        Permintaan currPermintaan = _listDataChildString.get(_listDataChild.get(childPosition));
                        Log.d("curr permintaan id", currPermintaan.guestId);

                        //Log.d("selected child state:", _listDataHeader.get(groupPosition));

                        //Get the list of permintaans in the next state
                        List<String> nextPermintaans = _listDataChild.get(_listDataHeader.get(groupPosition - 1));
                        //Create new Permintaan with updated state
                        Permintaan updatedPermintaan = new Permintaan(currPermintaan.owner, currPermintaan.type,
                                currPermintaan.roomNumber, currPermintaan.guestId, _listDataHeader.get(groupPosition-1),
                                currPermintaan.updated, currPermintaan.created, currPermintaan.content);
                        //Add child to the next state
                        nextPermintaans.add(currPermintaans.get(childPosition)); //print permintaans mapped to current string
                        //Remove the child from the current state
                        currPermintaans.remove(childPosition);
                        //TODO: I'm not actually removing the child from _listDataChild hashmap? or am I?



                        //TODO: change "new Permintaan()" to a permintaan that the staff has changed
                        PermintaanServer.getInstance(_context).updatePermintaan(updatedPermintaan)
                                .subscribe(new Observer<Boolean>() {
                                    @Override
                                    public void onCompleted() {
                                        Log.d(StaffPermintaanFragment.class.getCanonicalName(), "On completed");
                                        notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.d(StaffPermintaanFragment.class.getCanonicalName(), "On error");
                                        e.printStackTrace();
                                    }

                                    @Override
                                    public void onNext(Boolean result) {
                                        Log.d(StaffPermintaanFragment.class.getCanonicalName(), "staff permintaan update " + result);
                                    }
                                });
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
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
            convertView = infalInflater.inflate(R.layout.staff_permintaan_state, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.list_state);
        lblListHeader.setTypeface(null, Typeface.BOLD);
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