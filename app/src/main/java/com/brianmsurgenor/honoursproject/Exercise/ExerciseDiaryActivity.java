package com.brianmsurgenor.honoursproject.Exercise;

import android.content.ContentResolver;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brianmsurgenor.honoursproject.CommonBaseClasses.BaseActivity;
import com.brianmsurgenor.honoursproject.R;

public class ExerciseDiaryActivity extends BaseActivity {

    private ContentResolver mContentResolver;
    private ExerciseDiaryAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_exercise_diary);

        mContentResolver = getContentResolver();
        adapter = new ExerciseDiaryAdapter(mContentResolver, ExerciseDiaryActivity.this);
        recyclerView = (RecyclerView) findViewById(R.id.exercise_diary_recycler_view);
        mLayoutManager = new GridLayoutManager(ExerciseDiaryActivity.this, 1);
        recyclerView.setLayoutManager(mLayoutManager);

        if(adapter.getItemCount() == 0) {
            TextView t = new TextView(this);
            t.setText("No exercises have been logged");
            recyclerView.setVisibility(View.GONE);
            LinearLayout ll = (LinearLayout) findViewById(R.id.exDiaryLayout);
            ll.addView(t);
        } else {
            recyclerView.setAdapter(adapter);
        }
    }
}
