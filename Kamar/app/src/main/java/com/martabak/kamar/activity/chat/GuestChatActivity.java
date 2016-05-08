package com.martabak.kamar.activity.chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
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
 * An activity that simply loads the {@link ChatDetailFragment} into the view for a guest.
 */
public class GuestChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_chat);

        Bundle arguments = new Bundle();
        arguments.putString(ChatDetailFragment.GUEST_ID, getGuestId());
        arguments.putString(ChatDetailFragment.SENDER, getSender());
        ChatDetailFragment fragment = new ChatDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.chat_detail_container, fragment)
                .commit();
    }

    private String getGuestId() {
        SharedPreferences prefs = getSharedPreferences("userSettings", Context.MODE_PRIVATE);
        return prefs.getString("guestId", "-1"); // TODO make sure guestId shared pref is set somewhere
    }

    private String getSender() {
        return "GUEST";
    }

}