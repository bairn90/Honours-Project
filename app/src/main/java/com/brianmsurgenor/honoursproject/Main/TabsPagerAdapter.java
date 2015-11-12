package com.brianmsurgenor.honoursproject.Main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.brianmsurgenor.honoursproject.Pedometer.PedometerFragment;
import com.brianmsurgenor.honoursproject.Pet.PetFragment;
import com.brianmsurgenor.honoursproject.Trophy.TrophyFragment;

/**
 * Created by Brian on 01/11/2015.
 */

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new PetFragment();
            case 1:
                return new PedometerFragment();
            case 2:
                return new TrophyFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "My Pet";
            case 1:
                return "Pedometer";
            case 2:
                return "My Trophies";
            default:
                return null;
        }
    }
}