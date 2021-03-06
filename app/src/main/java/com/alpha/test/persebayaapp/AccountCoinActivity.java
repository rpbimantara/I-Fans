package com.alpha.test.persebayaapp;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.Collections;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.OArguments;
import oogbox.api.odoo.client.listeners.IOdooResponse;


import static com.alpha.test.persebayaapp.CommonUtils.formater;
import static com.alpha.test.persebayaapp.CommonUtils.getOdooConnection;
import static com.alpha.test.persebayaapp.CommonUtils.tanggal;
import static com.alpha.test.persebayaapp.CommonUtils.waktu;

public class AccountCoinActivity extends AppCompatActivity {

    ArrayList<AccountCoin> ArrayListCoin = new ArrayList<>();;
    int RecyclerViewItemPosition ;
    SharedPrefManager sharedPrefManager;
    RecyclerView rv;
    AdapterCoin adapter;
    SwipeRefreshLayout swiper;
    OdooClient client;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_coin);

        Toolbar toolbar = findViewById(R.id.account_coin_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        sharedPrefManager = new SharedPrefManager(this);
        rv =  findViewById(R.id.rv_recycler_view_account_coin);
        swiper = findViewById(R.id.swiperefresh_account_coin);
        adapter = new AdapterCoin(ArrayListCoin);
        Collections.sort(ArrayListCoin);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        client = getOdooConnection(getBaseContext());
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadCoin();
            }
        });
        loadCoin();
    }

    public void loadCoin(){
        swiper.setRefreshing(true);
        OArguments arguments = new OArguments();
        arguments.add(sharedPrefManager.getSpIdPartner());
        arguments.add(sharedPrefManager.getSpIdUser());

        client.call_kw("res.partner", "get_coin_history", arguments, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                // response
                ArrayListCoin.clear();
                OdooRecord[] records = result.getRecords();
                for (OdooRecord record : records) {

                    String date = CommonUtils.convertTime(record.getString("date"));
                    String tgl = tanggal(date.substring(0,10));
                    String waktu = waktu(date.substring(11,17)) + " "+ "WIB";
                    ArrayListCoin.add(new AccountCoin(
                            record.getString("id"),
                            record.getString("name"),
                            tgl.concat(" ").concat(waktu),
                            formater(Float.valueOf(record.getString("price"))),
                            record.getString("type")
                    ));
                }
                adapter.notifyDataSetChanged();
                swiper.setRefreshing(false);
            }
        });
    }
}
