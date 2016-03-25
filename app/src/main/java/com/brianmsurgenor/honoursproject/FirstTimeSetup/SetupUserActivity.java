package com.brianmsurgenor.honoursproject.FirstTimeSetup;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.brianmsurgenor.honoursproject.CommonBaseClasses.BaseActivity;
import com.brianmsurgenor.honoursproject.DBContracts.UserContract;
import com.brianmsurgenor.honoursproject.Main.MainActivity;
import com.brianmsurgenor.honoursproject.R;

public class SetupUserActivity extends BaseActivity {

    private TextView txtName, txtPetName;
    private String name, petName;
    private int petType;
    private static int selectedPet = 0;
    private ContentResolver mContentResolver;
    private PetPickerGridAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_user);
        activateToolbar();

        txtName = (TextView) findViewById(R.id.usernameSetup);
        txtPetName = (TextView) findViewById(R.id.petnameSetup);

        mContentResolver = getContentResolver();
        recyclerView = (RecyclerView) findViewById(R.id.pet_picker_recycler_view);
        mLayoutManager = new GridLayoutManager(SetupUserActivity.this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new PetPickerGridAdapter(mContentResolver, SetupUserActivity.this);
        recyclerView.setAdapter(adapter);
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
            saveDetailCheck();
        }

        return true;
    }

    private void saveDetailCheck() {
        name = txtName.getText().toString();
        petName = txtPetName.getText().toString();

        if (selectedPet == 0) {
            Toast.makeText(SetupUserActivity.this, "Please select a pet!", Toast.LENGTH_LONG).show();
            return;
        } else {
            petType = selectedPet;
        }

        if(name.equals("") || petName.equals("")) {
            Toast.makeText(SetupUserActivity.this, "Please enter both your name and " +
                    "a name for your pet!", Toast.LENGTH_LONG).show();
            return;
        }

        saveDetails();
    }

    private void saveDetails() {


        ContentValues values = new ContentValues();
        values.put(UserContract.Columns.USERNAME, name);
        values.put(UserContract.Columns.PET_NAME, petName);
        values.put(UserContract.Columns.PET_TYPE, petType);
        values.put(UserContract.Columns.CUSTOM_COLOUR,0);

        mContentResolver.insert(UserContract.URI_TABLE, values);

        changeAppIcon();

        startActivity(new Intent(SetupUserActivity.this, MainActivity.class));

    }

    @Override
    public void onBackPressed() {
        return;
    }

    public static void petSelected(int selected) {
        selectedPet = selected;
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

}
