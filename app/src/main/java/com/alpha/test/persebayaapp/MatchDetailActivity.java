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
import android.widget.TextView;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.OArguments;
import oogbox.api.odoo.client.listeners.IOdooResponse;

import static com.alpha.test.persebayaapp.CommonUtils.StringToBitMap;
import static com.alpha.test.persebayaapp.CommonUtils.getOdooConnection;

public class MatchDetailActivity extends AppCompatActivity {

    TextView txtTeamHome,txtTeamAway,txtStadion,txtTglhariini,txtScore,txtLiga;
    ImageView imageHome,imageAway;
    SharedPrefManager sharedPrefManager;
    OdooClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.match_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_match_detail);
        MatchDetailPageAdapter adapter = new MatchDetailPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs_match_detail);
        tabs.setupWithViewPager(viewPager);


        sharedPrefManager = new SharedPrefManager(getBaseContext());
        txtTeamHome = findViewById(R.id.match_detail_txt_namateamHome);
        txtTeamAway = findViewById(R.id.match_detail_txt_namateamAway);
        txtStadion = findViewById(R.id.match_detail_textView_stadionharini);
        txtTglhariini = findViewById(R.id.match_detail_textView_tglharini);
        txtLiga = findViewById(R.id.match_detail_textView_ligaharini);
        txtScore = findViewById(R.id.match_detail_txt_scoreterupdate);
        imageHome = findViewById(R.id.match_detail_home_image);
        imageAway = findViewById(R.id.match_detail_away_image);
        client  = getOdooConnection(getBaseContext());
        loadMatch();
    }

    public void loadMatch(){
        OArguments arguments = new OArguments();
        arguments.add(Integer.valueOf(getIntent().getExtras().get("id_jadwal").toString()));

        client.call_kw("persebaya.jadwal", "match_detail", arguments, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                OdooRecord[] records = result.getRecords();
                for (OdooRecord record : records) {
                    imageHome.setImageBitmap(StringToBitMap(record.getString("imageHome")));
                    txtTeamHome.setText(record.getString("home"));
                    imageAway.setImageBitmap(StringToBitMap(record.getString("imageAway")));
                    txtTeamAway.setText(record.getString("away"));
                    txtStadion.setText(record.getString("stadion"));
                    txtTglhariini.setText(record.getString("date"));
                    txtLiga.setText(record.getString("liga"));
                    txtScore.setText(record.getString("score"));
                }
            }
        });
    }

    private static class MatchDetailPageAdapter extends FragmentPagerAdapter {


        public MatchDetailPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    MatchMomentFragment tab1 = new MatchMomentFragment();
                    return tab1;
                case 1:
                    MatchLineUpFragment tab2 = new MatchLineUpFragment();
                    return tab2;
                case 2:
                    MatchStatisticFragment tab3 = new MatchStatisticFragment();
                    return tab3;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Moments";
                case 1:
                    return "Line Up";
                case 2:
                    return "Statistic";
            }
            return null;
        }
    }
}
