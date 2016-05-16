package com.martabak.kamar.activity.staff;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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


        final FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //Create sample permintaan fragment
        StaffPermintaanFragment fragment = StaffPermintaanFragment.newInstance();

        //Place the permintaan fragment inside the staff_container/content section of the layout
        fragmentTransaction.replace(R.id.staff_container, fragment);
        fragmentTransaction.commit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if (id == R.id.nav_home) {
                    // Handle the home click

                } else if (id == R.id.nav_chat) {
                    //Handle the chat click

                    Log.v(StaffHomeActivity.class.toString(), "Going to chat activity for staff");
                    startActivity(new Intent(StaffHomeActivity.this, ChatListActivity.class));
                } else if (id == R.id.nav_check_guest_in) {
                    //Handle the check guest in click
                    Log.v(StaffHomeActivity.class.toString(), "CheckGuestInFragment");
                    CheckGuestInFragment fragment = CheckGuestInFragment.newInstance();
                    fragmentTransaction.replace(R.id.staff_container, fragment);
                    fragmentTransaction.commit();


                } else if (id == R.id.nav_check_guest_out) {
                    //Handle the check guest out click
                    CheckGuestOutFragment fragment = CheckGuestOutFragment.newInstance();
                    fragmentTransaction.replace(R.id.staff_container, fragment);
                    fragmentTransaction.commit();



                } else if (id == R.id.nav_set_welcome_image) {
                    //Handle the set welcome image click

                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }

        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close){

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
            }

        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

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


}
