package com.martabak.kamar.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.martabak.kamar.domain.User;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Injects a Basic header into all requests, containing the user's details if existing.
 */
public class AuthorizationInterceptor implements Interceptor {

    private Context context;

    public AuthorizationInterceptor(Context c) {
        this.context = c;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder requestBuilder = original.newBuilder()
                .header("Accept", "application/json")
                .header("Authorization", getBasicString())
                .method(original.method(), original.body());

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }

    /**
     * @return The user's Basic auth base64 encoded string.
     */
    private String getBasicString() {
        SharedPreferences prefs = context.getSharedPreferences("userSettings", Context.MODE_PRIVATE);
        String username;
        String password;

        switch (prefs.getString("userType", User.TYPE_GUEST)) {
            case User.TYPE_GUEST:
                username = "guest";
                password = prefs.getString("userPassword", User.PASSWORD_GUEST);
                break;
            case User.TYPE_STAFF:
                username = prefs.getString("userSubType", "");
                switch (username.toUpperCase()) {
                    case User.TYPE_STAFF_FRONTDESK:
                        password = User.PASSWORD_FRONTDESK;
                        break;
                    case User.TYPE_STAFF_RESTAURANT:
                        password = User.PASSWORD_RESTAURANT;
                        break;
                    case User.TYPE_STAFF_SPA:
                        password = User.PASSWORD_SPA;
                        break;
                    default:
                        return "";
                }
                break;
            default:
                return "";
        }

//        Log.d(AuthorizationInterceptor.class.getCanonicalName(), username + ":" + password);
        String base64encoded = new String(Base64.encode((username + ":" + password).getBytes(), Base64.DEFAULT));
        return "Basic " + base64encoded.substring(0, base64encoded.length()-1);
    }

}