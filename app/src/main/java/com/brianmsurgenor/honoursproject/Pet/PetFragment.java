package com.brianmsurgenor.honoursproject.Pet;

import android.content.ContentResolver;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.brianmsurgenor.honoursproject.DBContracts.UserContract;
import com.brianmsurgenor.honoursproject.R;

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

        String[] projection = {UserContract.Columns.PET_TYPE};
        mCursor = mContentResolver.query(UserContract.URI_TABLE,projection,null,null,null);

        if(mCursor.moveToFirst()) {
            pet.setImageResource(mCursor.getInt(mCursor.getColumnIndex(UserContract.Columns.PET_TYPE)));
        }

        if(customColour != 0) {
            colourChange(customColour);
        }
        return view;
    }

    public static void colourChange(int colour) {
        customColour = colour;
        feedMe.setBackgroundTintList(ColorStateList.valueOf(colour));
    }

}
