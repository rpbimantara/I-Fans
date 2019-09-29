package com.alpha.test.i_fans;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.OdooVersion;
import oogbox.api.odoo.client.helper.OdooErrorException;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.OdooValues;
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooConnectListener;

import static com.alpha.test.i_fans.CommonUtils.StringToBitMap;
import static com.alpha.test.i_fans.CommonUtils.formater;
//import static com.alpha.test.i_fans.CommonUtils.getSaldo;
import static com.alpha.test.i_fans.CommonUtils.getOdooConnection;
import static com.alpha.test.i_fans.CommonUtils.tanggal;

public class DonationDetailActivity extends AppCompatActivity {
    TextView txtTerkumpul,txtTotal,txtNama,txtTotalDonatur,txtDescription,txtAuthor;
    ProgressBar progressBar;
    Button btn_order;
    SharedPrefManager sharedPrefManager;
    ImageView imageDonation;
    OdooClient client;
    EditText EtDonation;
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
        EtDonation = new EditText(this);
        EtDonation.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog.Builder builder = new AlertDialog.Builder(DonationDetailActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setMessage("Input Your Donation Value!");
        builder.setView(EtDonation);
        builder.setPositiveButton("Donate", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                CommonUtils.getSaldo(getBaseContext(), new IOdooResponse() {
                    @Override
                    public void onResult(OdooResult result) {
                        OdooRecord[] records = result.getRecords();

                        for (OdooRecord record : records) {
                            Log.d(getBaseContext().getClass().getSimpleName(),String.valueOf(record.getInt("saldo")));
                        }
                    }
                });
//                AddDonation(Integer.valueOf(EtDonation.getText().toString()));
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        final AlertDialog alertDialog = builder.create();
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
                EtDonation.getText().clear();
            }
        });

        client = getOdooConnection(getBaseContext());
        LoadData();
    }

    public void LoadData(){
        List<Integer> ids = Arrays.asList(Integer.valueOf(getIntent().getExtras().get("id").toString()));
        List<String> fields = Arrays.asList("id","image_medium","name","list_price","target_donasi", "description_sale","due_date","create_uid","donasi_ids");

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
                    txtDescription.setText(record.getString("description_sale"));
                    txtAuthor.setText(record.getString("create_uid")+" - "+tanggal(record.getString("due_date")));
                }
            }
        });
    }

    public void AddDonation(final Integer Donation){
        OdooValues values = new OdooValues();
        values.put("product_id", Integer.valueOf(getIntent().getExtras().get("id").toString()));
        values.put("user_bid", sharedPrefManager.getSpIdUser());
        values.put("nilai", Donation);
        values.put("keterang", "Donation");

        client.create("persebaya.donasi", values, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                int serverId = result.getInt("result");
                System.out.println(serverId);
            }

            @Override
            public boolean onError(OdooErrorException error) {
                Toast.makeText(getBaseContext(),String.valueOf(error.getMessage()),Toast.LENGTH_LONG).show();
                return super.onError(error);
            }
        });
    }
}