package com.brianmsurgenor.honoursproject;

import android.content.ContentResolver;
import android.os.Bundle;

public class SettingsActivity extends BaseActivity {

    private ContentResolver mContentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_settings);

        mContentResolver = getContentResolver();
    }

}
