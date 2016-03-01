package com.brianmsurgenor.honoursproject.Main;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.brianmsurgenor.honoursproject.CommonBaseClasses.BaseActivity;
import com.brianmsurgenor.honoursproject.DBContracts.TrophyContract;
import com.brianmsurgenor.honoursproject.DBContracts.UserContract;
import com.brianmsurgenor.honoursproject.FirstTimeSetup.SetupPetActivity;
import com.brianmsurgenor.honoursproject.FirstTimeSetup.SetupUserActivity;
import com.brianmsurgenor.honoursproject.FoodDiary.MealEntryActivity;
import com.brianmsurgenor.honoursproject.R;
import com.brianmsurgenor.honoursproject.Trophy.Trophies;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private ContentResolver mContentResolver;
    private Cursor mCursor;
    private static TabLayout tabs;
    private int customColour;

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
            //Check if the user was sent here via nav bar and so send to appropriate tab
            Bundle tabData = getIntent().getExtras();
            if(tabData != null) {
                pager.setCurrentItem(tabData.getInt("tab"));
            }

            //Run the colour check
            if(customColour != 0) {
                changeTabsColour(customColour);
            }

            //Check that there are no new trophies to be added to the database
            trophyChecker();

        } else if(check == 1) {
            //Send the user to the pet set up screen
            MainActivity.this.finish();
            startActivity(new Intent(MainActivity.this, SetupPetActivity.class));
        } else {
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

            //Data found but no pet data
            if (mCursor.getString(mCursor.getColumnIndex(UserContract.Columns.PET_NAME)) == null) {
                return 1;
            }

            //Setup has already been fully complete
            customColour = mCursor.getInt(mCursor.getColumnIndex(UserContract.Columns.CUSTOM_COLOUR));
            return 0;
        } else {
            return 2;
        }
    }

    private void trophyChecker() {
        int trophyCount = 0;
        ArrayList<String> trophyNames = new ArrayList<>();
        String[] projection = {TrophyContract.Columns.TROPHY_NAME};
        mCursor = mContentResolver.query(TrophyContract.URI_TABLE,projection,null,null,null);
        if(mCursor.moveToFirst()) {
            do {
                trophyNames.add(mCursor.getString(mCursor.getColumnIndex(TrophyContract.Columns.TROPHY_NAME)));
                trophyCount++;
            } while(mCursor.moveToNext());
        }

        Trophies trophies = new Trophies(getApplicationContext());
        if(trophyCount != trophies.numberOfTrophies()) {
            trophies.updateTrophiesDatabase(trophyNames);
        }

    }

    /**
     * Called when the user clicks the floating action button in the pet fragement.
     * Send to the meal entry activity
     * @param v
     */
    public void feedPet(View v) {
        startActivity(new Intent(MainActivity.this, MealEntryActivity.class));
    }


}
