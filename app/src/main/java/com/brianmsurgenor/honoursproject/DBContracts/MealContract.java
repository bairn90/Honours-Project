package com.brianmsurgenor.honoursproject.DBContracts;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Brian on 30/10/2015.
 * Contract class to prevent having to write numerous SQL statements all the time
 */
public class MealContract {

    //Interface to get the column names to be used for any db queries
    public interface Columns {
        String _ID = "_id";
        String MEAL_ID = "meal_type_id";
        String MEAL_ITEM = "meal_food";
        String MEAL_CATEGORY = "meal_category";
    }

    public static final String CONTENT_AUTHORITY = "com.brianmsurgenor.honoursproject.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    private static final String PATH_ARCHIVES = "meal";
    public static final Uri URI_TABLE = BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_ARCHIVES).build();

    public static class Meal implements Columns, BaseColumns {
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + ".meal";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + ".meal";

        public static Uri buildMealUri(String mealID) {
            return URI_TABLE.buildUpon().appendEncodedPath(mealID).build();
        }

        public static String getMealID(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}
