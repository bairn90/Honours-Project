package com.brianmsurgenor.honoursproject.Pet;

import android.content.ContentResolver;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.brianmsurgenor.honoursproject.DBContracts.UserContract;
import com.brianmsurgenor.honoursproject.R;
import com.easyandroidanimations.library.Animation;
import com.easyandroidanimations.library.BounceAnimation;
import com.easyandroidanimations.library.FlipVerticalAnimation;
import com.easyandroidanimations.library.RotationAnimation;
import com.easyandroidanimations.library.ShakeAnimation;
import com.easyandroidanimations.library.SlideInAnimation;

import java.util.Random;

public class PetFragment extends Fragment {

    private ImageView pet;
    private Cursor mCursor;
    private ContentResolver mContentResolver;
    private static int customColour = 0;
    private static FloatingActionButton feedMe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentResolver = getActivity().getContentResolver();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pet, container, false);

        pet = (ImageView) view.findViewById(R.id.petView);
        feedMe = (FloatingActionButton) view.findViewById(R.id.feedMe);

        //Get the pet and the app colour from the database and set them up
        String[] projection = {UserContract.Columns.PET_TYPE, UserContract.Columns.CUSTOM_COLOUR};
        mCursor = mContentResolver.query(UserContract.URI_TABLE,projection,null,null,null);

        if(mCursor.moveToFirst()) {
            pet.setImageResource(mCursor.getInt(mCursor.getColumnIndex(UserContract.Columns.PET_TYPE)));
            customColour = mCursor.getInt(mCursor.getColumnIndex(UserContract.Columns.CUSTOM_COLOUR));
        }

        if(customColour != 0) {
            colourChange(customColour);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        /*
         * Play a random pet animation after waiting 1 second in order to prevent the animation
         * playing while the screen is still in the middle of being drawn
         */
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Random random = new Random();
                int lower = 0;
                int upper = 5;
                int randomNum = random.nextInt(upper - lower) + lower;


                switch (randomNum) {
                    case 0: new BounceAnimation(pet).animate();
                        break;

                    case 1: new SlideInAnimation(pet)
                            .setDirection(Animation.DIRECTION_UP)
                            .animate();
                        break;

                    case 2: new ShakeAnimation(pet)
                            .setNumOfShakes(5)
                            .setDuration(Animation.DURATION_SHORT)
                            .animate();
                        break;

                    case 3: new FlipVerticalAnimation(pet).animate();
                        break;

                    case 4: new RotationAnimation(pet)
                            .setPivot(RotationAnimation.PIVOT_TOP_LEFT)
                            .animate();
                        break;
                }
            }
        }, 1000);

    }

    /**
     * Method used to change the floating action button colour either when the user changes it or
     * when the app is launched and the colour retrieved from the database
     * @param colour the new app color
     */
    public static void colourChange(int colour) {
        customColour = colour;
        feedMe.setBackgroundTintList(ColorStateList.valueOf(colour));
    }

}
