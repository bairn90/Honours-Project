package com.brianmsurgenor.honoursproject.FoodDiary;

import java.util.ArrayList;

/**
 * Created by Brian on 01/03/2016.
 */
public class Meal {

    private int _id;
    private String date, type;
    private ArrayList<String> categories;
    private boolean mealCardLoaded = false;

    public void setMealCardLoaded() {
        mealCardLoaded = true;
    }

    public boolean isMealCardLoaded() {
        return mealCardLoaded;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }
}
