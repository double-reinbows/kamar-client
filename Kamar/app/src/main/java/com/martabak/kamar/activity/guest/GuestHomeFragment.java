package com.martabak.kamar.activity.guest;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.Guest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by adarsh on 10/08/16.
 */
public class GuestHomeFragment extends Fragment {

    private String option;
//    private Guest guest;

    private GuestHomeIconListener guestHomeIconListener;

    public GuestHomeFragment() {
    }

    public static GuestHomeFragment newInstance() {
        return new GuestHomeFragment();
    }
    GuestHomeAdapter guestHomeAdapter;

    // binding the views here
    @BindView(R.id.guestgridview) GridView gridView;
    @BindView(R.id.room_number) TextView roomTextView;
    @BindView(R.id.bottombar_guest_message) TextView bottombarGuestText;

    // tapping on each individual item in the grid
    @OnItemClick(R.id.guestgridview)
    void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // perform action for each individual feature
        option = guestHomeAdapter.getItem(position).toString();
        guestHomeIconListener.onIconClick(option);
    }

    // tapping on the logout icon
    @OnClick(R.id.logoutIcon)
    public void onLogoutClick() {
        DialogFragment logoutDialogFragment = new LogoutDialogFragment();
        logoutDialogFragment.show(getFragmentManager(), "logout");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guest_home, container, false);

        ButterKnife.bind(this, view);
//        guest = new Guest();

        final String roomNumber = getActivity().getSharedPreferences("userSettings", getActivity().MODE_PRIVATE)
                .getString("roomNumber", "none");
        roomTextView.setText(roomNumber);
        guestHomeAdapter = new GuestHomeAdapter(this.getActivity());
        gridView.setAdapter(guestHomeAdapter);

        String guestName = getActivity().getSharedPreferences("userSettings", getActivity().MODE_PRIVATE)
                .getString("guestName", "none");
        bottombarGuestText.setText(getActivity().getString(R.string.hi)+" "+guestName);

        // update the text of the navigation menu items if language changed.
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.getMenu().getItem(0).setTitle(R.string.nav_drawer_home);
            navigationView.getMenu().getItem(0).setChecked(true);
            navigationView.getMenu().getItem(1).setTitle(R.string.nav_drawer_change_language);
            navigationView.getMenu().getItem(2).setTitle(R.string.nav_drawer_about);
        }

        // display feature text on each item click
        /*
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // perform action for each individual feature
                option = guestHomeAdapter.getItem(position).toString();
                guestHomeIconListener.onIconClick(option);
            }
        }); */
/*
        // open the change room number as a fragment
        if (passwordIconView != null) {
            passwordIconView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment changeRoomNumberFragment = new ChangeRoomNumberDialogFragment();
                    changeRoomNumberFragment.show(getFragmentManager(), "changeRoomNumber");
                }
            });
        }
*/
        // logout guest
        /*
        if (logoutView != null) {
            logoutView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment logoutDialogFragment = new LogoutDialogFragment();
                    logoutDialogFragment.show(getFragmentManager(), "logout");

                    /*
                    new AlertDialog.Builder(GuestHomeActivity.this)
                            .setMessage(getString(R.string.logout_options))
                            .setCancelable(false)

                            .setNegativeButton(getString(R.string.logout_change_room_number), new DialogInterface.OnClickListener() {
                                @Override public void onClick(DialogInterface dialog, int which) {
                                    DialogFragment changeRoomNumberFragment = new ChangeRoomNumberDialogFragment();
                                    changeRoomNumberFragment.show(getFragmentManager(), "changeRoomNumber");
                                }
                            })

                            .setNeutralButton(getString(R.string.logout_staff), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    SharedPreferences.Editor editor = GuestHomeActivity.this
                                            .getSharedPreferences("userSettings", MODE_PRIVATE)
                                            .edit();
                                    editor.putString("subUserType", User.TYPE_STAFF_FRONTDESK)
                                            .commit();

                                    startActivity(new Intent(GuestHomeActivity.this, StaffHomeActivity.class));
                                    GuestHomeActivity.this.finish();
                                }
                            })

                            .setPositiveButton(getString(R.string.logout_reset_tablet), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.v("App Action", "Resetting tablet");
                                    checkGuestOut(roomNumber);
                                }
                            })

                    .create().show();

*/
/*                }
            });
        }*/
        return view;
    }

    /*
    @Override
    public void onStop() {
        stopGuestServices();
        super.onStop();
    }
    */

    public interface GuestHomeIconListener {
        void onIconClick(String option);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        guestHomeIconListener = (GuestHomeIconListener) activity;
    }

}
