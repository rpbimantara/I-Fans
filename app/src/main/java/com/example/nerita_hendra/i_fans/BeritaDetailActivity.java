package com.example.nerita_hendra.i_fans;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.ArrayList;

public class BeritaDetailActivity extends AppCompatActivity {
    TextView txtTitle,txtHeadline,txtKonten,txtTanggal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berita_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        toolbar.setTitle(getIntent().getExtras().get("kategori").toString());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        txtTitle = findViewById(R.id.textView_judul_detail_berita);
        txtHeadline = findViewById(R.id.textView_headhline_detail_berita);
        txtKonten = findViewById(R.id.textView_konten_detail_berita);
        txtTanggal = findViewById(R.id.textView_tgl_detail_berita);
        txtTitle.setText(getIntent().getExtras().get("title").toString());
        txtHeadline.setText(getIntent().getExtras().get("headline").toString());
        txtKonten.setText(getIntent().getExtras().get("konten").toString());
        txtTanggal.setText(getIntent().getExtras().get("tanggalbuat").toString() + " - " +getIntent().getExtras().get("penulis").toString());


    }
}
