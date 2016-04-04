package com.brianmsurgenor.honoursproject.Database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

import com.brianmsurgenor.honoursproject.DBContracts.MealContract;
import com.brianmsurgenor.honoursproject.DBContracts.MealDateContract;
import com.brianmsurgenor.honoursproject.DBContracts.ExerciseContract;
import com.brianmsurgenor.honoursproject.DBContracts.TrophyContract;
import com.brianmsurgenor.honoursproject.DBContracts.UserContract;

/**
 * Created by Brian on 30/10/2015.
 * Controls all the interactions with the database
 */
public class DBProvider extends ContentProvider {

    protected AppDatabase mOpenHelper;
    private static final UriMatcher mUriMatcher = buildUriMatcher();

    /*
     * All below vars are unique ID's to refer to the items in the databasse
     */
    private static final int USER = 100;
    private static final int USER_ID = 101;

    private static final int TROPHY = 200;
    private static final int TROPHY_ID = 201;

    private static final int MEAL_DATE = 300;
    private static final int MEAL_DATE_ID = 301;

    private static final int MEAL = 400;
    private static final int MEAL_ID = 401;

    private static final int PEDOMETER = 500;
    private static final int PEDOMETER_ID = 501;


    /**
     * Sets up the URI's for each database table using the unique ID's above
     * @return
     */
    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        String authority = UserContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, "user", USER);
        matcher.addURI(authority, "user/*", USER_ID);

        authority = TrophyContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, "trophies", TROPHY);
        matcher.addURI(authority, "trophies/*", TROPHY_ID);

        authority = MealDateContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, "meal_date", MEAL_DATE);
        matcher.addURI(authority, "meal_date/*", MEAL_DATE_ID);

        authority = MealContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, "meal", MEAL);
        matcher.addURI(authority, "meal/*", MEAL_ID);

        authority = ExerciseContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, "pedometer", PEDOMETER);
        matcher.addURI(authority, "pedometer/*", PEDOMETER_ID);

        return matcher;
    }

    //Deletes the database and makes call to create a new one
    private void deleteDatabase() {
        mOpenHelper.close();
        AppDatabase.deleteDatabase(getContext());
        mOpenHelper = new AppDatabase(getContext());
    }

    /**
     * Sets up a new database on the first create
     */
    @Override
    public boolean onCreate() {
        mOpenHelper = new AppDatabase(getContext());
        return true;
    }

    /**
     * @param uri
     * @return the MIME type of the data from the given URI
     */
    @Override
    public String getType(Uri uri) {
        final int match = mUriMatcher.match(uri);

        switch (match) {
            case USER:
                return UserContract.User.CONTENT_TYPE;
            case USER_ID:
                return UserContract.User.CONTENT_ITEM_TYPE;
            case TROPHY:
                return TrophyContract.Trophy.CONTENT_TYPE;
            case TROPHY_ID:
                return TrophyContract.Trophy.CONTENT_ITEM_TYPE;
            case MEAL_DATE:
                return MealDateContract.MealDate.CONTENT_TYPE;
            case MEAL_DATE_ID:
                return MealDateContract.MealDate.CONTENT_ITEM_TYPE;
            case MEAL:
                return MealContract.Meal.CONTENT_TYPE;
            case MEAL_ID:
                return MealContract.Meal.CONTENT_ITEM_TYPE;
            case PEDOMETER:
                return ExerciseContract.Pedometer.CONTENT_TYPE;
            case PEDOMETER_ID:
                return ExerciseContract.Pedometer.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
    }

    /**
     * Queries the database for the given projection
     * @param uri
     * @param projection collumns to return
     * @param selection the where clause
     * @param selectionArgs the where clause subject
     * @param sortOrder
     * @return the data that has been retrieved from the database
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        final int match = mUriMatcher.match(uri);
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (match) {
            case USER:
                queryBuilder.setTables(AppDatabase.Tables.USER);
                break;
            case USER_ID:
                queryBuilder.setTables(AppDatabase.Tables.USER);
                String userID = UserContract.User.getUserID(uri);
                queryBuilder.appendWhere(BaseColumns._ID + "=" + userID);
                break;

            case TROPHY:
                queryBuilder.setTables(AppDatabase.Tables.TROPHIES);
                break;
            case TROPHY_ID:
                queryBuilder.setTables(AppDatabase.Tables.TROPHIES);
                String trophyID = TrophyContract.Trophy.getTrophyID(uri);
                queryBuilder.appendWhere(BaseColumns._ID + "=" + trophyID);
                break;

            case MEAL_DATE:
                queryBuilder.setTables(AppDatabase.Tables.MEAL_DATE);
                break;
            case MEAL_DATE_ID:
                queryBuilder.setTables(AppDatabase.Tables.MEAL_DATE);
                String mealDateID = MealDateContract.MealDate.getMealDateID(uri);
                queryBuilder.appendWhere(BaseColumns._ID + "=" + mealDateID);
                break;

            case MEAL:
                queryBuilder.setTables(AppDatabase.Tables.MEAL);
                break;
            case MEAL_ID:
                queryBuilder.setTables(AppDatabase.Tables.MEAL);
                String mealID = MealContract.Meal.getMealID(uri);
                queryBuilder.appendWhere(BaseColumns._ID + "=" + mealID);
                break;

            case PEDOMETER:
                queryBuilder.setTables(AppDatabase.Tables.EXERCISE);
                break;
            case PEDOMETER_ID:
                queryBuilder.setTables(AppDatabase.Tables.EXERCISE);
                String pedometerID = ExerciseContract.Pedometer.getPedometerID(uri);
                queryBuilder.appendWhere(BaseColumns._ID + "=" + pedometerID);
                break;

            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);

        }

        return queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    }

    /**
     * Takes in the URI of the table and then inserts the given values in to the database
     * @param uri
     * @param values the values to be inserted into the database
     * @return
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);

        switch(match) {
            case USER:
                long lRecordID = db.insertOrThrow(AppDatabase.Tables.USER,null,values);
                return UserContract.User.buildUserUri(String.valueOf(lRecordID));
            case TROPHY:
                long tRecordID = db.insertOrThrow(AppDatabase.Tables.TROPHIES,null,values);
                return TrophyContract.Trophy.buildTrophyUri(String.valueOf(tRecordID));
            case MEAL_DATE:
                long dRecordID = db.insertOrThrow(AppDatabase.Tables.MEAL_DATE,null,values);
                return MealDateContract.MealDate.buildMealDateUri(String.valueOf(dRecordID));
            case MEAL:
                long mRecordID = db.insertOrThrow(AppDatabase.Tables.MEAL,null,values);
                return MealContract.Meal.buildMealUri(String.valueOf(mRecordID));
            case PEDOMETER:
                long pRecordID = db.insertOrThrow(AppDatabase.Tables.EXERCISE,null,values);
                return ExerciseContract.Pedometer.buildPedometerUri(String.valueOf(pRecordID));
            default:
                throw new IllegalArgumentException("Unknown uri " + uri);
        }
    }

    /**
     * Takes in the URI of the table being updated and updates the values
     * @param uri
     * @param values the values to be updated dependent on the where
     * @param selection where clause
     * @param selectionArgs where clause subject
     * @return
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);

        String selectionCriteria = selection;
        switch (match) {
            case USER:
                return db.update(AppDatabase.Tables.USER, values, selection, selectionArgs);
            case USER_ID:
                String userID = UserContract.User.getUserID(uri);
                selectionCriteria = BaseColumns._ID + "=" + userID
                        + (!TextUtils.isEmpty(selection) ? "AND (" + selection + ")" : "");
                return db.update(AppDatabase.Tables.USER, values, selectionCriteria, selectionArgs);

            case TROPHY:
                return db.update(AppDatabase.Tables.TROPHIES, values, selection, selectionArgs);
            case TROPHY_ID:
                String trophyID = TrophyContract.Trophy.getTrophyID(uri);
                selectionCriteria = BaseColumns._ID + "=" + trophyID
                        + (!TextUtils.isEmpty(selection) ? "AND (" + selection + ")" : "");
                return db.update(AppDatabase.Tables.TROPHIES, values, selectionCriteria, selectionArgs);

            case MEAL_DATE:
                return db.update(AppDatabase.Tables.MEAL_DATE, values, selection, selectionArgs);
            case MEAL_DATE_ID:
                String mealDateID = MealDateContract.MealDate.getMealDateID(uri);
                selectionCriteria = BaseColumns._ID + "=" + mealDateID
                        + (!TextUtils.isEmpty(selection) ? "AND (" + selection + ")" : "");
                return db.update(AppDatabase.Tables.MEAL_DATE, values, selectionCriteria, selectionArgs);

            case MEAL:
                return db.update(AppDatabase.Tables.MEAL, values, selection, selectionArgs);
            case MEAL_ID:
                String mealID = MealContract.Meal.getMealID(uri);
                selectionCriteria = BaseColumns._ID + "=" + mealID
                        + (!TextUtils.isEmpty(selection) ? "AND (" + selection + ")" : "");
                return db.update(AppDatabase.Tables.MEAL, values, selectionCriteria, selectionArgs);

            case PEDOMETER:
                return db.update(AppDatabase.Tables.EXERCISE, values, selection, selectionArgs);
            case PEDOMETER_ID:
                String pedometerID = ExerciseContract.Pedometer.getPedometerID(uri);
                selectionCriteria = BaseColumns._ID + "=" + pedometerID
                        + (!TextUtils.isEmpty(selection) ? "AND (" + selection + ")" : "");
                return db.update(AppDatabase.Tables.EXERCISE, values, selectionCriteria, selectionArgs);

            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
    }

    /**
     * Takes in the URI of the table where a row is to be deleted and deletes where requested
     * Entrie database deleted with UserContract URI and 2x null for selections
     * @param uri
     * @param selection where clause
     * @param selectionArgs where clause subject
     * @return
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);

        /*
         * When only the usercontract URi is passed the selection == null then delete the entire
         * database as no selection clause made to delete specific row
         */
        if(uri.equals(UserContract.URI_TABLE) && selection == null) {
            deleteDatabase();
            return 0;
        }

        String selectionCriteria = selection;
        switch (match) {
            case USER_ID:
                String userID = UserContract.User.getUserID(uri);
                selectionCriteria = BaseColumns._ID + "=" + userID
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
                return db.delete(AppDatabase.Tables.USER, selectionCriteria, selectionArgs);

            case TROPHY_ID:
                String trophyID = TrophyContract.Trophy.getTrophyID(uri);
                selectionCriteria = BaseColumns._ID + "=" + trophyID
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
                return db.delete(AppDatabase.Tables.USER, selectionCriteria, selectionArgs);

            case MEAL_DATE_ID:
                String mealDateID = MealDateContract.MealDate.getMealDateID(uri);
                selectionCriteria = BaseColumns._ID + "=" + mealDateID
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
                return db.delete(AppDatabase.Tables.MEAL_DATE, selectionCriteria, selectionArgs);

            case MEAL_ID:
                String mealID = MealContract.Meal.getMealID(uri);
                selectionCriteria = MealContract.Columns.MEAL_ID + "=" + mealID
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
                return db.delete(AppDatabase.Tables.MEAL, selectionCriteria, selectionArgs);

            case PEDOMETER_ID:
                String pedometerID = ExerciseContract.Pedometer.getPedometerID(uri);
                selectionCriteria = BaseColumns._ID + "=" + pedometerID
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
                return db.delete(AppDatabase.Tables.EXERCISE, selectionCriteria, selectionArgs);

            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
    }
}
