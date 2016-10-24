package com.martabak.kamar.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

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
        String username;
        String password;
        SharedPreferences prefs = context.getSharedPreferences("userSettings", Context.MODE_PRIVATE);

        switch (prefs.getString("userType", "STAFF")) {
            case "GUEST":
                username = "guest";
                password = "guest123";
                break;
            case "STAFF":
                switch (prefs.getString("userSubType", "FRONTDESK")) {
                    case "FRONTDESK":
                        username = "frontdesk";
                        password = "frontdesk123";
                        break;
                    case "RESTAURANT":
                        username = "ic_restaurant";
                        password = "restaurant123";
                        break;
                    default:
                        return "";
                }
                break;
            default:
                return "";
        }

        Log.d(AuthorizationInterceptor.class.getCanonicalName(), username + ":" + password);
        String base64encoded = new String(Base64.encode((username + ":" + password).getBytes(), Base64.DEFAULT));
        return "Basic " + base64encoded.substring(0, base64encoded.length()-1);
    }

}