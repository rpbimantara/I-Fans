package com.alpha.test.i_fans;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.ArrayList;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.OdooVersion;
import oogbox.api.odoo.client.helper.OdooErrorException;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.OArguments;
import oogbox.api.odoo.client.helper.utils.ODomain;
import oogbox.api.odoo.client.helper.utils.OdooFields;
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooConnectListener;

public class AccountAuctionActivity extends AppCompatActivity {

    ArrayList<lelang> ArrayListLelang;
    int RecyclerViewItemPosition ;
    SharedPrefManager sharedPrefManager;
    RecyclerView rv;
    ProgressDialog progressDialog;
    AdapterLelang adapter;
    SwipeRefreshLayout swiper;
    OdooClient client;
    Toolbar toolbar;

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
        adapter = new AdapterLelang(ArrayListLelang,getApplicationContext());
        rv.setAdapter(adapter );
        rv.setLayoutManager(new LinearLayoutManager(this));
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new AccountAuctionTask().execute();
            }
        });
        progressDialog = new ProgressDialog(this);
    }

    @Override
    protected void onResume() {
        new AccountAuctionTask().execute();
        super.onResume();
    }

    public class AccountAuctionTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            swiper.setRefreshing(true);
        }


        @Override
        protected Void doInBackground(Void... voids) {

            ArrayListLelang = new ArrayList<>();
            client = new OdooClient.Builder(getApplicationContext())
                    .setHost(sharedPrefManager.getSP_Host_url())
                    .setSession(sharedPrefManager.getSpSessionId())
                    .setSynchronizedRequests(false)
                    .setConnectListener(new OdooConnectListener() {
                        @Override
                        public void onConnected(OdooVersion version) {
                            ODomain domain = new ODomain();
                            domain.add("status_lelang", "=", "jalan");
                            domain.add("create_uid", "=", sharedPrefManager.getSpIdUser());

                            OdooFields fields = new OdooFields();
                            fields.addAll("id","foto_lelang","nama_barang", "ob","inc","binow","due_date","create_uid");

                            int offset = 0;
                            int limit = 80;

                            String sorting = "id ASC";

                            client.searchRead("persebaya.lelang", domain, fields, offset, limit, sorting,new IOdooResponse() {
                                @Override
                                public void onResult(OdooResult result) {
                                    OdooRecord[] Records = result.getRecords();
                                    for (final OdooRecord record : Records) {
                                        ArrayListLelang.add(new lelang(
                                                String.valueOf(record.getInt("id")),
                                                record.getString("nama_barang"),
                                                record.getString("foto_lelang"),
                                                record.getString("due_date"),
                                                String.valueOf(Math.round(record.getFloat("ob"))),
                                                String.valueOf(Math.round(record.getFloat("binow"))),
                                                String.valueOf(Math.round(record.getFloat("inc"))),
                                                String.valueOf(record.getInt("create_uid"))));
                                    }
                                    adapter = new AdapterLelang(ArrayListLelang,getApplicationContext());
                                    rv.setAdapter(adapter);
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
                    }).build();
            return null;
        }
    }
}
