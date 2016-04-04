package com.brianmsurgenor.honoursproject.Exercise;

import android.content.Context;
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
import com.brianmsurgenor.honoursproject.R;

import java.util.ArrayList;

/**
 * Created by Brian on 19/02/2016.
 */
public class ExerciseEntryAdapter extends RecyclerView.Adapter<ExerciseEntryAdapter.ViewHolder>{

    private AppConstants appConstants;
    private ArrayList<ArrayList> exercises;
    private ArrayList<LinearLayout> backgroundList;
    private Context mContext;

    public ExerciseEntryAdapter(Context context) {
        super();

        backgroundList = new ArrayList<>();
        appConstants = new AppConstants();
        exercises = appConstants.getSports();
        mContext = context;

    }

    @Override
    public ExerciseEntryAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.picker_adapter, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ExerciseEntryAdapter.ViewHolder holder, int i) {

        final String exercise = (String) exercises.get(i).get(0);
        boolean selected = false;
        backgroundList.add(holder.exerciseBackground);

        holder.imgThumbnail.setImageResource((int) exercises.get(i).get(1));
        holder.txtName.setText(exercise);

        final boolean finalSelected = selected;
        holder.exerciseHolder.setOnClickListener(new View.OnClickListener() {
            boolean clickSelected = finalSelected;

            @Override
            public void onClick(View v) {
                if(!clickSelected) {
                    clickSelected = true;
                    unselectAll();
                    holder.exerciseBackground.setBackgroundColor(Color.YELLOW);
                    ExerciseEntryActivity.selectExercise(true, exercise);
                } else {
                    clickSelected = false;
                    holder.exerciseBackground.setBackgroundColor(mContext.getResources().
                            getColor(R.color.primaryBackground));
                    ExerciseEntryActivity.selectExercise(false, exercise);
                }
            }
        });

    }

    private void unselectAll() {
        for (LinearLayout l: backgroundList) {
            l.setBackgroundColor(mContext.getResources().getColor(R.color.primaryBackground));
        }
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgThumbnail;
        public TextView txtName;
        public CardView exerciseHolder;
        public LinearLayout exerciseBackground;

        public ViewHolder(View itemView) {
            super(itemView);
            exerciseHolder = (CardView) itemView.findViewById(R.id.item_CardView);
            exerciseBackground = (LinearLayout) itemView.findViewById(R.id.linearItem);
            imgThumbnail = (ImageView) itemView.findViewById(R.id.itemImage);
            txtName = (TextView) itemView.findViewById(R.id.itemName);
        }
    }
}
