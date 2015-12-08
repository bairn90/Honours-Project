package com.brianmsurgenor.honoursproject.FirstTimeSetup;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.brianmsurgenor.honoursproject.CommonBaseClasses.BaseActivity;
import com.brianmsurgenor.honoursproject.DBContracts.TrophyContract;
import com.brianmsurgenor.honoursproject.DBContracts.UserContract;
import com.brianmsurgenor.honoursproject.Main.MainActivity;
import com.brianmsurgenor.honoursproject.R;
import com.brianmsurgenor.honoursproject.Trophy.Trophies;

import java.util.LinkedList;

public class SetupPetActivity extends BaseActivity {

    private ContentResolver mContentResolver;
    private LinkedList<String> trophyNames, trophyDescriptions;
    private String selectedPet = null;
    private String petName = "";
    private String petType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_setup_pet);

        mContentResolver = getContentResolver();
    }


    @Override
    public void onBackPressed() {
        return;
    }

    public void savePet(View v) {

        if(selectedPet == null) {
            Toast.makeText(SetupPetActivity.this, "Please select a pet!", Toast.LENGTH_LONG).show();
            return;
        } else {
            petType = selectedPet;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Give your pet a name!");

        // Set up the input
        final EditText input = new EditText(this);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                petName = input.getText().toString();
                savePetData();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void savePetData() {
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

    public void petSelected (View view) {

        CardView card;
        unselectAllPets();

        switch(view.getId()) {
            case R.id.pet1:
                card = (CardView) findViewById(R.id.pet1C);
                card.setCardBackgroundColor(Color.YELLOW);
                selectedPet = "pet1";
                break;
            case R.id.pet2:
                card = (CardView) findViewById(R.id.pet2C);
                card.setCardBackgroundColor(Color.YELLOW);
                selectedPet = "pet2";
                break;
        }

    }

    public void unselectAllPets() {
        int petNums = 2;
        CardView card;

        card = (CardView) findViewById(R.id.pet1C);
        card.setCardBackgroundColor(Color.WHITE);

        card = (CardView) findViewById(R.id.pet2C);
        card.setCardBackgroundColor(Color.WHITE);
    }
}
