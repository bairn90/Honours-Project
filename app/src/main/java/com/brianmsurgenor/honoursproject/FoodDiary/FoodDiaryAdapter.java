package com.brianmsurgenor.honoursproject.FoodDiary;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brianmsurgenor.honoursproject.DBContracts.MealContract;
import com.brianmsurgenor.honoursproject.DBContracts.MealDateContract;
import com.brianmsurgenor.honoursproject.R;

import java.util.ArrayList;

/**
 * Created by Brian on 08/02/2016.
 */
public class FoodDiaryAdapter extends RecyclerView.Adapter<FoodDiaryAdapter.ViewHolder> {

    private Context mContext;
    private ContentResolver mContentResolver;
    private Cursor mCursor;
    private ArrayList<Integer> mealIDS;
    private ArrayList<String> mealDates, mealTypes;
    private ArrayList<ArrayList<String>> mealCategories;

    public FoodDiaryAdapter(ContentResolver contentResolver, Context context) {
        super();

        mContext = context;
        mContentResolver = contentResolver;
        mealIDS = new ArrayList<>();
        mealDates = new ArrayList<>();
        mealTypes = new ArrayList<>();
        mealCategories = new ArrayList<>();

        String[] projection = {MealDateContract.Columns._ID, MealDateContract.Columns.MEAL_DATE,
                MealDateContract.Columns.MEAL_TYPE};
        mCursor = mContentResolver.query(MealDateContract.URI_TABLE, projection, null, null,
                MealDateContract.Columns._ID + " DESC");

        if (mCursor.moveToFirst()) {
            do {
                mealIDS.add(mCursor.getInt(mCursor.getColumnIndex(MealDateContract.Columns._ID)));
                mealDates.add(mCursor.getString(mCursor.getColumnIndex(MealDateContract.Columns.MEAL_DATE)));
                mealTypes.add(mCursor.getString(mCursor.getColumnIndex(MealDateContract.Columns.MEAL_TYPE)));
            } while (mCursor.moveToNext());
        }

        String[] catProjection = {MealContract.Columns.MEAL_CATEGORY};
        for (int i = 0; i < mealIDS.size(); i++) {
            ArrayList<String> categoryList = new ArrayList<>();
            String filter = MealContract.Columns.MEAL_ID + " = " + mealIDS.get(i);
            mCursor = mContentResolver.query(MealContract.URI_TABLE, catProjection, filter, null, null);

            if (mCursor.moveToFirst()) {
                do {
                    categoryList.add(mCursor.getString(mCursor.getColumnIndex(MealContract.Columns.MEAL_CATEGORY)));
                } while (mCursor.moveToNext());
            }

            mealCategories.add(categoryList);
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

        final String date = mealDates.get(i);
        final String type = mealTypes.get(i);
        final ArrayList<String> listCategories = mealCategories.get(i);
        final int mealID = mealIDS.get(i);
        viewHolder.mealDate.setText(date);
        viewHolder.mealType.setText(type);
        int gCount = 0;
        int oCount = 0;
        int rCount = 0;

        for (int x = 0; x < listCategories.size(); x++) {
            String cat = listCategories.get(x);

            switch (cat) {
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

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(120,120);
        layoutParams.setMargins(10,10,10,10);
        if(gCount > 0) {
            RelativeLayout greenDot = new RelativeLayout(mContext);
            greenDot.setBackgroundResource(R.drawable.green);
            greenDot.setGravity(Gravity.CENTER);
            TextView txtGCount = new TextView(mContext);
            txtGCount.setText(gCount + "");
            greenDot.addView(txtGCount);

            viewHolder.mealLayout.addView(greenDot, layoutParams);
        }

        if(oCount > 0) {
            RelativeLayout orangeDot = new RelativeLayout(mContext);
            orangeDot.setBackgroundResource(R.drawable.orange);
            orangeDot.setGravity(Gravity.CENTER);
            TextView txtOCount = new TextView(mContext);
            txtOCount.setText(rCount + "");
            orangeDot.addView(txtOCount);

            viewHolder.mealLayout.addView(orangeDot, layoutParams);
        }

        if(rCount > 0) {
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
                Intent intent = new Intent(mContext.getApplicationContext(), MealEntryActivity.class);
                intent.putExtra(MealDateContract.Columns._ID, mealID);
                intent.putExtra(MealDateContract.Columns.MEAL_TYPE, type);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mealDates.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public CardView mealHolder;
        public TextView mealDate, mealType;
        public LinearLayout mealLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            mealHolder = (CardView) itemView.findViewById(R.id.item_CardView);
            mealDate = (TextView) itemView.findViewById(R.id.mealDate);
            mealType = (TextView) itemView.findViewById(R.id.mealType);
            mealLayout = (LinearLayout) itemView.findViewById(R.id.linearItem);
        }
    }

    class GreenDot extends View {

        private float x,y;
        private int r;

        public GreenDot(Context context, float x, float y, int r) {
            super(context);

            this.x = x;
            this.y = y;
            this.r = r;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint p = new Paint();
            p.setColor(Color.GREEN);
            canvas.drawCircle(x, y, r, p);
        }
    }
}