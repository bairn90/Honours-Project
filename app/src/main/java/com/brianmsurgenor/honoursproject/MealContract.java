package com.brianmsurgenor.honoursproject;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Brian on 30/10/2015.
 */
public class MealContract {

    interface Columns {
        String _ID = "_id";
        String MEAL_TYPE = "meal_type";
        String MEAL_TIME = "meal_time";
        String MEAL_FOOD = "meal_food";
        String MEAL_DRINK = "meal_drink";
        String MEAL_DATE = "meal_date";
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
