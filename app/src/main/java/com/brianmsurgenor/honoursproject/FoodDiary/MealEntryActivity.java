package com.brianmsurgenor.honoursproject.FoodDiary;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.brianmsurgenor.honoursproject.CommonBaseClasses.BaseActivity;
import com.brianmsurgenor.honoursproject.DBContracts.MealContract;
import com.brianmsurgenor.honoursproject.DBContracts.MealDateContract;
import com.brianmsurgenor.honoursproject.DBContracts.TrophyContract;
import com.brianmsurgenor.honoursproject.Main.MainActivity;
import com.brianmsurgenor.honoursproject.R;
import com.brianmsurgenor.honoursproject.Trophy.Trophies;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MealEntryActivity extends BaseActivity {

    private ContentResolver mContentResolver;
    private FoodPickerAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private Spinner txtMealType;
    private TextView txtMealTime;
    private String mealType;
    private int mealTypeIndex;
    private static ArrayList<String> foodEaten;
    private static ArrayList<String> foodCategories;
    private int mealID = 0;
    private long mealTime;
    private Calendar calendar;
    private SimpleDateFormat formatting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_meal_entry);

        txtMealType = (Spinner) findViewById(R.id.mealType);
        txtMealTime = (TextView) findViewById(R.id.mealTime);
        formatting = new SimpleDateFormat("HH:mm");

        calendar = Calendar.getInstance();
        mealTime = calendar.getTimeInMillis();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            if (!extras.getBoolean("Notification")) {
                mealID = extras.getInt(MealDateContract.Columns._ID);
                mealTime = extras.getLong(MealDateContract.Columns.MEAL_TIME);
            }

            mealType = extras.getString(MealDateContract.Columns.MEAL_TYPE);
            for (int i = 1; i < txtMealType.getCount(); i++) {
                if (txtMealType.getItemAtPosition(i).toString().equals(mealType)) {
                    mealTypeIndex = i;
                    break;
                }
            }
            txtMealType.setSelection(mealTypeIndex);

            calendar.setTimeInMillis(mealTime);
        }
        txtMealTime.setText("" + formatting.format(calendar.getTime()));

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
        if (mealID == 0) {
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

    public void setMealTime(View v) {

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                c.set(Calendar.MINUTE, minute);
                mealTime = c.getTimeInMillis();

                txtMealTime.setText("" + formatting.format(c.getTime()));
            }
        };

        TimePickerDialog timePicker = new TimePickerDialog(this, android.app.AlertDialog.THEME_HOLO_LIGHT,
                timeListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

        timePicker.show();
    }

    private void updateMeal() {

        if (!foodEaten.isEmpty() && !txtMealType.getSelectedItem().toString().equals("Meal")) {

            ContentValues values = new ContentValues();
            Uri uri = MealContract.Meal.buildMealUri(mealID + "");
            mContentResolver.delete(uri, null, null);

            for (int i = 0; i < foodEaten.size(); i++) {
                values.put(MealContract.Columns.MEAL_ID, mealID);
                values.put(MealContract.Columns.MEAL_ITEM, foodEaten.get(i));
                values.put(MealContract.Columns.MEAL_CATEGORY, foodCategories.get(i));
                mContentResolver.insert(MealContract.URI_TABLE, values);
            }

            values = new ContentValues();
            values.put(MealDateContract.Columns.MEAL_TYPE, txtMealType.getSelectedItem().toString());
            values.put(MealDateContract.Columns.MEAL_TIME, mealTime);
            String where = MealDateContract.Columns._ID + " = " + mealID;

            mContentResolver.update(MealDateContract.URI_TABLE, values, where, null);
        } else {
            if (foodEaten.isEmpty()) {
                Toast.makeText(MealEntryActivity.this, "You can't save a meal with no food!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MealEntryActivity.this, "What are you eating? Lunch? Dinner? Breakfast?", Toast.LENGTH_LONG).show();
            }
            return;
        }

        Toast.makeText(MealEntryActivity.this, "Meal updated!", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(MealEntryActivity.this, "Meal saved!", Toast.LENGTH_SHORT).show();
        } else {
            if (foodEaten.isEmpty()) {
                Toast.makeText(MealEntryActivity.this, "You can't save a meal with no food!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MealEntryActivity.this, "What are you eating? Lunch? Dinner? Breakfast?", Toast.LENGTH_LONG).show();
            }
            return;
        }

    }

    private void saveMealData() {

        boolean greenTrophyCheck = true;
        int month = calendar.get(Calendar.MONTH);
        month++;
        String date = calendar.get(Calendar.DAY_OF_MONTH) + "/" + month + "/" + calendar.get(Calendar.YEAR);

        //Insert for meal date
        ContentValues values = new ContentValues();
        values.put(MealDateContract.Columns.MEAL_DATE, date);
        values.put(MealDateContract.Columns.MEAL_TIME, mealTime);
        values.put(MealDateContract.Columns.MEAL_TYPE, txtMealType.getSelectedItem().toString());

        Uri returned = mContentResolver.insert(MealDateContract.URI_TABLE, values);

        String temp = returned.toString();
        int pos = temp.lastIndexOf('/') + 1;
        String mealID = temp.substring(pos);

        //Insert foods

        for (int i = 0; i < foodEaten.size(); i++) {
            values = new ContentValues();
            values.put(MealContract.Columns.MEAL_ID, mealID);
            values.put(MealContract.Columns.MEAL_ITEM, foodEaten.get(i));
            values.put(MealContract.Columns.MEAL_CATEGORY, foodCategories.get(i));
            mContentResolver.insert(MealContract.URI_TABLE, values);

            if (!foodCategories.equals("Green")) {
                greenTrophyCheck = false;
            }
        }

        //Set up the next meal notification based on the meal that was just entered
        switch (txtMealType.getSelectedItem().toString()) {

            case "Breakfast":
                getAndSetNextNotification("Lunch");
                break;

            case "Lunch":
                getAndSetNextNotification("Dinner");
                break;

            case "Dinner":
                getAndSetNextNotification("Breakfast");
                break;
        }

        //Check for trophy win
        Trophies trophies = new Trophies(this);
        String[] projection = {TrophyContract.Columns.ACHIEVED, TrophyContract.Columns.TROPHY_NAME};
        String where = TrophyContract.Columns.TROPHY_NAME + " = '" + Trophies.TrophyDetails.firstMeal[0] + "' OR " +
                TrophyContract.Columns.TROPHY_NAME + " = '" + Trophies.TrophyDetails.greenMeal[0] + "'";
        Cursor mCursor = mContentResolver.query(TrophyContract.URI_TABLE, projection, where, null, null);

        if (mCursor.moveToFirst()) {
            if (mCursor.getInt(mCursor.getColumnIndex(TrophyContract.Columns.ACHIEVED)) == 0) {
                temp = mCursor.getString(mCursor.getColumnIndex(TrophyContract.Columns.TROPHY_NAME));

                trophies.winTrophy(temp, MainActivity.class);
            }

            if(greenTrophyCheck) {
                mCursor.moveToNext();
                if (mCursor.getInt(mCursor.getColumnIndex(TrophyContract.Columns.ACHIEVED)) == 0) {
                    temp = mCursor.getString(mCursor.getColumnIndex(TrophyContract.Columns.TROPHY_NAME));
                    trophies.winTrophy(temp, MainActivity.class);
                }
            }

        } else {
            startActivity(new Intent(this, MainActivity.class));
        }

    }

    private void getAndSetNextNotification(String meal) {

        long avgTime = 0;
        int count = 0;
        String[] projection = {MealDateContract.Columns.MEAL_TIME};
        String where = MealDateContract.Columns.MEAL_TYPE + " = '" + meal + "'";
        Cursor mCursor = mContentResolver.query(MealDateContract.URI_TABLE, projection, where, null, null);
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        if (meal.equals("Breakfast")) {
            day++;
        }


        if (mCursor.moveToFirst()) {
            do {
//                avgTime += mCursor.getLong(mCursor.getColumnIndex(MealDateContract.Columns.MEAL_TIME));
                c.setTimeInMillis(mCursor.getLong(mCursor.getColumnIndex(MealDateContract.Columns.MEAL_TIME)));
                c.set(year, month, day);
                avgTime += c.getTimeInMillis();
                Log.d("Avg", "" + c.getTimeInMillis());
                count++;
            } while (mCursor.moveToNext());
        }

        mCursor.close();

        if (count != 0) {
            avgTime = avgTime / count;
            Intent intent = new Intent(this, MealNotifReceiver.class);
            intent.putExtra(MealDateContract.Columns.MEAL_TYPE, meal);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC, avgTime, pendingIntent);
        }

    }

    public static void selectedFood(boolean selected, String selectedFood, String foodCategory) {

        if (selected) {
            foodEaten.add(selectedFood);
            foodCategories.add(foodCategory);
        } else {
            for (int i = 0; i < foodEaten.size(); i++) {
                if (foodEaten.get(i).equals(selectedFood)) {
                    foodEaten.remove(i);
                    foodCategories.remove(i);
                    return;
                }
            }
        }

    }

}
