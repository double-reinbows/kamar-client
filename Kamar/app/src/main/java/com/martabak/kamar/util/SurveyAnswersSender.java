package com.martabak.kamar.util;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.martabak.kamar.domain.SurveyAnswer;
import com.martabak.kamar.domain.SurveyAnswers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.String.format;

public class SurveyAnswersSender {

    private static SimpleDateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void sendAnswers(Context c, SurveyAnswers answers) {
        String roomNumber = getRoomNumber(c);
        String body = buildBody(roomNumber, answers);
        EmailSender.getInstance(c).sendEmail(
                "Survey Answers",
                body,
                Constants.HOTEL_EMAIL);
    }

    private static String getRoomNumber(Context c) {
        return getUserSetting(c, "roomNumber");
    }

    private static String getUserSetting(Context c, String key) {
        return c.getSharedPreferences("userSettings", MODE_PRIVATE)
                .getString(key, "none");
    }

    private static String buildBody(
            String roomNumber,
            SurveyAnswers answers
    ) {
        StringBuilder sb = new StringBuilder();
        sb.append("<b>Time:</b> " + DF.format(new Date()) + " " + TimeZone.getDefault().getDisplayName());
        sb.append("<br/>");
        sb.append("<b>Room Number:</b> " + roomNumber);
        sb.append("<br/>");
        sb.append("<b>Survey Answers:</b>");
        sb.append("<br/>");
        float averageRating = 0;
        for (SurveyAnswer a : answers.answers) {
            sb.append(format("&nbsp;&nbsp;&nbsp;&nbsp;%s: %d", a.question, a.rating));
            sb.append("<br/>");
            averageRating += a.rating;
        }
        averageRating /= answers.answers.size();
        sb.append(format("<b>Average Rating:</b> %.2f", averageRating));
        sb.append("<br/>");
        return sb.toString();
    }

}
