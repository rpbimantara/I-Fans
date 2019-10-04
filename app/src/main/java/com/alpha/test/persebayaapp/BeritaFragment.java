package com.alpha.test.persebayaapp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class BeritaFragment extends Fragment {
    private FragmentTabHost mTabHost;

    public static BeritaFragment newInstance(){
        BeritaFragment frag = new BeritaFragment();
        return frag;
    }
    public BeritaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_berita,container, false);
        Adapter adapter = new Adapter(getChildFragmentManager());
        ViewPager viewPager = view.findViewById(R.id.viewpager_berita);
        TabLayout tabs = view.findViewById(R.id.tabs_berita);
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);

        return view;
    }

    // Add Fragments to Tabs
//    private void setupViewPager(ViewPager viewPager) {
//
//        Adapter adapter = new Adapter(getChildFragmentManager());
//        adapter.addFragment(new ListBeritaFragment(), "Today");
//        adapter.addFragment(new JadwalFragment(), "Schedule");
//        adapter.addFragment(new KlasemenFragment(), "Standings");
//        viewPager.setAdapter(adapter);
//
//    }

    static class Adapter extends FragmentPagerAdapter {
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
                    return ListBeritaFragment.newInstance();
                case 1:
                    return JadwalFragment.newInstance();
                case 2:
                    return KlasemenFragment.newInstance();
                default:
                    return ListBeritaFragment.newInstance();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "News";
                case 1:
                    return "Schedule";
                case 2:
                    return "Standings";
                default:
                    return "News";
            }
        }
    }

}
