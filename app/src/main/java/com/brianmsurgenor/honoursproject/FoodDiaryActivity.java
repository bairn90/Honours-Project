package com.brianmsurgenor.honoursproject;

import android.os.Bundle;

public class FoodDiaryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_food_diary);
    }

}
