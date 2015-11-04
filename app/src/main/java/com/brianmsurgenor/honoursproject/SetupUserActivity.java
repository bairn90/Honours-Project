package com.brianmsurgenor.honoursproject;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SetupUserActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {

    private TextView txtName, txtDOB;
    private Spinner txtGender;
    private String DOB, name, gender;
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
        getMenuInflater().inflate(R.menu.menu_setup_user, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        DOB = dayOfMonth + "/" + monthOfYear + "/" + year;
        txtDOB.setText(DOB);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void saveDetails(View v) {
        name = txtName.getText().toString();
        gender = txtGender.getSelectedItem().toString();

        ContentValues values = new ContentValues();
        values.put(UserContract.Columns.USERNAME, name);
        values.put(UserContract.Columns.GENDER, gender);
        values.put(UserContract.Columns.DOB, DOB);

        Uri returned = mContentResolver.insert(UserContract.URI_TABLE, values);

        Toast.makeText(SetupUserActivity.this, returned.toString(), Toast.LENGTH_SHORT).show();

        startActivity(new Intent(SetupUserActivity.this, SetupPetActivity.class));

    }

    @Override
    public void onBackPressed() {
        return;
    }
}
