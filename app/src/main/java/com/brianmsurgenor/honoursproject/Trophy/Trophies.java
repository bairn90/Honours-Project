package com.brianmsurgenor.honoursproject.Trophy;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;
import android.widget.Toast;

import com.brianmsurgenor.honoursproject.DBContracts.TrophyContract;
import com.brianmsurgenor.honoursproject.R;
import com.easyandroidanimations.library.Animation;
import com.easyandroidanimations.library.SlideInUnderneathAnimation;

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
    private final Context mContext;

    /*
     * Used so that all classes that watch for a trophy being won can easily access
     */
    public interface TrophyDetails {
        String[] firstMeal = {"First Meal", "This trohpy is awarded the first time you feed your pet!"};
        String[] firstWalk = {"First walk", "One of the most important things about owning a pet " +
                "is exercising them. You'll win this trophy the first time you take your pet out on a walk!"};
        String[] greenMeal = {"Green Meal", "Congrats! " +
                "You've just fed your pet your first entirely healthy meal! Pets love eating healthy. It makes them very happy!"};
        String[] longWalk = {"Long Walk", "Walked a whole mile?! That's sure to be one tired, but very happy pet!"};
        String[] firstSport = {"First Sport",""};
        String[] y = {"",""};
        String[] z = {"",""};
        String[] a = {"",""};
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

    public void winTrophy(String trophyName, final Class nextScreen) {

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
                                    mContext.startActivity(new Intent(mContext, nextScreen));
                                }
                            }
                        });
        builder.show();

        new SlideInUnderneathAnimation(image)
                .setDirection(Animation.DIRECTION_DOWN)
                .animate();

    }
}
