package com.brianmsurgenor.honoursproject.FoodDiary;

import android.content.ContentResolver;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.brianmsurgenor.honoursproject.CommonBaseClasses.BaseActivity;
import com.brianmsurgenor.honoursproject.R;

public class FoodDiaryActivity extends BaseActivity {

    private ContentResolver mContentResolver;
    private FoodDiaryAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_food_diary);

        mContentResolver = getContentResolver();
        adapter = new FoodDiaryAdapter(mContentResolver, FoodDiaryActivity.this);
        recyclerView = (RecyclerView) findViewById(R.id.food_diary_recycler_view);
        mLayoutManager = new GridLayoutManager(FoodDiaryActivity.this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
    }

}
