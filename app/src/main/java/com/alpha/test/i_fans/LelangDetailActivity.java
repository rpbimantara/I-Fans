package com.alpha.test.i_fans;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.OdooVersion;
import oogbox.api.odoo.client.helper.OdooErrorException;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.ODomain;
import oogbox.api.odoo.client.helper.utils.OdooFields;
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooConnectListener;

import static com.alpha.test.i_fans.CommonUtils.StringToBitMap;
import static com.alpha.test.i_fans.CommonUtils.tanggal;

public class LelangDetailActivity extends AppCompatActivity {
    TextView txtNamaBarang,txtBid,txtDeskripsi,txtInfoDetail;
    Button btnEdit;
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
        btnEdit = findViewById(R.id.button_edit_lelang);
        txtBid = findViewById(R.id.textView_bid_detail_lelang);
        txtDeskripsi = findViewById(R.id.textView_deskripsi_lelang);
        txtInfoDetail = findViewById(R.id.textView_info_detail_lelang);
        LoadData();
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fabIntent = new Intent(LelangDetailActivity.this,LelangAddActivity.class);
                fabIntent.putExtra("id",Integer.valueOf(getIntent().getExtras().get("id").toString()));
                startActivity(fabIntent);
            }
        });

    }
    public void LoadData(){
        client = new OdooClient.Builder(getApplicationContext())
                .setHost(sharedPrefManager.getSP_Host_url())
                .setSession(sharedPrefManager.getSpSessionId())
                .setSynchronizedRequests(false)
                .setConnectListener(new OdooConnectListener() {
                    @Override
                    public void onConnected(OdooVersion version) {

                        ODomain domain = new ODomain();
                        domain.add("id", "=", Integer.valueOf(getIntent().getExtras().get("id").toString()));

                        OdooFields fields = new OdooFields();
                        fields.addAll("id","image_medium","name", "ob","inc","binow","due_date","create_uid");

                        int offset = 0;
                        int limit = 80;

                        String sorting = "id ASC";

                        client.searchRead("product.template", domain, fields, offset, limit, sorting,new IOdooResponse() {
                            @Override
                            public void onResult(OdooResult result) {
                                OdooRecord[] Records = result.getRecords();
                                for (final OdooRecord record : Records) {
                                    imageDetail.setImageBitmap(StringToBitMap(record.getString("image_medium")));
                                    txtNamaBarang.setText(record.getString("name"));
                                    txtBid.setText("Open Bid : "
                                            + String.valueOf(Math.round(record.getFloat("ob")))
                                            + "\n"
                                            + "BIN : "
                                            + String.valueOf(Math.round(record.getFloat("binow")))
                                            + "\n"
                                            + "INC : "
                                            + String.valueOf(Math.round(record.getFloat("inc")))
                                    );
                                    if (record.getInt("create_uid") == sharedPrefManager.getSpIdUser()){
                                        btnEdit.setVisibility(View.INVISIBLE);
                                    }else{
                                        btnEdit.setVisibility(View.INVISIBLE);
                                    }
                                    txtDeskripsi.setText("Deskripsi : \n\n" + "-");
                                    txtInfoDetail.setText(record.getString("create_uid")+" - "+tanggal(record.getString("due_date")));
                                }
                            }

                            @Override
                            public boolean onError(OdooErrorException error) {
                                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                                return super.onError(error);
                            }
                        });
                    }
                }).build();
    }

}
