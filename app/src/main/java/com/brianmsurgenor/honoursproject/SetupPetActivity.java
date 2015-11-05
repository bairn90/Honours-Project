package com.brianmsurgenor.honoursproject;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SetupPetActivity extends BaseActivity {

    private TextView txtPetName, txtPetType;
    private ContentResolver mContentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_pet);
        activateToolbar("Choose your pet");

        txtPetName = (TextView) findViewById(R.id.petnameSetup);
        txtPetType = (TextView) findViewById(R.id.petType);
        mContentResolver = getContentResolver();
    }


    @Override
    public void onBackPressed() {
        return;
    }

    public void savePet(View v) {
        String petName, petType;

        petName = txtPetName.getText().toString();
        petType = txtPetType.getText().toString();

        ContentValues values = new ContentValues();
        values.put(UserContract.Columns.PET_NAME, petName);
        values.put(UserContract.Columns.PET_TYPE, petType);

        mContentResolver.update(UserContract.URI_TABLE, values, null, null);

        startActivity(new Intent(SetupPetActivity.this,MainActivity.class));
    }
}
