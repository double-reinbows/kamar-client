package com.martabak.kamar.activity.staff;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.martabak.kamar.R;
import com.martabak.kamar.activity.chat.StaffChatFragment;
import com.martabak.kamar.activity.chat.StaffChatService;
import com.martabak.kamar.activity.guest.PermintaanDialogListener;
import com.martabak.kamar.activity.home.SplashScreenActivity;
import com.martabak.kamar.activity.restaurant.RestaurantActivity;
import com.martabak.kamar.domain.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StaffHomeActivity extends AbstractStaffBarsActivity
        implements PermintaanDialogListener {

    String staffType;

    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.toolbar) Toolbar toolbar;

    protected AbstractStaffBarsActivity.Options getOptions() {
        return new AbstractStaffBarsActivity.Options()
                .withBaseLayout(R.layout.activity_staff_home)
                .enableChatIcon(true);
    }

    // on click bindings for the guest home activity
    @OnClick(R.id.refresh_icon)
    public void onRefreshIconClick() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        staffType = getSharedPreferences("userSettings", MODE_PRIVATE).getString("userSubType", "none");
        startStaffServices(staffType);
        setContentView(R.layout.activity_staff_home);
        ButterKnife.bind(this);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(new NavigationViewListener());
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        /*
        ImageView staffImageView = (ImageView) findViewById(R.id.staff_image);
        if (staffImageView != null) {
            int staffImage;
            switch (staffType) {
                case Permintaan.OWNER_FRONTDESK:
                    staffImage = R.drawable.hotel_information;
                    break;
                case Permintaan.OWNER_RESTAURANT:
                    staffImage = R.drawable.ic_restaurant;
                    break;
                default:
                    staffImage = R.drawable.question_mark;
            }
            Server.picasso(this)
                    .load(staffImage)
                    .into(staffImageView);
        }*/
        TextView staffTitleView = (TextView) findViewById(R.id.staff_title);
        if (staffTitleView != null) staffTitleView.setText(staffType);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        String fragType = getIntent().getStringExtra("FragType");
        String restaurantResult = getIntent().getStringExtra("RestaurantResult");

        //open chat when staff home entered from notification
        if (fragType != null) {
            switch (fragType) {
                case "StaffChatFragment":
                    Bundle staffChatBundle = new Bundle();
                    String selectedChatGuestId = getIntent().getStringExtra("GuestId");
                    staffChatBundle.putString("SelectedChatGuestId", selectedChatGuestId);
                    Log.d(StaffHomeActivity.class.getCanonicalName(), "Loading chat for guest ID " + selectedChatGuestId);
                    StaffChatFragment staffChatFragment = StaffChatFragment.newInstance();
                    staffChatFragment.setArguments(staffChatBundle);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.staff_container, staffChatFragment)
                            .commit();

            }
        } else if (restaurantResult != null) {
            switch (restaurantResult) {
                case "Success":
                    makeSnackBar();
            }
            getFragmentManager().beginTransaction()
                    .add(R.id.staff_container, StaffPermintaanFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
            navigationView.getMenu().getItem(0).setChecked(true);
        } else {
            getFragmentManager().beginTransaction()
                    .add(R.id.staff_container, StaffPermintaanFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
            navigationView.getMenu().getItem(0).setChecked(true);
        }

        hideNavMenu(navigationView);
    }

    private void hideNavMenu(NavigationView navigationView) {
        Menu navMenu = navigationView.getMenu();
        if (!staffType.equalsIgnoreCase(User.TYPE_STAFF_FRONTDESK)) {
            navMenu.findItem(R.id.nav_check_guest_in).setVisible(false);
            navMenu.findItem(R.id.nav_check_guest_out).setVisible(false);
        }
        if (!staffType.equalsIgnoreCase(User.TYPE_STAFF_RESTAURANT)) {
            navMenu.findItem(R.id.nav_edit_restaurant).setVisible(false);
        }
    }

    @Override
    public void onPause() {
        stopStaffServices();
        super.onPause();
    }

    /*
     * On Back pressed don't exit the activity
     */
    @Override
    public void onBackPressed() {}

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, Boolean success) {
        dialog.dismiss();
        getFragmentManager().beginTransaction()
                .replace(R.id.staff_container, StaffPermintaanFragment.newInstance())
                .addToBackStack(null)
                .commit();
        // create snackbar for bellboy
        makeSnackBar();

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
    }

    class NavigationViewListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    Log.v(StaffHomeActivity.class.toString(), "Loading staff requests fragment");
                    getFragmentManager().beginTransaction()
                            .replace(R.id.staff_container, StaffPermintaanFragment.newInstance()).commit();
                    break;
                case R.id.nav_chat:
                    Log.v(StaffHomeActivity.class.toString(), "Going to staff chat fragment");
                    getFragmentManager().beginTransaction()
                            .replace(R.id.staff_container, StaffChatFragment.newInstance())
                            .commit();
                    break;
                case R.id.nav_check_guest_in:
                    Log.v(StaffHomeActivity.class.toString(), "Loading check-guest-in fragment");
                    getFragmentManager().beginTransaction()
                            .replace(R.id.staff_container, CheckGuestInFragment.newInstance())
                            .addToBackStack(null)
                            .commit();
                    break;
                case R.id.nav_check_guest_out:
                    Log.v(StaffHomeActivity.class.toString(), "Loading check-guest-out fragment");
                    getFragmentManager().beginTransaction()
                            .replace(R.id.staff_container, CheckGuestOutFragment.newInstance())
                            .addToBackStack(null)
                            .commit();
                    break;
                case R.id.nav_create_permintaan:
                    Log.v(StaffHomeActivity.class.toString(), "Loading create permintaan fragment");
                    getFragmentManager().beginTransaction()
                            .replace(R.id.staff_container, CreatePermintaanFragment.newInstance())
                            .addToBackStack(null)
                            .commit();
                    break;
//                case R.id.nav_staff_hours:
//                    Log.v(StaffHomeActivity.class.toString(), "Loading staff hours fragment");
//                    getFragmentManager().beginTransaction()
//                            .replace(R.id.staff_container, StaffHoursFragment.newInstance())
//                            .addToBackStack(null)
//                            .commit();
//                    break;
                case R.id.nav_edit_restaurant:
                    Log.v(StaffHomeActivity.class.toString(), "Loading edit restaurant fragment");
                    startActivityForResult(new Intent(StaffHomeActivity.this, RestaurantActivity.class), 1);
                    break;
                case R.id.nav_permintaan_report:
                    Log.v(StaffHomeActivity.class.toString(), "Creating permintaan report");
                    getFragmentManager().beginTransaction()
                            .replace(R.id.staff_container, PermintaanReportFragment.newInstance())
                            .addToBackStack(null)
                            .commit();
                    break;
                case R.id.nav_logout:
                    stopStaffServices();
                    Log.v(StaffHomeActivity.class.toString(), "Loading splash screen activity");
                    finishAffinity();
                    startActivity(new Intent(StaffHomeActivity.this, SplashScreenActivity.class));
                    stopStaffServices();
                    finish();
                    break;
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
    }

    /**
     * Make snackbar for requests unable to be process by create permintaan fragment
     */
    private void makeSnackBar() {
        Snackbar.make(this.getWindow().getDecorView()
                        .findViewById(android.R.id.content),
                R.string.request_success,Snackbar.LENGTH_LONG).
                setAction(R.string.positive, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getFragmentManager().beginTransaction()
                                .replace(R.id.staff_container, CreatePermintaanFragment.newInstance())
                                .addToBackStack(null)
                                .commit();
                    }
                })
                .show();
    }

    /**
     * Start any relevant staff services.
     */
    private void startStaffServices(String userSubType) {
        Log.v(StaffHomeActivity.class.getCanonicalName(), "Starting " + StaffPermintaanService.class.getCanonicalName() + " as " + userSubType);
        startService(new Intent(this, StaffPermintaanService.class)
                .putExtra("subUserType", userSubType));
        Log.v(StaffHomeActivity.class.getCanonicalName(), "Starting " + StaffChatService.class.getCanonicalName() + " as " + userSubType);
        startService(new Intent(this, StaffChatService.class));
    }


    /**
     * Stop any relevant staff services.
     */
    private void stopStaffServices() {
        Log.v(StaffHomeActivity.class.getCanonicalName(), "Stopping " + StaffPermintaanService.class.getCanonicalName());
        stopService(new Intent(this, StaffPermintaanService.class));
        Log.v(StaffHomeActivity.class.getCanonicalName(), "Stopping " + StaffChatService.class.getCanonicalName());
        stopService(new Intent(this, StaffChatService.class));
    }

}
