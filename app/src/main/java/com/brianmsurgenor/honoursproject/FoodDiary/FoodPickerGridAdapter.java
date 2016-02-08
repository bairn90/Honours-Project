package com.brianmsurgenor.honoursproject.FoodDiary;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.brianmsurgenor.honoursproject.CommonBaseClasses.AppConstants;
import com.brianmsurgenor.honoursproject.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brian on 27/01/2016.
 */
public class FoodPickerGridAdapter extends RecyclerView.Adapter<FoodPickerGridAdapter.ViewHolder> {

    private List<Integer> foodPics;
    private List<String> foodCategories;
    private List<LinearLayout> backgroundList;
    private Context mContext;
    private AppConstants appConstants;

    public FoodPickerGridAdapter(ContentResolver contentResolver, Context context) {
        super();

        appConstants = new AppConstants();
        foodPics = new ArrayList<>();
        foodCategories = new ArrayList<>();
        backgroundList = new ArrayList<>();
        mContext = context;

        foodPics = appConstants.getFoodPics();
        foodCategories = appConstants.getFoodCategories();
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
