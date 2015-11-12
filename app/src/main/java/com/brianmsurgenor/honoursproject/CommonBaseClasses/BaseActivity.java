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
import com.brianmsurgenor.honoursproject.FoodDiary.FoodDiaryActivity;
import com.brianmsurgenor.honoursproject.FoodDiary.MealEntryActivity;
import com.brianmsurgenor.honoursproject.Main.MainActivity;
import com.brianmsurgenor.honoursproject.R;
import com.brianmsurgenor.honoursproject.Settings.SettingsActivity;

import yuku.ambilwarna.AmbilWarnaDialog;

/**
 * Created by Brian on 30/10/2015.
 */
public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private int customColour;
    private Cursor mCursor;
    private ContentResolver mContentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_base_activity);

        mContentResolver = getContentResolver();

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        mNavigationView = (NavigationView) findViewById(R.id.navigation);
        mNavigationView.setNavigationItemSelectedListener(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);

        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        setColour();
    }

    /**
     * called in extending activities instead of setContentView
     *
     * @param layoutId The content Layout Id of extending activities
     */
    public void addContentView(int layoutId) {
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = inflater.inflate(layoutId, null, false);
        mDrawerLayout.addView(contentView, 0);
    }

    protected Toolbar activateToolbar(String title) {

        if (toolbar == null) {
            toolbar = (Toolbar) findViewById(R.id.app_bar);
            toolbar.setTitle(title);
            //toolbar.setLogo(R.mipmap.ic_launcher);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
            }
        }

        return toolbar;
    }

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

    private void launchColourDialog() {
        int initialColor = getResources().getColor(R.color.toolbar);

        // initialColor is the initially-selected color to be shown in the rectangle on the left of the arrow.
        // for example, 0xff000000 is black, 0xff0000ff is blue. Please be aware of the initial 0xff which is the alpha.
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, initialColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int colour) {
                MainActivity.changeTabsColour(colour);
                changeColours(colour);
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                return;
            }
        });

        dialog.show();
    }

    private void changeColours(int colour) {

        //change the colours currenty on the screen
        toolbar.setBackgroundColor(colour);
        findViewById(R.id.navHeader).setBackgroundColor(colour);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(colour);
        }

        //Update the database with the users new customer colour
        ContentValues values = new ContentValues();
        values.put(UserContract.Columns.CUSTOM_COLOUR, colour);
        mContentResolver.update(UserContract.URI_TABLE, values, null, null);
    }

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
