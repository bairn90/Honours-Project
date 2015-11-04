package com.brianmsurgenor.honoursproject;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

public class MainActivity extends BaseActivity
        implements PetFragment.OnFragmentInteractionListener,
        PedometerFragment.OnFragmentInteractionListener,
        TrophyFragment.OnFragmentInteractionListener {

    private ContentResolver mContentResolver;
    private Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_main);

        mContentResolver = getContentResolver();

        if (startupCheck()) {
            //Set up the users 'home screen'
        } else {
            //Send the user to the first set up screen
            MainActivity.this.finish();
            startActivity(new Intent(MainActivity.this, SetupUserActivity.class));
        }

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        TabsPagerAdapter adapter = new TabsPagerAdapter(getSupportFragmentManager());

        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);

    }

    private boolean startupCheck() {
        //Pass 4 nulls to obtain all columns from the User table, 1st null can be projection
        mCursor = mContentResolver.query(UserContract.URI_TABLE, null, null, null, null);

        //If there is data retrieve it otherwise return false to go to user setup
        if (mCursor.moveToFirst()) {


            return true;
        } else {
            return false;
        }
    }

    public void feedPet(View v) {
        startActivity(new Intent(MainActivity.this, MealEntryActivity.class));
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
