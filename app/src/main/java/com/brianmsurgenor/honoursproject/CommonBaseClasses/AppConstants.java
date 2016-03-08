package com.brianmsurgenor.honoursproject.CommonBaseClasses;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

import com.brianmsurgenor.honoursproject.DBContracts.UserContract;
import com.brianmsurgenor.honoursproject.R;

import java.util.ArrayList;

/**
 * Created by Brian on 03/02/2016.
 */
public class AppConstants {
    
    private ArrayList foodPics;
    private ArrayList foodCategories;
    private ArrayList sports;
    private final static String GREEN = "green";
    private final static String ORANGE = "orange";
    private final static String RED = "red";
    private String userName;
    private Cursor mCursor;
    private ContentResolver mContentResolver;

    public AppConstants(Context context) {

        //Call to the second constructor
        this();

        String[] projection = {UserContract.Columns.USERNAME};
        mContentResolver = context.getContentResolver();
        mCursor = mContentResolver.query(UserContract.URI_TABLE, projection, null, null, null);

        if(mCursor.moveToFirst()) {
            userName = mCursor.getString(mCursor.getColumnIndex(UserContract.Columns.USERNAME));
        }

    }

    public AppConstants() {
        foodPics = new ArrayList();
        foodCategories = new ArrayList();
        sports = new ArrayList();

        setSports();
        setFoodPics();
        setFoodCategories();
    }

    public void setSports() {

    }

    public void setFoodPics() {
        foodPics.add(R.drawable.fizzy_drink);
        foodPics.add(R.drawable.water);
        foodPics.add(R.drawable.bacon);
        foodPics.add(R.drawable.fruit);
        foodPics.add(R.drawable.french_fries);
        foodPics.add(R.drawable.cheese);
        foodPics.add(R.drawable.sweets);
    }

    public void setFoodCategories() {
        foodCategories.add(RED);
        foodCategories.add(GREEN);
        foodCategories.add(RED);
        foodCategories.add(GREEN);
        foodCategories.add(RED);
        foodCategories.add(ORANGE);
        foodCategories.add(RED);
    }

    public ArrayList getFoodPics() {
        return foodPics;
    }

    public ArrayList getFoodCategories() {
        return foodCategories;
    }

    public ArrayList getSports() {
        return sports;
    }

    
}
