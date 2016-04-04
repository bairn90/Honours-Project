package com.brianmsurgenor.honoursproject.CommonBaseClasses;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

import com.brianmsurgenor.honoursproject.DBContracts.UserContract;
import com.brianmsurgenor.honoursproject.FoodDiary.Food;
import com.brianmsurgenor.honoursproject.R;

import java.util.ArrayList;

/**
 * Created by Brian on 03/02/2016.
 */
public class AppConstants {
    
    private ArrayList<Food> foods;
    private ArrayList<ArrayList> sports;
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
        foods = new ArrayList();
        sports = new ArrayList();

        setSports();
        setFoods();
    }

    public void setSports() {

        ArrayList t = new ArrayList();

        t.add("Football");
        t.add(R.drawable.football);
        sports.add(t);

        t = new ArrayList();
        t.add("Rugby");
        t.add(R.drawable.rugby);
        sports.add(t);

        t = new ArrayList();
        t.add("Running");
        t.add(R.drawable.running);
        sports.add(t);

        t = new ArrayList();
        t.add("Tennis");
        t.add(R.drawable.tennis);
        sports.add(t);

        t = new ArrayList();
        t.add("Skiing");
        t.add(R.drawable.skiing);
        sports.add(t);
    }

    public void setFoods() {

        Food food = new Food();
        food.setPicture(R.drawable.fizzy_drink);
        food.setName("Fizzy Drink");
        food.setCategory(RED);
        foods.add(food);

        food = new Food();
        food.setPicture(R.drawable.water);
        food.setName("Water");
        food.setCategory(GREEN);
        foods.add(food);

        food = new Food();
        food.setPicture(R.drawable.bacon);
        food.setName("Bacon");
        food.setCategory(RED);
        foods.add(food);

        food = new Food();
        food.setPicture(R.drawable.fruit);
        food.setName("Fruit");
        food.setCategory(GREEN);
        foods.add(food);

        food = new Food();
        food.setPicture(R.drawable.french_fries);
        food.setName("Chips");
        food.setCategory(RED);
        foods.add(food);

        food = new Food();
        food.setPicture(R.drawable.cheese);
        food.setName("Cheese");
        food.setCategory(ORANGE);
        foods.add(food);

        food = new Food();
        food.setPicture(R.drawable.sweets);
        food.setName("Chocolate");
        food.setCategory(RED);
        foods.add(food);
    }

    public ArrayList getFoods() {
        return foods;
    }

    public ArrayList getSports() {
        return sports;
    }

    
}
