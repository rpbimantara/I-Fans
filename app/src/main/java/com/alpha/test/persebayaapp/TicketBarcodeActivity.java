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
import oogbox.api.odoo.client.helper.OdooErrorException;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.OArguments;
import oogbox.api.odoo.client.helper.utils.ODomain;
import oogbox.api.odoo.client.helper.utils.OdooFields;
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooErrorListener;

import static com.alpha.test.persebayaapp.CommonUtils.getOdooConnection1;
import static com.alpha.test.persebayaapp.CommonUtils.tanggal;
import static com.alpha.test.persebayaapp.CommonUtils.waktu;

public class TicketBarcodeActivity extends AppCompatActivity {

    ArrayList<TicketBarcode> ArrayListTiketBarcode = new ArrayList<>();
    SharedPrefManager sharedPrefManager;
    int RecyclerViewItemPosition ;
    RecyclerView rv;
    RecyclerView.LayoutManager llm;
    SwipeRefreshLayout swiper;
    AdapterTicketBarcode adapter;
    OdooClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_barcode);
        Toolbar toolbar = findViewById(R.id.ticket_barcode_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        rv =  findViewById(R.id.rv_recycler_view_ticket_barcode);
        swiper = findViewById(R.id.swiperefresh_ticket_barcode);
        sharedPrefManager = new SharedPrefManager(this);
        llm = new LinearLayoutManager(this);
        adapter = new AdapterTicketBarcode(ArrayListTiketBarcode);
        rv.setAdapter(adapter);
        rv.setLayoutManager(llm);
        client = getOdooConnection1(this, new OdooErrorListener() {
            @Override
            public void onError(OdooErrorException error) {
                swiper.setRefreshing(false);
            }
        });
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadBarcode();
            }
        });
        loadBarcode();
    }

    public void loadBarcode(){
        swiper.setRefreshing(true);

        OArguments arguments = new OArguments();
        arguments.add(sharedPrefManager.getSpIdPartner());

        client.call_kw("event.registration", "search_ticket", arguments, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                ArrayListTiketBarcode.clear();
                OdooRecord[] records = result.getRecords();
                for (OdooRecord record : records) {
                    String date = CommonUtils.convertTime(record.getString("date_open"));
                    String tgl = tanggal(date.substring(0,10));
                    String waktu = waktu(date.substring(11,17)) + " "+ "WIB";
                    ArrayListTiketBarcode.add(new TicketBarcode(
                            String.valueOf(record.getInt("id")),
                            record.getString("name"),
                            tgl.concat(" ").concat(waktu),
                            record.getString("event_id"),
                            record.getString("event_ticket_id"),
                            record.getString("barcode_image")));
                }
                adapter.notifyDataSetChanged();
                swiper.setRefreshing(false);
            }
        });
    }
}
