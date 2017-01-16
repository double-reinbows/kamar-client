package com.martabak.kamar.activity.massage;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.martabak.kamar.R;
import com.martabak.kamar.activity.guest.AbstractGuestBarsActivity;
import com.martabak.kamar.activity.guest.SimpleDividerItemDecoration;
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
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;

/**
 * This activity generates the list of massage options and allows the guest to request one.
 */
public class MassageActivity extends AbstractGuestBarsActivity implements View.OnClickListener {

    // bind view here
    @BindView(R.id.massage_list) RecyclerView recyclerView;
    private List<MassageOption> massageOptions;
    private Map<String, String> statuses; // Maps massage option ID -> request status
    private MassageRecyclerViewAdapter recyclerViewAdapter;

    protected Options getOptions() {
        return new Options()
                .withBaseLayout(R.layout.activity_massage)
                .withToolbarLabel(getString(R.string.massage_label))
                .showTabLayout(false)
                .showLogoutIcon(false)
                .enableChatIcon(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        loadOptions();
    }

    private void loadOptions() {
        massageOptions = new ArrayList<>();
        StaffServer.getInstance(this).getMassageOptions().subscribe(new Observer<List<MassageOption>>() {
            @Override public void onCompleted() {
                Log.d(MassageActivity.class.getCanonicalName(), "getMassageOptions#onCompleted");
                getStatuses();
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

    private void getStatuses() {
        PermintaanManager.getInstance().getMassageStatuses(getBaseContext()).subscribe(new Observer<Map<String, String>>() {
            @Override public void onCompleted() {
                Log.d(MassageActivity.class.getCanonicalName(), "getMassageStatus#onCompleted");
                recyclerViewAdapter = new MassageRecyclerViewAdapter(massageOptions);
                recyclerView.setAdapter(recyclerViewAdapter);
                recyclerView.addItemDecoration(new SimpleDividerItemDecoration(MassageActivity.this));
            }
            @Override public void onError(Throwable e) {
                Log.d(MassageActivity.class.getCanonicalName(), "getMassageStatus#onError", e);
                e.printStackTrace();
            }
            @Override public void onNext(Map<String, String> statuses) {
                Log.d(MassageActivity.class.getCanonicalName(), "Fetching statuses of size " + statuses.size());
                MassageActivity.this.statuses = statuses;
            }
        });
    }

    private boolean requestInProgress() {
        for (String status : statuses.values())
            if (status.equals(Permintaan.STATE_INPROGRESS) ||
                    status.equals(Permintaan.STATE_NEW)) {
                return true;
            }
        return false;
    }

    /**
     * Handle a click on a single massage option.
     * Bring up a confirmation dialog.
     * @param buttonView The view that was clicked on.
     */
    @Override
    public void onClick(final View buttonView) {
        final View view = (View)buttonView.getParent().getParent();
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
                        final String creator = MassageActivity.this.getSharedPreferences("userSettings", MassageActivity.this.MODE_PRIVATE)
                                .getString("userType", "none");
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
                            buttonView.setEnabled(true);
                            return;
                        }
                        String state = Permintaan.STATE_NEW;
                        Date currentDate = Calendar.getInstance().getTime();
                        PermintaanServer.getInstance(MassageActivity.this).createPermintaan(
                                new Permintaan(owner, creator, type, roomNumber, guestId, state, currentDate,
                                        new Massage("", item))
                        ).subscribe(new Observer<Permintaan>() {
                            boolean success;
                            @Override
                            public void onCompleted() {
                                Log.d(MassageActivity.class.getCanonicalName(), "On completed");
                                if (success) {
                                    MassageActivity.this.statuses.put(item._id, Permintaan.STATE_NEW);
                                    recyclerViewAdapter.notifyDataSetChanged();
                                    if (creator.equals("GUEST")) {
                                        Toast.makeText(
                                                MassageActivity.this.getApplicationContext(),
                                                R.string.massage_result,
                                                Toast.LENGTH_SHORT
                                        ).show();
                                    }
                                    else if (creator.equals("STAFF")) {
                                        setResult(Permintaan.SUCCESS);
                                        finish();
                                    }

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
                                success = permintaan != null;
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
            view.findViewById(R.id.order_button).setOnClickListener(MassageActivity.this);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.itemView.setSelected(selectedPos == position);
            holder.item = mValues.get(position);

            Log.d(MassageActivity.class.getCanonicalName(), "Loading image " + holder.item.getImageUrl() + " into " + holder.imageView);
            Server.picasso(MassageActivity.this)
                    .load(holder.item.getImageUrl())
                    .resize(250, 125)
                    .placeholder(R.drawable.loading_batik)
                    .error(R.drawable.error)
                    .into(holder.imageView);
            holder.nameView.setText(holder.item.getName());
            if (holder.item.length != null) {
                holder.lengthView.setText(holder.item.length.toString() + " " + getString(R.string.transport_minutes));
            }
            if (holder.item.price != null) {
                holder.priceView.setText("Rp. " + holder.item.price.toString());
            }
//            holder.descriptionView.setText(holder.item.getDescription());

            String state = statuses.containsKey(holder.item._id) ? statuses.get(holder.item._id) : Permintaan.STATE_CANCELLED;
            Log.d(MassageActivity.class.getCanonicalName(), "Status for massage " + holder.item.getName() + " is " + state);
            switch (state) {
                case Permintaan.STATE_NEW:
                    holder.sentImageView.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.circle_green));
                    holder.processedImageView.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.circle_red));
                    holder.completedImageView.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.circle_red));
                    break;
                case Permintaan.STATE_INPROGRESS:
                    holder.sentImageView.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.circle_green));
                    holder.processedImageView.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.circle_green));
                    holder.completedImageView.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.circle_red));
                    break;
                case Permintaan.STATE_COMPLETED:
                    holder.sentImageView.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.circle_green));
                    holder.processedImageView.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.circle_green));
                    holder.completedImageView.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.circle_green));
                    break;
            }
            if (requestInProgress()) {
                holder.buttonView.setEnabled(false);
            } else {
                holder.buttonView.setEnabled(true);
            }
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public MassageOption item;
            public final View rootView;
            @BindView(R.id.massage_image) ImageView imageView;
            @BindView(R.id.massage_name) TextView nameView;
            @BindView(R.id.massage_length) TextView lengthView;
            @BindView(R.id.massage_price) TextView priceView;
//            public final TextView descriptionView;
            @BindView(R.id.sent_image) View sentImageView;
            @BindView(R.id.processed_image) View processedImageView;
            @BindView(R.id.completed_image) View completedImageView;
            @BindView(R.id.order_button) View buttonView;

            public ViewHolder(View view) {
                super(view);
                rootView = view;
//                descriptionView = (TextView) view.findViewById(R.id.massage_description);
                ButterKnife.bind(this, view);
            }

            @Override
            public String toString() {
                return super.toString() + " " + nameView.getText();
            }
        }
    }

}
