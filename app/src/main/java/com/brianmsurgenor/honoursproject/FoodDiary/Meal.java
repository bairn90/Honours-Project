package com.brianmsurgenor.honoursproject.FoodDiary;

import java.util.ArrayList;

/**
 * Simple class used to hold the meals used by the food diary
 */
public class Meal {

    private int _id;
    private String date, type;
    private long realTime;
    private ArrayList<String> categories;

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

    public long getRealTime() {
        return realTime;
    }

    public void setRealTime(long realTime) {
        this.realTime = realTime;
    }
}
