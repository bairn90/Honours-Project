package com.brianmsurgenor.honoursproject.Exercise;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
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

import java.util.Calendar;

public class ExerciseEntryActivity extends BaseActivity {

    private ExerciseEntryAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ContentResolver mContentResolver;
    private TextView exerciseLength, exerciseDate;
    private static String savedExercise = "";

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
                year,++month,day);

        datePickerDialog.show();
    }

    public static void selectExercise(boolean selected, String selectedExercise) {

        if(selected) {
            savedExercise = selectedExercise;
        } else {
            savedExercise = "";
        }

    }

    private void saveExercise() {

        String length = exerciseLength.getText().toString();
        String date = exerciseDate.getText().toString();

        if(length.equals("Enter Time") || savedExercise.equals("")) {
            Toast.makeText(ExerciseEntryActivity.this, "Please select an exercise and enter " +
                    "the length of time", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(ExerciseContract.Columns.EXERCISE, length);
        values.put(ExerciseContract.Columns.DATE, date + " " + savedExercise);

        mContentResolver = getContentResolver();

        mContentResolver.insert(ExerciseContract.URI_TABLE, values);

        Toast.makeText(ExerciseEntryActivity.this, "Exercise saved!", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(ExerciseEntryActivity.this, ExerciseDiaryActivity.class));

    }
}
