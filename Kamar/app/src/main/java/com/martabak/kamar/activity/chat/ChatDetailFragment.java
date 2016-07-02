package com.martabak.kamar.activity.chat;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.chat.ChatMessage;
import com.martabak.kamar.domain.chat.GuestChat;
import com.martabak.kamar.domain.permintaan.Permintaan;
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
     * The fragment argument representing the guest's ID of the chat that this fragment
     * represents.
     */
    public static final String GUEST_ID = "guest_id";

    /**
     * The fragment argument representing the sender that is loading this fragment.
     * One of "FRONTDESK", "RESTAURANT" or "GUEST".
     */
    public static final String SENDER = "sender";

    /**
     * The guest's ID.
     */
    private String mGuestId;

    /**
     * The current viewer of chats, either "RESTAURANT", "FRONTDESK" or "GUEST".
     */
    private String mSender;

    /**
     * The chat message list.
     */
    private List<ChatMessage> mChatMessages;

    /**
     * The chat message recycler view adapter.
     */
    private MessageRecyclerViewAdapter mRecyclerViewAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ChatDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(GUEST_ID) && getArguments().containsKey(SENDER)) {
            mGuestId = getArguments().getString(GUEST_ID);
            mSender = getArguments().getString(SENDER);
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
            mChatMessages = new ArrayList<>();
            mRecyclerViewAdapter = new MessageRecyclerViewAdapter(mChatMessages);
            recyclerView.setAdapter(mRecyclerViewAdapter);
            refreshChatMessages();
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setStackFromEnd(true);
            recyclerView.setLayoutManager(layoutManager);

            EditText messageEditText = (EditText) rootView.findViewById(R.id.chat_message_edit_text);
            Button sendButton = (Button) rootView.findViewById(R.id.chat_send_button);
            sendButton.setOnClickListener(new MessageSender(messageEditText, mGuestId, mSender));
        } else {
            Log.d(ChatDetailFragment.class.getCanonicalName(), "mGuestId is null");
        }

        return rootView;
    }

    /**
     * Pull the latest chat messages and notify the recycler view that the data has changed.
     */
    private void refreshChatMessages() {
        mChatMessages.clear();

        // Get the latest messages.
        ChatServer.getInstance(getActivity()).getGuestChat(mGuestId).subscribe(new Observer<GuestChat>() {
            @Override public void onCompleted() {
                Log.d(ChatDetailFragment.class.getCanonicalName(), "getGuestChat#onCompleted");
                mRecyclerViewAdapter.notifyDataSetChanged();
                readChatMessages();
            }
            @Override public void onError(Throwable e) {
                Log.d(ChatDetailFragment.class.getCanonicalName(), "getGuestChat#onError", e);
                e.printStackTrace();
            }
            @Override public void onNext(GuestChat guestChat) {
                Log.d(ChatDetailFragment.class.getCanonicalName(), "getGuestChat#onNext Guest chat received with " + guestChat.messages.size() + " messages");
                mChatMessages.addAll(guestChat.messages);
            }
        });

    }

    /**
     * Set any unread messages from the "other side" to be read.
     */
    private void readChatMessages() {
        for (ChatMessage message : mChatMessages) {
            if (message.read == null) { // If it's unread...and
                if (message.fromGuest() && !mSender.equals(ChatMessage.SENDER_GUEST) // From guest and restaurant or front-desk are reading it...or
                        || !message.fromGuest() && mSender.equals(ChatMessage.SENDER_GUEST)) { // From restaurant or front-desk and guest is reading it
                    // Then set the message to read
                    Log.d(ChatDetailFragment.class.getCanonicalName(), "Setting chat message " + message._id + " to read");
                    ChatServer.getInstance(getActivity()).setChatMessageToRead(message)
                            .subscribe(new Observer<Boolean>() {
                                @Override public void onCompleted() {}
                                @Override public void onError(Throwable e) {
                                    Log.d(ChatDetailFragment.class.getCanonicalName(), "setChatMessageToRead#onError",  e);
                                    e.printStackTrace();
                                }
                                @Override public void onNext(Boolean result) {
                                    Log.d(ChatDetailFragment.class.getCanonicalName(), "setChatMessageToRead#onNext Chat read result: " + result);
                                }
                            });
                }
            }
        }
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

            //set the background row colors here
            if (position % 2 == 1) {
                holder.mView.setBackgroundColor(Color.GRAY);
            } else {
                holder.mView.setBackgroundColor(Color.WHITE);
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

        @Override public void onClick(final View v) {
            InputMethodManager imm = (InputMethodManager)ChatDetailFragment.this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(messageEditText.getWindowToken(), 0);
            messageEditText.setEnabled(false);
            v.setEnabled(false);

            Log.d(ChatDetailFragment.class.getCanonicalName(), "Send button clicked");
            ChatServer.getInstance(getActivity()).sendChatMessage(buildMessage())
                    .subscribe(new Observer<Boolean>() {
                        @Override public void onCompleted() {
                            messageEditText.setEnabled(true);
                            v.setEnabled(true);
                            refreshChatMessages();
                        }
                        @Override public void onError(Throwable e) {
                            messageEditText.setEnabled(true);
                            v.setEnabled(true);
                            e.printStackTrace();
                            Toast.makeText(
                                    getActivity().getApplicationContext(),
                                    R.string.something_went_wrong,
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                        @Override public void onNext(Boolean result) {
                            if (result) {
                                messageEditText.getText().clear();
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
