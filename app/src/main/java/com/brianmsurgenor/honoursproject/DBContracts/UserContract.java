package com.brianmsurgenor.honoursproject.DBContracts;

import android.net.Uri;
import android.provider.BaseColumns;

/**
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
        String PET_NAME = "pet_name";
        String PET_TYPE = "pet_type";
        String CUSTOM_COLOUR = "customer_colour";
    }

    public static final String CONTENT_AUTHORITY = "com.brianmsurgenor.honoursproject.provider";
    //Gets creates the base content URI to be built upon to create Uri for this specific table
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    private static final String PATH_ARCHIVES = "user";
    //Uses the table name to build the Uri for this table
    public static final Uri URI_TABLE = BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_ARCHIVES).build();

    public static class User implements Columns, BaseColumns {
        //Content type variable to get all entries from the database; used by the provider
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + ".user";
        //Content type variable to get one entry from the database; used by the provider
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + ".user";

        //Builds the URI for the insert & delete queries for the database
        public static Uri buildUserUri(String userID) {
            return URI_TABLE.buildUpon().appendEncodedPath(userID).build();
        }

        /*
         * Uses the Uri to get the path segments to then get the ID required for any queries.
         * Gets 1 as the segments return the table name followed by the ID
         */
        public static String getUserID(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}
