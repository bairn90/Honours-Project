package com.brianmsurgenor.honoursproject.Exercise;

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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.brianmsurgenor.honoursproject.DBContracts.ExerciseContract;
import com.brianmsurgenor.honoursproject.Main.MainActivity;
import com.brianmsurgenor.honoursproject.R;

import java.util.ArrayList;

/**
 * Adapter used to fill the recycler view for the diary
 */
public class ExerciseDiaryAdapter extends RecyclerView.Adapter<ExerciseDiaryAdapter.ViewHolder> {

    private ContentResolver mContentResolver;
    private Context mContext;
    private Cursor mCursor;
    private ArrayList<String> exerciseDates, exerciseDetails;
    private ArrayList<Integer> dateIDs;

    public ExerciseDiaryAdapter(ContentResolver mContentResolver, Context mContext) {
        super();

        this.mContentResolver = mContentResolver;
        this.mContext = mContext;
        exerciseDates = new ArrayList<>();
        exerciseDetails = new ArrayList<>();
        dateIDs = new ArrayList<>();

        mCursor = mContentResolver.query(ExerciseContract.URI_TABLE,null,null,null,
                ExerciseContract.Columns._ID + " DESC");

        //Get all of the users exercises from the database
        if(mCursor.moveToFirst()) {
            do {
                exerciseDates.add(mCursor.getString(mCursor.getColumnIndex(ExerciseContract.Columns.DATE)));
                exerciseDetails.add(mCursor.getString(mCursor.getColumnIndex(ExerciseContract.Columns.EXERCISE)));
                dateIDs.add(mCursor.getInt(mCursor.getColumnIndex(ExerciseContract.Columns._ID)));
            } while(mCursor.moveToNext());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.exercise_diary_adapter, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {

        final String date = exerciseDates.get(i);
        final String details = exerciseDetails.get(i);
        final String id = dateIDs.get(i) + "";

        viewHolder.txtDate.setText(date);
        viewHolder.txtExerciseDetails.setText(details);

        //When the user long presses on the exercise card then present them with an option to delete
        viewHolder.exerciseHolder.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                MenuItem delete = menu.add(0, 1, 0, "Delete");
                delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        /*
                         * Pop up dialog used to check whether the user really wants to delete the entry
                         * if yes then delete from the database and refresh the view
                         */
                        new AlertDialog.Builder(mContext)
                                .setIcon(R.drawable.ic_warning_black_24dp)
                                .setTitle("Delete Steps")
                                .setMessage("Are you sure you want to delete this days exercise?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Uri uri = ExerciseContract.Pedometer.buildExerciseUri(id);
                                        mContentResolver.delete(uri, null, null);
                                        exerciseDates.remove(i);
                                        //if it was the last item in the list return to main to avoid weird crash
                                        if (!exerciseDates.isEmpty()) {
                                            notifyItemRemoved(i);
                                            notifyItemRangeChanged(i, getItemCount());
                                        } else {
                                            mContext.startActivity(new Intent(mContext, MainActivity.class));
                                        }

                                        Toast.makeText(mContext.getApplicationContext(), "Exercise has been deleted", Toast.LENGTH_SHORT).show();
                                    }

                                })
                                .setNegativeButton("No", null)
                                .show();
                        return false;
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return exerciseDates.size();
    }

    //Viewholder used to define the view in which the exercise graphic is held
    class ViewHolder extends RecyclerView.ViewHolder {

        public CardView exerciseHolder;
        public TextView txtDate, txtExerciseDetails;

        public ViewHolder(View itemView) {
            super(itemView);

            exerciseHolder = (CardView) itemView.findViewById(R.id.item_CardView);
            txtDate = (TextView) itemView.findViewById(R.id.pedometerDate);
            txtExerciseDetails = (TextView) itemView.findViewById(R.id.pedometerSteps);
        }
    }
}
