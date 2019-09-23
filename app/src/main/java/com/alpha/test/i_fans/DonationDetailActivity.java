package com.alpha.test.i_fans;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.OdooVersion;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooConnectListener;

import static com.alpha.test.i_fans.CommonUtils.StringToBitMap;
import static com.alpha.test.i_fans.CommonUtils.formater;
import static com.alpha.test.i_fans.CommonUtils.tanggal;

public class DonationDetailActivity extends AppCompatActivity {
    TextView txtTerkumpul,txtTotal,txtNama,txtTotalDonatur,txtDescription,txtAuthor;
    ProgressBar progressBar;
    Button btn_order;
    SharedPrefManager sharedPrefManager;
    ImageView imageDonation;
    OdooClient client;
    int total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_detail);
        Toolbar toolbar = findViewById(R.id.detail_donasi_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        sharedPrefManager = new SharedPrefManager(this);
        imageDonation = findViewById(R.id.donasi_detail_imageView);
        btn_order = findViewById(R.id.button_donate);
        progressBar = findViewById(R.id.progressBarDonation);
        txtTerkumpul = findViewById(R.id.textView_terkumpul_donasi);
        txtTotal = findViewById(R.id.textView_total_donasi);
        txtNama = findViewById(R.id.textView_nama_donasi_detail);
        txtTotalDonatur = findViewById(R.id.textView_total_donatur);
        txtDescription = findViewById(R.id.textView_deskripsi_donation_detail);
        txtAuthor = findViewById(R.id.textView_author_donasi);
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
                        List<String> fields = Arrays.asList("id","image_medium","name","list_price","target_donasi", "description_purchase","due_date","create_uid","donasi_ids");

                        client.read("product.template", ids, fields, new IOdooResponse() {
                            @Override
                            public void onResult(OdooResult result) {
                                OdooRecord[] records = result.getRecords();
                                int donasi_ids = result.getArray("donasi_ids").size();

                                for(OdooRecord record: records) {

                                    imageDonation.setImageBitmap(StringToBitMap(record.getString("image_medium")));
                                    txtTerkumpul.setText(formater(record.getFloat("list_price")));
                                    txtTotal.setText(formater(record.getFloat("target_donasi")));
                                    txtNama.setText(record.getString("name"));
                                    txtTotalDonatur.setText(String.valueOf(donasi_ids));
                                    txtDescription.setText(record.getString("description_purchase"));
                                    txtAuthor.setText(record.getString("create_uid")+" - "+tanggal(record.getString("due_date")));
                                }
                            }
                        });
                    }
                }).build();
    }
}
