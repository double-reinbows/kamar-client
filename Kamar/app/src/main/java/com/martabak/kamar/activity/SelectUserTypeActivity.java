package com.martabak.kamar.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
//import android.support.v7.app.AppCompatActivity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;
import android.app.Fragment;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.martabak.kamar.R;
import com.martabak.kamar.domain.Guest;
import com.martabak.kamar.service.StaffServer;


import retrofit2.http.Field;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action1;


public class SelectUserTypeActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private StaffTypeFragment staffTypeFragment;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_type);

        final Button guestButton = (Button) findViewById(R.id.guest);
        final Button staffButton = (Button) findViewById(R.id.staff);

        guestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUserType("GUEST");
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

                //Check that the current layout has "fragment_container" id in it
                if (findViewById(R.id.fragment_container) != null) {
                    //Check that the fragment isn't being created via the Back button
                    //because otherwise we could end up with double fragments
                    if (savedInstanceState != null) {
                        return;
                    }
                }

                //Create a new fragment
                staffTypeFragment = new StaffTypeFragment();

                //Start up a FragmentManager and then replace "fragment_container" inside the layout
                //with your fresh StaffTypeFragment
                getFragmentManager().beginTransaction().
                        replace(R.id.fragment_container, staffTypeFragment).commit();


                Log.d(SelectUserTypeActivity.class.getCanonicalName(), "Set user to Staff");

                SharedPreferences pref = getSharedPreferences("userSettings", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("userType", "STAFF");
                editor.commit();

                //switchActivity();

            }
        });
    }

    public void sendPassword(View v) {
        staffTypeFragment.sendPassword();
    }

    public class StaffTypeFragment extends Fragment {
        public StaffTypeFragment() {}
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_staff_login, container, false);
        }
        public void sendPassword() {
            //Cast view to EditText and then to a String
            String passwordString   = ((EditText)findViewById(R.id.EditTextPassword)).getText().toString();
            Log.v("Password String", passwordString);
            StaffServer.getInstance(getBaseContext()).login(passwordString).subscribe(new Observer<Boolean>() {
                @Override
                public void onCompleted() {
                    Log.d(SelectUserTypeActivity.class.getCanonicalName(), "On completed");
                }

                @Override
                public void onError(Throwable e) {
                    Log.d(SelectUserTypeActivity.class.getCanonicalName(), "On error");
                    //TextView textView = (TextView) findViewById(R.id.doSomethingText);
                    //textView.setText(e.getMessage());
                    e.printStackTrace();
                }

                @Override
                public void onNext(Boolean loginResponse) {
                    Log.d(SelectUserTypeActivity.class.getCanonicalName(), "On next");
                    Log.v("loginResponse", loginResponse.toString());
                    if (loginResponse) {
                        //TODO
                        //Switch to StaffMainActivity
                    } else {
                        Context context = getApplicationContext();
                        String text = getResources().getString(R.string.incorrect_password) + " ";
                        Toast toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            });


        }
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
            case "GUEST":
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
            case "GUEST":
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }


    }

}
