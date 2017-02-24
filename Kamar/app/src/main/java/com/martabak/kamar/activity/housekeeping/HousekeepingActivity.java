package com.martabak.kamar.activity.housekeeping;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.martabak.kamar.R;
import com.martabak.kamar.activity.guest.AbstractGuestBarsActivity;
import com.martabak.kamar.activity.guest.SimpleDividerItemDecoration;
import com.martabak.kamar.domain.User;
import com.martabak.kamar.domain.managers.PermintaanManager;
import com.martabak.kamar.domain.options.HousekeepingOption;
import com.martabak.kamar.domain.managers.HousekeepingManager;
import com.martabak.kamar.domain.permintaan.Housekeeping;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.service.PermintaanServer;
import com.martabak.kamar.service.StaffServer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;

/**
 * This activity generates the list of housekeeping sections.
 */
public class HousekeepingActivity extends AbstractGuestBarsActivity {

    private List<String> housekeepingSections;
    private List<HousekeepingOption> hkOptions;
    private HashMap<String, Integer> idToQuantity;
    //binding view here
    @BindView(R.id.tabs) TabLayout tabLayout;

    protected Options getOptions() {
        return new Options()
                .withBaseLayout(R.layout.activity_housekeeping)
                .withToolbarLabel(getString(R.string.housekeeping_label))
                .showTabLayout(true)
                .showLogoutIcon(false)
                .enableChatIcon(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        hkOptions = HousekeepingManager.getInstance().getOptions();

        if (hkOptions == null) {
            housekeepingSections = new ArrayList<>();
            idToQuantity = new HashMap<>();
            StaffServer.getInstance(this).getHousekeepingOptions().subscribe(new Observer<List<HousekeepingOption>>() {
                @Override
                public void onCompleted() {
                    Log.d(HousekeepingActivity.class.getCanonicalName(), "onCompleted");
                    setupSectionsOrder();
                    HousekeepingManager.getInstance().setOrder(idToQuantity);
                    HousekeepingManager.getInstance().setHkOptions(hkOptions);
                    //initialize tabs
                    for (String section : housekeepingSections) {
                        tabLayout.addTab(tabLayout.newTab().setText(section));
                    }
                    setTabListener();
                    tabLayout.getTabAt(0).select();
                }

                @Override
                public void onError(Throwable e) {
                    Log.d(HousekeepingActivity.class.getCanonicalName(), "onError", e);
                    e.printStackTrace();
                }

                @Override
                public void onNext(final List<HousekeepingOption> options) {
                    Log.d(HousekeepingActivity.class.getCanonicalName(), options.size() + " housekeeping options found");
                    hkOptions = options;
                }
            });
        } else {
            housekeepingSections = new ArrayList<>();
            for (HousekeepingOption hk : hkOptions) {
                if (!housekeepingSections.contains(hk.getSection())) {
                    housekeepingSections.add(String.valueOf(hk.getSection()));
                }
            }
            //Get previous orders
            idToQuantity = HousekeepingManager.getInstance().getOrder();
            //initialize tabs
            for (String section : housekeepingSections) {
                tabLayout.addTab(tabLayout.newTab().setText(section));
            }
            setTabListener();
            tabLayout.getTabAt(0).select();
        }
    }

    public void setTabListener() {
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String selectedTab = tab.getText().toString();
                startHKFragment(selectedTab);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                String selectedTab = tab.getText().toString();
                startHKFragment(selectedTab);
            }
        });
    }

    public void startHKFragment(String selectedTab) {
        HousekeepingFragment f = new HousekeepingFragment();
        Bundle args = new Bundle();
        args.putString("section", selectedTab);
        f.setArguments(args);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction().replace(R.id.hk_fragment_container, f);
        ft.commit();
    }

    /**
     * Sets up the list of sections and the quantity dictionary
     */
    private void setupSectionsOrder() {
        for (HousekeepingOption hk : hkOptions) {
            if (!housekeepingSections.contains(hk.getSection())) {
                housekeepingSections.add(String.valueOf(hk.getSection()));
            }
            idToQuantity.put(hk._id, 0);
        }
    }

    /**
     * This fragment generates the list of housekeeping options.
     */
    public static class HousekeepingFragment extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

        @BindView(R.id.hk_option_recycler) RecyclerView optionRecyclerView;
        private Map<String, String> idToStatus;
        Integer quantity;
        private HousekeepingOptionAdapter hkOptionRecyclerAdapter;

        public HousekeepingFragment() {}

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_housekeeping, container, false);
            ButterKnife.bind(this,view);
            final ArrayList<HousekeepingOption> temp = buildOptions();
            idToStatus = new HashMap<>();
            quantity = 0;

            // Get the statuses,
            PermintaanManager.getInstance().getHousekeepingStatuses(getContext()).subscribe(new Observer<Map<String, String>>() {
                @Override public void onCompleted() {
                    Log.d(HousekeepingActivity.class.getCanonicalName(), "getHousekeepingStatuses#onCompleted");
                    hkOptionRecyclerAdapter = new HousekeepingOptionAdapter(temp, 
                            HousekeepingFragment.this.getContext(), HousekeepingFragment.this, idToStatus, HousekeepingFragment.this);
                    optionRecyclerView.setAdapter(hkOptionRecyclerAdapter);
                    optionRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));

                }
                @Override public void onError(Throwable e) {
                    Log.d(HousekeepingActivity.class.getCanonicalName(), "getHousekeepingStatuses#onError", e);
                    e.printStackTrace();
                }
                @Override public void onNext(final Map<String, String> statuses) {
                    Log.d(HousekeepingActivity.class.getCanonicalName(), "Fetching statuses of size " + statuses.size());
                    idToStatus = statuses;
                }
            });

            return view;
        }

        public void onClick(final View view) {
            final HousekeepingOptionAdapter.ViewHolder holder =
                    (HousekeepingOptionAdapter.ViewHolder)optionRecyclerView.getChildViewHolder((View)view.getParent());
            final HousekeepingOption option = holder.item;
            String guestId = getActivity().getSharedPreferences("userSettings", getActivity().MODE_PRIVATE)
                    .getString("guestId", "none");
            Log.v(HousekeepingActivity.class.getCanonicalName(), guestId);

            if (idToStatus.containsKey(option._id)) { //pre-existing request
                switch (idToStatus.get(option._id)) {
                    case Permintaan.STATE_COMPLETED:
                    case Permintaan.STATE_INPROGRESS:
                    case Permintaan.STATE_NEW:
                        Toast.makeText(
                                getActivity().getApplicationContext(),
                                R.string.existing_request,
                                Toast.LENGTH_SHORT
                        ).show();
                        return;
                }
            } else if (quantity == 0) {//.spinner.getSelectedItem().toString().equals("0")) {//quantity = 0
                 new AlertDialog.Builder(getContext())
                         .setTitle(option.getName())
                         .setMessage(R.string.housekeeping_zero_order)
                 .show();
                return;
            //Guest chose quantity > 0
            } else {
                new AlertDialog.Builder(getContext())
                        .setTitle(option.getName())
                        .setMessage(R.string.housekeeping_confirm)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String owner = User.TYPE_STAFF_FRONTDESK;
                                String type = Permintaan.TYPE_HOUSEKEEPING;
                                String guestId = getActivity().getSharedPreferences("userSettings", getActivity().MODE_PRIVATE)
                                        .getString("guestId", "none");
                                if (guestId.equals("none")) {
                                    new AlertDialog.Builder(getContext())
                                            .setTitle(option.getName())
                                            .setMessage(R.string.no_guest_in_room)
                                            .show();
                                    return;
                                }                                String state = Permintaan.STATE_NEW;
                                Date currentDate = Calendar.getInstance().getTime();
                                final String creator = getActivity().getSharedPreferences("userSettings", getActivity().MODE_PRIVATE)
                                        .getString("userType", "none");
                                String roomNumber = getActivity().getSharedPreferences("userSettings", getActivity().MODE_PRIVATE)
                                        .getString("roomNumber", "none");
                                //Create the Housekeeping permintaan
                                PermintaanServer.getInstance(getActivity()).createPermintaan(
                                        new Permintaan(owner, creator, type, roomNumber, guestId, state, currentDate,
                                                new Housekeeping("", quantity, option))
                                ).subscribe(new Observer<Permintaan>() {
                                    boolean success;

                                    @Override
                                    public void onCompleted() {
                                        Log.d(HousekeepingActivity.class.getCanonicalName(), "On completed");
                                        if (success) {
                                            //get, modify then set the order dictionary in the Manager
                                            HashMap idToQuantity = HousekeepingManager.getInstance().getOrder();
                                            idToQuantity.put(option._id, quantity);
                                            HousekeepingManager.getInstance().setOrder(idToQuantity);
                                            quantity = 0;
                                            idToStatus.put(option._id, Permintaan.STATE_NEW);
                                            hkOptionRecyclerAdapter.notifyDataSetChanged();
                                            if (creator.equals("GUEST")) {
                                                Toast.makeText(
                                                        getActivity().getApplicationContext(),
                                                        R.string.housekeeping_result,
                                                        Toast.LENGTH_SHORT
                                                ).show();
                                            }
                                            else if (creator.equals("STAFF")) {
                                                getActivity().setResult(Permintaan.SUCCESS);
                                                getActivity().finish();
                                            }

                                        } else {
                                            Toast.makeText(
                                                    getActivity().getApplicationContext(),
                                                    R.string.something_went_wrong,
                                                    Toast.LENGTH_SHORT
                                            ).show();
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.d(HousekeepingActivity.class.getCanonicalName(), "On error");
                                        e.printStackTrace();
                                        success = false;
                                    }

                                    @Override
                                    public void onNext(Permintaan permintaan) {
                                        Log.d(HousekeepingActivity.class.getCanonicalName(), "On next");
                                        if (permintaan != null) {
                                            success = true;
                                        } else {
                                            success = false;
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                .show();
            }
        }

        /*
         * Returns a list of options in the chosen section
         */
        public ArrayList<HousekeepingOption> buildOptions() {
            Bundle args = getArguments();
            String section = null;
            if (args != null) {
                section = args.getString("section");
            }
            List<HousekeepingOption> hkOptions = HousekeepingManager.getInstance().getOptions();
            ArrayList<HousekeepingOption> temp = new ArrayList<>();
            for (HousekeepingOption hk : hkOptions) {
                if (hk.getSection().equals(section)) {
                    temp.add(hk);
                }
            }
            return temp;
        }

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            /*final HousekeepingOptionAdapter.ViewHolder holder =
                    (HousekeepingOptionAdapter.ViewHolder)optionRecyclerView.getChildViewHolder((View)radioGroup.getParent());
            final HousekeepingOption question = holder.item;*/

            RadioButton checkedRadioButton = (RadioButton)radioGroup.findViewById(i);

            Log.v("CheckedButton", checkedRadioButton.toString());
            // This puts the value (true/false) into the variable
            boolean isChecked = checkedRadioButton.isChecked();
            // If the radio button that has changed in check state is now checked...
            if (isChecked) {
                quantity = Integer.parseInt(checkedRadioButton.getTag().toString());
//                idToQuantity.put(question._id, j);
            }
        }
    }

}
