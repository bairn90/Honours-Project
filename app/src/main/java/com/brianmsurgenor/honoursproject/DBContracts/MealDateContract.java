package com.brianmsurgenor.honoursproject.DBContracts;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Contract class to prevent having to write numerous SQL statements all the time
 */
public class MealDateContract {

    //Interface to get the column names to be used for any db queries
    public interface Columns {
        String _ID = "_id";
        String MEAL_DATE = "meal_date";
        String MEAL_TIME = "meal_time";
        String MEAL_TYPE = "meal_type";
    }

    public static final String CONTENT_AUTHORITY = "com.brianmsurgenor.honoursproject.provider";
    //Gets creates the base content URI to be built upon to create Uri for this specific table
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    private static final String PATH_ARCHIVES = "meal_date";
    //Uses the table name to build the Uri for this table
    public static final Uri URI_TABLE = BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_ARCHIVES).build();

    public static class MealDate implements Columns, BaseColumns {
        //Content type variable to get all entries from the database; used by the provider
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + ".meal_date";
        //Content type variable to get one entry from the database; used by the provider
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + ".meal_date";

        //Builds the URI for the insert & delete queries for the database
        public static Uri buildMealDateUri(String mealDateID) {
            return URI_TABLE.buildUpon().appendEncodedPath(mealDateID).build();
        }

        /*
         * Uses the Uri to get the path segments to then get the ID required for any queries.
         * Gets 1 as the segments return the table name followed by the ID
         */
        public static String getMealDateID(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}
