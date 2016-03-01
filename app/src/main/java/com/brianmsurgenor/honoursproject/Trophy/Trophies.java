package com.brianmsurgenor.honoursproject.Trophy;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.widget.Toast;

import com.brianmsurgenor.honoursproject.DBContracts.TrophyContract;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Brian on 09/11/2015.
 * Holds the details of the trophies that can be won
 * For new trophies to be added add the name and description to the interface
 * Then add to the arraylists in the constructor. DB updates all handled by checks in MainActivity
 * and DBProvider on create.
 */
public class Trophies {

    private LinkedList<String> trophyNames = new LinkedList<>();
    private LinkedList<String> trophyDescriptions = new LinkedList<>();
    private ContentResolver mContentResolver;
    private Context mContext;

    /*
     * Used so that all classes that watch for a trophy being won can easily access
     */
    public interface TrophyDetails {
        String[] trophy1 = {"Trophy1", "This is a trophy"};
        String[] trophy2 = {"Trophy2", "This is a trophy"};
        String[] test = {"Testing", "Testing"};
    }

    public Trophies(Context context) {

        mContext = context;
        mContentResolver = mContext.getContentResolver();

        trophyNames.add(TrophyDetails.trophy1[0]);
        trophyDescriptions.add(TrophyDetails.trophy1[1]);

        trophyNames.add(TrophyDetails.trophy2[0]);
        trophyDescriptions.add(TrophyDetails.trophy2[1]);

        trophyNames.add(TrophyDetails.test[0]);
        trophyDescriptions.add(TrophyDetails.test[1]);

    }

    public int numberOfTrophies() {
        return trophyNames.size();
    }

    public LinkedList<String> getTrophyNames() {
        return trophyNames;
    }

    public LinkedList<String> getTrophyDescriptions() {
        return trophyDescriptions;
    }

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

    public void winTrophy(String trophyName) {

        ContentValues values = new ContentValues();
        values.put(TrophyContract.Columns.ACHIEVED, 1);

        String where = TrophyContract.Columns.TROPHY_NAME + " = '" + trophyName + "'";

        mContentResolver.update(TrophyContract.URI_TABLE, values, where, null);
    }
}
