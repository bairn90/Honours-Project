package com.brianmsurgenor.honoursproject;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

/**
 * Created by Brian on 30/10/2015.
 */
public class DBProvider extends ContentProvider {

    protected AppDatabase mOpenHelper;
    private static final UriMatcher mUriMatcher = buildUriMatcher();

    private static final int USER = 100;
    private static final int USER_ID = 101;

    private static final int MEAL_DATE = 300;
    private static final int MEAL_DATE_ID = 301;

    private static final int MEAL = 400;
    private static final int MEAL_ID = 401;


    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        String authority = UserContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, "user", USER);
        matcher.addURI(authority, "user/*", USER_ID);

        authority = MealDateContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, "meal_date", MEAL_DATE);
        matcher.addURI(authority, "meal_date/*", MEAL_DATE_ID);

        authority = MealContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, "meal", MEAL);
        matcher.addURI(authority, "meal/*", MEAL_ID);

        return matcher;
    }

    private void deleteDatabase() {
        mOpenHelper.close();
        AppDatabase.deleteDatabase(getContext());
        mOpenHelper = new AppDatabase(getContext());
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new AppDatabase(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        final int match = mUriMatcher.match(uri);

        switch (match) {
            case USER:
                return UserContract.User.CONTENT_TYPE;
            case USER_ID:
                return UserContract.User.CONTENT_ITEM_TYPE;
            case MEAL_DATE:
                return MealDateContract.MealDate.CONTENT_TYPE;
            case MEAL_DATE_ID:
                return MealDateContract.MealDate.CONTENT_ITEM_TYPE;
            case MEAL:
                return MealContract.Meal.CONTENT_TYPE;
            case MEAL_ID:
                return MealContract.Meal.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
    }


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
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);

        }

        return queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);

        switch(match) {
            case USER:
                long lRecordID = db.insertOrThrow(AppDatabase.Tables.USER,null,values);
                return UserContract.User.buildUserUri(String.valueOf(lRecordID));
            case MEAL_DATE:
                long dRecordID = db.insertOrThrow(AppDatabase.Tables.MEAL_DATE,null,values);
                return MealDateContract.MealDate.buildMealDateUri(String.valueOf(dRecordID));
            case MEAL:
                long mRecordID = db.insertOrThrow(AppDatabase.Tables.MEAL,null,values);
                return MealContract.Meal.buildMealUri(String.valueOf(mRecordID));
            default:
                throw new IllegalArgumentException("Unknown uri " + uri);
        }
    }

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

            case MEAL_DATE:
                return db.update(AppDatabase.Tables.USER, values, selection, selectionArgs);
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

            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);

        if(uri.equals(UserContract.URI_TABLE)) {
            deleteDatabase();
            return 0;
        }

        String selectionCriteria = selection;
        switch (match) {
            case USER_ID:
                String userID = UserContract.User.getUserID(uri);
                selectionCriteria = BaseColumns._ID + "=" + userID
                        + (!TextUtils.isEmpty(selection) ? "AND (" + selection + ")" : "");
                return db.delete(AppDatabase.Tables.USER, selectionCriteria, selectionArgs);

            case MEAL_DATE_ID:
                String mealDateID = MealDateContract.MealDate.getMealDateID(uri);
                selectionCriteria = BaseColumns._ID + "=" + mealDateID
                        + (!TextUtils.isEmpty(selection) ? "AND (" + selection + ")" : "");
                return db.delete(AppDatabase.Tables.MEAL_DATE, selectionCriteria, selectionArgs);

            case MEAL_ID:
                String mealID = MealContract.Meal.getMealID(uri);
                selectionCriteria = BaseColumns._ID + "=" + mealID
                        + (!TextUtils.isEmpty(selection) ? "AND (" + selection + ")" : "");
                return db.delete(AppDatabase.Tables.MEAL, selectionCriteria, selectionArgs);

            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
    }
}
