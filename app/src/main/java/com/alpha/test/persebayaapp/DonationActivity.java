package com.alpha.test.persebayaapp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
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
import oogbox.api.odoo.client.helper.OdooErrorException;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.ODomain;
import oogbox.api.odoo.client.helper.utils.OdooFields;
import oogbox.api.odoo.client.listeners.IOdooResponse;

import static com.alpha.test.persebayaapp.CommonUtils.getOdooConnection;
import static com.alpha.test.persebayaapp.CommonUtils.getSaldo;

public class DonationActivity extends AppCompatActivity {

    ArrayList<Donation> ArrayListDonation = new ArrayList<>();
    SharedPrefManager sharedPrefManager;
    FloatingActionButton fab;
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
        fab = findViewById(R.id.fab_donasi);
        llm = new LinearLayoutManager(this);
        sharedPrefManager = new SharedPrefManager(this);
        adapter = new AdapterDonation(ArrayListDonation);
        rv.setAdapter(adapter);
        rv.setLayoutManager(llm);
        client = getOdooConnection(getBaseContext());
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDonation();
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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSaldo(getBaseContext(), new IOdooResponse() {
                    @Override
                    public void onResult(OdooResult result) {
                        OdooRecord[] Records = result.getRecords();
                        for (final OdooRecord record : Records) {
                            if (record.getString("state").equalsIgnoreCase("draft")){
                                Toast.makeText(getBaseContext(), "Update your profile first!", Toast.LENGTH_SHORT).show();
                            }else{
                                Intent intent = new Intent(DonationActivity.this,DonationAddActivity.class);
                                intent.putExtra("id","false");
                                startActivity(intent);
                            }
                        }
                    }
                });
            }
        });
        loadDonation();
//        new DonationTask().execute();
    }

    public void loadDonation(){
        swiper.setRefreshing(true);
        ODomain domain = new ODomain();
        domain.add("status_donasi", "=", "jalan");
        domain.add("type", "=", "donasi");

        OdooFields fields = new OdooFields();
        fields.addAll("id","image_medium","name", "list_price","create_uid","due_date");

        int offset = 0;
        int limit = 80;

        String sorting = "due_date ASC";

        client.searchRead("product.template", domain, fields, offset, limit, sorting,new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                ArrayListDonation.clear();
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
}
