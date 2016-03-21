package com.martabak.kamar.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.martabak.kamar.R;

public class GuestHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_home);
        final ImageAdapter imgAdapter = new ImageAdapter(this);
        final GridView gridView = (GridView) findViewById(R.id.guestgridview);
        gridView.setAdapter(imgAdapter);

        //display feature text on each item click
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(GuestHomeActivity.this, "" + imgAdapter.getItem(position),
                        Toast.LENGTH_SHORT).show();
            }
        });


    }

}

