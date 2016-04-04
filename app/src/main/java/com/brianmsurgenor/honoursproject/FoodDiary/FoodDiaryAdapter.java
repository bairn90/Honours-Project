package com.brianmsurgenor.honoursproject.FoodDiary;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.brianmsurgenor.honoursproject.DBContracts.MealContract;
import com.brianmsurgenor.honoursproject.DBContracts.MealDateContract;
import com.brianmsurgenor.honoursproject.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Brian on 08/02/2016.
 */
public class FoodDiaryAdapter extends RecyclerView.Adapter<FoodDiaryAdapter.ViewHolder> {

    private Context mContext;
    private ContentResolver mContentResolver;
    private Cursor mCursor, catCursor;
    private ArrayList<Meal> meals;
    private Calendar timeCalendar;

    public FoodDiaryAdapter(ContentResolver contentResolver, Context context) {
        super();

        timeCalendar = Calendar.getInstance();
        SimpleDateFormat formating = new SimpleDateFormat("HH:mm");
        String typeTime;
        String[] catProjection = {MealContract.Columns.MEAL_CATEGORY};

        mContext = context;
        mContentResolver = contentResolver;
        meals = new ArrayList<>();

        mCursor = mContentResolver.query(MealDateContract.URI_TABLE, null, null, null,
                MealDateContract.Columns.MEAL_TIME + " DESC");

        if (mCursor.moveToFirst()) {
            do {
                Meal m = new Meal();
                m.setId(mCursor.getInt(mCursor.getColumnIndex(MealDateContract.Columns._ID)));
                m.setDate(mCursor.getString(mCursor.getColumnIndex(MealDateContract.Columns.MEAL_DATE)));
                timeCalendar.setTimeInMillis(mCursor.getLong(mCursor.getColumnIndex(MealDateContract.Columns.MEAL_TIME)));
                typeTime = mCursor.getString(mCursor.getColumnIndex(MealDateContract.Columns.MEAL_TYPE)) + " - " +
                        formating.format(timeCalendar.getTime());
                m.setType(typeTime);
                m.setRealTime(timeCalendar.getTimeInMillis());

                //Get the categories for the foods in the meal just obtained
                ArrayList<String> categoryList = new ArrayList<>();
                String filter = MealContract.Columns.MEAL_ID + " = " + m.getId();

                catCursor = mContentResolver.query(MealContract.URI_TABLE, catProjection, filter, null, null);

                if (catCursor.moveToFirst()) {
                    do {
                        categoryList.add(catCursor.getString(catCursor.getColumnIndex(MealContract.Columns.MEAL_CATEGORY)));
                    } while (catCursor.moveToNext());
                }

                m.setCategories(categoryList);

                meals.add(m);
            } while (mCursor.moveToNext());
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.food_diary_adapter, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        Meal m = meals.get(i);

        final String date = m.getDate();
        final String type = m.getType();
        final long realTime = m.getRealTime();
        final ArrayList<String> listCategories = m.getCategories();
        final int mealID = m.getId();
        int gCount = 0;
        int oCount = 0;
        int rCount = 0;

        viewHolder.mealLayout.removeAllViews();

        viewHolder.mealDate.setText(date);
        viewHolder.mealTypeTime.setText(type);

            for (int x = 0; x < listCategories.size(); x++) {

                switch (listCategories.get(x)) {
                    case "green":
                        gCount++;
                        break;
                    case "orange":
                        oCount++;
                        break;

                    case "red":
                        rCount++;
                        break;
                }
            }

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(120, 120);
            layoutParams.setMargins(10, 10, 10, 10);
            if (gCount > 0) {

                RelativeLayout greenDot = new RelativeLayout(mContext);
                greenDot.setBackgroundResource(R.drawable.green);
                greenDot.setGravity(Gravity.CENTER);
                TextView txtGCount = new TextView(mContext);
                txtGCount.setText(gCount + "");
                greenDot.addView(txtGCount);

                viewHolder.mealLayout.addView(greenDot, layoutParams);
            }

            if (oCount > 0) {
                RelativeLayout orangeDot = new RelativeLayout(mContext);
                orangeDot.setBackgroundResource(R.drawable.orange);
                orangeDot.setGravity(Gravity.CENTER);
                TextView txtOCount = new TextView(mContext);
                txtOCount.setText(oCount + "");
                orangeDot.addView(txtOCount);

                viewHolder.mealLayout.addView(orangeDot, layoutParams);
            }

            if (rCount > 0) {
                RelativeLayout redDot = new RelativeLayout(mContext);
                redDot.setBackgroundResource(R.drawable.red);
                redDot.setGravity(Gravity.CENTER);
                TextView txtRCount = new TextView(mContext);
                txtRCount.setText(rCount + "");
                redDot.addView(txtRCount);

                viewHolder.mealLayout.addView(redDot, layoutParams);
            }

            viewHolder.mealHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editMeal(type, mealID, realTime);
                }
            });

            viewHolder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    MenuItem edit = menu.add(0, 0, 0, "Edit");//groupId, itemId, order, title
                    edit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            editMeal(type, mealID, realTime);
                            return false;
                        }
                    });
                    MenuItem delete = menu.add(0, 1, 0, "Delete");
                    delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            deleteMeal(mealID);
                            return false;
                        }
                    });
                }
            });

    }



    private void editMeal(String type, int mealID, long time) {
        Intent intent = new Intent(mContext.getApplicationContext(), MealEntryActivity.class);
        intent.putExtra(MealDateContract.Columns._ID, mealID);
        String[] splitType = type.split(" ");
        intent.putExtra(MealDateContract.Columns.MEAL_TYPE, splitType[0]);
        intent.putExtra(MealDateContract.Columns.MEAL_TIME, time);
        mContext.startActivity(intent);
    }

    private void deleteMeal(int mealID) {

        final int mealIDF = mealID;

        new AlertDialog.Builder(mContext)
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setTitle("Delete meal")
                .setMessage("Are you sure you want to delete this meal?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = MealContract.Meal.buildMealUri(mealIDF + "");
                        mContentResolver.delete(uri, null, null);

                        uri = MealDateContract.MealDate.buildMealDateUri(mealIDF + "");
                        mContentResolver.delete(uri, null, null);
                        Toast.makeText(mContext.getApplicationContext(), "Meal has been deleted", Toast.LENGTH_SHORT).show();
                        mContext.startActivity(new Intent(mContext.getApplicationContext(), FoodDiaryActivity.class));
                    }

                })
                .setNegativeButton("No", null)
                .show();

    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public CardView mealHolder;
        public TextView mealDate, mealTypeTime;
        public LinearLayout mealLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            mealHolder = (CardView) itemView.findViewById(R.id.item_CardView);
            mealDate = (TextView) itemView.findViewById(R.id.mealDate);
            mealTypeTime = (TextView) itemView.findViewById(R.id.mealType);
            mealLayout = (LinearLayout) itemView.findViewById(R.id.categoryLayout);
        }
    }

}
