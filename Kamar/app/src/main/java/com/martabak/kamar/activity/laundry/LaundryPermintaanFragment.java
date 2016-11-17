package com.martabak.kamar.activity.laundry;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.managers.PermintaanManager;
import com.martabak.kamar.domain.permintaan.Permintaan;

import rx.Observer;

/**
 * Created by adarsh on 16/11/16.
 */
public class LaundryPermintaanFragment extends Fragment {
    public LaundryPermintaanFragment() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.permintaan_status, container, false);

        //Change text colour of permintaans
        TextView orderSent = (TextView)view.findViewById(R.id.permintaan_order_sent_text);
        TextView orderProcessed = (TextView)view.findViewById(R.id.permintaan_order_received_text);
        TextView orderCompleted = (TextView)view.findViewById(R.id.permintaan_order_completed_text);

        orderSent.setTextColor(ContextCompat.getColor(getActivity().getBaseContext(), R.color.guestTextColor));
        orderProcessed.setTextColor(ContextCompat.getColor(getActivity().getBaseContext(), R.color.guestTextColor));
        orderCompleted.setTextColor(ContextCompat.getColor(getActivity().getBaseContext(), R.color.guestTextColor));

        // Get the statuses,
        PermintaanManager.getInstance().getLaundryStatus(getContext()).subscribe(new Observer<String>() {
            String state;
            @Override
            public void onCompleted() {
                if (state != null) {
                    switch (state) {
                        case Permintaan.STATE_COMPLETED:
                            view.findViewById(R.id.completed_image).setBackground(ContextCompat.getDrawable(getActivity().getBaseContext(), R.drawable.circle_green));
                        case Permintaan.STATE_INPROGRESS:
                            view.findViewById(R.id.processed_image).setBackground(ContextCompat.getDrawable(getActivity().getBaseContext(), R.drawable.circle_green));
                        case Permintaan.STATE_NEW:
                            view.findViewById(R.id.sent_image).setBackground(ContextCompat.getDrawable(getActivity().getBaseContext(), R.drawable.circle_green));
                            break;
                    }
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                state = s;
            }
        });


        return view;
    }

}
