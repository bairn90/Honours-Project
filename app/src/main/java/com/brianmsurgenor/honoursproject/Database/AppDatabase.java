package com.brianmsurgenor.honoursproject.Database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.brianmsurgenor.honoursproject.DBContracts.MealContract;
import com.brianmsurgenor.honoursproject.DBContracts.MealDateContract;
import com.brianmsurgenor.honoursproject.DBContracts.PedometerContract;
import com.brianmsurgenor.honoursproject.DBContracts.TrophyContract;
import com.brianmsurgenor.honoursproject.DBContracts.UserContract;
import com.brianmsurgenor.honoursproject.Trophy.Trophies;

import java.util.LinkedList;

/**
 * Created by Brian on 30/10/2015.
 * Database class used by the provider to set up the database tables when the app first boots or
 * when the previous data has been deleted
 */
public class AppDatabase extends SQLiteOpenHelper {

    //Tag used for logging purposes on error occuring
    private static final String TAG = AppDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "health.db";
    private static final int DATABASE_VERSION = 2;
    private LinkedList<String> trophyNames, trophyDescriptions;
    private ContentResolver mContentResolver;
    private Context mContext;

    interface Tables {
        String USER = "user";
        String MEAL_DATE = "meal_date";
        String MEAL = "meal";
        String TROPHIES = "trophies";
        String PEDOMETER = "pedometer";
    }


    public AppDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        mContentResolver = mContext.getContentResolver();
    }

    /**
     * Runs the SQL statements to set up the database tables
     */
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
                + UserContract.Columns.PET_TYPE + " INTEGER,"
                + UserContract.Columns.CUSTOM_COLOUR + " INTEGER)");

        db.execSQL("CREATE TABLE " + Tables.MEAL_DATE + "("
                + MealDateContract.Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MealDateContract.Columns.MEAL_DATE + " TEXT NOT NULL,"
                + MealDateContract.Columns.MEAL_TIME + " SIGNED BIGINT NOT NULL,"
                + MealDateContract.Columns.MEAL_TYPE + " TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + Tables.MEAL + "("
                + MealContract.Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MealContract.Columns.MEAL_ID + " TEXT NOT NULL,"
                + MealContract.Columns.MEAL_ITEM + " TEXT NOT NULL,"
                + MealContract.Columns.MEAL_CATEGORY + " TEXT NOT NULL,"
                + "FOREIGN KEY(" + MealContract.Columns.MEAL_ID + ") "
                + "REFERENCES " + Tables.MEAL_DATE + "(" + MealDateContract.Columns._ID + ") )");

        db.execSQL("CREATE TABLE " + Tables.PEDOMETER + "("
                + PedometerContract.Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PedometerContract.Columns.DATE + " TEXT NOT NULL,"
                + PedometerContract.Columns.STEPS + " INTEGER NOT NULL)");

        db.execSQL("CREATE TABLE " + Tables.TROPHIES + "("
                + TrophyContract.Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TrophyContract.Columns.TROPHY_NAME + " TEXT NOT NULL,"
                + TrophyContract.Columns.TROPHY_DESCRIPTION + " TEXT NOT NULL,"
                + TrophyContract.Columns.ACHIEVED + " INTEGER NOT NULL)");


        //Setup trophies in database
        Trophies setupTrophies = new Trophies(mContext);
        trophyNames = setupTrophies.getTrophyNames();
        trophyDescriptions = setupTrophies.getTrophyDescriptions();

        for (int i = 0; i < trophyNames.size(); i++) {
            ContentValues trophyValues = new ContentValues();
            trophyValues.put(TrophyContract.Columns.TROPHY_NAME, trophyNames.get(i));
            trophyValues.put(TrophyContract.Columns.TROPHY_DESCRIPTION, trophyDescriptions.get(i));
            trophyValues.put(TrophyContract.Columns.ACHIEVED, 0);

            db.insert(Tables.TROPHIES, null, trophyValues);
        }
    }


    /**
     * Called automatically from the OS when it detects a new db schema.
     * Not really sure how this is supposed to work yet. I'll learn when I need it.
     * @param db
     * @param oldVersion
     * @param newVersion
     */
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
            db.execSQL("DROP TABLE IF EXISTS " + Tables.TROPHIES);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.PEDOMETER);
            onCreate(db);
        }
    }

    /**
     * Deletes the database
     * @param context
     */
    public static void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }
}
