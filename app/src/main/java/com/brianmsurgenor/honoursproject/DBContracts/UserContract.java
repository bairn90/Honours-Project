package com.brianmsurgenor.honoursproject.DBContracts;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Brian on 30/10/2015.
 * Contract class to prevent having to write numerous SQL statements all the time
 */
public class UserContract {

    //Interface to get the column names to be used for any db queries
    public interface Columns {
        String _ID = "_id";
        String USERNAME = "username";
        String DOB = "dob";
        String GENDER = "gender";
        String HEIGHT = "height";
        String WEIGHT = "weight";
        String BREAKFAST_NOTIF = "breakfast_notif";
        String LUNCH_NOTIF = "lunch_notif";
        String DINNER_NOTIF = "dinner_notif ";
        String PET_NAME = "pet_name";
        String PET_TYPE = "pet_type";
        String CUSTOM_COLOUR = "customer_colour";
    }

    public static final String CONTENT_AUTHORITY = "com.brianmsurgenor.honoursproject.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    private static final String PATH_ARCHIVES = "user";
    public static final Uri URI_TABLE = BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_ARCHIVES).build();

    public static class User implements Columns, BaseColumns {
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + ".user";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + ".user";

        public static Uri buildUserUri(String userID) {
            return URI_TABLE.buildUpon().appendEncodedPath(userID).build();
        }

        public static String getUserID(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}
