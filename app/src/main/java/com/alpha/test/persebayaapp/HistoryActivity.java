package com.alpha.test.persebayaapp;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.history_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_history);
        HistoryPageAdapter adapter = new HistoryPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs_history);
        tabs.setupWithViewPager(viewPager);
    }

    private static class HistoryPageAdapter extends FragmentPagerAdapter {


        public HistoryPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    HistoryPurchaseFragment tab1 = new HistoryPurchaseFragment();
                    return tab1;
                case 1:
                    HistorySaleFragment tab2 = new HistorySaleFragment();
                    return tab2;
//                case 2:
//                    HistorySaleFragment tab3 = new HistorySaleFragment();
//                    return tab3;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Purchase";
                case 1:
                    return "Sale";
//                case 2:
//                    return "Auction";
            }
            return null;
        }
    }
}
