package com.martabak.kamar.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.martabak.kamar.R;


public class SelectUserTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_type);

        final Button guestButton = (Button) findViewById(R.id.guest);
        final Button staffButton = (Button) findViewById(R.id.staff);

        guestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUserType("guest");
                displayUserTypeToast();
                Log.d(SelectUserTypeActivity.class.getCanonicalName(), "Set user to Guest");

                SharedPreferences pref = getSharedPreferences("userSettings", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("userType", "GUEST");
                editor.putString("userPassword", "guest123");
                editor.commit();

                switchActivity();
            }
        });
        staffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUserType("STAFF");
                displayUserTypeToast();
                Log.d(SelectUserTypeActivity.class.getCanonicalName(), "Set user to Staff");

                SharedPreferences pref = getSharedPreferences("userSettings", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("userType", "STAFF");
                editor.commit();

                switchActivity();
            }
        });
    }

    /**
     * Set the user type.
     *
     * @param userType The user type, either "guest" or "staff".
     */
    private void setUserType(String userType) {
        SharedPreferences languagePref = getSharedPreferences("userSettings", MODE_PRIVATE);
        SharedPreferences.Editor editor = languagePref.edit().
                putString("userType", userType);
        editor.commit();
    }

    /**
     * Display a toast after user type set.
     */
    private void displayUserTypeToast() {
        Context context = getApplicationContext();
        String text = getResources().getString(R.string.user_set_to) + " ";
        String userType = getSharedPreferences("userSettings", MODE_PRIVATE).getString("userType", "none");
        switch (userType) {
            case "STAFF":
                text += getResources().getString(R.string.staff);
                break;
            case "guest":
                text += getResources().getString(R.string.guest);
                break;
            default:
                text += "none";
                break;
        }
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }



    /**
     * Switch activity.
     */
    private void switchActivity() {
        Intent intent;
        String userType = getSharedPreferences("userSettings", MODE_PRIVATE).getString("userType", "none");
        switch (userType) {
            case "STAFF":
                intent = new Intent(this, StaffHomeActivity.class);
                startActivity(intent);
                break;
            case "guest":
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }


    }

}
