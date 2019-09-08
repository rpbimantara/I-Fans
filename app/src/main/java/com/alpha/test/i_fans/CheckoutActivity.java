package com.alpha.test.i_fans;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.OdooVersion;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.OArguments;
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooConnectListener;

public class CheckoutActivity extends AppCompatActivity {
    ArrayList<Checkout> ArrayListCheckout;
    SharedPrefManager sharedPrefManager;
    RecyclerView rv;
    RecyclerView.LayoutManager llm;
    SwipeRefreshLayout swiper;
    AdapterCheckout adapter;
    OdooClient client;

    private Listener mListener;

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public interface Listener {
        void returnData(int result);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        Toolbar toolbar = findViewById(R.id.checkout_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        rv =  findViewById(R.id.rv_recycler_view_checkout);
        swiper = findViewById(R.id.swiperefresh_checkout);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new LoadCheckoutAsync().execute();
            }
        });
        llm = new LinearLayoutManager(this);
        adapter = new AdapterCheckout(ArrayListCheckout);
        sharedPrefManager = new SharedPrefManager(this);
        rv.setAdapter(adapter );
        rv.setLayoutManager(llm);
        new LoadCheckoutAsync().execute();
    }


    public class LoadCheckoutAsync extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            swiper.setRefreshing(true);
        }


        @Override
        protected Void doInBackground(Void... voids) {
            ArrayListCheckout = new ArrayList<>();
            client = new OdooClient.Builder(getBaseContext())
                    .setHost(sharedPrefManager.getSP_Host_url())
                    .setSession(sharedPrefManager.getSpSessionId())
                    .setSynchronizedRequests(false)
                    .setConnectListener(new OdooConnectListener() {
                        @Override
                        public void onConnected(OdooVersion version) {
                            // Success connection

                            OArguments arguments = new OArguments();
                            arguments.add(Integer.valueOf(sharedPrefManager.getSpIdPartner()));

                            client.call_kw("sale.order.line", "get_checkout_list", arguments, new IOdooResponse() {
                                @Override
                                public void onResult(OdooResult result) {
                                    // response
                                    OdooRecord[] Records = result.getRecords();
                                    System.out.println(result.toString());

                                    for (final OdooRecord record : Records) {
                                        ArrayListCheckout.add(new Checkout(
                                                String.valueOf(record.getInt("id")),
                                                record.getString("nama"),
                                                String.valueOf(Math.round(record.getFloat("harga"))),
                                                String.valueOf(record.getInt("qty")),
                                                record.getString("image"),
                                                String.valueOf(record.getInt("stock"))
                                                        ));
                                    }
                                    adapter = new AdapterCheckout(ArrayListCheckout);
                                    rv.setAdapter(adapter );
                                    adapter.notifyDataSetChanged();
                                    swiper.setRefreshing(false);
                                }
                            });
                        }
                    })
                    .build();
            return null;
        }
    }


}
