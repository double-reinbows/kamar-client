package com.martabak.kamar.util;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;

public class CrashReportSender {

    private static SimpleDateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void sendCrashReport(Context c, Throwable e) {
        String stacktrace = getStacktrace(e);
        String appVersion = getAppVersion(c);
        String userType = getUserType(c);
        String userSubType = getUserSubType(c);
        String guestId = getGuestId(c);
        String roomNumber = getRoomNumber(c);
        String body = buildBody(userType, userSubType, guestId, roomNumber, appVersion, stacktrace);
        EmailSender.getInstance(c).sendEmail(
                "Crash Report",
                body,
                Constants.TEAM_EMAIL);
    }

    private static String getStacktrace(Throwable e) {
        final Writer result = new StringWriter();
        try (final PrintWriter printWriter = new PrintWriter(result)) {
            e.printStackTrace(printWriter);
        }
        return result.toString();
    }

    private static String getAppVersion(Context c) {
        try {
            PackageInfo pInfo = c.getPackageManager().getPackageInfo(c.getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "Unknown";
        }
    }

    private static String getUserType(Context c) {
        return getUserSetting(c, "userType");
    }

    private static String getUserSubType(Context c) {
        return getUserSetting(c, "userSubType");
    }

    private static String getGuestId(Context c) {
        return getUserSetting(c, "guestId");
    }

    private static String getRoomNumber(Context c) {
        return getUserSetting(c, "roomNumber");
    }

    private static String getUserSetting(Context c, String key) {
        return c.getSharedPreferences("userSettings", MODE_PRIVATE)
                .getString(key, "none");
    }

    private static String buildBody(
            String userType,
            String userSubType,
            String guestId,
            String roomNumber,
            String appVersion,
            String stacktrace
    ) {
        StringBuilder sb = new StringBuilder();
        sb.append("<b>Time:</b> " + DF.format(new Date()) + " " + TimeZone.getDefault().getDisplayName());
        sb.append("<br/>");
        sb.append("<b>User Type (Sub Type):</b> " + userType + " (" + userSubType + ")");
        sb.append("<br/>");
        sb.append("<b>Guest ID:</b> " + guestId);
        sb.append("<br/>");
        sb.append("<b>Room Number:</b> " + roomNumber);
        sb.append("<br/>");
        sb.append("<b>OS Version:</b> " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")");
        sb.append("<br/>");
        sb.append("<b>OS API Level:</b> " + android.os.Build.VERSION.SDK_INT);
        sb.append("<br/>");
        sb.append("<b>Device:</b> " + android.os.Build.DEVICE);
        sb.append("<br/>");
        sb.append("<b>Model (Product):</b> " + android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ")");
        sb.append("<br/>");
        sb.append("<b>App Version:</b> " + appVersion);
        sb.append("<br/>");
        sb.append("<b>Stack trace:</b>");
        sb.append("<br/>");
        sb.append("<code>");
        sb.append(stacktrace);
        sb.append("</code>");
        return sb.toString();
    }

}
