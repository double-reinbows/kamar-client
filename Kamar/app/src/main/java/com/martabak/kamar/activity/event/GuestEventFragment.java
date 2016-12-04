package com.martabak.kamar.activity.event;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.martabak.kamar.R;
import com.martabak.kamar.service.Server;

/**
 * Created by adarsh on 31/10/16.
 */
public class GuestEventFragment extends Fragment {

    private String imageURL;


    public static GuestEventFragment getInstance(String imageURL) {
        GuestEventFragment guestEventFragment = new GuestEventFragment();
        Bundle args = new Bundle();
        args.putString("image_url", imageURL);
        guestEventFragment.setArguments(args);
        return guestEventFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageURL = getArguments().getString("image_url");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.event_item_image, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView eventImageView = (ImageView) view.findViewById(R.id.event_image);
        Server.picasso(this.getActivity())
                .load(imageURL)
                .placeholder(R.drawable.loading_batik)
                .error(R.drawable.error)
                .into(eventImageView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
