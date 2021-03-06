package com.alpha.test.persebayaapp;


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

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BelanjaFragment extends Fragment {
    SharedPrefManager sharedPrefManager;

    public static BelanjaFragment newInstance(){
        BelanjaFragment fragment = new BelanjaFragment();
        return fragment;
    }
    public BelanjaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment\
        View view = inflater.inflate(R.layout.fragment_belanja,container, false);
        sharedPrefManager = new SharedPrefManager(getContext());
        // Setting ViewPager for each Tabs
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager_belanja);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                if (i == 0) {
                    sharedPrefManager.saveSPString(sharedPrefManager.SP_FAB_Belanja, "Store");
                    sharedPrefManager.saveSPString(sharedPrefManager.SP_FAB,"Store");
                }else{
                    sharedPrefManager.saveSPString(sharedPrefManager.SP_FAB_Belanja, "Lelang");
                    sharedPrefManager.saveSPString(sharedPrefManager.SP_FAB,"Lelang");
                }
            }

            @Override
            public void onPageSelected(int i) {
                if (i == 0) {
                    sharedPrefManager.saveSPString(sharedPrefManager.SP_FAB_Belanja, "Store");
                    sharedPrefManager.saveSPString(sharedPrefManager.SP_FAB,"Store");
                }else{
                    sharedPrefManager.saveSPString(sharedPrefManager.SP_FAB_Belanja, "Lelang");
                    sharedPrefManager.saveSPString(sharedPrefManager.SP_FAB,"Lelang");
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) view.findViewById(R.id.tabs_belanja);
        tabs.setupWithViewPager(viewPager);


        return view;
    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {

        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new StoreFragment(), "Store");
        adapter.addFragment(new LelangFragment(), "Auction");
        viewPager.setAdapter(adapter);

    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
