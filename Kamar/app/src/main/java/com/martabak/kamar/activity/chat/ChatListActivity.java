package com.martabak.kamar.activity.chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.martabak.kamar.R;

import com.martabak.kamar.domain.Guest;
import com.martabak.kamar.service.GuestServer;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;

/**
 * An activity representing a list of Chats. The activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ChatListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.chat_list);
        final List<Guest> guests = new ArrayList<Guest>();
        final ChatRecyclerViewAdapter recyclerViewAdapter = new ChatRecyclerViewAdapter(guests);
        recyclerView.setAdapter(recyclerViewAdapter);

        GuestServer.getInstance(this).getGuestsCheckedIn().subscribe(new Observer<Guest>() {
            @Override public void onCompleted() {
                Log.d(ChatListActivity.class.getCanonicalName(), "onCompleted");
                recyclerViewAdapter.notifyDataSetChanged();
            }
            @Override public void onError(Throwable e) {
                Log.d(ChatListActivity.class.getCanonicalName(), "onError");
                e.printStackTrace();
            }
            @Override public void onNext(final Guest guest) {
                Log.d(ChatListActivity.class.getCanonicalName(), "Guest found in room " + guest.roomNumber);
                guests.add(guest);
            }
        });
    }

    public class ChatRecyclerViewAdapter
            extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder> {

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
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).roomNumber);
            holder.mContentView.setText(mValues.get(position).firstName + " " + mValues.get(position).lastName);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ChatDetailFragment.GUEST_ID, holder.mItem._id);
                    arguments.putString(ChatDetailFragment.SENDER, getSender());
                    ChatDetailFragment fragment = new ChatDetailFragment();
                    fragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.chat_detail_container, fragment)
                            .commit();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public Guest mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }

    private String getSender() {
        SharedPreferences prefs = getSharedPreferences("userSettings", Context.MODE_PRIVATE);
        return prefs.getString("userSubType", "FRONTDESK");
    }
}
