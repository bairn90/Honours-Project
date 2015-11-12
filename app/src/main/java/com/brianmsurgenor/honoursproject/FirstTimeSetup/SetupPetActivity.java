package com.brianmsurgenor.honoursproject.FirstTimeSetup;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.brianmsurgenor.honoursproject.CommonBaseClasses.BaseActivity;
import com.brianmsurgenor.honoursproject.Main.MainActivity;
import com.brianmsurgenor.honoursproject.R;
import com.brianmsurgenor.honoursproject.Trophy.Trophies;
import com.brianmsurgenor.honoursproject.DBContracts.TrophyContract;
import com.brianmsurgenor.honoursproject.DBContracts.UserContract;

import java.util.LinkedList;

public class SetupPetActivity extends BaseActivity {

    private TextView txtPetName, txtPetType;
    private ContentResolver mContentResolver;
    private LinkedList<String> trophyNames, trophyDescriptions;

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

        Trophies setupTrophies = new Trophies();
        trophyNames = setupTrophies.getTrophyNames();
        trophyDescriptions = setupTrophies.getTrophyDescriptions();

        for(int i=0;i<trophyNames.size();i++) {
            ContentValues trophyValues = new ContentValues();
            trophyValues.put(TrophyContract.Columns.TROPHY_NAME,trophyNames.get(i));
            trophyValues.put(TrophyContract.Columns.TROPHY_DESCRIPTION,trophyDescriptions.get(i));
            trophyValues.put(TrophyContract.Columns.ACHIEVED,0);

            mContentResolver.insert(TrophyContract.URI_TABLE,trophyValues);
        }

        startActivity(new Intent(SetupPetActivity.this,MainActivity.class));
    }
}
