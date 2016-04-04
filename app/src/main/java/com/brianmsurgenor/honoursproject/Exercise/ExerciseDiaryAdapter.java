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
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.brianmsurgenor.honoursproject.DBContracts.ExerciseContract;
import com.brianmsurgenor.honoursproject.R;

import java.util.ArrayList;

/**
 * Created by Brian on 18/02/2016.
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
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        Log.d("TEST","TTT");

        final String date = exerciseDates.get(i);
        final String details = exerciseDetails.get(i);
        final String id = dateIDs.get(i) + "";

        viewHolder.txtDate.setText(date);
        viewHolder.txtExerciseDetails.setText(details);

        viewHolder.exerciseHolder.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                MenuItem delete = menu.add(0, 1, 0, "Delete");
                delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        deletePedometer(id);
                        return false;
                    }
                });
            }
        });

    }

    private void deletePedometer(String id) {

        final String idF = id;

        new AlertDialog.Builder(mContext)
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setTitle("Delete Steps")
                .setMessage("Are you sure you want to delete this days exercise?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = ExerciseContract.Pedometer.buildPedometerUri(idF);
                        mContentResolver.delete(uri,null,null);

                        Toast.makeText(mContext.getApplicationContext(), "Day has been deleted", Toast.LENGTH_SHORT).show();
                        mContext.startActivity(new Intent(mContext.getApplicationContext(), ExerciseDiaryActivity.class));
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return exerciseDates.size();
    }

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
