package com.brianmsurgenor.honoursproject.FoodDiary;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brianmsurgenor.honoursproject.CommonBaseClasses.BaseActivity;
import com.brianmsurgenor.honoursproject.DBContracts.UserContract;
import com.brianmsurgenor.honoursproject.R;

import java.util.ArrayList;
import java.util.Random;

public class FoodDiaryActivity extends BaseActivity {

    private ContentResolver mContentResolver;
    private FoodDiaryAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> gFeedback, rFeedback, oFeedback, foods;
    private int green, orange, red;
    private String uName, petName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_food_diary);

        gFeedback = new ArrayList<>();
        rFeedback = new ArrayList<>();
        oFeedback = new ArrayList<>();
        foods = new ArrayList<>();
        feedbackArrays();

        mContentResolver = getContentResolver();
        adapter = new FoodDiaryAdapter(mContentResolver, FoodDiaryActivity.this);
        recyclerView = (RecyclerView) findViewById(R.id.food_diary_recycler_view);
        mLayoutManager = new GridLayoutManager(FoodDiaryActivity.this, 1);
        recyclerView.setLayoutManager(mLayoutManager);

        Bundle args = getIntent().getExtras();
        if(args != null) {
            green = args.getInt("green");
            orange = args.getInt("orange");
            red = args.getInt("red");
            feedback();
        }

        if(adapter.getItemCount() == 0) {
            TextView t = new TextView(this);
            t.setText("No meals have been entered. Your pet is hungry!");
            LinearLayout ll = (LinearLayout) findViewById(R.id.foodDiaryLayout);
            recyclerView.setVisibility(View.GONE);
            ll.addView(t);
        } else {
            recyclerView.setAdapter(adapter);
        }
    }

    private void feedback() {

        String[] projection = {UserContract.Columns.USERNAME, UserContract.Columns.PET_NAME, UserContract.Columns.PET_TYPE};
        Cursor mCursor = mContentResolver.query(UserContract.URI_TABLE,projection, null, null, null);

        int pet = 0;

        if(mCursor.moveToFirst()) {
            uName = mCursor.getString(mCursor.getColumnIndex(UserContract.Columns.USERNAME));
            petName = mCursor.getString(mCursor.getColumnIndex(UserContract.Columns.PET_NAME));
            pet = mCursor.getInt(mCursor.getColumnIndex(UserContract.Columns.PET_TYPE));
        }

        String feedback;
        if(red > green & red > orange) {
            feedback = createFeedback("red");
        } else if(green > red && green > orange) {
            feedback = createFeedback("green");
        } else {
            feedback = createFeedback("orange");
        }


        AlertDialog.Builder firstOpen = new AlertDialog.Builder(this);
        firstOpen.setMessage(feedback);
        AlertDialog alert = firstOpen.create();
        alert.setTitle("I'm so full!!");
        alert.setIcon(pet);
        alert.show();

    }

    private String createFeedback(String type) {
        Random random = new Random();
        int lower = 0;
        int upper = 2;
        int randomNum = random.nextInt(upper - lower) + lower;
        int upperFood = 5;
        int randomFood = random.nextInt(upperFood - lower) + lower;

        switch (type) {

            case "red":
                return rFeedback.get(randomNum) + foods.get(randomFood);

            case "orange":
                return oFeedback.get(randomNum);

            case "green":
                return gFeedback.get(randomNum);

            default:
                return "Error, unknown type";
        }

    }

    private void feedbackArrays() {
        gFeedback.add("Yum!! Thanks for keeping me healthy " + uName);
        gFeedback.add("Woo! I really enjoyed that " + uName + "!");
        gFeedback.add("I'm quite full, but that was so healthy I almost want more!");

        rFeedback.add("WOAH! So much sugar! You know " + uName + " I'd really quite like some ");
        rFeedback.add("I hope that was just a treat " + uName + "! I was really looking forward to ");
        rFeedback.add("I just really wanted some ");

        oFeedback.add("Ok " + uName + ", so that was ok, any chance of some ");
        oFeedback.add("Yum! Thanks + " + uName + ". Maybe next time some ");
        oFeedback.add("Maybe there's still time for some ");

        foods.add("Broccoli");
        foods.add("Carrots");
        foods.add("Apple");
        foods.add("Orange");
        foods.add("Pears");
        foods.add("Mushrooms");
    }

}
