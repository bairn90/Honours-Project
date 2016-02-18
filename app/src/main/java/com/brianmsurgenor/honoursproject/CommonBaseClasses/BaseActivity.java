package com.brianmsurgenor.honoursproject.CommonBaseClasses;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.brianmsurgenor.honoursproject.DBContracts.UserContract;
import com.brianmsurgenor.honoursproject.Exercise.ExerciseDiaryActivity;
import com.brianmsurgenor.honoursproject.FoodDiary.FoodDiaryActivity;
import com.brianmsurgenor.honoursproject.FoodDiary.MealEntryActivity;
import com.brianmsurgenor.honoursproject.Main.MainActivity;
import com.brianmsurgenor.honoursproject.Pedometer.PedometerFragment;
import com.brianmsurgenor.honoursproject.Pet.PetFragment;
import com.brianmsurgenor.honoursproject.R;
import com.brianmsurgenor.honoursproject.Settings.SettingsActivity;

import yuku.ambilwarna.AmbilWarnaDialog;

/**
 * Created by Brian on 30/10/2015.
 * Class to be inherited by all activities and used to set up the toolbar for the activity and the
 * navigation drawer if needed
 */
public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Cursor mCursor;
    private ContentResolver mContentResolver;
    private int customColour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_base_activity);

        mContentResolver = getContentResolver();

        //Sets the toolbar for any activity using the nav bar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        //Sets up the navbar and adds the listener for the items inside the nav bar
        mNavigationView = (NavigationView) findViewById(R.id.navigation);
        mNavigationView.setNavigationItemSelectedListener(this);

        //Adds the nav bar hamburger icon to the toolbar
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);

        //Adds the listener to the hamburger icon so user can open the nav bar and syncs the state
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        //Sets up the colours if the user has a saved custom colour in the database
        setColour();
    }

    /**
     * called in extending activities instead of setContentView
     * @param layoutId The content Layout Id of extending activities
     */
    public void addContentView(int layoutId) {
        LayoutInflater inflater =
                (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Inflate the layout passed to the method and then add it to the nav drawer layout
        View contentView = inflater.inflate(layoutId, null, false);
        mDrawerLayout.addView(contentView, 0);
    }

    /**
     * Used by an activity where no nav bar id needed. Only sets up toolbar
     */
    public Toolbar activateToolbar() {

        //set the toolbar from the ID & if not already set then add it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        return toolbar;
    }

    /**
     * Gets the colour from the database & if not 0 i.e. a custom colour is saved then call the
     * change colours method
     */
    private void setColour() {
        //Pass 4 nulls to obtain all columns from the User table, 1st null can be projection
        mCursor = mContentResolver.query(UserContract.URI_TABLE, null, null, null, null);

        //If there is data retrieve it otherwise return false to go to user setup
        if (mCursor.moveToFirst()) {
            customColour = mCursor.getInt(mCursor.getColumnIndex(UserContract.Columns.CUSTOM_COLOUR));
            if (customColour != 0) {
                changeColours(customColour);
            }
        }

    }

    /**
     * Called when a user selects an item from the nav drawer. Based on the id of the item selected
     * send user to activity
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        Intent intent;
        mDrawerLayout.closeDrawers();

        switch (menuItem.getItemId()) {
            case R.id.MyPet:
                intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("tab", 0);
                startActivity(intent);
                break;
            case R.id.Pedometer:
                intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("tab", 1);
                startActivity(intent);
                break;
            case R.id.Trophies:
                intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("tab", 2);
                startActivity(intent);
                break;
            case R.id.Games:
                Toast.makeText(getApplicationContext(), "Nothing to see here yet", Toast.LENGTH_SHORT).show();
                break;
            case R.id.FoodDiary:
                startActivity(new Intent(getApplicationContext(), FoodDiaryActivity.class));
                break;
            case R.id.AddMeal:
                startActivity(new Intent(getApplicationContext(), MealEntryActivity.class));
                break;
            case R.id.PedometerDiary:
                startActivity(new Intent(getApplicationContext(), ExerciseDiaryActivity.class));
                break;
            case R.id.ColourChange:
                launchColourDialog();
                break;
            case R.id.ColourReset:
                resetColours();
                break;
            case R.id.Settings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                break;
            case R.id.Delete:
                deleteDatabase();
                break;
        }

        return false;
    }

    /**
     * Launches the dialog box for the user to choose their new app colour theme
     */
    private void launchColourDialog() {
        // If there isn't currently a custom colour set then set it to the default app colour
        if(customColour == 0) {
            customColour = getResources().getColor(R.color.toolbar);
        }

        /*
         * customColour is the initially-selected color to be shown in the rectangle on the left of the arrow.
         * for example, 0xff000000 is black, 0xff0000ff is blue. Please be aware of the initial 0xff which is the alpha.
         */
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, customColour, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int colour) {
                /*
                 * Call the individual change colour methods for any fragment/activity with a button
                 * or other item to change colour scheme
                 */
                MainActivity.changeTabsColour(colour);
                PetFragment.colourChange(colour);
                PedometerFragment.colourChange(colour);
                changeColours(colour);
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                return;
            }
        });

        dialog.show();
    }

    /**
     * Called when user selects new colour to change the toolbar and navbar
     * @param colour the new toolbar colour
     */
    private void changeColours(int colour) {

        //change the colours currenty on the screen
        toolbar.setBackgroundColor(colour);
        findViewById(R.id.navHeader).setBackgroundColor(colour);

        //If the API version is sufficient change the status bar colour too (where notifications are)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(colour);
        }

        //Update the database with the users new customer colour to be loaded up everytime app opens
        ContentValues values = new ContentValues();
        values.put(UserContract.Columns.CUSTOM_COLOUR, colour);
        mContentResolver.update(UserContract.URI_TABLE, values, null, null);
    }

    /**
     * Called when the user selects the reset colours option from the nav bar
     * Asks the user to confirm that they want to reset the colours and the calls the relevant
     * methods
     */
    private void resetColours() {

        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setTitle("Reset the app colours")
                .setMessage("Are you sure you want to reset the app colours back to default?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int originalColour = getResources().getColor(R.color.toolbar);
                        int topBarOriginal = getResources().getColor(R.color.topbar);

                        toolbar.setBackgroundColor(originalColour);
                        MainActivity.changeTabsColour(originalColour);
                        PetFragment.colourChange(originalColour);
                        PedometerFragment.colourChange(originalColour);
                        findViewById(R.id.navHeader).setBackgroundColor(originalColour);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setStatusBarColor(topBarOriginal);
                        }

                        ContentValues values = new ContentValues();
                        values.put(UserContract.Columns.CUSTOM_COLOUR, 0);

                        mContentResolver.update(UserContract.URI_TABLE, values, null, null);
                    }

                })
                .setNegativeButton("No", null)
                .show();


    }

    /**
     * Called when the user selects the delete database option from the nav bar.
     * Asks the user to confirm and deletes all data
     */
    public void deleteDatabase() {

        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setTitle("Delete all of your data?")
                .setMessage("Are you sure you want to delete all of your data including your " +
                        "food diaries, pet and pedometer readings")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mContentResolver.delete(UserContract.URI_TABLE, null, null);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}
