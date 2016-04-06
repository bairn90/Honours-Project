package com.brianmsurgenor.honoursproject.Exercise;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.brianmsurgenor.honoursproject.CommonBaseClasses.BaseActivity;
import com.brianmsurgenor.honoursproject.DBContracts.ExerciseContract;
import com.brianmsurgenor.honoursproject.R;
import com.brianmsurgenor.honoursproject.Trophy.Trophies;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Adapter used to fill the recycler view for to fill the exercise graphics
 */
public class ExerciseEntryActivity extends BaseActivity {

    private ExerciseEntryAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ContentResolver mContentResolver;
    private TextView exerciseLength, exerciseDate;
    private static String savedExercise = "";
    private String length; //used to hold the exercise length string

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_exercise_entry);

        exerciseLength = (TextView) findViewById(R.id.exerciseLength);
        exerciseDate = (TextView) findViewById(R.id.exerciseDate);

        adapter = new ExerciseEntryAdapter(getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.exercise_picker_recycler);
        mLayoutManager = new GridLayoutManager(ExerciseEntryActivity.this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        month++;
        exerciseDate.setText(calendar.get(Calendar.DAY_OF_MONTH) + "/" + month + "/" + calendar.get(Calendar.YEAR));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.meal_entry_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case R.id.save:
                saveExercise();
                break;

        }

        return true;
    }

    /**
     * Called when the user clicks on the the time text box in order to set the legnth of exercise
     * @param view
     */
    public void setLength(View view) {

        TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                exerciseLength.setText(hourOfDay + " h, " + minute + " mins");
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, android.app.AlertDialog.THEME_HOLO_LIGHT,
                timeListener, 0, 0, true);
        timePickerDialog.show();
    }

    /**
     * Called when the user clicks on the date textbox incase they want to change the date of e
     * exercise
     * @param view
     */
    public void setDate(View view) {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear++;
                exerciseDate.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateListener,
                year, ++month, day);

        datePickerDialog.show();
    }

    /**
     * Static method called from the adapter to pass back the exercise that the user has just
     * selected/unselected
     * @param selectedExercise
     */
    public static void selectExercise(String selectedExercise) {
        savedExercise = selectedExercise;
    }

    /**
     * Called to check the user has entered all the necessary details and then saves the data into
     * the db. Then makes call to the trophy check method
     */
    private void saveExercise() {

        length  = exerciseLength.getText().toString();
        String date = exerciseDate.getText().toString();

        if (length.equals("Enter Time") || savedExercise.equals("")) {
            Toast.makeText(ExerciseEntryActivity.this, "Please select an exercise and enter " +
                    "the length of time", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(ExerciseContract.Columns.EXERCISE, length);
        values.put(ExerciseContract.Columns.DATE, date + " " + savedExercise);

        mContentResolver = getContentResolver();
        mContentResolver.insert(ExerciseContract.URI_TABLE, values);

        if(!trophyCheck()) {
            Toast.makeText(ExerciseEntryActivity.this, "Exercise saved!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ExerciseEntryActivity.this, ExerciseDiaryActivity.class));
        }

    }

    /**
     * Called to check the db incase the user has won any trophies
     */
    private boolean trophyCheck() {

        ArrayList<String> lengths, dates;
        lengths = new ArrayList<>();
        dates = new ArrayList<>();
        String[] projection = {ExerciseContract.Columns.DATE, ExerciseContract.Columns.EXERCISE};
        Cursor mCursor = mContentResolver.query(ExerciseContract.URI_TABLE, projection, null, null, null);
        int exerciseCount = 0;
        Trophies trophies = new Trophies(this);

        if(mCursor.moveToFirst()){
            do {
                lengths.add(mCursor.getString(mCursor.getColumnIndex(ExerciseContract.Columns.EXERCISE)));
                dates.add(mCursor.getString(mCursor.getColumnIndex(ExerciseContract.Columns.DATE)));
            } while(mCursor.moveToNext());
        }

        for(String exercise : dates) {
            if(exercise.contains(savedExercise)) {
                exerciseCount++;
            }
        }

        Intent intent = new Intent(this,ExerciseDiaryActivity.class);
        if(exerciseCount == 1) {
            switch (savedExercise) {
                case "Football":
                    trophies.winTrophy(Trophies.TrophyDetails.firstFootball[0],intent);
                    return true;

                case "Rugby":
                    trophies.winTrophy(Trophies.TrophyDetails.firstRugby[0],intent);
                    return true;

                case "Running":
                    trophies.winTrophy(Trophies.TrophyDetails.firstRun[0],intent);
                    return true;

                case "Skiing":
                    trophies.winTrophy(Trophies.TrophyDetails.firstSki[0],intent);
                    return true;

                case "Tennis":
                    trophies.winTrophy(Trophies.TrophyDetails.firstTennis[0],intent);
                    return true;
            }
        } else {
            int lengthNumber = Integer.parseInt(length.split(" ")[0]);
            if(lengthNumber > 0 && savedExercise.equals("Football")) {
                trophies.winTrophy(Trophies.TrophyDetails.hourFootball[0],intent);
                return true;
            }
        }

        return false;
    }
}
