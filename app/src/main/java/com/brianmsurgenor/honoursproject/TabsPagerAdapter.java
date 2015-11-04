package com.brianmsurgenor.honoursproject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Brian on 01/11/2015.
 */

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        position++;

        switch (position) {
            case 1:
                return PetFragment.newInstance();
            case 2:
                return PedometerFragment.newInstance();
            case 3:
                return TrophyFragment.newInstance();
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

        position++;

        switch (position) {
            case 1:
                return "My Pet";
            case 2:
                return "Pedometer";
            case 3:
                return "My Trophies";
            default:
                return null;
        }
    }
}