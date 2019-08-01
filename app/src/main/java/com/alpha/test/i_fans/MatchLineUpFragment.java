package com.alpha.test.i_fans;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class MatchLineUpFragment extends Fragment {

    private View rootView;

    public MatchLineUpFragment() {
        // Required empty public constructor
    }

    public static MatchLineUpFragment newInstance() {
        Bundle args = new Bundle();
        MatchLineUpFragment fragment = new MatchLineUpFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null){
            rootView = inflater.inflate(R.layout.fragment_match_line_up, container, false);
            ViewPager viewPager = rootView.findViewById(R.id.viewpager_match_line_up);
            MatchLineUpPageAdapter adapter = new MatchLineUpPageAdapter(getChildFragmentManager());
            viewPager.setAdapter(adapter);

            // Set Tabs inside Toolbar
            TabLayout tabs = (TabLayout) rootView.findViewById(R.id.tabs_match_line_up);
            tabs.setupWithViewPager(viewPager);
        }
        return rootView;
    }


    private static class MatchLineUpPageAdapter extends FragmentPagerAdapter {


        public MatchLineUpPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    MatchStatisticFragment tab1 = new MatchStatisticFragment();
                    return tab1;
                case 1:
                    MatchStatisticFragment tab2 = new MatchStatisticFragment();
                    return tab2;
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
                    return "Home";
                case 1:
                    return "Away";
            }
            return null;
        }
    }
}
