package com.example.nerita_hendra.i_fans;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class StoreDetailActivity extends AppCompatActivity {
    TextView txtNamaBarang,txtHargaBarang,txtDeskripsi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_store_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        txtNamaBarang = findViewById(R.id.textView_nama_barang);
        txtHargaBarang = findViewById(R.id.textView_harga_barang);
        txtDeskripsi = findViewById(R.id.textView_deskripsi);
        txtNamaBarang.setText(getIntent().getExtras().get("nama").toString());
        txtHargaBarang.setText(getIntent().getExtras().get("harga").toString());
        txtDeskripsi.setText(getIntent().getExtras().get("deskripsi").toString());
    }
}
