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

public class NotifikasiActivity extends AppCompatActivity {

    ArrayList<Notifikasi> ArrayListNotifikasi;
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
//        adapter = new AdapterNotifikasi(ArrayListNotifikasi);
        llm = new LinearLayoutManager(this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(llm);
        sharedPrefManager = new SharedPrefManager(this);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNotifikasi();
//                new NotifikasiTask().execute();
            }
        });
        client = getOdooConnection(getBaseContext());
        loadNotifikasi();
//        new NotifikasiTask().execute();
    }

    public void loadNotifikasi(){
        swiper.setRefreshing(true);
        ArrayListNotifikasi = new ArrayList<>();
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
                OdooRecord[] records = result.getRecords();
                for (OdooRecord record : records) {
                    String tgl = tanggal(record.getString("date").substring(0,10));
                    String waktu = CommonUtils.waktu(record.getString("date").substring(11,17)) + " "+ "WIB";
                    ArrayListNotifikasi.add(new Notifikasi(
                            record.getInt("id"),
                            record.getString("subject"),
                            record.getString("body"),
                            tgl.concat(" ").concat(waktu)));
                }
                adapter = new AdapterNotifikasi(ArrayListNotifikasi);
                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                swiper.setRefreshing(false);
            }
        });
    }

//    public class NotifikasiTask extends AsyncTask<Void,Void,Void>{
//        @Override
//        protected void onPreExecute() {
//            swiper.setRefreshing(true);
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            ArrayListNotifikasi = new ArrayList<>();
//            client = new OdooClient.Builder(getBaseContext())
//                    .setHost(sharedPrefManager.getSP_Host_url())
//                    .setSession(sharedPrefManager.getSpSessionId())
//                    .setSynchronizedRequests(false)
//                    .setConnectListener(new OdooConnectListener() {
//                        @Override
//                        public void onConnected(OdooVersion version) {
//                            // Success connection
//
//                            ODomain domain = new ODomain();
//                            domain.add("partner_ids", "in", Arrays.asList(sharedPrefManager.getSpIdPartner()));
//
//                            OdooFields fields = new OdooFields();
//                            fields.addAll("id", "subject", "body", "date");
//
//                            int offset = 0;
//                            int limit = 5;
//
//                            String sorting = "date DESC";
//
//                            client.searchRead("mail.message", domain, fields, offset, limit, sorting, new IOdooResponse() {
//                                @Override
//                                public void onResult(OdooResult result) {
//                                    OdooRecord[] records = result.getRecords();
//                                    for (OdooRecord record : records) {
//                                        String tgl = tanggal(record.getString("date").substring(0,10));
//                                        String waktu = CommonUtils.waktu(record.getString("date").substring(11,17)) + " "+ "WIB";
//                                        ArrayListNotifikasi.add(new Notifikasi(
//                                                record.getInt("id"),
//                                                record.getString("subject"),
//                                                tgl.concat(" ").concat(waktu)));
//                                    }
//                                    adapter = new AdapterNotifikasi(ArrayListNotifikasi);
//                                    rv.setAdapter(adapter);
//                                    adapter.notifyDataSetChanged();
//                                    swiper.setRefreshing(false);
//                                }
//                            });
//                        }
//                    })
//                    .build();
//            return null;
//        }
//    }
}
