package com.brianmsurgenor.honoursproject.CommonBaseClasses;

import com.brianmsurgenor.honoursproject.FoodDiary.Food;
import com.brianmsurgenor.honoursproject.R;

import java.util.ArrayList;

/**
 * This class is used to get the sports and foods used by the application and offer them to any
 * class
 */
public class AppConstants {
    
    private ArrayList<Food> foods;
    private ArrayList<ArrayList> sports;
    private final static String GREEN = "green";
    private final static String ORANGE = "orange";
    private final static String RED = "red";

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
        food.setPicture(R.drawable.milk);
        food.setName("Milk");
        food.setCategory(GREEN);
        foods.add(food);

        food = new Food();
        food.setPicture(R.drawable.fruit_juice);
        food.setName("Fruit Juice");
        food.setCategory(GREEN);
        foods.add(food);


        food = new Food();
        food.setPicture(R.drawable.coffee);
        food.setName("Tea/Coffee");
        food.setCategory(ORANGE);
        foods.add(food);

        food = new Food();
        food.setPicture(R.drawable.fruit);
        food.setName("Fruit");
        food.setCategory(GREEN);
        foods.add(food);

        food = new Food();
        food.setPicture(R.drawable.vegetables);
        food.setName("Vegetables");
        food.setCategory(GREEN);
        foods.add(food);

        food = new Food();
        food.setPicture(R.drawable.bread);
        food.setName("Bread");
        food.setCategory(ORANGE);
        foods.add(food);

        food = new Food();
        food.setPicture(R.drawable.red_meat);
        food.setName("Red Meat");
        food.setCategory(ORANGE);
        foods.add(food);

        food = new Food();
        food.setPicture(R.drawable.white_meat);
        food.setName("White Meat");
        food.setCategory(ORANGE);
        foods.add(food);

        food = new Food();
        food.setPicture(R.drawable.bacon);
        food.setName("Bacon");
        food.setCategory(RED);
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
