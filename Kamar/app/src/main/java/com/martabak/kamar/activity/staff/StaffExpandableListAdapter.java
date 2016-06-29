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
    private List<String> states; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> stateToPermintaanIds;
    private HashMap<String, Permintaan> idToPermintaan;

    public StaffExpandableListAdapter(Context context, List<String> states,
                                 HashMap<String, List<String>> stateToPermintaanIds, HashMap<String,
                                    Permintaan> idToPermintaan) {
        this._context = context;
        this.states = states;
        this.stateToPermintaanIds = stateToPermintaanIds;
        this.idToPermintaan = idToPermintaan;
    }

    @Override
    public Permintaan getChild(int groupPosition, int childPosition) {
        return idToPermintaan.get(stateToPermintaanIds.get(states.get(groupPosition)).get(childPosition));
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {


        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.staff_permintaan_item, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.permintaan_list_item);

        //Set up child text
        final String childText = (String) getChild(groupPosition, childPosition).type;


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
                            Permintaan currPermintaan = getChild(groupPosition, childPosition);
                            Log.v("id", currPermintaan._id);

                            doGetAndUpdatePermintaan(currPermintaan._id, groupPosition, 1);

                            //Get the list of permintaans in the next state
                            List<String> nextPermintaans = stateToPermintaanIds.get(states.get(groupPosition + 1));

                            //Add child to the next state
                            nextPermintaans.add(currPermintaan._id);
                            //Remove the child from the current state
                            stateToPermintaanIds.get(states.get(groupPosition)).remove(currPermintaan._id);
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
                builder.setMessage(_context.getApplicationContext().getString(R.string.permintaan_regress_confirmation));
                builder.setCancelable(false);
                builder.setPositiveButton(_context.getApplicationContext().getString(R.string.positive), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Check permintaan can be regressed
                        if (groupPosition - 1 > -1) {

                            Permintaan currPermintaan = getChild(groupPosition, childPosition);

                            doGetAndUpdatePermintaan(currPermintaan._id, groupPosition, -1);

                            //Get the list of permintaans in the current & previous state
                            List<String> currPermintaans = stateToPermintaanIds.get(states.get(groupPosition));
                            List<String> prevPermintaans = stateToPermintaanIds.get(states.get(groupPosition - 1));
                            //Add child to the previous state and remove from current state
                            prevPermintaans.add(currPermintaan._id);
                            currPermintaans.remove(currPermintaan._id);
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
        return this.stateToPermintaanIds.get(this.states.get(groupPosition))
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