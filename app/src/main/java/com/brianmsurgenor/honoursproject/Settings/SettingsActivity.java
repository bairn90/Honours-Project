package com.brianmsurgenor.honoursproject.Settings;

import android.content.ContentResolver;
import android.os.Bundle;

import com.brianmsurgenor.honoursproject.CommonBaseClasses.BaseActivity;
import com.brianmsurgenor.honoursproject.R;

public class SettingsActivity extends BaseActivity {

    private ContentResolver mContentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_settings);

        mContentResolver = getContentResolver();
    }

}
