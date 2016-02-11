package com.brianmsurgenor.honoursproject.FoodDiary;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.brianmsurgenor.honoursproject.CommonBaseClasses.AppConstants;
import com.brianmsurgenor.honoursproject.DBContracts.MealContract;
import com.brianmsurgenor.honoursproject.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brian on 27/01/2016.
 */
public class FoodPickerAdapter extends RecyclerView.Adapter<FoodPickerAdapter.ViewHolder> {

    private List<Integer> foodPics;
    private List<String> foodCategories, loadedFoods;
    private List<Boolean> loadedFoodPicks;
    private List<LinearLayout> backgroundList;
    private Context mContext;
    private AppConstants appConstants;
    private int mealID;

    public FoodPickerAdapter(Context context, int mealID) {
        super();

        appConstants = new AppConstants();
        foodPics = new ArrayList<>();
        foodCategories = new ArrayList<>();
        loadedFoods = new ArrayList<>();
        loadedFoodPicks = new ArrayList<>();
        backgroundList = new ArrayList<>();
        mContext = context;
        this.mealID = mealID;

        foodPics = appConstants.getFoodPics();
        foodCategories = appConstants.getFoodCategories();
        if(mealID != 0) {
            loadFoods();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.picker_adapter, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        final int id = foodPics.get(i);
        final String foodCategory = foodCategories.get(i);
        viewHolder.imgThumbnail.setImageResource(id);
        backgroundList.add(viewHolder.foodBackground);

        /*
         * To highlight the selected foods when user has come to this screen from the diary
         * selected food method called here but will only call when the user scroll and the item
         * appears on screen (need to move)
         */
        if(mealID != 0) {
            for (String food: loadedFoods) {
                if(food.equals(id + "")) {
                    viewHolder.foodBackground.setBackgroundColor(Color.YELLOW);
                    MealEntryActivity.selectedFood(true, "" + id, foodCategory);
                }
            }
        }

        viewHolder.foodHolder.setOnClickListener(new View.OnClickListener() {
            boolean selected = false;

            @Override
            public void onClick(View v) {
                if(!selected) {
                    selected = true;
                    viewHolder.foodBackground.setBackgroundColor(Color.YELLOW);
                    MealEntryActivity.selectedFood(true,"" + id, foodCategory);
                } else {
                    selected = false;
                    viewHolder.foodBackground.setBackgroundColor(mContext.getResources().
                            getColor(R.color.primaryBackground));
                    MealEntryActivity.selectedFood(false, "" + id, foodCategory);
                }
            }
        });

    }

    private void loadFoods() {

        ContentResolver mContentResolver = mContext.getContentResolver();
        Cursor mCursor;

        String[] projection = {MealContract.Columns.MEAL_ITEM};
        String selection = MealContract.Columns.MEAL_ID + " = " + mealID;

        mCursor = mContentResolver.query(MealContract.URI_TABLE, projection , selection, null, null);

        if(mCursor.moveToFirst()) {
            do {
                loadedFoods.add(mCursor.getString(mCursor.getColumnIndex(MealContract.Columns.MEAL_ITEM)));
            } while (mCursor.moveToNext());
        }

    }

    @Override
    public int getItemCount() {
        return foodPics.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgThumbnail;
        public CardView foodHolder;
        public LinearLayout foodBackground;

        public ViewHolder(View itemView) {
            super(itemView);
            foodHolder = (CardView) itemView.findViewById(R.id.item_CardView);
            foodBackground = (LinearLayout) itemView.findViewById(R.id.linearItem);
            imgThumbnail = (ImageView) itemView.findViewById(R.id.itemImage);
        }
    }
}
