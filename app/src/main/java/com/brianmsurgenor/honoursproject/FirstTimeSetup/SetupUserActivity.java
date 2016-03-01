package com.brianmsurgenor.honoursproject.FirstTimeSetup;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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
        values.put(UserContract.Columns.CUSTOM_COLOUR,0);

        Uri returned = mContentResolver.insert(UserContract.URI_TABLE, values);

        Toast.makeText(SetupUserActivity.this, returned.toString(), Toast.LENGTH_SHORT).show();

        startActivity(new Intent(SetupUserActivity.this, SetupPetActivity.class));

    }

    @Override
    public void onBackPressed() {
        return;
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            month++;
            DOB = day + "/" + month + "/" + year;
            txtDOB.setText(DOB);
        }
    }
}
