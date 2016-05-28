package com.martabak.kamar.activity.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Fragment;
import com.martabak.kamar.R;
import com.martabak.kamar.activity.guest.GuestHomeActivity;
import com.martabak.kamar.activity.staff.StaffHomeActivity;
import com.martabak.kamar.domain.User;
import com.martabak.kamar.service.StaffServer;

import rx.Observer;


public class SelectUserTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_type);

        final Button guestButton = (Button) findViewById(R.id.guest);
        final Button staffButton = (Button) findViewById(R.id.staff);

        if (guestButton != null) {
            guestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences pref = getSharedPreferences("userSettings", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("userType", User.TYPE_GUEST);
                    editor.putString("userPassword", User.PASSWORD_GUEST);
                    editor.commit();

                    Toast.makeText(
                            SelectUserTypeActivity.this,
                            R.string.user_set_to_guest,
                            Toast.LENGTH_LONG
                    ).show();
                    Log.d(SelectUserTypeActivity.class.getCanonicalName(), "Set user to GUEST");

                    startActivity(new Intent(SelectUserTypeActivity.this, GuestHomeActivity.class));
                    finish();
                }
            });
        }
        if (staffButton != null) {
            staffButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences pref = getSharedPreferences("userSettings", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("userType", User.TYPE_STAFF);
                    editor.commit();

                    Toast.makeText(
                            SelectUserTypeActivity.this,
                            R.string.user_set_to_staff,
                            Toast.LENGTH_LONG
                    ).show();
                    Log.d(SelectUserTypeActivity.class.getCanonicalName(), "Set user to STAFF");

                    if (findViewById(R.id.fragment_container) != null) {
                        if (savedInstanceState != null) {
                            return;
                        }
                    }

                    getFragmentManager().beginTransaction().
                            replace(R.id.fragment_container, new StaffTypeFragment()).commit();
                }
            });
        }
    }

    public static class StaffTypeFragment extends Fragment {

        public StaffTypeFragment() {}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_staff_login, container, false);
            Button submitButton = (Button) view.findViewById(R.id.submit);
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    String password = ((EditText)getView().findViewById(R.id.EditTextPassword)).getText().toString();
                    sendPassword(password);
                }
            });
            return view;
        }

        /**
         * Send a password to login.
         * @param password The password.
         */
        public void sendPassword(String password) {
            StaffServer.getInstance(getActivity()).login(password).subscribe(new Observer<Boolean>() {
                boolean loginResult;
                @Override public void onCompleted() {
                    if (loginResult) {
                        Log.d(SelectUserTypeActivity.class.getCanonicalName(), "On completed");

                        final SharedPreferences.Editor editor = getActivity()
                                .getSharedPreferences("userSettings", MODE_PRIVATE)
                                .edit();
                        new AlertDialog.Builder(getActivity())
                                .setMessage(getString(R.string.frontdesk_or_restaurant))
                                .setCancelable(false)
                                .setPositiveButton(getString(R.string.frontdesk), new DialogInterface.OnClickListener() {
                                    @Override public void onClick(DialogInterface dialog, int which) {
                                        editor.putString("subUserType", User.TYPE_STAFF_FRONTDESK)
                                                .commit();

                                        Log.v(SelectUserTypeActivity.class.getCanonicalName(), "userType is " + getActivity().getSharedPreferences("userSettings", MODE_PRIVATE).getString("userType", "none"));
                                        Log.v(SelectUserTypeActivity.class.getCanonicalName(), "subUserType is " + getActivity().getSharedPreferences("userSettings", MODE_PRIVATE).getString("subUserType", "none"));

                                        startActivity(new Intent(getActivity(), StaffHomeActivity.class));
                                        getActivity().finish();
                                    }
                                })
                                .setNegativeButton(getString(R.string.restaurant), new DialogInterface.OnClickListener() {
                                    @Override public void onClick(DialogInterface dialog, int which) {
                                        editor.putString("subUserType", User.TYPE_STAFF_RESTAURANT)
                                                .commit();

                                        Log.v(SelectUserTypeActivity.class.getCanonicalName(), "userType is " + getActivity().getSharedPreferences("userSettings", MODE_PRIVATE).getString("userType", "none"));
                                        Log.v(SelectUserTypeActivity.class.getCanonicalName(), "subUserType is " + getActivity().getSharedPreferences("userSettings", MODE_PRIVATE).getString("subUserType", "none"));

                                        startActivity(new Intent(getActivity(), StaffHomeActivity.class));
                                        getActivity().finish();
                                    }
                                })
                                .create().show();
                    } else {
                        Toast.makeText(
                                getActivity(),
                                getString(R.string.incorrect_password),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
                @Override public void onError(Throwable e) {
                    Log.d(SelectUserTypeActivity.class.getCanonicalName(), "On error");
                    e.printStackTrace();
                    Toast.makeText(
                            getActivity().getApplicationContext(),
                            getString(R.string.something_went_wrong),
                            Toast.LENGTH_SHORT
                    ).show();
                }
                @Override public void onNext(Boolean loginResponse) {
                    Log.d(SelectUserTypeActivity.class.getCanonicalName(), "On next " + loginResponse);
                    loginResult = loginResponse;
                }
            });
        }
    }

}
