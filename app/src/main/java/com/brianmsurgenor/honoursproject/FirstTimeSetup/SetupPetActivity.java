package com.brianmsurgenor.honoursproject.FirstTimeSetup;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
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
    private static String selectedPet = null;
    private String petName = "";
    private String petType;
    private PetPickerGridAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_pet);
        activateToolbar();

        mContentResolver = getContentResolver();
        recyclerView = (RecyclerView) findViewById(R.id.pet_picker_recycler_view);
        mLayoutManager = new GridLayoutManager(SetupPetActivity.this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new PetPickerGridAdapter(mContentResolver, SetupPetActivity.this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setup_pet_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.savePet) {
            savePet();
        }

        return true;
    }

    public void savePet() {

        if (selectedPet == null) {
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

        for (int i = 0; i < trophyNames.size(); i++) {
            ContentValues trophyValues = new ContentValues();
            trophyValues.put(TrophyContract.Columns.TROPHY_NAME, trophyNames.get(i));
            trophyValues.put(TrophyContract.Columns.TROPHY_DESCRIPTION, trophyDescriptions.get(i));
            trophyValues.put(TrophyContract.Columns.ACHIEVED, 0);

            mContentResolver.insert(TrophyContract.URI_TABLE, trophyValues);
        }

        startActivity(new Intent(SetupPetActivity.this, MainActivity.class));
    }

    public static void petSelected(String selected) {
        selectedPet = selected;
    }

}
