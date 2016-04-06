package com.brianmsurgenor.honoursproject.Main;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.brianmsurgenor.honoursproject.CommonBaseClasses.BaseActivity;
import com.brianmsurgenor.honoursproject.DBContracts.UserContract;
import com.brianmsurgenor.honoursproject.FirstTimeSetup.SetupUserActivity;
import com.brianmsurgenor.honoursproject.FoodDiary.MealEntryActivity;
import com.brianmsurgenor.honoursproject.R;
import com.brianmsurgenor.honoursproject.Trophy.Trophies;

public class MainActivity extends BaseActivity {

    private ContentResolver mContentResolver;
    private Cursor mCursor;
    private static TabLayout tabs;
    private int customColour, pet;
    private String uName, petName;
    private int first = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_main);

        mContentResolver = getContentResolver();

        //Set up the tabs inside the Main Activity
        tabs = (TabLayout) findViewById(R.id.tabs);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        TabsPagerAdapter adapter = new TabsPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);

        int check = startupCheck();
        /*
         * If the user has fully set up their pet and profile then run the activity
         * else send to appropriate setup screen
         */
        if (check == 0) {
            /*
             * Check if the user was sent here via nav bar and so send to appropriate tab or
             * if first start up
             */
            Bundle tabData = getIntent().getExtras();
            if(tabData != null) {
                //If first start up display welcome msg otherwise send to correct tab
                if(tabData.getInt("first") == 1) {
                    first = 1;
                    AlertDialog.Builder firstOpen = new AlertDialog.Builder(this);
                    firstOpen.setMessage("Hi " + uName + "! This app will allow you to take care " +
                            "of " + petName + " your new virtual pet! To feed your pet you simply " +
                            "tell the app what you've eaten, to exercise " + petName + " you do the same! " +
                            "On this screen you'll find all the main functions of the application. " +
                            "There are more features to be explored using the icon in the top left. " +
                            "\n\nOH! Before you go, you also now have a new app icon found in " +
                            "Android app drawer!" );
                    AlertDialog alert = firstOpen.create();
                    alert.setTitle("Welcome to health pet");
                    alert.setIcon(pet);
                    alert.show();
                } else {
                    pager.setCurrentItem(tabData.getInt("tab"));
                }
            }

            //Run the colour check
            if(customColour != 0) {
                changeTabsColour(customColour);
            }

            //Check that there are no new trophies to be added to the database
            trophyChecker();

        } else if(check == 1) {
            //Send the user to the first set up screen
            MainActivity.this.finish();
            startActivity(new Intent(MainActivity.this, SetupUserActivity.class));
        }
    }

    public static void changeTabsColour(int colour) {
        tabs.setBackgroundColor(colour);
    }

    /**
     * Checks whether the user has opened the app before and setup their user profile
     * @return 0 when setup complete, 1 when user data but no pet setup, 2 when no setup at all
     */
    private int startupCheck() {
        //Pass 4 nulls to obtain all columns from the User table, 1st null can be projection, 2nd filter
        mCursor = mContentResolver.query(UserContract.URI_TABLE, null, null, null, null);

        //If there is data retrieve it otherwise return false to go to user setup
        if (mCursor.moveToFirst()) {
            //Setup has already been fully complete so check if they have changed the app colours
            customColour = mCursor.getInt(mCursor.getColumnIndex(UserContract.Columns.CUSTOM_COLOUR));
            uName = mCursor.getString(mCursor.getColumnIndex(UserContract.Columns.USERNAME));
            petName = mCursor.getString(mCursor.getColumnIndex(UserContract.Columns.PET_NAME));
            pet = mCursor.getInt(mCursor.getColumnIndex(UserContract.Columns.PET_TYPE));
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * Check whether any new trophies have been added to the database
     */
    private void trophyChecker() {
        Trophies trophies = new Trophies(getApplicationContext());
        trophies.updateTrophyCheck();
    }

    /**
     * Called when the user clicks the floating action button in the pet fragment.
     * Send to the meal entry activity
     * @param v
     */
    public void feedPet(View v) {
        startActivity(new Intent(MainActivity.this, MealEntryActivity.class));
    }

    @Override
    public void onBackPressed() {
        if(first != 1) {
            super.onBackPressed();
        } else {
            return;
        }
    }
}
