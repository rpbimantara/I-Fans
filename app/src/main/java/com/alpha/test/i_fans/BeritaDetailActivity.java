package com.alpha.test.i_fans;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.OdooVersion;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooConnectListener;

public class BeritaDetailActivity extends AppCompatActivity {
    TextView txtTitle,txtHeadline,txtKonten,txtTanggal;
    ImageView image;
    SharedPrefManager sharedPrefManager;
    OdooClient client;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berita_detail);
        sharedPrefManager = new SharedPrefManager(this);
        toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        txtTitle = findViewById(R.id.textView_judul_detail_berita);
        txtHeadline = findViewById(R.id.textView_headhline_detail_berita);
        txtKonten = findViewById(R.id.textView_konten_detail_berita);
        txtTanggal = findViewById(R.id.textView_tgl_detail_berita);
        image = findViewById(R.id.berita_detail_imageView);
        LoadBerita();
    }

    public  void LoadBerita(){
        client = new OdooClient.Builder(getApplicationContext())
                .setHost(sharedPrefManager.getSP_Host_url())
                .setSession(sharedPrefManager.getSpSessionId())
                .setSynchronizedRequests(false)
                .setConnectListener(new OdooConnectListener() {
                    @Override
                    public void onConnected(OdooVersion version) {
                        List<Integer> ids = Arrays.asList(Integer.valueOf(getIntent().getExtras().get("id").toString()));
                        List<String> fields = Arrays.asList("id", "image", "title", "headline", "content", "kategori_brita_id", "create_date", "create_uid", "write_date", "write_uid");

                        client.read("persebaya.berita", ids, fields, new IOdooResponse() {
                            @Override
                            public void onResult(OdooResult result) {
                                OdooRecord[] records = result.getRecords();

                                for(OdooRecord record: records) {
                                    toolbar.setTitle(record.getString("kategori_brita_id"));
                                    txtTitle.setText(record.getString("title"));
                                    txtHeadline.setText(record.getString("headline"));
                                    txtKonten.setText(record.getString("content"));
                                    txtTanggal.setText( record.getString("create_uid")+ " - " +tanggal(record.getString("create_date")));
                                    image.setImageBitmap(StringToBitMap(record.getString("image")));
                                }
                            }
                        });
                    }
                }).build();
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

    public String tanggal(String tgl){
        try {
            tgl = new SimpleDateFormat("dd MMM yyyy", Locale.US).format(new SimpleDateFormat("yyyy-MM-dd").parse(tgl));
        }catch (Exception ex){
            System.out.println("Error Convert Tanggal: " + ex);
        }

        return tgl;
    }
}