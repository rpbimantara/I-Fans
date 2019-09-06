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
public class HomeFragment extends Fragment {

    public static HomeFragment newInstance() {
        HomeFragment frag = new HomeFragment();
        return frag;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Adapter adapter = new Adapter(getChildFragmentManager());
        ViewPager viewPager = view.findViewById(R.id.viewpager_home);
        TabLayout tabs = view.findViewById(R.id.tabs_home);
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);


        return view;
    }

    public class Adapter extends FragmentPagerAdapter {
        private int NUM_ITEMS = 3;

        public Adapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return TerupdateFragment.newInstance();
                case 1:
                    return LiveFragment.newInstance();
                case 2:
                    return TeamFragment.newInstance();
                default:
                    return TeamFragment.newInstance();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Overview";
                case 1:
                    return "Live";
                case 2:
                    return "Team";
                default:
                    return "Overview";
            }
        }
    }

}
