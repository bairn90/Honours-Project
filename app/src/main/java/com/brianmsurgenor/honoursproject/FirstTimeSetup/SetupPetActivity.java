package com.brianmsurgenor.honoursproject.FirstTimeSetup;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.brianmsurgenor.honoursproject.CommonBaseClasses.BaseActivity;
import com.brianmsurgenor.honoursproject.DBContracts.UserContract;
import com.brianmsurgenor.honoursproject.Main.MainActivity;
import com.brianmsurgenor.honoursproject.R;

public class SetupPetActivity extends BaseActivity {

    private ContentResolver mContentResolver;
    private static int selectedPet = 0;
    private String petName = "";
    private int petType;
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

        if (selectedPet == 0) {
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

        changeAppIcon();

        startActivity(new Intent(SetupPetActivity.this, MainActivity.class));
    }

    private void changeAppIcon() {

        switch (petType) {

            case R.drawable.frog:
                getPackageManager().setComponentEnabledSetting(
                        new ComponentName("com.brianmsurgenor.honoursproject", "com.brianmsurgenor.honoursproject.Main.MainActivity-Frog"),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                break;

            case R.drawable.puppy:
                getPackageManager().setComponentEnabledSetting(
                        new ComponentName("com.brianmsurgenor.honoursproject", "com.brianmsurgenor.honoursproject.Main.MainActivity-Puppy"),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                break;

        }

    }

    public static void petSelected(int selected) {
        selectedPet = selected;
    }

}
