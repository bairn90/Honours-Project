package com.brianmsurgenor.honoursproject;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

public class SettingsActivity extends BaseActivity {

    private ContentResolver mContentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_settings);

        mContentResolver = getContentResolver();
    }

    public void deleteDatabase(View view) {

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Delete all of your data?")
                .setMessage("Are you sure you want to delete all of your data including your " +
                        "food diaries, pet and pedometer readings")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mContentResolver.delete(UserContract.URI_TABLE, null, null);
                        startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                    }

                })
                .setNegativeButton("No", null)
                .show();


    }

}
