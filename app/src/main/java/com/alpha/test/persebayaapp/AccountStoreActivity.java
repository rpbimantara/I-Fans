package com.alpha.test.persebayaapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.ODomain;
import oogbox.api.odoo.client.helper.utils.OdooFields;
import oogbox.api.odoo.client.listeners.IOdooResponse;

import static com.alpha.test.persebayaapp.CommonUtils.getOdooConnection;

public class AccountStoreActivity extends AppCompatActivity {

    ArrayList<Store> ArrayListStore = new ArrayList<>();
    int RecyclerViewItemPosition ;
    SharedPrefManager sharedPrefManager;
    RecyclerView rv;
    ProgressDialog progressDialog;
    AdapterStore adapter;
    SwipeRefreshLayout swiper;
    OdooClient client;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_store);
        Toolbar toolbar = findViewById(R.id.account_store_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        sharedPrefManager = new SharedPrefManager(this);
        rv =  findViewById(R.id.rv_recycler_view_account_store);
        swiper = findViewById(R.id.swiperefresh_account_store);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadStore();
            }
        });
        adapter = new AdapterStore(ArrayListStore);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new GridLayoutManager(this,3));
        rv.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override public boolean onSingleTapUp(MotionEvent motionEvent) {

                    return true;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View ChildView = rv.findChildViewUnder(e.getX(), e.getY());

                if(ChildView != null && gestureDetector.onTouchEvent(e)) {
                    if (adapter.getItemCount()<=1) return false;
                    RecyclerViewItemPosition = rv.getChildAdapterPosition(ChildView);
                    Intent intent = new Intent(getApplicationContext(),StoreDetailActivity.class);
                    intent.putExtra("id",ArrayListStore.get(RecyclerViewItemPosition).getId());
                    intent.putExtra("nama",ArrayListStore.get(RecyclerViewItemPosition).getNamabarang());
                    intent.putExtra("harga",ArrayListStore.get(RecyclerViewItemPosition).getHargabarang());
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
        client = getOdooConnection(getBaseContext());
        loadStore();
    }

    public void loadStore(){
        swiper.setRefreshing(true);
        ODomain domain = new ODomain();
        domain.add("active", "=", true);
        domain.add("type", "=", "product");
        domain.add("create_uid", "=", sharedPrefManager.getSpIdUser());

        OdooFields fields = new OdooFields();
        fields.addAll("id","image_medium","name", "type","default_code","cated_ig","list_price");

        int offset = 0;
        int limit = 80;

        String sorting = "id ASC";

        client.searchRead("product.template", domain, fields, offset, limit, sorting, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                ArrayListStore.clear();
                OdooRecord[] records = result.getRecords();
                for (OdooRecord record : records) {
                    String code = " ";
                    if (record.getString("default_code").equalsIgnoreCase("false") || record.getString("default_code").equalsIgnoreCase("")){
                        code = "";
                    }else{
                        code = "["+record.getString("default_code") +"] ";
                    }
                    ArrayListStore.add(new Store(
                            String.valueOf(record.getInt("id")),
                            record.getString("image_medium"),
                            code +record.getString("name"),
                            String.valueOf(Math.round(record.getFloat("list_price")))));
                }
                adapter.notifyDataSetChanged();
                swiper.setRefreshing(false);
            }

        });
    }

}
