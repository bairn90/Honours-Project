package com.brianmsurgenor.honoursproject;

import android.os.Bundle;

public class MealEntryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_meal_entry);
    }

}
