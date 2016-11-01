package com.martabak.kamar.activity.massage;

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
import com.martabak.kamar.activity.guest.AbstractGuestBarsActivity;
import com.martabak.kamar.domain.managers.PermintaanManager;
import com.martabak.kamar.domain.options.MassageOption;
import com.martabak.kamar.domain.permintaan.Massage;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.service.PermintaanServer;
import com.martabak.kamar.service.Server;
import com.martabak.kamar.service.StaffServer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.Observer;

/**
 * This activity generates the list of massage options and allows the guest to request one.
 */
public class MassageActivity extends AbstractGuestBarsActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private View sentImageView;
    private View processedImageView;
    private View completedImageView;
    private List<MassageOption> massageOptions;
    private String status;

    protected int getBaseLayout() {
        return R.layout.activity_massage;
    }

    protected String getToolbarLabel() {
        return getString(R.string.massage_label);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadOptions();
        loadStatus();
    }

    private void loadOptions() {
        recyclerView = (RecyclerView)findViewById(R.id.massage_list);
        massageOptions = new ArrayList<>();
        final MassageRecyclerViewAdapter recyclerViewAdapter = new MassageRecyclerViewAdapter(massageOptions);
        recyclerView.setAdapter(recyclerViewAdapter);

        StaffServer.getInstance(this).getMassageOptions().subscribe(new Observer<List<MassageOption>>() {
            @Override public void onCompleted() {
                Log.d(MassageActivity.class.getCanonicalName(), "getMassageOptions#onCompleted");
                recyclerViewAdapter.notifyDataSetChanged();
            }
            @Override public void onError(Throwable e) {
                Log.d(MassageActivity.class.getCanonicalName(), "getMassageOptions#onError", e);
                e.printStackTrace();
            }
            @Override public void onNext(final List<MassageOption> options) {
                Log.d(MassageActivity.class.getCanonicalName(), options.size() + " massage options found");
                massageOptions.addAll(options);
            }
        });
    }

    private void loadStatus() {
        status = Permintaan.STATE_CANCELLED;
        sentImageView = findViewById(R.id.sent_image);
        processedImageView = findViewById(R.id.processed_image);
        completedImageView = findViewById(R.id.completed_image);

        PermintaanManager.getInstance().getMassageStatus(getBaseContext()).subscribe(new Observer<String>() {
            @Override public void onCompleted() {
                Log.d(MassageActivity.class.getCanonicalName(), "getMassageStatus#onCompleted");
            }
            @Override public void onError(Throwable e) {
                Log.d(MassageActivity.class.getCanonicalName(), "getMassageStatus#onError", e);
                e.printStackTrace();
            }
            @Override public void onNext(final String status) {
                Log.d(MassageActivity.class.getCanonicalName(), "Massage status is " + status);
                MassageActivity.this.status = status;
                switch (status) {
                    case Permintaan.STATE_COMPLETED:
                        completedImageView.setBackground(getResources().getDrawable(R.drawable.circle_green));
                    case Permintaan.STATE_INPROGRESS:
                        processedImageView.setBackground(getResources().getDrawable(R.drawable.circle_green));
                    case Permintaan.STATE_NEW:
                        sentImageView.setBackground(getResources().getDrawable(R.drawable.circle_green));
                        break;
                }
            }
        });
    }

    private boolean requestInProgress() {
        switch (status) {
            case Permintaan.STATE_INPROGRESS:
            case Permintaan.STATE_NEW:
                return true;
            default:
                return false;
        }
    }

    /**
     * Handle a click on a single massage option.
     * Bring up a confirmation dialog.
     * @param view The view that was clicked on.
     */
    @Override
    public void onClick(final View view) {
        if (requestInProgress()) {
            Toast.makeText(
                    MassageActivity.this.getApplicationContext(),
                    R.string.existing_request,
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        Log.d(MassageActivity.class.getCanonicalName(), "Selected recycler row: " + view.toString());
        int itemPosition = recyclerView.getChildLayoutPosition(view);
        final MassageOption item = massageOptions.get(itemPosition);
        new AlertDialog.Builder(this)
                .setTitle(item.getName())
                .setMessage(R.string.massage_message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String owner = Permintaan.OWNER_FRONTDESK;
                        String type = Permintaan.TYPE_MASSAGE;
                        String roomNumber = MassageActivity.this.getSharedPreferences("userSettings", MassageActivity.this.MODE_PRIVATE)
                                .getString("roomNumber", "none");
                        String guestId = MassageActivity.this.getSharedPreferences("userSettings", MassageActivity.this.MODE_PRIVATE)
                                .getString("guestId", "none");
                        if (guestId.equals("none")) {
                            Toast.makeText(
                                    MassageActivity.this.getApplicationContext(),
                                    R.string.no_guest_in_room,
                                    Toast.LENGTH_SHORT
                            ).show();
                            return;
                        }
                        String state = Permintaan.STATE_NEW;
                        Date currentDate = Calendar.getInstance().getTime();
                        PermintaanServer.getInstance(MassageActivity.this).createPermintaan(
                                new Permintaan(owner, type, roomNumber, guestId, state, currentDate,
                                        new Massage("", item))
                        ).subscribe(new Observer<Permintaan>() {
                            boolean success;
                            @Override
                            public void onCompleted() {
                                Log.d(MassageActivity.class.getCanonicalName(), "On completed");
                                if (success) {
                                    Toast.makeText(
                                            MassageActivity.this.getApplicationContext(),
                                            R.string.massage_result,
                                            Toast.LENGTH_SHORT
                                    ).show();
                                    status = Permintaan.STATE_NEW;
                                    sentImageView.setBackground(getResources().getDrawable(R.drawable.circle_green));
                                } else {
                                    Toast.makeText(
                                            MassageActivity.this.getApplicationContext(),
                                            R.string.something_went_wrong,
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            }
                            @Override
                            public void onError(Throwable e) {
                                Log.d(MassageActivity.class.getCanonicalName(), "On error");
                                e.printStackTrace();
                                success = false;
                            }
                            @Override
                            public void onNext(Permintaan permintaan) {
                                Log.d(MassageActivity.class.getCanonicalName(), "On next");
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

    public class MassageRecyclerViewAdapter
            extends RecyclerView.Adapter<MassageRecyclerViewAdapter.ViewHolder> {

        protected int selectedPos = -1;

        private final List<MassageOption> mValues;

        public MassageRecyclerViewAdapter(List<MassageOption> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.massage_list_row, parent, false);
            view.setOnClickListener(MassageActivity.this);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.itemView.setSelected(selectedPos == position);
            holder.item = mValues.get(position);

            Log.d(MassageActivity.class.getCanonicalName(), "Loading image " + holder.item.getImageUrl() + " into " + holder.imageView);
            Server.picasso(MassageActivity.this)
                    .load(holder.item.getImageUrl())
                    .placeholder(R.drawable.loading_batik)
                    .error(R.drawable.error)
                    .into(holder.imageView);
            holder.nameView.setText(holder.item.getName());
            if (holder.item.length != null) {
                holder.lengthView.setText(holder.item.length.toString() + " mins");
            }
            if (holder.item.price != null) {
                holder.priceView.setText("Rp. " + holder.item.price.toString());
            }
            holder.descriptionView.setText(holder.item.getDescription());
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public MassageOption item;
            public final View rootView;
            public final ImageView imageView;
            public final TextView nameView;
            public final TextView lengthView;
            public final TextView priceView;
            public final TextView descriptionView;

            public ViewHolder(View view) {
                super(view);
                rootView = view;
                imageView = (ImageView) view.findViewById(R.id.massage_image);
                nameView = (TextView) view.findViewById(R.id.massage_name);
                lengthView = (TextView) view.findViewById(R.id.massage_length);
                priceView = (TextView) view.findViewById(R.id.massage_price);
                descriptionView = (TextView) view.findViewById(R.id.massage_description);
            }

            @Override
            public String toString() {
                return super.toString() + " " + nameView.getText();
            }
        }
    }

}