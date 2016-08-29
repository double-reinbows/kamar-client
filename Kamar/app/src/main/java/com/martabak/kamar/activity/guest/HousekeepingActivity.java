package com.martabak.kamar.activity.guest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.options.HousekeepingOption;
import com.martabak.kamar.service.StaffServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observer;

/**
 * This activity generates the list of massage options and allows the guest to request one.
 */
public class HousekeepingActivity extends AppCompatActivity implements
    View.OnClickListener {

    private RecyclerView recyclerView;
    //private RecyclerView recyclerView2;
    private ArrayList<String> housekeepingSections;
    private List<HousekeepingOption> housekeepingOptions;
    private HashMap<HousekeepingOption, Integer> hkOptionDict;
    public RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_housekeeping);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });
        TextView roomNumberTextView = (TextView)findViewById(R.id.toolbar_roomnumber);
        java.lang.String roomNumber = getSharedPreferences("userSettings", MODE_PRIVATE)
                .getString("roomNumber", "none");
        roomNumberTextView.setText(getString(R.string.room_number) + ": " + roomNumber);
        // END GENERIC LAYOUT STUFF

        recyclerView = (RecyclerView)findViewById(R.id.housekeeping_list);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        housekeepingSections = new ArrayList<>();
        housekeepingOptions = new ArrayList<>();
        hkOptionDict = new HashMap<>();
        final HousekeepingSectionAdapter recyclerViewAdapter = new HousekeepingSectionAdapter(this, housekeepingSections);
        recyclerView.setAdapter(recyclerViewAdapter);
        //onSaveInstanceState(savedInstanceState);

        StaffServer.getInstance(this).getHousekeepingOptions().subscribe(new Observer<List<HousekeepingOption>>() {
            @Override public void onCompleted() {
                Log.d(HousekeepingActivity.class.getCanonicalName(), "onCompleted");
                addSections();
                recyclerViewAdapter.notifyDataSetChanged();
            }
            @Override public void onError(Throwable e) {
                Log.d(HousekeepingActivity.class.getCanonicalName(), "onError", e);
                e.printStackTrace();
            }
            @Override public void onNext(final List<HousekeepingOption> options) {
                Log.d(HousekeepingActivity.class.getCanonicalName(), options.size() + " housekeeping options found");
                housekeepingOptions.addAll(options);
            }
        });

    }
/*
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable("layoutManager", );
        super.onSaveInstanceState(savedInstanceState);
    }*/

    /**
     * Handle a click on a single massage option.
     * Bring up a confirmation dialog.
    */
    @Override
    public void onClick(final View view) {
/*
        if (findViewById(R.id.hk_fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
        }*/
        int itemPosition = recyclerView.getChildLayoutPosition(view);
        final String selectedSection = housekeepingSections.get(itemPosition);
        //Log.v("selectedSection", selectedSection);

        HousekeepingFragment f = new HousekeepingFragment();
        Bundle args = new Bundle();
        args.putString("section", selectedSection);
        args.putSerializable("hkOptionDict", hkOptionDict);

        //getIntent().putExtras(args);
        f.setArguments(args);
        Log.d("beforeView", findViewById(R.id.hk_fragment_container).toString());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction().replace(R.id.hk_fragment_container, f);
        ft.commit();

        Log.d("afterView", findViewById(R.id.hk_fragment_container).toString());
    }

    private void addSections() {
        for (HousekeepingOption hk : housekeepingOptions) {
            if (!housekeepingSections.contains(hk.sectionEn)) {
                housekeepingSections.add(String.valueOf(hk.sectionEn));
            }
            hkOptionDict.put(hk, 0);
        }
        //Log.v("hkSections", housekeepingSections.toString());
    }

    public static class HousekeepingFragment extends Fragment {

        protected RecyclerView.LayoutManager mLayoutManager;


        public HousekeepingFragment() {}

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            //Log.v("selectedSection", section);
            View view = inflater.inflate(R.layout.fragment_housekeeping, container, false);
            RecyclerView recyclerView2 = (RecyclerView) view.findViewById(R.id.housekeeping_list2);
            RecyclerView.LayoutManager parentManager = ((HousekeepingActivity)getActivity()).recyclerView.getLayoutManager();

            mLayoutManager = new LinearLayoutManager(this.getContext());

            //Log.v("Layoutmanager", layoutManagerType.toString());
            recyclerView2.setLayoutManager(mLayoutManager);

            String section = null;
            HashMap hkOptionDict = null;
            Bundle args = getArguments();
            if (args != null) {
                section = args.getString("section");
                hkOptionDict = (HashMap) args.getSerializable("hkOptionDict");
            }
            final HousekeepingOptionAdapter recyclerViewAdapter2 =
                    new HousekeepingOptionAdapter(hkOptionDict, section);
            recyclerView2.setAdapter(recyclerViewAdapter2);
            return view;
        }

        /*
        @Override
        public void onSaveInstanceState(Bundle savedInstanceState) {
            // Save currently selected layout manager.
            savedInstanceState.putSerializable("LinearLayout", layoutManagerType);
            super.onSaveInstanceState(savedInstanceState);
        }*/
    }
}
