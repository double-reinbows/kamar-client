package com.martabak.kamar.activity.chat;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.Guest;
import com.martabak.kamar.domain.User;
import com.martabak.kamar.service.GuestServer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;

/**
 * Staff chat fragment loaded by {@link com.martabak.kamar.activity.staff.StaffHomeActivity}.
 */
public class StaffChatFragment extends Fragment {

    public static StaffChatFragment newInstance() {
        return new StaffChatFragment();
    }

    @BindView(R.id.chat_list) RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_chat_list, container, false);
        ButterKnife.bind(this,rootView);
        final List<Guest> guests = new ArrayList<>();
        final ChatRecyclerViewAdapter adapter = new ChatRecyclerViewAdapter(guests, tryGetPreselectedGuestId());
        recyclerView.setAdapter(adapter);
        populateChats(guests, adapter);
        return rootView;
    }

    private void populateChats(final List<Guest> guests, final ChatRecyclerViewAdapter adapter) {
        GuestServer.getInstance(this.getActivity()).getGuestsCheckedIn().subscribe(new Observer<Guest>() {
            @Override public void onCompleted() {
                Log.v(StaffChatFragment.class.getCanonicalName(), "onCompleted");
                adapter.notifyDataSetChanged();
            }
            @Override public void onError(Throwable e) {
                Log.d(StaffChatFragment.class.getCanonicalName(), "onError", e);
                e.printStackTrace();
            }
            @Override public void onNext(final Guest guest) {
                Log.d(StaffChatFragment.class.getCanonicalName(), "Guest found in room " + guest.roomNumber);
                guests.add(guest);
            }
        });
    }

    /**
     * If we've come here via a notification then let's get the guest ID argument from the bundle
     * to pre-select that chat.
     */
    private String tryGetPreselectedGuestId() {
        try {
            String selectedChatGuestId = getArguments().getString("SelectedChatGuestId");
            Log.d(StaffChatFragment.class.getCanonicalName(), "Selecting chat with guest ID " + selectedChatGuestId);
            return selectedChatGuestId;
        } catch (NullPointerException e) {
            Log.d(StaffChatFragment.class.getCanonicalName(), "No pre-selected chat found");
            return "";
        }
    }

    public class ChatRecyclerViewAdapter
            extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder> {

        protected int selectedPos = -1;
        private String preselectedGuestId;
        private final List<Guest> mValues;

        public ChatRecyclerViewAdapter(List<Guest> items, String preselectedGuestId) {
            mValues = items;
            this.preselectedGuestId = preselectedGuestId;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.itemView.setSelected(selectedPos == position);
            holder.mItem = mValues.get(position);
            holder.mIdView.setText("ROOM " + mValues.get(position).roomNumber);
            holder.mContentView.setText(mValues.get(position).firstName + " " + mValues.get(position).lastName);
            if (mValues.get(position)._id.equals(preselectedGuestId)) {
                Log.d(StaffChatFragment.class.getCanonicalName(), "Selected position " + position + " for guest ID " + preselectedGuestId);
                holder.loadChatFragmentView();
                holder.itemView.setSelected(true);
                preselectedGuestId = null;
            }
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            @BindView(R.id.id) TextView mIdView;
            @BindView(R.id.content) TextView mContentView;
            public Guest mItem;

            public ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this,view);
                mView = view;
                mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notifyItemChanged(selectedPos);
                        loadChatFragmentView();
                        notifyItemChanged(selectedPos);
                    }
                });
            }

            public void loadChatFragmentView() {
                ChatRecyclerViewAdapter.this.selectedPos = getLayoutPosition();

                Bundle arguments = new Bundle();
                arguments.putString(ChatDetailFragment.GUEST_ID, mItem._id);
                arguments.putString(ChatDetailFragment.SENDER, getSender());
                ChatDetailFragment fragment = new ChatDetailFragment();
                fragment.setArguments(arguments);
                getFragmentManager().beginTransaction()
                        .replace(R.id.chat_detail_container, fragment)
                        .commit();
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }

    private String getSender() {
        SharedPreferences prefs = getActivity().getSharedPreferences("userSettings", Context.MODE_PRIVATE);
        return prefs.getString("userSubType", User.TYPE_STAFF_FRONTDESK);
    }

}
