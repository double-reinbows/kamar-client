package com.martabak.kamar.domain.permintaan;

import com.martabak.kamar.domain.Staff;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class PermintaanTest {

    // Note:
    // Method name should be of this format:
    // codeUnderTest_scenario_expectedResult()

    Permintaan permintaanCreatedToday;
    Permintaan permintaanCreatedYesterday;

    @Before
    public void setUp() {
        permintaanCreatedToday = new Permintaan(
                Staff.RESP_HOUSEKEEPING,
                "Some Creator",
                Permintaan.TYPE_HOUSEKEEPING,
                "101",
                "guestId123",
                Permintaan.STATE_NEW,
                new Date(),
                null
        );

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        permintaanCreatedYesterday = new Permintaan(
                Staff.RESP_HOUSEKEEPING,
                "Some Creator",
                Permintaan.TYPE_HOUSEKEEPING,
                "101",
                "guestId123",
                Permintaan.STATE_NEW,
                c.getTime(),
                null
        );
    }

    @Test
    public void isOlderThan_todayComparedToDayAgo_returnsFalse() throws Exception {
        assertFalse(permintaanCreatedToday.isOlderThan(-1));
    }

    @Test
    public void isOlderThan_todayComparedToToday_returnsFalse() throws Exception {
        assertFalse(permintaanCreatedToday.isOlderThan(0));
    }

    @Test
    public void isOlderThan_todayComparedToDayInFuture_returnsTrue() throws Exception {
        assertTrue(permintaanCreatedToday.isOlderThan(1));
    }

    @Test
    public void isOlderThan_yesterdayComparedToTwoDaysAgo_returnsFalse() throws Exception {
        assertFalse(permintaanCreatedYesterday.isOlderThan(-2));
    }

    @Test
    public void isOlderThan_yesterdayComparedToDayAgo_returnsFalse() throws Exception {
        assertFalse(permintaanCreatedYesterday.isOlderThan(-1));
    }

    @Test
    public void isOlderThan_yesterdayComparedToToday_returnsTrue() throws Exception {
        assertTrue(permintaanCreatedYesterday.isOlderThan(0));
    }

    @Test
    public void isOlderThan_yesterdayComparedToDayInFuture_returnsTrue() throws Exception {
        assertTrue(permintaanCreatedYesterday.isOlderThan(1));
    }

}