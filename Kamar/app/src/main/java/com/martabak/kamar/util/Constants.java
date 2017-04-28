package com.martabak.kamar.util;

import com.amazonaws.regions.Regions;

public class Constants {

    public static final boolean SEND_CRASH_REPORTS = false;

    public static final String TEAM_EMAIL = "rayssoftwarecompany@gmail.com";
    public static final String HOTEL_EMAIL = "rayssoftwarecompany@gmail.com";

    public static final Regions AWS_SES_REGION = Regions.US_EAST_1;
    public static final String AWS_SES_IDENTITY_POOL = "us-east-1:713c594f-d1d4-483b-998d-a5f4e3e28834";

    public static final int MASSAGE_OPENING_TIME = 9; // 9am
    public static final int MASSAGE_CLOSING_TIME = 22; // 10pm

    public static final int STAFF_PERMINTAAN_REFRESH_TIME_IN_SECONDS = 30;
    public static final int GUEST_PERMINTAAN_REFRESH_TIME_IN_SECONDS = 60;
    public static final int STAFF_CHAT_REFRESH_TIME_IN_SECONDS = 30;
    public static final int GUEST_CHAT_REFRESH_TIME_IN_SECONDS = 60;

}
