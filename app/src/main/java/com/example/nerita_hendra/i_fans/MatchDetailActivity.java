package com.example.nerita_hendra.i_fans;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

public class MatchDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.match_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Toast.makeText(this,getIntent().getExtras().get("id_jadwal").toString(),Toast.LENGTH_LONG).show();
    }
}
