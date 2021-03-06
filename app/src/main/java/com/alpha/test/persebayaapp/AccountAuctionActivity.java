package com.alpha.test.persebayaapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.ArrayList;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.helper.OdooErrorException;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.ODomain;
import oogbox.api.odoo.client.helper.utils.OdooFields;
import oogbox.api.odoo.client.listeners.IOdooResponse;

import static com.alpha.test.persebayaapp.CommonUtils.getOdooConnection;

public class AccountAuctionActivity extends AppCompatActivity implements AdapterLelang.InterfaceLelang {

    ArrayList<lelang> ArrayListLelang = new ArrayList<>();
    int RecyclerViewItemPosition ;
    SharedPrefManager sharedPrefManager;
    RecyclerView rv;
    ProgressDialog progressDialog;
    AdapterLelang adapter;
    SwipeRefreshLayout swiper;
    OdooClient client;
    Toolbar toolbar;

    @Override
    public void Addbidder(String idlelang, String nilai, String status,ProgressDialog progressDialog) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_auction);
        Toolbar toolbar = findViewById(R.id.account_auction_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        sharedPrefManager = new SharedPrefManager(this);
        rv =  findViewById(R.id.rv_recycler_view_account_auction);
        swiper = findViewById(R.id.swiperefresh_account_auction);
        adapter = new AdapterLelang(ArrayListLelang,getApplicationContext(),AccountAuctionActivity.this);
        rv.setAdapter(adapter);
        client = getOdooConnection(getBaseContext());
        rv.setLayoutManager(new LinearLayoutManager(this));
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadAuction();
            }
        });
        loadAuction();
        progressDialog = new ProgressDialog(this);
    }


    public void loadAuction(){
        swiper.setRefreshing(true);
        ODomain domain = new ODomain();
        domain.add("active", "=", true);
        domain.add("type", "=", "lelang");
        domain.add("create_uid", "=", sharedPrefManager.getSpIdUser());

        OdooFields fields = new OdooFields();
        fields.addAll("id","image_medium","name", "ob","inc","binow","due_date","create_uid");

        int offset = 0;
        int limit = 80;

        String sorting = "id ASC";

        client.searchRead("product.template", domain, fields, offset, limit, sorting,new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                ArrayListLelang.clear();
                OdooRecord[] Records = result.getRecords();
                for (final OdooRecord record : Records) {
                    ArrayListLelang.add(new lelang(
                            String.valueOf(record.getInt("id")),
                            record.getString("name"),
                            record.getString("image_medium"),
                            record.getString("due_date"),
                            String.valueOf(Math.round(record.getFloat("ob"))),
                            String.valueOf(Math.round(record.getFloat("binow"))),
                            String.valueOf(Math.round(record.getFloat("inc"))),
                            String.valueOf(record.getInt("create_uid"))));
                }
                adapter.notifyDataSetChanged();
                swiper.setRefreshing(false);
            }

            @Override
            public boolean onError(OdooErrorException error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                return super.onError(error);
            }
        });
    }
}
