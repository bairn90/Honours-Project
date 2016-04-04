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
import android.widget.TextView;

import com.brianmsurgenor.honoursproject.CommonBaseClasses.AppConstants;
import com.brianmsurgenor.honoursproject.DBContracts.MealContract;
import com.brianmsurgenor.honoursproject.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brian on 27/01/2016.
 */
public class FoodPickerAdapter extends RecyclerView.Adapter<FoodPickerAdapter.ViewHolder> {

    private List<Food> foods;
    private List<String> loadedFoods;
//    private List<Boolean> loadedFoodPicks;
    private List<LinearLayout> backgroundList;
    private Context mContext;
    private AppConstants appConstants;
    private int mealID;

    public FoodPickerAdapter(Context context, int mealID) {
        super();

        appConstants = new AppConstants();
        foods = new ArrayList<>();
        loadedFoods = new ArrayList<>();
//        loadedFoodPicks = new ArrayList<>();
        backgroundList = new ArrayList<>();
        mContext = context;
        this.mealID = mealID;

        foods = appConstants.getFoods();
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

        final int id = foods.get(i).getPicture();
        final String foodCategory = foods.get(i).getCategory();
        boolean selected = false;
        viewHolder.imgThumbnail.setImageResource(id);
        viewHolder.txtName.setText(foods.get(i).getName());
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
                    selected = true;
                    MealEntryActivity.selectedFood(true, "" + id, foodCategory);
                }
            }
        }

        final boolean finalSelected = selected;
        viewHolder.foodHolder.setOnClickListener(new View.OnClickListener() {
            boolean clickSelected = finalSelected;

            @Override
            public void onClick(View v) {
                if(!clickSelected) {
                    clickSelected = true;
                    viewHolder.foodBackground.setBackgroundColor(Color.YELLOW);
                    MealEntryActivity.selectedFood(true,"" + id, foodCategory);
                } else {
                    clickSelected = false;
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
        String where = MealContract.Columns.MEAL_ID + " = " + mealID;
        mCursor = mContentResolver.query(MealContract.URI_TABLE, projection , where, null, null);

        if(mCursor.moveToFirst()) {
            do {
                loadedFoods.add(mCursor.getString(mCursor.getColumnIndex(MealContract.Columns.MEAL_ITEM)));
            } while (mCursor.moveToNext());
        }

    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgThumbnail;
        public TextView txtName;
        public CardView foodHolder;
        public LinearLayout foodBackground;

        public ViewHolder(View itemView) {
            super(itemView);
            foodHolder = (CardView) itemView.findViewById(R.id.item_CardView);
            foodBackground = (LinearLayout) itemView.findViewById(R.id.linearItem);
            imgThumbnail = (ImageView) itemView.findViewById(R.id.itemImage);
            txtName = (TextView) itemView.findViewById(R.id.itemName);
        }
    }
}
