package com.brianmsurgenor.honoursproject.Trophy;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;
import android.widget.Toast;

import com.brianmsurgenor.honoursproject.DBContracts.TrophyContract;
import com.brianmsurgenor.honoursproject.R;
import com.easyandroidanimations.library.Animation;
import com.easyandroidanimations.library.SlideInUnderneathAnimation;

import java.util.ArrayList;

/**
 * Holds the details of the trophies that can be won
 * For new trophies to be added add the name and description to the interface
 * Then add to the arraylists in the constructor. DB updates all handled by checks in MainActivity
 * and DBProvider on create.
 */
public class Trophies {

    private ArrayList<String> trophyNames = new ArrayList<>();
    private ArrayList<String> trophyDescriptions = new ArrayList<>();
    private ContentResolver mContentResolver;
    private final Context mContext;

    /**
     * Used so that all classes that watch for a trophy being won can easily access
     */
    public interface TrophyDetails {
        String[] firstMeal = {"First Meal", "This trohpy is awarded the first time you feed your pet!"};
        String[] firstWalk = {"First walk", "One of the most important things about owning a pet " +
                "is exercising them. You'll win this trophy the first time you take your pet out on a walk!"};
        String[] greenMeal = {"Green Meal", "Congrats! " +
                "You've just fed your pet your first entirely healthy meal! Pets love eating healthy. It makes them very happy!"};
        String[] longWalk = {"Long Walk", "Walked a whole mile?! That's sure to be one tired, but very happy pet!"};
        String[] firstFootball = {"First Football","Congratulations you've tried a new sport for the first time!"};
        String[] firstRugby = {"First Rugby","Congratulations you've tried a new sport for the first time!"};
        String[] firstRun = {"First Run","Congratulations you've tried a new sport for the first time!"};
        String[] firstSki = {"First Ski","Congratulations you've tried a new sport for the first time!"};
        String[] firstTennis = {"First Tennis","Congratulations you've tried a new sport for the first time!"};
        String[] hourFootball = {"Hour of Football","Nice! A whole hour! You'll be a pro in no time!"};
    }

    public Trophies(Context context) {

        mContext = context;
        mContentResolver = mContext.getContentResolver();

        trophyNames.add(TrophyDetails.firstWalk[0]);
        trophyDescriptions.add(TrophyDetails.firstWalk[1]);

        trophyNames.add(TrophyDetails.firstMeal[0]);
        trophyDescriptions.add(TrophyDetails.firstMeal[1]);

        trophyNames.add(TrophyDetails.greenMeal[0]);
        trophyDescriptions.add(TrophyDetails.greenMeal[1]);

        trophyNames.add(TrophyDetails.longWalk[0]);
        trophyDescriptions.add(TrophyDetails.longWalk[1]);

        trophyNames.add(TrophyDetails.firstFootball[0]);
        trophyDescriptions.add(TrophyDetails.firstFootball[1]);

        trophyNames.add(TrophyDetails.firstRugby[0]);
        trophyDescriptions.add(TrophyDetails.firstRugby[1]);

        trophyNames.add(TrophyDetails.firstRun[0]);
        trophyDescriptions.add(TrophyDetails.firstRun[1]);

        trophyNames.add(TrophyDetails.firstTennis[0]);
        trophyDescriptions.add(TrophyDetails.firstTennis[1]);

        trophyNames.add(TrophyDetails.firstSki[0]);
        trophyDescriptions.add(TrophyDetails.firstSki[1]);

        trophyNames.add(TrophyDetails.hourFootball[0]);
        trophyDescriptions.add(TrophyDetails.hourFootball[1]);
    }

    public int numberOfTrophies() {
        return trophyNames.size();
    }

    public ArrayList<String> getTrophyNames() {
        return trophyNames;
    }

    public ArrayList<String> getTrophyDescriptions() {
        return trophyDescriptions;
    }

    /**
     * Checks whether or not any new trophies have been added to the application and adds these to
     * the database if there are new trophies
     */
    public void updateTrophyCheck() {

        Cursor mCursor;
        int trophyCount = 0;
        ArrayList<String> trophyNames = new ArrayList<>();

        /*
         * Gets all the trophies saved in the database to be compared with the trophies
         * held in this class
         */
        String[] projection = {TrophyContract.Columns.TROPHY_NAME};
        mCursor = mContentResolver.query(TrophyContract.URI_TABLE,projection,null,null,null);
        if(mCursor.moveToFirst()) {
            do {
                trophyNames.add(mCursor.getString(mCursor.getColumnIndex(TrophyContract.Columns.TROPHY_NAME)));
                trophyCount++;
            } while(mCursor.moveToNext());
        }

        /*
         * If the number of trophies in the database is different from the
         * number in this class then update
         */
        if(trophyCount != numberOfTrophies()) {
            updateTrophiesDatabase(trophyNames);
        }

    }

    /**
     * Updates the trophies in the database
     * @param dbTrophyNames
     */
    public void updateTrophiesDatabase(ArrayList dbTrophyNames) {
        ArrayList<String[]> toBeAdded = new ArrayList<>();

        for (int i=0;i<trophyNames.size();i++) {
            if(!dbTrophyNames.contains(trophyNames.get(i))) {
                String[] temp = {trophyNames.get(i),trophyDescriptions.get(i)};
                toBeAdded.add(temp);
            }
        }

        if(!toBeAdded.isEmpty()) {
            ContentValues values;
            for(int i=0;i<toBeAdded.size();i++) {
                values = new ContentValues();
                values.put(TrophyContract.Columns.TROPHY_NAME, toBeAdded.get(i)[0]);
                values.put(TrophyContract.Columns.TROPHY_DESCRIPTION, toBeAdded.get(i)[1]);
                values.put(TrophyContract.Columns.ACHIEVED,0);
                mContentResolver.insert(TrophyContract.URI_TABLE, values);
            }
            Toast.makeText(mContext.getApplicationContext(), "New trophies added", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Called when a trophy has been won, awards the trophy and then passes the user to the next
     * screen. Done this way to prevent the screen changing and cancelling the winnning alert
     * @param trophyName the name of the trophy obtained from the interface in this class
     * @param nextScreen the next screen the user should see after winning the trophy null if no change
     */
    public void winTrophy(String trophyName, final Intent nextScreen) {

        ContentValues values = new ContentValues();
        values.put(TrophyContract.Columns.ACHIEVED, 1);

        String where = TrophyContract.Columns.TROPHY_NAME + " = '" + trophyName + "'";
        mContentResolver.update(TrophyContract.URI_TABLE, values, where, null);

        ImageView image = new ImageView(mContext);
        image.setImageResource(R.drawable.ic_trophy_winner);

        AlertDialog.Builder builder =
                new AlertDialog.Builder(mContext)
                        .setTitle("You've just won the " + trophyName + " trophy!!")
                        .setView(image)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if (nextScreen != null) {
                                    mContext.startActivity(nextScreen);
                                }
                            }
                        });
        builder.show();

        new SlideInUnderneathAnimation(image)
                .setDirection(Animation.DIRECTION_DOWN)
                .animate();

    }
}
