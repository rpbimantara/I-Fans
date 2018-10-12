package com.example.nerita_hendra.i_fans;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class LelangDetailActivity extends AppCompatActivity {
    TextView txtNamaBarang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lelang_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_lelang_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        txtNamaBarang = findViewById(R.id.textView_nama_barang_lelang);
        txtNamaBarang.setText(getIntent().getExtras().get("nama").toString());
    }
}
