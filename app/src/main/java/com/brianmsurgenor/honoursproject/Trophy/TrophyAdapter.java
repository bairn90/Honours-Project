package com.brianmsurgenor.honoursproject.Trophy;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brianmsurgenor.honoursproject.DBContracts.TrophyContract;
import com.brianmsurgenor.honoursproject.R;

import java.util.ArrayList;

/**
 *
 */
public class TrophyAdapter extends RecyclerView.Adapter<TrophyAdapter.ViewHolder> {

    private ArrayList<Trophy> trophyList; //used to hold a list of all the trophies in the db
    private ContentResolver mContentResolver;
    private Cursor mCursor;
    private Context mContext;

    public TrophyAdapter(ContentResolver contentResolver, Context context) {
        super();
        trophyList = new ArrayList<>();
        mContentResolver = contentResolver;
        mContext = context;

        //Get all trophies from the database and store the trophy objects in the trophyList
        Trophy trophy;
        mCursor = mContentResolver.query(TrophyContract.URI_TABLE, null, null, null, null);
        if(mCursor.moveToFirst()) {
            do {
                trophy = new Trophy();
                trophy.set_id(mCursor.getInt(mCursor.getColumnIndex(TrophyContract.Columns._ID)));
                trophy.setName(mCursor.getString(mCursor.getColumnIndex(TrophyContract.Columns.TROPHY_NAME)));
                trophy.setDescription(mCursor.getString(mCursor.getColumnIndex(TrophyContract.Columns.TROPHY_DESCRIPTION)));
                trophy.setAchieved(mCursor.getInt(mCursor.getColumnIndex(TrophyContract.Columns.ACHIEVED)));
                trophyList.add(trophy);
            } while (mCursor.moveToNext());
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trophy_adapter_layout, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final Trophy trophy = trophyList.get(i);
        viewHolder.trophyName.setText(trophy.getName());

        //Depending whether or not the user has won the trophy populate the approprate graphic
        if(trophy.getAchieved() != 0) {
            viewHolder.imgThumbnail.setImageResource(R.drawable.ic_trophy_winner);
        } else {
            viewHolder.imgThumbnail.setImageResource(R.drawable.ic_trophy_loser);
            viewHolder.imgThumbnail.setColorFilter(Color.argb(150, 200, 200, 200));
        }

        //Set up an onclicklistener so that when the user clicks the trophy they are given a description
        viewHolder.trophyHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder trophyDetails = new AlertDialog.Builder(mContext);
                trophyDetails.setMessage(trophy.getDescription());
                AlertDialog alert = trophyDetails.create();
                alert.setTitle(trophy.getName());
                alert.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return trophyList.size();
    }

    //Viewholder used to define the view in which the trophy graphic is held
    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgThumbnail;
        public TextView trophyName;
        public CardView trophyHolder;

        public ViewHolder(View itemView) {
            super(itemView);
            trophyHolder = (CardView) itemView.findViewById(R.id.cardView_Trophy);
            trophyName = (TextView) itemView.findViewById(R.id.trophyName);
            imgThumbnail = (ImageView) itemView.findViewById(R.id.trophyImage);
        }
    }

}

