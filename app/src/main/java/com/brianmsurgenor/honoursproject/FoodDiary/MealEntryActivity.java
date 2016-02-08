package com.brianmsurgenor.honoursproject.FoodDiary;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.Toast;

import com.brianmsurgenor.honoursproject.CommonBaseClasses.BaseActivity;
import com.brianmsurgenor.honoursproject.DBContracts.MealContract;
import com.brianmsurgenor.honoursproject.DBContracts.MealDateContract;
import com.brianmsurgenor.honoursproject.Main.MainActivity;
import com.brianmsurgenor.honoursproject.R;

import java.util.ArrayList;
import java.util.Calendar;

public class MealEntryActivity extends BaseActivity {

    private ContentResolver mContentResolver;
    private FoodPickerGridAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private Spinner txtMealType;
    private static ArrayList<String> foodEaten;
    private static ArrayList<String> foodCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_meal_entry);

        mContentResolver = getContentResolver();
        recyclerView = (RecyclerView) findViewById(R.id.food_picker_recycler_view);
        mLayoutManager = new GridLayoutManager(MealEntryActivity.this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new FoodPickerGridAdapter(mContentResolver, MealEntryActivity.this);
        recyclerView.setAdapter(adapter);

        txtMealType = (Spinner) findViewById(R.id.mealType);
        foodEaten = new ArrayList<>();
        foodCategories = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.meal_entry_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.saveMeal) {
            saveMeal();
        }

        return true;
    }

    private void saveMeal() {

        if (!foodEaten.isEmpty()) {
            saveMealData();
        } else {
            Toast.makeText(MealEntryActivity.this, "You can't save a meal with no food!", Toast.LENGTH_SHORT).show();
        }

        startActivity(new Intent(MealEntryActivity.this, MainActivity.class));
    }

    private void saveMealData() {

        Calendar c = Calendar.getInstance();
        String date = c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR);
        String time = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);

        //Insert for meal date
        ContentValues values = new ContentValues();
        values.put(MealDateContract.Columns.MEAL_DATE, date);
        values.put(MealDateContract.Columns.MEAL_TIME, time);
        values.put(MealDateContract.Columns.MEAL_TYPE, txtMealType.getSelectedItem().toString());

        Uri returned = mContentResolver.insert(MealDateContract.URI_TABLE, values);

        String temp = returned.toString();
        int pos = temp.lastIndexOf('/') + 1;
        String mealID = temp.substring(pos);

        //Insert foods

        for(int i=0;i<foodEaten.size();i++) {
            values = new ContentValues();
            values.put(MealContract.Columns.MEAL_ID, mealID);
            values.put(MealContract.Columns.MEAL_ITEM, foodEaten.get(i));
            values.put(MealContract.Columns.MEAL_CATEGORY, foodCategories.get(i));
            mContentResolver.insert(MealContract.URI_TABLE, values);
        }

    }

    public static void selectedFood(boolean selected, String selectedFood, String foodCategory) {

        if(selected) {
            foodEaten.add(selectedFood);
            foodCategories.add(foodCategory);
        } else {
            for(int i=0;i<foodEaten.size();i++) {
                if(foodEaten.get(i).equals(selectedFood)) {
                    foodEaten.remove(i);
                    foodCategories.remove(i);
                    return;
                }
            }
        }

    }

}
