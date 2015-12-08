package com.brianmsurgenor.honoursproject.Pet;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brianmsurgenor.honoursproject.R;

public class PetFragment extends Fragment {

    private static FloatingActionButton feedMe;
    private static int customColour = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pet, container, false);

        feedMe = (FloatingActionButton) view.findViewById(R.id.feedMe);

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
