package com.martabak.kamar.activity.staff;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.martabak.kamar.R;
import com.martabak.kamar.activity.chat.ChatListActivity;

public class StaffHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.staff_toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(new NavigationViewListener());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        getFragmentManager().beginTransaction()
                .replace(R.id.staff_container, StaffPermintaanFragment.newInstance()).commit();
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.staff_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                    Log.v(StaffHomeActivity.class.toString(), "Going to chat activity for staff");
                    startActivity(new Intent(StaffHomeActivity.this, ChatListActivity.class));
                    break;
                case R.id.nav_check_guest_in:
                    Log.v(StaffHomeActivity.class.toString(), "Loading check-guest-in fragment");
                    getFragmentManager().beginTransaction()
                            .replace(R.id.staff_container, CheckGuestInFragment.newInstance()).commit();
                    break;
                case R.id.nav_check_guest_out:
                    Log.v(StaffHomeActivity.class.toString(), "Loading check-guest-out fragment");
                    getFragmentManager().beginTransaction()
                            .replace(R.id.staff_container, CheckGuestOutFragment.newInstance()).commit();
                    break;
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
    }

}
