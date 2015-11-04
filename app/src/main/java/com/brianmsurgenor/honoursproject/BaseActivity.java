package com.brianmsurgenor.honoursproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by Brian on 30/10/2015.
 */
public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_base_activity);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        mNavigationView = (NavigationView) findViewById(R.id.navigation);
        mNavigationView.setNavigationItemSelectedListener(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);

        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     * called in extending activities instead of setContentView
     * @param layoutId The content Layout Id of extending activities
     */
    public void addContentView(int layoutId) {
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = inflater.inflate(layoutId, null, false);
        mDrawerLayout.addView(contentView, 0);
    }

    protected Toolbar activateToolbar() {

        if(toolbar == null) {
            toolbar = (Toolbar) findViewById(R.id.app_bar);
            //toolbar.setLogo(R.mipmap.ic_launcher);
            if(toolbar != null) {
                setSupportActionBar(toolbar);
            }
        }

        return toolbar;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        mDrawerLayout.closeDrawers();

        switch (menuItem.getItemId()) {
            case R.id.MyPet:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
            case R.id.Settings:
                startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                break;
        }

        return false;
    }
}
