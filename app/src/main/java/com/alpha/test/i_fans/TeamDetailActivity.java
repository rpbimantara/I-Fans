package com.alpha.test.i_fans;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.OdooVersion;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.OArguments;
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooConnectListener;

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
        new TeamDetailTask().execute();
    }

    public class TeamDetailTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            client = new OdooClient.Builder(getBaseContext())
                    .setHost(sharedPrefManager.getSP_Host_url())
                    .setSession(sharedPrefManager.getSpSessionId())
                    .setSynchronizedRequests(false)
                    .setConnectListener(new OdooConnectListener() {
                        @Override
                        public void onConnected(OdooVersion version) {
                            List<Integer> ids = Arrays.asList(Integer.valueOf(getIntent().getExtras().get("id_atlete").toString()));
                            List<String> fields = Arrays.asList("id","image","name", "job_id","status_pemain","no_punggung");

                            client.read("hr.employee", ids, fields, new IOdooResponse() {
                                @Override
                                public void onResult(OdooResult result) {
                                    OdooRecord[] records = result.getRecords();
                                    for (OdooRecord record : records) {
                                        imageAtlete.setImageBitmap(StringToBitMap(record.getString("image")));
                                        ratingAtlete.setRating(4);
                                        txtNo.setText(String.valueOf(record.getInt("no_punggung")));
                                        txtNama.setText(record.getString("name"));
                                    }
                                }
                            });
                        }
                    }).build();
            return null;
        }
    }

    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
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
