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
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.listeners.IOdooResponse;

import static com.alpha.test.persebayaapp.CommonUtils.StringToBitMap;
import static com.alpha.test.persebayaapp.CommonUtils.getOdooConnection;

public class TeamDetailActivity extends AppCompatActivity {

    ImageView imageAtlete;
    RatingBar ratingAtlete;
    TextView txtNo,txtNama;
    SharedPrefManager sharedPrefManager;

    OdooClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.team_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_team_detail);
        TeamDetailPageAdapter adapter = new TeamDetailPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs_team_detail);
        tabs.setupWithViewPager(viewPager);
        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        imageAtlete = findViewById(R.id.image_TeamDetail);
        ratingAtlete = findViewById(R.id.rating_TeamDetail);
        txtNo = findViewById(R.id.txt_no_team_detail);
        txtNama = findViewById(R.id.txt_nama_team_detail);
        client = getOdooConnection(getBaseContext());
        loadTeam();
//        new TeamDetailTask().execute();
    }
    public void loadTeam(){
        List<Integer> ids = Arrays.asList(Integer.valueOf(getIntent().getExtras().get("id_atlete").toString()));
        List<String> fields = Arrays.asList("id","image","rating","name", "job_id","status_pemain","no_punggung");

        client.read("hr.employee", ids, fields, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                OdooRecord[] records = result.getRecords();
                for (OdooRecord record : records) {
                    String rating = record.getString("rating");
                    if (rating.equalsIgnoreCase("false")){
                        rating = "0";
                    }
                    imageAtlete.setImageBitmap(StringToBitMap(record.getString("image")));
                    ratingAtlete.setRating(Float.valueOf(rating));
                    txtNo.setText(String.valueOf(record.getInt("no_punggung")));
                    txtNama.setText(record.getString("name"));
                }
            }
        });
    }

    private static class TeamDetailPageAdapter extends FragmentPagerAdapter {


        public TeamDetailPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    TeamDetailStatisticFragment tab1 = new TeamDetailStatisticFragment();
                    return tab1;
                case 1:
                    TeamDetailReviewFragment tab2 = new TeamDetailReviewFragment();
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
                    return "Statistic";
                case 1:
                    return "Review";
                case 2:
            }
            return null;
        }
    }
}
