package com.example.nerita_hendra.i_fans;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class BeritaDetailActivity extends AppCompatActivity {
    TextView txtTitle,txtHeadline,txtKonten,txtTanggal;
    ImageView image;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berita_detail);
        sharedPrefManager = new SharedPrefManager(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        toolbar.setTitle(getIntent().getExtras().get("kategori").toString());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        txtTitle = findViewById(R.id.textView_judul_detail_berita);
        txtHeadline = findViewById(R.id.textView_headhline_detail_berita);
        txtKonten = findViewById(R.id.textView_konten_detail_berita);
        txtTanggal = findViewById(R.id.textView_tgl_detail_berita);
        image = findViewById(R.id.berita_detail_imageView);
        txtTitle.setText(getIntent().getExtras().get("title").toString());
        txtHeadline.setText(getIntent().getExtras().get("headline").toString());
        txtKonten.setText(getIntent().getExtras().get("konten").toString());
        txtTanggal.setText(getIntent().getExtras().get("tanggalbuat").toString() + " - " +getIntent().getExtras().get("penulis").toString());
        image.setImageBitmap(StringToBitMap(sharedPrefManager.getSpImageNews()));
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
}
