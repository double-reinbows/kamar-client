package com.martabak.kamar.activity.chat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.GuestChat;
import com.martabak.kamar.service.ChatServer;

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
     * The chat this fragment is presenting.
     */
    private GuestChat mItem;

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
            String guestId = getArguments().getString(ARG_ITEM_ID);
            ChatServer.getInstance(getActivity()).getGuestChat(guestId).subscribe(new Observer<GuestChat>() {
                @Override public void onCompleted() {
                    Log.d(ChatDetailFragment.class.getCanonicalName(), "onCompleted");
                }
                @Override public void onError(Throwable e) {
                    Log.d(ChatDetailFragment.class.getCanonicalName(), "onError");
                    e.printStackTrace();
                }
                @Override public void onNext(GuestChat guestChat) {
                    mItem = guestChat;
                }
            });

//            Activity activity = this.getActivity();
//            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
//            if (appBarLayout != null) {
//                appBarLayout.setTitle(mItem.content);
//            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chat_detail, container, false);

        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.chat_detail)).setText(mItem.messages.toString());
        }

        return rootView;
    }
}
