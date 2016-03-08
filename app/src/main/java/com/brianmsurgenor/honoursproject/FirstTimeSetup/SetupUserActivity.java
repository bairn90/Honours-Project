package com.brianmsurgenor.honoursproject.FirstTimeSetup;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.brianmsurgenor.honoursproject.CommonBaseClasses.BaseActivity;
import com.brianmsurgenor.honoursproject.DBContracts.UserContract;
import com.brianmsurgenor.honoursproject.R;

import java.util.Calendar;

public class SetupUserActivity extends BaseActivity {

    private Spinner txtGender;
    private TextView txtName;
    private static TextView txtDOB;
    private static String DOB;
    private String name, gender;
    private ContentResolver mContentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_user);
        activateToolbar();

        txtName = (TextView) findViewById(R.id.usernameSetup);
        txtDOB = (TextView) findViewById(R.id.DOBSetup);
        txtGender = (Spinner) findViewById(R.id.genderSetup);
        mContentResolver = getContentResolver();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setup_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.saveSetup) {
            saveDetails();
        }

        return true;
    }

    public void showDatePickerDialog(View v) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int yearDialog, int monthOfYear, int dayOfMonth) {
                monthOfYear++;
                DOB = dayOfMonth + "/" + monthOfYear + "/" + yearDialog;
                txtDOB.setText(DOB);
            }
        };

        DatePickerDialog dateDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT,
                dateListener, year,month,day);
        dateDialog.show();
    }

    public void saveDetails() {
        name = txtName.getText().toString();
        gender = txtGender.getSelectedItem().toString();

        ContentValues values = new ContentValues();
        values.put(UserContract.Columns.USERNAME, name);
        values.put(UserContract.Columns.GENDER, gender);
        values.put(UserContract.Columns.DOB, DOB);
        values.put(UserContract.Columns.CUSTOM_COLOUR,0);

        Uri returned = mContentResolver.insert(UserContract.URI_TABLE, values);

        Toast.makeText(SetupUserActivity.this, returned.toString(), Toast.LENGTH_SHORT).show();

        startActivity(new Intent(SetupUserActivity.this, SetupPetActivity.class));

    }

    @Override
    public void onBackPressed() {
        return;
    }

}
