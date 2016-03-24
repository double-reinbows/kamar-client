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


public class SelectUserTypeActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
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
                setUserType("guest");
                displayUserTypeToast();
                Log.d(SelectUserTypeActivity.class.getCanonicalName(), "Set user to Guest");

                SharedPreferences pref = getSharedPreferences("userSettings", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("userType", "guest");
                editor.putString("userPassword", "guest123");
                editor.commit();

                switchActivity();
            }
        });
        staffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUserType("staff");
                //displayUserTypeToast();
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

                //final Button submitButton = (Button) findViewById(R.id.submit);


                Log.d(SelectUserTypeActivity.class.getCanonicalName(), "Set user to Staff");

                SharedPreferences pref = getSharedPreferences("userSettings", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("userType", "staff");
                editor.commit();

                //switchActivity();

            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void sendPassword(View v) {
        staffTypeFragment.sendPassword(v);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "SelectUserType Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.martabak.kamar.activity/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "SelectUserType Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.martabak.kamar.activity/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    public class StaffTypeFragment extends Fragment {
        public StaffTypeFragment() {}
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_staff_login, container, false);
        }
        public void sendPassword(View v) {
            //staffTypeFragment.sendPassword(v);
            EditText passwordString   = (EditText) findViewById(R.id.EditTextPassword);
            Log.v("Password String", passwordString.getText().toString());
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
            case "staff":
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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
