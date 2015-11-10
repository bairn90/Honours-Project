package com.brianmsurgenor.honoursproject;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Brian on 09/11/2015.
 */
public class TrophyContract {

    interface Columns {
        String _ID = "_id";
        String TROPHY_NAME = "trophy_name";
        String TROPHY_DESCRIPTION = "trophy_description";
        String ACHIEVED = "achieved";
    }

    public static final String CONTENT_AUTHORITY = "com.brianmsurgenor.honoursproject.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    private static final String PATH_ARCHIVES = "trophies";
    public static final Uri URI_TABLE = BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_ARCHIVES).build();

    public static class Trophy implements Columns, BaseColumns {
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + ".trophies";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + ".trophies";

        public static Uri buildTrophyUri(String trophyID) {
            return URI_TABLE.buildUpon().appendEncodedPath(trophyID).build();
        }

        public static String getTrophyID(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}
