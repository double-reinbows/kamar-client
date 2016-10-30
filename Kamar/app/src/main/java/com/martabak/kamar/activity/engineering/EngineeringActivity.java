package com.martabak.kamar.activity.engineering;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.managers.PermintaanManager;
import com.martabak.kamar.domain.options.EngineeringOption;
import com.martabak.kamar.domain.permintaan.Engineering;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.service.PermintaanServer;
import com.martabak.kamar.service.Server;
import com.martabak.kamar.service.StaffServer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import rx.Observer;

/**
 * This activity generates the list of ic_engineering options and allows the guest to request one.
 */
public class EngineeringActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private List<EngineeringOption> engOptions;
    private Map<String, String> statuses; // Maps engineering option ID -> request status
    private EngineeringRecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engineering);
        // BEGIN GENERIC LAYOUT STUFF
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });
        TextView roomNumberTextView = (TextView)findViewById(R.id.toolbar_roomnumber);
        String roomNumber = getSharedPreferences("userSettings", MODE_PRIVATE)
                .getString("roomNumber", "none");
        roomNumberTextView.setText(getString(R.string.room_number) + ": " + roomNumber);
        // END GENERIC LAYOUT STUFF
        // Get the options
        recyclerView = (RecyclerView)findViewById(R.id.engineering_list);
        engOptions = new ArrayList<>();
        StaffServer.getInstance(this).getEngineeringOptions().subscribe(new Observer<List<EngineeringOption>>() {
            @Override public void onCompleted() {
                Log.d(EngineeringActivity.class.getCanonicalName(), "onCompleted");
                Log.v(EngineeringActivity.class.getCanonicalName(), engOptions.toString());
                getStatuses();
            }
            @Override public void onError(Throwable e) {
                Log.d(EngineeringActivity.class.getCanonicalName(), "onError", e);
                e.printStackTrace();
            }
            @Override public void onNext(final List<EngineeringOption> options) {
                Log.d(EngineeringActivity.class.getCanonicalName(), options.size() + " ic_engineering options found");
                engOptions.addAll(options);
            }
        });
    }

    // Get the statuses
    public void getStatuses() {
        PermintaanManager.getInstance().getEngineeringStatuses(getBaseContext()).subscribe(new Observer<Map<String, String>>() {
            @Override public void onCompleted() {
                Log.d(EngineeringActivity.class.getCanonicalName(), "getEngineeringStatuses#onCompleted");
                recyclerViewAdapter = new EngineeringRecyclerViewAdapter(engOptions);
                recyclerView.setAdapter(recyclerViewAdapter);
            }
            @Override public void onError(Throwable e) {
                Log.d(EngineeringActivity.class.getCanonicalName(), "getEngineeringStatuses#onError", e);
                e.printStackTrace();
            }
            @Override public void onNext(Map<String, String> statuses) {
                Log.d(EngineeringActivity.class.getCanonicalName(), "Fetching statuses of size " + statuses.size());
                EngineeringActivity.this.statuses = statuses;
            }
        });
    }

    /**
     * Handle a click on a single ic_engineering option.
     * Bring up a confirmation dialog.
     * @param view The view that was clicked on.
     */
    @Override
    public void onClick(final View view) {
        int itemPosition = recyclerView.getChildLayoutPosition(view);
        final EngineeringOption item = engOptions.get(itemPosition);
        Log.d(EngineeringActivity.class.getCanonicalName(), "Selected " + item.getName() + " with ID " + item._id);
        Log.d(EngineeringActivity.class.getCanonicalName(), "Statuses map is " + Arrays.toString(statuses.entrySet().toArray()));
        if (statuses.containsKey(item._id)) {
            switch (statuses.get(item._id)) {
                case Permintaan.STATE_COMPLETED:
                case Permintaan.STATE_INPROGRESS:
                case Permintaan.STATE_NEW:
                    Toast.makeText(
                            EngineeringActivity.this.getApplicationContext(),
                            R.string.existing_request,
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
            }
        }
        new AlertDialog.Builder(this)
                .setTitle(item.getName())
                .setMessage(R.string.engineering_message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String owner = Permintaan.OWNER_FRONTDESK;
                        String type = Permintaan.TYPE_ENGINEERING;
                        String roomNumber = EngineeringActivity.this.getSharedPreferences("userSettings", EngineeringActivity.this.MODE_PRIVATE)
                                .getString("roomNumber", "none");
                        String guestId = EngineeringActivity.this.getSharedPreferences("userSettings", EngineeringActivity.this.MODE_PRIVATE)
                                .getString("guestId", "none");
                        if (guestId.equals("none")) {
                            Toast.makeText(
                                    EngineeringActivity.this.getApplicationContext(),
                                    R.string.no_guest_in_room,
                                    Toast.LENGTH_SHORT
                            ).show();
                            return;
                        }
                        String state = Permintaan.STATE_NEW;
                        Date currentDate = Calendar.getInstance().getTime();
                        PermintaanServer.getInstance(EngineeringActivity.this).createPermintaan(
                                new Permintaan(owner, type, roomNumber, guestId, state, currentDate,
                                        new Engineering("", item))
                        ).subscribe(new Observer<Permintaan>() {
                            boolean success;
                            @Override public void onCompleted() {
                                Log.d(EngineeringActivity.class.getCanonicalName(), "On completed");
                                if (success) {
                                    Toast.makeText(
                                            EngineeringActivity.this.getApplicationContext(),
                                            R.string.engineering_result,
                                            Toast.LENGTH_SHORT
                                    ).show();
                                    EngineeringActivity.this.statuses.put(item._id, Permintaan.STATE_NEW);
                                    View sentImageView = view.findViewById(R.id.sent_image);
                                    sentImageView.setBackground(getResources().getDrawable(R.drawable.circle_green));
                                } else {
                                    Toast.makeText(
                                            EngineeringActivity.this.getApplicationContext(),
                                            R.string.something_went_wrong,
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            }
                            @Override public void onError(Throwable e) {
                                Log.d(EngineeringActivity.class.getCanonicalName(), "On error");
                                e.printStackTrace();
                                success = false;
                            }
                            @Override public void onNext(Permintaan permintaan) {
                                Log.d(EngineeringActivity.class.getCanonicalName(), "On next");
                                if (permintaan != null) {
                                    success = true;
                                } else {
                                    success = false;
                                }
                            }
                        });
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    public class EngineeringRecyclerViewAdapter
            extends RecyclerView.Adapter<EngineeringRecyclerViewAdapter.ViewHolder> {

        protected int selectedPos = -1;

        private final List<EngineeringOption> mValues;

        public EngineeringRecyclerViewAdapter(List<EngineeringOption> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.engineering_list_row, parent, false);
            view.setOnClickListener(EngineeringActivity.this);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.itemView.setSelected(selectedPos == position);
            holder.item = mValues.get(position);

            Log.d(EngineeringActivity.class.getCanonicalName(), "Loading image " + holder.item.getImageUrl() + " into " + holder.imageView);
            Server.picasso(EngineeringActivity.this)
                    .load(holder.item.getImageUrl())
                    .placeholder(R.drawable.loading_batik)
                    .error(R.drawable.error)
                    .into(holder.imageView);
            holder.nameView.setText(holder.item.getName());
            String state = statuses.containsKey(holder.item._id) ? statuses.get(holder.item._id) : Permintaan.STATE_CANCELLED;
            Log.d(EngineeringActivity.class.getCanonicalName(), "Status for ic_engineering " + holder.item.getName() + " is " + state);
            switch (state) {
                case Permintaan.STATE_COMPLETED:
                    holder.completedImageView.setBackground(getResources().getDrawable(R.drawable.circle_green));
                case Permintaan.STATE_INPROGRESS:
                    holder.processedImageView.setBackground(getResources().getDrawable(R.drawable.circle_green));
                case Permintaan.STATE_NEW:
                    holder.sentImageView.setBackground(getResources().getDrawable(R.drawable.circle_green));
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public EngineeringOption item;
            public final View rootView;
            public final ImageView imageView;
            public final TextView nameView;
            public final View sentImageView;
            public final View processedImageView;
            public final View completedImageView;

            public ViewHolder(View view) {
                super(view);
                rootView = view;
                imageView = (ImageView) view.findViewById(R.id.engineering_image);
                nameView = (TextView) view.findViewById(R.id.engineering_name);
                sentImageView = view.findViewById(R.id.sent_image);
                processedImageView = view.findViewById(R.id.processed_image);
                completedImageView = view.findViewById(R.id.completed_image);
            }

            @Override
            public String toString() {
                return super.toString() + " " + nameView.getText();
            }
        }
    }

}