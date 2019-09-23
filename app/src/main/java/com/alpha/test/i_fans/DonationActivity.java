package com.alpha.test.i_fans;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.OdooVersion;
import oogbox.api.odoo.client.helper.OdooErrorException;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.ODomain;
import oogbox.api.odoo.client.helper.utils.OdooFields;
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooConnectListener;

public class DonationActivity extends AppCompatActivity {

    ArrayList<Donation> ArrayListDonation;
    SharedPrefManager sharedPrefManager;
    int RecyclerViewItemPosition ;
    RecyclerView rv;
    RecyclerView.LayoutManager llm;
    SwipeRefreshLayout swiper;
    OdooClient client;
    AdapterDonation adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);
        Toolbar toolbar = findViewById(R.id.donasi_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        rv =  findViewById(R.id.rv_recycler_view_donasi);
        swiper = findViewById(R.id.swiperefresh_donasi);
        llm = new LinearLayoutManager(this);
        sharedPrefManager = new SharedPrefManager(this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(llm);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new DonationTask().execute();
            }
        });
        rv.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(getBaseContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override public boolean onSingleTapUp(MotionEvent motionEvent) {

                    return true;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View ChildView = rv.findChildViewUnder(e.getX(), e.getY());

                if(ChildView != null && gestureDetector.onTouchEvent(e)) {

                    RecyclerViewItemPosition = rv.getChildAdapterPosition(ChildView);
                    Intent intent = new Intent(getBaseContext(),DonationDetailActivity.class);
                    intent.putExtra("id",ArrayListDonation.get(RecyclerViewItemPosition).getIdDonation());
                    startActivity(intent);
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        new DonationTask().execute();
    }

    public class DonationTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            swiper.setRefreshing(true);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ArrayListDonation = new ArrayList<>();
            client = new OdooClient.Builder(getBaseContext())
                    .setHost(sharedPrefManager.getSP_Host_url())
                    .setSession(sharedPrefManager.getSpSessionId())
                    .setSynchronizedRequests(false)
                    .setConnectListener(new OdooConnectListener() {
                        @Override
                        public void onConnected(OdooVersion version) {
                            ODomain domain = new ODomain();
//                            domain.add("status_donasi", "=", "jalan");
                            domain.add("type", "=", "donasi");

                            OdooFields fields = new OdooFields();
                            fields.addAll("id","image_medium","name", "list_price","create_uid","due_date");

                            int offset = 0;
                            int limit = 80;

                            String sorting = "due_date ASC";

                            client.searchRead("product.template", domain, fields, offset, limit, sorting,new IOdooResponse() {
                                @Override
                                public void onResult(OdooResult result) {
                                    OdooRecord[] Records = result.getRecords();
                                    for (final OdooRecord record : Records) {
                                        ArrayListDonation.add(new Donation(
                                                String.valueOf(record.getInt("id")),
                                                record.getString("name"),
                                                record.getString("image_medium"),
                                                record.getString("due_date"),
                                                record.getString("create_uid"),
                                                record.getString("list_price")));
                                    }
                                    adapter = new AdapterDonation(ArrayListDonation);
                                    rv.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                    swiper.setRefreshing(false);
                                }

                                @Override
                                public boolean onError(OdooErrorException error) {
                                    Toast.makeText(getBaseContext(),error.toString(),Toast.LENGTH_SHORT).show();
                                    return super.onError(error);
                                }
                            });
                        }
                    }).build();
            return null;
        }
    }
}
