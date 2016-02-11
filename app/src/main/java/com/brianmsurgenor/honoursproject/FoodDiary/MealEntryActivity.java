package com.brianmsurgenor.honoursproject.FoodDiary;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
    private FoodPickerAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private Spinner txtMealType;
    private String mealType;
    private int mealTypeIndex;
    private static ArrayList<String> foodEaten;
    private static ArrayList<String> foodCategories;
    private int mealID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_meal_entry);

        txtMealType = (Spinner) findViewById(R.id.mealType);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            mealID = extras.getInt(MealDateContract.Columns._ID);
            mealType = extras.getString(MealDateContract.Columns.MEAL_TYPE);

            for(int i=1; i<txtMealType.getCount();i++) {
                if(txtMealType.getItemAtPosition(i).toString().equals(mealType)) {
                    mealTypeIndex = i;
                    break;
                }
            }
            txtMealType.setSelection(mealTypeIndex);
        }

        mContentResolver = getContentResolver();
        recyclerView = (RecyclerView) findViewById(R.id.food_picker_recycler_view);
        mLayoutManager = new GridLayoutManager(MealEntryActivity.this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new FoodPickerAdapter(MealEntryActivity.this, mealID);
        recyclerView.setAdapter(adapter);

        foodEaten = new ArrayList<>();
        foodCategories = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(mealID == 0) {
            getMenuInflater().inflate(R.menu.meal_entry_menu, menu);
        } else {
            getMenuInflater().inflate(R.menu.edit_meal_entry_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case R.id.saveMeal:
                saveMeal();
                break;

            case R.id.reSaveMeal:
                updateMeal();
                break;

            case R.id.deleteMeal:
                deleteMeal();
                break;

        }

        return true;
    }

    private void updateMeal() {

        if (!foodEaten.isEmpty() || !txtMealType.getSelectedItem().toString().equals("Meal")) {

            ContentValues values = new ContentValues();
            Uri uri = MealContract.Meal.buildMealUri(mealID + "");
            mContentResolver.delete(uri, null, null);

            for(int i=0;i<foodEaten.size();i++) {
                values.put(MealContract.Columns.MEAL_ID, mealID);
                values.put(MealContract.Columns.MEAL_ITEM, foodEaten.get(i));
                values.put(MealContract.Columns.MEAL_CATEGORY, foodCategories.get(i));
                mContentResolver.insert(MealContract.URI_TABLE, values);
            }

            values = new ContentValues();
            values.put(MealDateContract.Columns.MEAL_TYPE, txtMealType.getSelectedItem().toString());
            String where = MealDateContract.Columns._ID + " = " + mealID;

            mContentResolver.update(MealDateContract.URI_TABLE, values, where, null);
        } else {
            if(foodEaten.isEmpty()) {
                Toast.makeText(MealEntryActivity.this, "You can't save a meal with no food!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MealEntryActivity.this, "What are you eating? Lunch? Dinner? Breakfast?", Toast.LENGTH_LONG).show();
            }
            return;
        }

        startActivity(new Intent(MealEntryActivity.this, FoodDiaryActivity.class));

    }

    private void deleteMeal() {

        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setTitle("Delete meal")
                .setMessage("Are you sure you want to delete this meal?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = MealContract.Meal.buildMealUri(mealID + "");
                        mContentResolver.delete(uri, null, null);

                        uri = MealDateContract.MealDate.buildMealDateUri(mealID + "");
                        mContentResolver.delete(uri, null, null);

                        Toast.makeText(MealEntryActivity.this, "Meal has been deleted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MealEntryActivity.this, FoodDiaryActivity.class));
                    }

                })
                .setNegativeButton("No", null)
                .show();

    }

    private void saveMeal() {

        if (!foodEaten.isEmpty() && !txtMealType.getSelectedItem().toString().equals("Meal")) {
            saveMealData();
        } else {
            if(foodEaten.isEmpty()) {
                Toast.makeText(MealEntryActivity.this, "You can't save a meal with no food!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MealEntryActivity.this, "What are you eating? Lunch? Dinner? Breakfast?", Toast.LENGTH_LONG).show();
            }
            return;
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
