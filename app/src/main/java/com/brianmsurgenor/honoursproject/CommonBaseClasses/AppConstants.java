package com.brianmsurgenor.honoursproject.CommonBaseClasses;

import com.brianmsurgenor.honoursproject.R;

import java.util.ArrayList;

/**
 * Created by Brian on 03/02/2016.
 */
public class AppConstants {
    
    private ArrayList foodPics;
    private ArrayList foodCategories;
    private final static String GREEN = "green";
    private final static String ORANGE = "orange";
    private final static String RED = "red";

    public AppConstants() {
        foodPics = new ArrayList();
        foodCategories = new ArrayList();

        setFoodPics();
        setFoodCategories();
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
}
