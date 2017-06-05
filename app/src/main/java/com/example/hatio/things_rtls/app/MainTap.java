package com.example.hatio.things_rtls.app;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.hatio.things_rtls.R;
import com.example.hatio.things_rtls.assist.TabPagerAdapter;

/**
 * Created by hatio on 2017. 5. 31..
 */

public class MainTap extends ActionBarActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_main_tap);

        // Adding Toolbar to the activity
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        // Initializing the TabLayout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
//        tabLayout.addTab(tabLayout.newTab());
//        tabLayout.addTab(tabLayout.newTab());
//        tabLayout.addTab(tabLayout.newTab());

        View view1 = getLayoutInflater().inflate(R.layout.icon_view_selector_camera, null);
        view1.findViewById(R.id.icon).setBackgroundResource(R.drawable.tab_selector_camera);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view1));

        View view2 = getLayoutInflater().inflate(R.layout.icon_view_selector_odometer, null);
        view2.findViewById(R.id.icon).setBackgroundResource(R.drawable.tab_selector_odometer);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view2));

        View view3 = getLayoutInflater().inflate(R.layout.icon_view_selector_setting, null);
        view3.findViewById(R.id.icon).setBackgroundResource(R.drawable.tab_selector_setting);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view3));


        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Initializing ViewPager
        viewPager = (ViewPager) findViewById(R.id.pager);

        // Creating TabPagerAdapter adapter
        TabPagerAdapter pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // Set TabSelectedListener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

//                tab.getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
//                TextView txt=(TextView)findViewById(R.id.tabText);
//                txt.setTextColor(Color.GRAY);
//                tab.setCustomView();

                switch(tab.getPosition()){
                    case 0:
                        setActionBarTitle("Camera Mode");
                        break;
                    case 1:
                        setActionBarTitle("Odometer Mode");
                        break;
                    case 2:
                        setActionBarTitle("Setting");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}
