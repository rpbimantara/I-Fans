package com.alpha.test.persebayaapp;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.Arrays;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.ODomain;
import oogbox.api.odoo.client.helper.utils.OdooFields;
import oogbox.api.odoo.client.listeners.IOdooResponse;

import static com.alpha.test.persebayaapp.CommonUtils.getOdooConnection;
import static com.alpha.test.persebayaapp.CommonUtils.tanggal;
import static com.alpha.test.persebayaapp.CommonUtils.waktu;

public class NotifikasiActivity extends AppCompatActivity {

    ArrayList<Notifikasi> ArrayListNotifikasi = new ArrayList<>();
    SharedPrefManager sharedPrefManager;
    int RecyclerViewItemPosition ;
    RecyclerView rv;
    RecyclerView.LayoutManager llm;
    SwipeRefreshLayout swiper;
    AdapterNotifikasi adapter;
    OdooClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifikasi);
        Toolbar toolbar = findViewById(R.id.notifikasi_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        rv =  findViewById(R.id.rv_recycler_view_notifikasi);
        swiper = findViewById(R.id.swiperefresh_notifikasi);
        llm = new LinearLayoutManager(this);
        adapter = new AdapterNotifikasi(ArrayListNotifikasi);
        rv.setAdapter(adapter);
        rv.setLayoutManager(llm);
        sharedPrefManager = new SharedPrefManager(this);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNotifikasi();
            }
        });
        client = getOdooConnection(getBaseContext());
        loadNotifikasi();
    }

    public void loadNotifikasi(){
        swiper.setRefreshing(true);
        ODomain domain = new ODomain();
        domain.add("partner_ids", "in", Arrays.asList(sharedPrefManager.getSpIdPartner()));

        OdooFields fields = new OdooFields();
        fields.addAll("id", "subject", "body", "date");

        int offset = 0;
        int limit = 5;

        String sorting = "date DESC";

        client.searchRead("mail.message", domain, fields, offset, limit, sorting, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                ArrayListNotifikasi.clear();
                OdooRecord[] records = result.getRecords();
                for (OdooRecord record : records) {
                    String date = CommonUtils.convertTime(record.getString("date"));
                    String tgl = tanggal(date.substring(0,10));
                    String waktu = waktu(date.substring(11,17)) + " "+ "WIB";
                    ArrayListNotifikasi.add(new Notifikasi(
                            record.getInt("id"),
                            record.getString("subject"),
                            record.getString("body"),
                            tgl.concat(" ").concat(waktu)));
                }
                adapter.notifyDataSetChanged();
                swiper.setRefreshing(false);
            }
        });
    }
}
