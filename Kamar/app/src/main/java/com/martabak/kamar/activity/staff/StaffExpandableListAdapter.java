package com.martabak.kamar.activity.staff;

import java.util.Date;
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

class StaffExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private HashMap<String, Permintaan> _listDataChildString;

    public StaffExpandableListAdapter(Context context, List<String> listDataHeader,
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
                builder.setMessage(_context.getApplicationContext().getString(R.string.permintaan_progress_confirmation));
                builder.setCancelable(false);
                builder.setPositiveButton(_context.getApplicationContext().getString(R.string.positive), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Check permintaan can be progressed
                        if (groupPosition + 1 < 4) {
                            Permintaan currPermintaan;
                            List<String> currPermintaans = _listDataChild.get(_listDataHeader.get(groupPosition));
                            currPermintaan = _listDataChildString.get(currPermintaans.get(childPosition));
                            Log.v("id", currPermintaan._id);

                            doGetAndUpdatePermintaan(currPermintaan._id, groupPosition, 1);

                            //Get the list of permintaans in the next state
                            List<String> nextPermintaans = _listDataChild.get(_listDataHeader.get(groupPosition + 1));

                            //TODO: Should the following block be done in onCompleted()?
                            //Add child to the next state
                            nextPermintaans.add(currPermintaans.get(childPosition));
                            //Remove the child from the current state
                            currPermintaans.remove(childPosition);
                        } else {
                            //TODO: Tell the user they can't progress the permintaan
                        }
                    }
                });

                builder.setNegativeButton(_context.getApplicationContext().getString(R.string.negative), new DialogInterface.OnClickListener() {
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
                builder.setMessage(_context.getApplicationContext().getString(R.string.permintaan_progress_confirmation));
                builder.setCancelable(false);
                builder.setPositiveButton(_context.getApplicationContext().getString(R.string.positive), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Check permintaan can be regressed
                        if (groupPosition - 1 > -1) {
                            Permintaan currPermintaan;
                            List<String> currPermintaans = _listDataChild.get(_listDataHeader.get(groupPosition));
                            currPermintaan = _listDataChildString.get(currPermintaans.get(childPosition));

                            doGetAndUpdatePermintaan(currPermintaan._id, groupPosition, -1);

                            //Get the list of permintaans in the previous state
                            List<String> prevPermintaans = _listDataChild.get(_listDataHeader.get(groupPosition - 1));

                            //TODO: Should the following block should be done in onCompleted()?
                            //Add child to the next state
                            prevPermintaans.add(currPermintaans.get(childPosition));
                            //Remove the child from the current state
                            currPermintaans.remove(childPosition);
                        } else {
                            //TODO tell user they cannot regress
                        }
                    }
                });

                builder.setNegativeButton(_context.getApplicationContext().getString(R.string.negative), new DialogInterface.OnClickListener() {
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

    private String setState(int groupPosition) {

        String state = new String();

        if (groupPosition == 0) {
            state = Permintaan.STATE_NEW;
        } else if (groupPosition == 1) {
            state = Permintaan.STATE_INPROGRESS;
        } else if (groupPosition == 2) {
            state = Permintaan.STATE_INDELIVERY;
        } else if (groupPosition == 3) {
            state = Permintaan.STATE_COMPLETED;
        }

        return state;
    }
    private void doGetAndUpdatePermintaan(final String _id, final int groupPosition, final int increment) {
        Log.d(StaffExpandableListAdapter.class.getCanonicalName(), "Doing get permintaan of state");

        String currState = setState(groupPosition);
        final String targetState = setState(groupPosition + increment);

        PermintaanServer.getInstance(_context).getPermintaansOfState(currState)
                                                .subscribe(new Observer<Permintaan>() {
            Permintaan tempPermintaan = new Permintaan();

            @Override
            public void onCompleted() {
                Log.d(StaffExpandableListAdapter.class.getCanonicalName(), "doGetAndUpdatePermintaan() On completed");
                Permintaan updatedPermintaan = new Permintaan(tempPermintaan._id, tempPermintaan._rev, tempPermintaan.owner, tempPermintaan.type,
                        tempPermintaan.roomNumber, tempPermintaan.guestId, targetState,
                        tempPermintaan.created, new Date(), tempPermintaan.content);
                PermintaanServer.getInstance(_context).updatePermintaan(updatedPermintaan)
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onCompleted() {
                            Log.d(StaffExpandableListAdapter.class.getCanonicalName(), "updatePermintaan() On completed");
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(StaffExpandableListAdapter.class.getCanonicalName(), "updatePermintaan() On error");
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(Boolean result) {
                            Log.d(StaffExpandableListAdapter.class.getCanonicalName(), "updatePermintaan() staff permintaan update " + result);
                        }
                    });
            }

            @Override
            public void onError(Throwable e) {
                Log.d(StaffExpandableListAdapter.class.getCanonicalName(), "getPermintaansOfState() On error");
                }

            @Override
            public void onNext(Permintaan result) {
                Log.d(StaffExpandableListAdapter.class.getCanonicalName(), "getPermintaansOfState() On next" + result._id +" "+ _id);
                if (result._id.equals(_id)) {
                    tempPermintaan = result;
                    Log.v("Id", tempPermintaan._id);
                }
            }
        });
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