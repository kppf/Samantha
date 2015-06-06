package com.alkeste.app.voice.util;

import android.app.Activity;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Kushagra on 05-06-2015.
 *
 * @class Util has utility functions for the app
 */
public class Util {
    public static String getOwnerName(Activity activity) {
        final String[] SELF_PROJECTION = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,};
        Cursor cursor = activity.getContentResolver().query(
                ContactsContract.Profile.CONTENT_URI, SELF_PROJECTION, null, null, null);
        cursor.moveToFirst();
        return cursor.getString(0);
    }

    public static String getCurrentTimeAsString() {
        String timeString;
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm a");
        date.setTimeZone(TimeZone.getDefault());
        timeString = date.format(currentLocalTime);
        return timeString;
    }
}
