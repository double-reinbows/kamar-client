package com.martabak.kamar.activity.chat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.chat.ChatMessage;
import com.martabak.kamar.domain.chat.GuestChat;
import com.martabak.kamar.service.ChatServer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import rx.Observer;

/**
 * A fragment representing a single Chat detail screen.
 * This fragment is either contained in a {@link ChatListActivity}.
 */
public class ChatDetailFragment extends Fragment {

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The guest's ID.
     */
    private String mGuestId;

    /**
     * The current viewer of chats, either "RESTAURANT", "FRONTDESK" or "GUEST".
     */
    private String mSender;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ChatDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mGuestId = getArguments().getString(ARG_ITEM_ID);
            mSender = "FRONTDESK"; // FIXME
        }

        // TODO else say "please select a guest from the sidebar"
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chat_detail, container, false);

        if (mGuestId != null) {
            Log.d(ChatDetailFragment.class.getCanonicalName(), "Creating view for " + mGuestId);

            RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.chat_message_list);
            final List<ChatMessage> messages = new ArrayList<ChatMessage>();
            final MessageRecyclerViewAdapter recyclerViewAdapter = new MessageRecyclerViewAdapter(messages);
            recyclerView.setAdapter(recyclerViewAdapter);

            ChatServer.getInstance(getActivity()).getGuestChat(mGuestId).subscribe(new Observer<GuestChat>() {
                @Override public void onCompleted() {
                    Log.d(ChatDetailFragment.class.getCanonicalName(), "onCompleted");
                    recyclerViewAdapter.notifyDataSetChanged();
                }
                @Override public void onError(Throwable e) {
                    Log.d(ChatDetailFragment.class.getCanonicalName(), "onError");
                    e.printStackTrace();
                }
                @Override public void onNext(GuestChat guestChat) {
                    Log.d(ChatDetailFragment.class.getCanonicalName(), "Guest chat received with " + guestChat.messages.size() + " messages");
                    messages.addAll(guestChat.messages);
                    Collections.reverse(messages);
                }
            });

            EditText messageEditText = (EditText) rootView.findViewById(R.id.chat_message_edit_text);
            Button sendButton = (Button) rootView.findViewById(R.id.chat_send_button);
            sendButton.setOnClickListener(new MessageSender(messageEditText, mGuestId, mSender));
        } else {
            Log.d(ChatDetailFragment.class.getCanonicalName(), "mGuestId is null");
        }

        return rootView;
    }

    public class MessageRecyclerViewAdapter
            extends RecyclerView.Adapter<MessageRecyclerViewAdapter.ViewHolder> {

        private final List<ChatMessage> mValues;

        public MessageRecyclerViewAdapter(List<ChatMessage> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_message, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mFromView.setText(mValues.get(position).from);
            holder.mSentTimeView.setText(mValues.get(position).sent.toString());
            holder.mMessageView.setText(mValues.get(position).message);
            if (mValues.get(position).read == null) { // If the message has been sent but not read
                holder.mIconView.setImageResource(R.drawable.ic_menu_slideshow);
            } else { // If the message has been sent and read
                holder.mIconView.setImageResource(R.drawable.ic_menu_share);
            }
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mFromView;
            public final TextView mSentTimeView;
            public final ImageView mIconView;
            public final TextView mMessageView;
            public ChatMessage mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mFromView = (TextView) view.findViewById(R.id.chat_message_from);
                mSentTimeView = (TextView) view.findViewById(R.id.chat_message_sent_time);
                mIconView = (ImageView) view.findViewById(R.id.chat_message_icon);
                mMessageView = (TextView) view.findViewById(R.id.chat_message_text);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mMessageView.getText() + "'";
            }
        }
    }

    public class MessageSender implements View.OnClickListener {

        EditText messageEditText;
        String guestId;
        String sender;

        public MessageSender(EditText messageEditText, String guestId, String sender) {
            this.messageEditText = messageEditText;
            this.guestId = guestId;
            this.sender = sender;
        }

        private ChatMessage buildMessage() {
            return new ChatMessage(
                    guestId,
                    sender,
                    messageEditText.getText().toString(),
                    new Date(),
                    null
            );
        }

        @Override public void onClick(View v) {
            Log.d(ChatDetailFragment.class.getCanonicalName(), "Send button clicked");
            ChatServer.getInstance(getActivity()).sendChatMessage(buildMessage())
                    .subscribe(new Observer<Boolean>() {
                        @Override public void onCompleted() {
                        }
                        @Override public void onError(Throwable e) {
                            e.printStackTrace();
                            Toast.makeText(
                                    getActivity().getApplicationContext(),
                                    R.string.something_went_wrong,
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                        @Override public void onNext(Boolean result) {
                            if (result) {
                                Toast.makeText(
                                        getActivity().getApplicationContext(),
                                        R.string.chat_message_sent,
                                        Toast.LENGTH_SHORT
                                ).show();
                            } else {
                                Toast.makeText(
                                        getActivity().getApplicationContext(),
                                        R.string.something_went_wrong,
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                    });
        }
    }
}
