package com.example.nerita_hendra.i_fans;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class ClubDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_detail_club_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("");
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_detail_club);
        ClubFragmentPageAdapter adapter = new ClubFragmentPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs_detail_club);
        tabs.setupWithViewPager(viewPager);

    }


    private static class ClubFragmentPageAdapter extends FragmentPagerAdapter {


        public ClubFragmentPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    LiveFragment tab1 = new LiveFragment();
                    return tab1;
                case 1:
                    LiveFragment tab2 = new LiveFragment();
                    return tab2;
                case 2:
                    LiveFragment tab3 = new LiveFragment();
                    return tab3;
                case 3:
                    LiveFragment tab4 = new LiveFragment();
                    return tab4;
                case 4:
                    LiveFragment tab5= new LiveFragment();
                    return tab5;
                case 5:
                    LiveFragment tab6 = new LiveFragment();
                    return tab6;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 6;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Summary";
                case 1:
                    return "Results";
                case 2:
                    return "Schedule";
                case 3:
                    return "Player";
                case 4:
                    return "Statistics";
                case 5:
                    return "Standings";
            }
            return null;
        }
    }
}
