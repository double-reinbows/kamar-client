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

/**
 * Created by adarsh on 10/08/16.
 */
public class GuestHomeFragment extends Fragment {


    private String option;
    private TextView roomNumberTextView;
    private String welcomeMessage;
    private Guest guest;

    private GuestHomeIconListener guestHomeIconListener;


    public GuestHomeFragment() {
    }

    public static GuestHomeFragment newInstance() {
        return new GuestHomeFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_guest_home, container, false);
        final GuestHomeAdapter guestHomeAdapter = new GuestHomeAdapter(this.getActivity());
        final GridView gridView = (GridView) view.findViewById(R.id.guestgridview);
        //View passwordIconView = findViewById(R.id.passwordChangeIcon);
        View logoutView = view.findViewById(R.id.logoutIcon);
        guest = new Guest();


        gridView.setAdapter(guestHomeAdapter);

        // update the text of the navigation menu items if language changed.
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.getMenu().getItem(0).setTitle(R.string.nav_drawer_home);
            navigationView.getMenu().getItem(0).setChecked(true);
            navigationView.getMenu().getItem(1).setTitle(R.string.nav_drawer_change_language);
            navigationView.getMenu().getItem(2).setTitle(R.string.nav_drawer_about);
        }

        // display feature text on each item click
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // perform action for each individual feature
                option = guestHomeAdapter.getItem(position).toString();
                guestHomeIconListener.onIconClick(option);
            }
        });
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

                                    //Log.v(SelectUserTypeActivity.class.getCanonicalName(), "userType is " + getActivity().getSharedPreferences("userSettings", MODE_PRIVATE).getString("userType", "none"));
                                    //Log.v(SelectUserTypeActivity.class.getCanonicalName(), "subUserType is " + getActivity().getSharedPreferences("userSettings", MODE_PRIVATE).getString("subUserType", "none"));

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
                }
            });
        }
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
