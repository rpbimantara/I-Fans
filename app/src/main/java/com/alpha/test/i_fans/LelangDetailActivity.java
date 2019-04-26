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

public class LelangDetailActivity extends AppCompatActivity {
    TextView txtNamaBarang,txtBid,txtDeskripsi,txtInfoDetail;
    ImageView imageDetail;
    OdooClient client;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lelang_detail);
        Toolbar toolbar = findViewById(R.id.detail_lelang_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        sharedPrefManager = new SharedPrefManager(this);
        imageDetail = findViewById(R.id.lelang_detail_imageView);
        txtNamaBarang = findViewById(R.id.textView_nama_barang_lelang);
        txtBid = findViewById(R.id.textView_bid_detail_lelang);
        txtDeskripsi = findViewById(R.id.textView_deskripsi_lelang);
        txtInfoDetail = findViewById(R.id.textView_info_detail_lelang);
        LoadData();
    }
    public void LoadData(){
        client = new OdooClient.Builder(getApplicationContext())
                .setHost(sharedPrefManager.getSP_Host_url())
                .setSession(sharedPrefManager.getSpSessionId())
                .setSynchronizedRequests(false)
                .setConnectListener(new OdooConnectListener() {
                    @Override
                    public void onConnected(OdooVersion version) {
                        List<Integer> ids = Arrays.asList(Integer.valueOf(getIntent().getExtras().get("id").toString()));
                        List<String> fields = Arrays.asList("id", "foto_lelang", "nama_barang", "ob", "inc", "binow","due_date","deskripsi_barang", "create_date", "create_uid", "write_date", "write_uid");

                        client.read("persebaya.lelang", ids, fields, new IOdooResponse() {
                            @Override
                            public void onResult(OdooResult result) {
                                OdooRecord[] records = result.getRecords();

                                for(OdooRecord record: records) {
                                    imageDetail.setImageBitmap(StringToBitMap(record.getString("foto_lelang")));
                                    txtNamaBarang.setText(record.getString("nama_barang"));
                                    txtBid.setText("Open Bid : "
                                            + String.valueOf(Math.round(record.getFloat("ob")))
                                            + "\n"
                                            + "BIN : "
                                            + String.valueOf(Math.round(record.getFloat("binow")))
                                            + "\n"
                                            + "INC : "
                                            + String.valueOf(Math.round(record.getFloat("inc")))
                                    );
                                    txtDeskripsi.setText("Deskripsi : \n\n" + record.getString("deskripsi_barang"));
                                    txtInfoDetail.setText(record.getString("create_uid")+" - "+tanggal(record.getString("create_date")));
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
