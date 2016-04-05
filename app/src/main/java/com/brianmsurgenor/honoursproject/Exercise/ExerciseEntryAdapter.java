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
    private ArrayList<ArrayList> exerciseGraphics;
    private ArrayList<LinearLayout> backgroundList; //used to loop through the exercise layouts
    private Context mContext; // used to get the resources in order to change background colour

    public ExerciseEntryAdapter(Context context) {
        super();

        backgroundList = new ArrayList<>();
        appConstants = new AppConstants();
        exerciseGraphics = appConstants.getSports();
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

        final String exercise = (String) exerciseGraphics.get(i).get(0);
        backgroundList.add(holder.exerciseBackground);

        holder.imgThumbnail.setImageResource((int) exerciseGraphics.get(i).get(1));
        holder.txtName.setText(exercise);

        //on click listener to mark the exercise as selected and pass this info back to the entryActivity
        holder.exerciseHolder.setOnClickListener(new View.OnClickListener() {
            boolean clickSelected = false;

            @Override
            public void onClick(View v) {

                /*
                 * If the item hasn't been selected then set it to selected and change colour
                 * and pass the name back to the activity, otherwise reset colour and pass "" back
                 * to cancel any previous selections
                 */
                if(!clickSelected) {
                    clickSelected = true;
                    unselectAll();
                    holder.exerciseBackground.setBackgroundColor(Color.YELLOW);
                    ExerciseEntryActivity.selectExercise(exercise);
                } else {
                    clickSelected = false;
                    holder.exerciseBackground.setBackgroundColor(mContext.getResources().
                            getColor(R.color.primaryBackground));
                    ExerciseEntryActivity.selectExercise("");
                }
            }
        });

    }

    /**
     * Loops through each exercise to change the colour to primary on a pet being deselected
     */
    private void unselectAll() {
        for (LinearLayout l: backgroundList) {
            l.setBackgroundColor(mContext.getResources().getColor(R.color.primaryBackground));
        }
    }

    @Override
    public int getItemCount() {
        return exerciseGraphics.size();
    }

    //Viewholder used to define the view in which the exercise graphic is held
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
