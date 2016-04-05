package com.brianmsurgenor.honoursproject.FirstTimeSetup;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.brianmsurgenor.honoursproject.CommonBaseClasses.BaseActivity;
import com.brianmsurgenor.honoursproject.DBContracts.UserContract;
import com.brianmsurgenor.honoursproject.Main.MainActivity;
import com.brianmsurgenor.honoursproject.R;

/**
 * Activity to set up the users details and pet, uses the petPicker adapter to display the pets
 * in the recyclerView
 */
public class SetupUserActivity extends BaseActivity {

    private TextView txtName, txtPetName;
    private String name, petName;
    private int petType;
    private static int selectedPet = 0;
    private ContentResolver mContentResolver;
    private PetPickerAdapter adapter;
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

        //Sets up the adapter for the recylerView to show the pets
        recyclerView = (RecyclerView) findViewById(R.id.pet_picker_recycler_view);
        mLayoutManager = new GridLayoutManager(SetupUserActivity.this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new PetPickerAdapter(SetupUserActivity.this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setup_menu, menu);
        return true;
    }

    /*
     * If the user clicks on the save icon in the menu bar call the method
     */
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

        //Ensure that the user has filled in all their details before passing the save method
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

        //Close the keyboard incase the user hasn't dont this already
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(txtPetName.getWindowToken(), 0);

        ContentValues values = new ContentValues();
        values.put(UserContract.Columns.USERNAME, name);
        values.put(UserContract.Columns.PET_NAME, petName);
        values.put(UserContract.Columns.PET_TYPE, petType);
        values.put(UserContract.Columns.CUSTOM_COLOUR, 0);

        mContentResolver.insert(UserContract.URI_TABLE, values);

        changeAppIcon();

        /*
         * Send the user to the main activity and tell it that this is the first time so that setup
         * message can be displayed. 1 is pointess value added because something needs to be there
         */
        Intent intent = new Intent(SetupUserActivity.this, MainActivity.class);
        intent.putExtra("first",1);

        startActivity(intent);
    }

    //Overide the method to ensure the user can click back and find themselves at the main activity
    @Override
    public void onBackPressed() {
        return;
    }

    /*
     * Static method called from the adapter to tell this activity what pet the user has selected
     * or if the user has unselected a pet 0 is passed back
     */
    public static void petSelected(int selected) {
        selectedPet = selected;
    }

    /**
     * This changes the app icon to the users pet and disables the original launcher icon
     */
    private void changeAppIcon() {

        getPackageManager().setComponentEnabledSetting(
                new ComponentName("com.brianmsurgenor.honoursproject", "com.brianmsurgenor.honoursproject.Main.MainActivity-Original"),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

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

            case R.drawable.turtle:
                getPackageManager().setComponentEnabledSetting(
                        new ComponentName("com.brianmsurgenor.honoursproject", "com.brianmsurgenor.honoursproject.Main.MainActivity-Turtle"),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                break;

            case R.drawable.cat:
                getPackageManager().setComponentEnabledSetting(
                        new ComponentName("com.brianmsurgenor.honoursproject", "com.brianmsurgenor.honoursproject.Main.MainActivity-Cat"),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                break;

        }

    }

}
