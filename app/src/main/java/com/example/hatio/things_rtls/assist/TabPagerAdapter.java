package com.example.hatio.things_rtls.assist;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.hatio.things_rtls.app.GaugeMode;

/**
 * Created by hatio on 2017. 6. 1..
 */

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    // Count number of tabs
    private int tabCount;

    public TabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        // Returning the current tabs
        switch (position) {
            case 0:
//                CameraMode cameraMode = new CameraMode();
//                return cameraMode;
            case 1:
                GaugeMode gaugeMode = new GaugeMode();
                return gaugeMode;
            case 2:
                GaugeMode gaugeMode3 = new GaugeMode();
                return gaugeMode3;
            default:
                 return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}