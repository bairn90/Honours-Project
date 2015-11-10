package com.brianmsurgenor.honoursproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Brian on 30/10/2015.
 */
public class AppDatabase extends SQLiteOpenHelper {

    private static final String TAG = AppDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "health.db";
    private static final int DATABASE_VERSION = 1;
    private final Context mcontext;

    interface Tables {
        String USER = "user";
        String MEAL_DATE = "meal_date";
        String MEAL = "meal";
        String TROPHIES = "trophies";
    }


    public AppDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mcontext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Tables.USER + "("
                + UserContract.Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + UserContract.Columns.USERNAME + " TEXT NOT NULL,"
                + UserContract.Columns.DOB + " TEXT NOT NULL,"
                + UserContract.Columns.GENDER + " TEXT NOT NULL,"
                + UserContract.Columns.HEIGHT + " TEXT,"
                + UserContract.Columns.WEIGHT + " TEXT,"
                + UserContract.Columns.PET_NAME + " TEXT,"
                + UserContract.Columns.PET_TYPE + " TEXT,"
                + UserContract.Columns.CUSTOM_COLOUR + " INTEGER)");

        db.execSQL("CREATE TABLE " + Tables.MEAL_DATE + "("
                + MealDateContract.Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MealDateContract.Columns.MEAL_DATE + " TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + Tables.MEAL + "("
                + MealContract.Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MealContract.Columns.MEAL_TYPE + " TEXT NOT NULL,"
                + MealContract.Columns.MEAL_TIME + " TEXT NOT NULL,"
                + MealContract.Columns.MEAL_FOOD + " TEXT NOT NULL,"
                + MealContract.Columns.MEAL_DRINK + " TEXT NOT NULL,"
                + MealContract.Columns.MEAL_DATE + " TEXT NOT NULL,"
                + "FOREIGN KEY(" + MealContract.Columns.MEAL_DATE + ") "
                + "REFERENCES " + Tables.MEAL_DATE + "(" + MealDateContract.Columns.MEAL_DATE + ") )");

        db.execSQL("CREATE TABLE " + Tables.TROPHIES + "("
                + TrophyContract.Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TrophyContract.Columns.TROPHY_NAME + " TEXT NOT NULL,"
                + TrophyContract.Columns.TROPHY_DESCRIPTION + " TEXT NOT NULL,"
                + TrophyContract.Columns.ACHIEVED + " INTEGER NOT NULL)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int version = oldVersion;

        if (version == 1) {
            //Add some extra fields to the database without deleting existing data
            version = 2;
        }

        if (version != DATABASE_VERSION) {
            db.execSQL("DROP TABLE IF EXISTS " + Tables.USER);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.MEAL_DATE);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.MEAL);
            onCreate(db);
        }
    }

    public static void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }
}
