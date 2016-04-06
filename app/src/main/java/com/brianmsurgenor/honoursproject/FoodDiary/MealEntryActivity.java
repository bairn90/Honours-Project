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
    private Spinner spinMealType;
    private TextView txtMealTime;
    private String mealType;
    private int mealTypeIndex;
    private static ArrayList<String> foodEaten;
    private static ArrayList<String> foodCategories;
    private int mealID = 0;
    private int green = 0;
    private int orange = 0;
    private int red = 0;
    private long mealTime;
    private Calendar calendar;
    private SimpleDateFormat formatting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_meal_entry);

        spinMealType = (Spinner) findViewById(R.id.mealType);
        txtMealTime = (TextView) findViewById(R.id.mealTime);
        formatting = new SimpleDateFormat("HH:mm");

        calendar = Calendar.getInstance();
        mealTime = calendar.getTimeInMillis();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            //If the user wasn't sent to this screen via the notification then it must be to edit
            if (!extras.getBoolean("Notification")) {
                mealID = extras.getInt(MealDateContract.Columns._ID);
                mealTime = extras.getLong(MealDateContract.Columns.MEAL_TIME);
            }

            //Get the type (lunch etc) and then assign that to the spinner
            mealType = extras.getString(MealDateContract.Columns.MEAL_TYPE);
            for (int i = 1; i < spinMealType.getCount(); i++) {
                if (spinMealType.getItemAtPosition(i).toString().equals(mealType)) {
                    mealTypeIndex = i;
                    break;
                }
            }

            spinMealType.setSelection(mealTypeIndex);
            calendar.setTimeInMillis(mealTime);
        }
        txtMealTime.setText("" + formatting.format(calendar.getTime()));

        foodEaten = new ArrayList<>();
        foodCategories = new ArrayList<>();

        //Setup the adapter to the recyclerview
        mContentResolver = getContentResolver();
        recyclerView = (RecyclerView) findViewById(R.id.food_picker_recycler_view);
        mLayoutManager = new GridLayoutManager(MealEntryActivity.this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new FoodPickerAdapter(MealEntryActivity.this, mealID);
        recyclerView.setAdapter(adapter);

    }

    //Depending on if a meal ID was passed inflate the relevant menu
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

            case R.id.save:
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

    /**
     * Shows the time picker dialog when the user taps on the time text view
     * @param v
     */
    public void setMealTime(View v) {

        final Calendar c = Calendar.getInstance();

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

    /**
     * Called to update the meal
     */
    private void updateMeal() {

        if (!foodEaten.isEmpty() && !spinMealType.getSelectedItem().toString().equals("Meal")) {

            ContentValues values = new ContentValues();
            //First delete all current foods in the db as some may have been unselected
            Uri uri = MealContract.Meal.buildMealUri(mealID + "");
            mContentResolver.delete(uri, null, null);

            //Now insert the foods into the db
            for (int i = 0; i < foodEaten.size(); i++) {
                values.put(MealContract.Columns.MEAL_ID, mealID);
                values.put(MealContract.Columns.MEAL_ITEM, foodEaten.get(i));
                values.put(MealContract.Columns.MEAL_CATEGORY, foodCategories.get(i));
                mContentResolver.insert(MealContract.URI_TABLE, values);
            }

            //Update the meal time and type
            values = new ContentValues();
            values.put(MealDateContract.Columns.MEAL_TYPE, spinMealType.getSelectedItem().toString());
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

    /**
     * If the user has selected delete then ask them to confirm and delete the meal
     */
    private void deleteMeal() {

        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setTitle("Delete meal")
                .setMessage("Are you sure you want to delete this meal?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //First delete the foods from the meal
                        Uri uri = MealContract.Meal.buildMealUri(mealID + "");
                        mContentResolver.delete(uri, null, null);

                        //Then delete the meal from the DB
                        uri = MealDateContract.MealDate.buildMealDateUri(mealID + "");
                        mContentResolver.delete(uri, null, null);

                        Toast.makeText(MealEntryActivity.this, "Meal has been deleted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MealEntryActivity.this, FoodDiaryActivity.class));
                    }

                })
                .setNegativeButton("No", null)
                .show();

    }

    /**
     * Check whether or not the user has entered all the details and if they have call the
     * saveMealData method to actually save to the db
     */
    private void saveMeal() {

        if (!foodEaten.isEmpty() && !spinMealType.getSelectedItem().toString().equals("Meal")) {
            saveMealData();
        } else {
            if (foodEaten.isEmpty()) {
                Toast.makeText(MealEntryActivity.this, "You can't save a meal with no food!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MealEntryActivity.this, "What are you eating? Lunch? Dinner? Breakfast?", Toast.LENGTH_LONG).show();
            }
            return;
        }

    }

    /**
     * Saves the meal data into the database
     */
    private void saveMealData() {

        boolean greenTrophyCheck = true;
        int month = calendar.get(Calendar.MONTH);
        month++;
        String date = calendar.get(Calendar.DAY_OF_MONTH) + "/" + month + "/" + calendar.get(Calendar.YEAR);

        //Insert for meal date
        ContentValues values = new ContentValues();
        values.put(MealDateContract.Columns.MEAL_DATE, date);
        values.put(MealDateContract.Columns.MEAL_TIME, mealTime);
        values.put(MealDateContract.Columns.MEAL_TYPE, spinMealType.getSelectedItem().toString());

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

            switch (foodCategories.get(i)) {

                case "green":
                    green++;
                    break;

                case "orange":
                    orange++;
                    break;

                case "red":
                    red++;
                    break;

            }

            if (!foodCategories.equals("green")) {
                greenTrophyCheck = false;
            }
        }

        //Set up the next meal notification based on the meal that was just entered
        switch (spinMealType.getSelectedItem().toString()) {

            case "Breakfast":
                setNextNotif("Lunch");
                break;

            case "Lunch":
                setNextNotif("Dinner");
                break;

            case "Dinner":
                setNextNotif("Breakfast");
                break;
        }

        //Check for trophy win
        Trophies trophies = new Trophies(this);
        String[] projection = {TrophyContract.Columns.ACHIEVED, TrophyContract.Columns.TROPHY_NAME};
        String where = TrophyContract.Columns.TROPHY_NAME + " = '" + Trophies.TrophyDetails.firstMeal[0] + "' OR " +
                TrophyContract.Columns.TROPHY_NAME + " = '" + Trophies.TrophyDetails.greenMeal[0] + "'";
        Cursor mCursor = mContentResolver.query(TrophyContract.URI_TABLE, projection, where, null, null);
        Intent intent = new Intent(this, FoodDiaryActivity.class);
        intent.putExtra("green",green);
        intent.putExtra("orange",orange);
        intent.putExtra("red",red);

        if (mCursor.moveToFirst()) {
            if (mCursor.getInt(mCursor.getColumnIndex(TrophyContract.Columns.ACHIEVED)) == 0) {
                temp = mCursor.getString(mCursor.getColumnIndex(TrophyContract.Columns.TROPHY_NAME));

                trophies.winTrophy(temp, intent);
                return;
            }

            if(greenTrophyCheck) {
                mCursor.moveToNext();
                if (mCursor.getInt(mCursor.getColumnIndex(TrophyContract.Columns.ACHIEVED)) == 0) {
                    temp = mCursor.getString(mCursor.getColumnIndex(TrophyContract.Columns.TROPHY_NAME));
                    trophies.winTrophy(temp, intent);
                    return;
                }
            }

        }

        startActivity(intent);

    }

    /**
     * Used to set the next notification for the next meal based on the avg time of past meals
     * @param meal
     */
    private void setNextNotif(String meal) {

        long avgTime = 0;
        int count = 0;
        String[] projection = {MealDateContract.Columns.MEAL_TIME};
        String where = MealDateContract.Columns.MEAL_TYPE + " = '" + meal + "'";
        Cursor mCursor = mContentResolver.query(MealDateContract.URI_TABLE, projection, where, null, null);
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        //If the next meal is breakfast increment the day to set notification for tomorrow
        if (meal.equals("Breakfast")) {
            day++;
        }


        //Get the past meal average
        if (mCursor.moveToFirst()) {
            do {
                c.setTimeInMillis(mCursor.getLong(mCursor.getColumnIndex(MealDateContract.Columns.MEAL_TIME)));
                c.set(year, month, day);
                avgTime += c.getTimeInMillis();
                count++;
            } while (mCursor.moveToNext());
        }

        mCursor.close();

        //If there were meals found in the db then set the next meal notif otherwise no point
        if (count != 0) {
            avgTime = avgTime / count;
            Intent intent = new Intent(this, MealNotifReceiver.class);
            intent.putExtra(MealDateContract.Columns.MEAL_TYPE, meal);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC, avgTime, pendingIntent);
        }

    }

    /**
     * Static method called from the adapter to add the food and category arrays or remove depending
     * on what is recieved from the adapter
     */
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
