package com.example.hatio.things_rtls.assist;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.hatio.things_rtls.app.CVCameraMode;
import com.example.hatio.things_rtls.app.GaugeMode;
import com.example.hatio.things_rtls.app.Setting;

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
                CVCameraMode cameraMode = new CVCameraMode();
                return cameraMode;
            case 1:
                GaugeMode gaugeMode = new GaugeMode();
                return gaugeMode;
            case 2:
                Setting setting = new Setting();
                return setting;
            default:
                 return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}