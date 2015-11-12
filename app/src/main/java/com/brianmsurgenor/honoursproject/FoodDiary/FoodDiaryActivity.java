package com.brianmsurgenor.honoursproject.FoodDiary;

import android.os.Bundle;

import com.brianmsurgenor.honoursproject.CommonBaseClasses.BaseActivity;
import com.brianmsurgenor.honoursproject.R;

public class FoodDiaryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_food_diary);
    }

}
