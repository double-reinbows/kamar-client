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
import com.martabak.kamar.domain.chat.ChatMessage;
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

    /**
     * @return A new instance of fragment StaffChatFragment.
     */
    public static StaffChatFragment newInstance() {
        return new StaffChatFragment();
    }

    //bind views here
    @BindView(R.id.chat_list) RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_chat_list, container, false);
        ButterKnife.bind(this,rootView);
        final List<Guest> guests = new ArrayList<Guest>();
        final ChatRecyclerViewAdapter recyclerViewAdapter = new ChatRecyclerViewAdapter(guests);
        recyclerView.setAdapter(recyclerViewAdapter);
        // get the guest/room number
        // get the position in guests list
        // call the onclick method on the room number to display the chat


        GuestServer.getInstance(this.getActivity()).getGuestsCheckedIn().subscribe(new Observer<Guest>() {
            @Override public void onCompleted() {
                Log.d(StaffChatFragment.class.getCanonicalName(), "onCompleted");
                recyclerViewAdapter.notifyDataSetChanged();
                //room number chat is not empty hence the appropriate room  number
                String roomNumberChatNotification = "";
                try{
                    roomNumberChatNotification = getArguments().getString("roomNumberChatNotification");
                } catch (NullPointerException e) {}
                if (!roomNumberChatNotification.equals("")) {
                    for (Guest guest : guests) {
                        Log.v("RoomNumberStaffChat", guest.roomNumber);
                        if (guest.roomNumber.equals(roomNumberChatNotification)) {

                            /*View childView = recyclerView.get
                            childView.performClick();*/
                        }
                    }

                }
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





        return rootView;
    }

    public class ChatRecyclerViewAdapter
            extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder> {

        protected int selectedPos = -1;

        private final List<Guest> mValues;

        public ChatRecyclerViewAdapter(List<Guest> items) {
            mValues = items;
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
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }



        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            //bind views here
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
                        ChatRecyclerViewAdapter.this.selectedPos = getLayoutPosition();
                        notifyItemChanged(selectedPos);

                        Bundle arguments = new Bundle();
                        arguments.putString(ChatDetailFragment.GUEST_ID, mItem._id);
                        arguments.putString(ChatDetailFragment.SENDER, getSender());
                        ChatDetailFragment fragment = new ChatDetailFragment();
                        fragment.setArguments(arguments);
                        getFragmentManager().beginTransaction()
                                .replace(R.id.chat_detail_container, fragment)
                                .commit();
                    }
                });
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }

    private String getSender() {
        SharedPreferences prefs = getActivity().getSharedPreferences("userSettings", Context.MODE_PRIVATE);
        return prefs.getString("userSubType", ChatMessage.SENDER_FRONTDESK);
    }

}
