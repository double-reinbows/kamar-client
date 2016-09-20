package com.martabak.kamar.activity.staff;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Html;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.Staff;
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

import rx.Observer;

class StaffExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> states; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> stateToPermIds;
    private HashMap<String, Permintaan> idToPermintaan;
    ImageView assignPermintaanButton;
    ImageView infoPermintaanButton;
    ImageView progressPermintaanButton;
    ImageView regressPermintaanButton;


    public StaffExpandableListAdapter(Context context, List<String> states,
                                 HashMap<String, List<String>> stateToPermIds, HashMap<String,
                                    Permintaan> idToPermintaan) {
        this.context = context;
        this.states = states;
        this.stateToPermIds = stateToPermIds;
        this.idToPermintaan = idToPermintaan;
    }

    @Override
    public Permintaan getChild(int groupPosition, int childPosition) {
//        Log.v("states", states.toString());
//        Log.v("stateToPermIds", stateToPermIds.toString());
        return idToPermintaan.get(stateToPermIds.get(states.get(groupPosition)).get(childPosition));
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Permintaan currPermintaan = getChild(groupPosition, childPosition);

        LayoutInflater infalInflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.staff_permintaan_row, null);


        //Set main text
        TextView txtListChild = (TextView) convertView.findViewById(R.id.permintaan_list_item);
        String simpleCreated = new SimpleDateFormat("hh:mm a").format(currPermintaan.created);
        final String childText = currPermintaan.type+" ~~~ Room No. "+currPermintaan.roomNumber+" ~~~ Ordered @ "+simpleCreated;
        txtListChild.setText(childText);

        //Set up grey filter
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);  //0 means grayscale
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);

        //Initialize the ImageViews (buttons)
        assignPermintaanButton =(ImageView) convertView.findViewById(R.id.assign_permintaan_button);
        infoPermintaanButton = (ImageView) convertView.findViewById(R.id.info_permintaan_button);
        progressPermintaanButton = (ImageView) convertView.findViewById(R.id.progress_permintaan_button);
        regressPermintaanButton = (ImageView) convertView.findViewById(R.id.regress_permintaan_button);

        /**
         * Assign staff member to a permintaan
         */
        if (currPermintaan.assignee.equals("none") && currPermintaan.state.equals(Permintaan.STATE_NEW)) {
            assignPermintaanButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(context.getApplicationContext().getString(R.string.permintaan_assign_person));

                    // Set up the input
                    final EditText textInput = new EditText(context);
                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    textInput.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(textInput);

                    // Set up the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String assignee = textInput.getText().toString();
                            getAndUpdatePermintaan(currPermintaan._id, 0, assignee);
                            ((ViewGroup) assignPermintaanButton.getParent()).removeView(assignPermintaanButton);

                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            });
        } else {
            ((ViewGroup) assignPermintaanButton.getParent()).removeView(assignPermintaanButton);
        }

        /**
         * Display info on a permintaan
         */
        infoPermintaanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    lastStateChange = (new Date().getTime() - currPermintaan.updated.getTime()) / 1000;
                } else {
                    simpleUpdated = "never";
                    lastStateChange = 0;
                }
                String contentString = "";
                String simpleCreated = new SimpleDateFormat("hh:mm a").format(currPermintaan.created);
                if (currPermintaan.content.getType().equals(Permintaan.TYPE_RESTAURANT)) {
                    RestaurantOrder restoOrder = (RestaurantOrder) currPermintaan.content;
                    for (OrderItem o : restoOrder.items) {
                        contentString += "<br>" + o.quantity + " " + o.name + " Rp. " + o.price;
                    }
                    contentString += " <br>Total: Rp. " + restoOrder.totalPrice;
                } else if (currPermintaan.content.getType().equals(Permintaan.TYPE_TRANSPORT)) {
                    Transport transportOrder = (Transport) currPermintaan.content;
                    contentString += "<br>Departing in: " + transportOrder.departingIn +
                            "<br>Destination: " + transportOrder.destination +
                            "<br>Number of passengers: " + transportOrder.passengers;
                } else if (currPermintaan.content.getType().equals(Permintaan.TYPE_HOUSEKEEPING)) {
                    Housekeeping hkOrder = (Housekeeping) currPermintaan.content;
                    contentString += "<br>Order: " + hkOrder.option.nameEn +
                            "<br>Quantity: " + hkOrder.quantity;
                } else if (currPermintaan.content.getType().equals(Permintaan.TYPE_LAUNDRY)) {
                    LaundryOrder laundryOrder = (LaundryOrder) currPermintaan.content;
                    for (LaundryOrderItem l : laundryOrder.items) {
                        String laundryString = "Tidak";
                        String pressingString = "Tidak<br>";
                        if (l.laundry) { laundryString = "Iya"; }
                        if (l.pressing) { pressingString = "Iya<br>"; }
                        contentString += "<br>"+l.quantity+" "+l.option.nameEn+": "+"<br>"+l.price+"<br>"
                        +"Cuci: "+laundryString+
                        "<br>Seterika: "+pressingString;
                    }
                    contentString += "<i>"+laundryOrder.message;
                } else if (currPermintaan.content.getType().equals(Permintaan.TYPE_ENGINEERING)) {
                    Engineering engOrder = (Engineering) currPermintaan.content;
                    contentString += "<br>Order: "+engOrder.option.nameEn;
                } else if (currPermintaan.content.getType().equals(Permintaan.TYPE_MASSAGE)) {
                    Massage massageOrder = (Massage)currPermintaan.content;
                    contentString += "<br>Order: "+massageOrder.option.nameEn;
                }

                builder
                        .setTitle("KAMAR NOMOR "+currPermintaan.roomNumber+ "- PESANAN "+currPermintaan.type)
                        .setMessage(Html.fromHtml("State: "+currPermintaan.state+"<br>"+
                                //"Message: "+currPermintaan.content.message+"<br>"+
                                "Pesan masuk jam: "+simpleCreated+"<br>"+
                                "Waktu terakhir kali pesan dirubah: "+simpleUpdated+"<br>"+
                                "Waktu sejak terakhir kali pesan diruabah: "+lastStateChange/60+" minutes ago<br>"+
                                "Petugas: "+currPermintaan.assignee+
                                "<br>Rincian: <b><br>"+contentString+"</b>"))
                        .setCancelable(true)
                ;
                AlertDialog alertDialog = builder.create();
                //set font size
                alertDialog.show();
                TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
                textView.setTextSize(25);
                alertDialog.getWindow().setLayout(width, (height-100));
            }
        });

        /**
         * Move a permintaan to the next state
         */
        if (currPermintaan.isProgressable() && !currPermintaan.assignee.equals("none")) {
            progressPermintaanButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("progressPermintaan", String.valueOf(groupPosition)+" "+String.valueOf(childPosition));
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(context.getApplicationContext().getString(R.string.permintaan_progress_confirmation));
                    builder.setCancelable(false);
                    builder.setPositiveButton(context.getApplicationContext().getString(R.string.positive), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Check permintaan can be progressed
                            if (currPermintaan.isProgressable()) {
                                Log.v("id", currPermintaan._id);

                                getAndUpdatePermintaan(currPermintaan._id, 1, null);
                            } else {
                                //TODO: Tell the user they can't progress the permintaan
                            }
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
        } else {
            progressPermintaanButton.setColorFilter(cf);
            progressPermintaanButton.setAlpha(0.5f);   // 128 = 0.5
            progressPermintaanButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Tolong periksa anda sudah menugaskan seseorang",
                            Toast.LENGTH_LONG).show();
                        }
                    });

        }

        /**
         * Move a permintaan to a previous state
         */
        if (currPermintaan.isRegressable()) {
            regressPermintaanButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("regressPermintaan", String.valueOf(groupPosition)+" "+String.valueOf(childPosition));
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(context.getApplicationContext().getString(R.string.permintaan_regress_confirmation));
                    builder.setCancelable(false);
                    builder.setPositiveButton(context.getApplicationContext().getString(R.string.positive), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Check permintaan can be regressed
                            if (currPermintaan.isRegressable()) {
                                getAndUpdatePermintaan(currPermintaan._id, -1, null);
                            } else {
                                //TODO tell user they cannot regress
                            }
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
        } else {
            regressPermintaanButton.setColorFilter(cf);
            regressPermintaanButton.setAlpha(0.5f);
        }

        return convertView;
    }

    /**
     * Gets the permintaan from the server to obtain the latest _rev and then
     * updates the permintaan on the server before updating the view.
     * @param _id id of permintaan to be updated
     * @param increment 1 to progress, -1 to regress
     * @param assignee name of staff member to be assigned, null if progressing/regressing
     */
    private void getAndUpdatePermintaan(final String _id, final int increment, String assignee) {
        Log.d(StaffExpandableListAdapter.class.getCanonicalName(), "Doing get permintaan of state");

        final Permintaan currPermintaan = idToPermintaan.get(_id);

        final String updatedAssignee = setAssignee(assignee, currPermintaan);
        final String targetState = setTargetState(currPermintaan.state, increment);

        PermintaanServer.getInstance(context).getPermintaansOfState(currPermintaan.state)
                                                .subscribe(new Observer<Permintaan>() {
            String rev;
            @Override
            public void onCompleted() {
                Log.d(StaffExpandableListAdapter.class.getCanonicalName(), "Completed getting _rev");
                Permintaan updatedPermintaan = new Permintaan(currPermintaan._id, rev, currPermintaan.owner, currPermintaan.type,
                        currPermintaan.roomNumber, currPermintaan.guestId, targetState,
                        currPermintaan.created, new Date(), updatedAssignee, currPermintaan.content);
                updatePermintaanAndView(updatedPermintaan, currPermintaan.state);
            }
            @Override
            public void onError(Throwable e) {
                Log.d(StaffExpandableListAdapter.class.getCanonicalName(), "getPermintaansOfState() On error");
                }
            @Override
            public void onNext(Permintaan result) {
                Log.d(StaffExpandableListAdapter.class.getCanonicalName(), "getPermintaansOfState() On next" + result._id +" "+ _id);
                if (result._id.equals(_id)) { //found the right permintaan
                    rev = result._rev;
                }
            }
        });
    }

    /**
     *
     */
    private void updatePermintaanAndView(final Permintaan permintaan, final String prevState) {
        Log.d(StaffExpandableListAdapter.class.getCanonicalName(), "completed getpermintaan, now updating");

        PermintaanServer.getInstance(context).updatePermintaan(permintaan)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {
                        Log.d(StaffExpandableListAdapter.class.getCanonicalName(), "updatePermintaan() On completed");
                        //Add child to the next state
                        stateToPermIds.get(permintaan.state).add(permintaan._id);
                        //Remove the child from the current state
                        stateToPermIds.get(prevState).remove(permintaan._id);
                        //update Permintaan dictionary
                        idToPermintaan.put(permintaan._id, permintaan);
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
    /**
     * Decides the next or previous state depending on the increment
     * @param currState current state
     * @param increment 1 to progress, -1 to regress
     * @return next or previous state
     */
    private String setTargetState(String currState, int increment) {

        String state = new String();

        if (increment == 0) {
            return currState;
        }

        if (currState.equals(Permintaan.STATE_NEW)) {
            return Permintaan.STATE_INPROGRESS;
        } else if (currState.equals(Permintaan.STATE_INPROGRESS)) {
            if (increment == 1) {
                return Permintaan.STATE_COMPLETED;
            } else {
                return Permintaan.STATE_NEW;
            }
        } else if (currState.equals((Permintaan.STATE_COMPLETED))) {
            return Permintaan.STATE_INPROGRESS;
        }

        return state;
    }
    /**
     * Decides the next or previous state depending on the increment
     * @param assignee current state
     * @return new or previous assignee
     */
    private String setAssignee(String assignee, Permintaan currPermintaan) {
        if (assignee == null) {
            return currPermintaan.assignee;
        } else {
            return assignee;
        }
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
        String headerTitle = (String) getGroup(groupPosition);
        LayoutInflater infalInflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.staff_permintaan_state, null);

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