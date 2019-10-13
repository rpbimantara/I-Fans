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

public class TicketBarcodeActivity extends AppCompatActivity {

    ArrayList<TicketBarcode> ArrayListTiketBarcode;
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
        rv.setAdapter(adapter );
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
//                new TicketBarcodeTask().execute();
            }
        });
        loadBarcode();
//        new TicketBarcodeTask().execute();
    }

    public void loadBarcode(){
        swiper.setRefreshing(true);
        ArrayListTiketBarcode = new ArrayList<>();
//        ODomain domain = new ODomain();
//        domain.add("partner_id", "=", sharedPrefManager.getSpIdPartner());
//        domain.add("state", "=", "open");
//
//        OdooFields fields = new OdooFields();
//        fields.addAll("id","name","event_id","event_ticket_id","date_open","barcode_image");
//
//        int offset = 0;
//        int limit = 80;
//
//        String sorting = "id ASC";

        OArguments arguments = new OArguments();
        arguments.add(sharedPrefManager.getSpIdPartner());

        client.call_kw("event.registration", "search_ticket", arguments, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                OdooRecord[] records = result.getRecords();
//                System.out.println(result.toString());
                for (OdooRecord record : records) {
                    ArrayListTiketBarcode.add(new TicketBarcode(
                            String.valueOf(record.getInt("id")),
                            record.getString("name"),
                            record.getString("date_open"),
                            record.getString("event_id"),
                            record.getString("event_ticket_id"),
                            record.getString("barcode_image")));
                }
                adapter = new AdapterTicketBarcode(ArrayListTiketBarcode);
                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                swiper.setRefreshing(false);
            }
        });
//        client.searchRead("event.registration", domain, fields, offset, limit, sorting, new IOdooResponse() {
//            @Override
//            public void onResult(OdooResult result) {
//                OdooRecord[] records = result.getRecords();
//                System.out.println(result.toString());
//                for (OdooRecord record : records) {
//                    ArrayListTiketBarcode.add(new TicketBarcode(
//                            String.valueOf(record.getInt("id")),
//                            record.getString("name"),
//                            record.getString("date_open"),
//                            record.getString("event_id"),
//                            record.getString("event_ticket_id"),
//                            record.getString("barcode_image")));
//                }
//                adapter = new AdapterTicketBarcode(ArrayListTiketBarcode);
//                rv.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
//                swiper.setRefreshing(false);
//            }
//        });
    }

//    public class TicketBarcodeTask extends AsyncTask<Void,Void,Void> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            ArrayListTiketBarcode = new ArrayList<>();
//            client = new OdooClient.Builder(getApplicationContext())
//                    .setHost(sharedPrefManager.getSP_Host_url())
//                    .setSession(sharedPrefManager.getSpSessionId())
//                    .setSynchronizedRequests(false)
//                    .setConnectListener(new OdooConnectListener() {
//                        @Override
//                        public void onConnected(OdooVersion version) {
//                            ODomain domain = new ODomain();
//                            domain.add("partner_id", "=", sharedPrefManager.getSpIdPartner());
//                            domain.add("state", "in", Arrays.asList("open","draft"));
//
//                            OdooFields fields = new OdooFields();
//                            fields.addAll("id","name","event_id","event_ticket_id","date_open","barcode_image");
//
//                            int offset = 0;
//                            int limit = 80;
//
//                            String sorting = "id ASC";
//
//                            client.searchRead("event.registration", domain, fields, offset, limit, sorting, new IOdooResponse() {
//                                @Override
//                                public void onResult(OdooResult result) {
//                                    OdooRecord[] records = result.getRecords();
//                                    for (OdooRecord record : records) {
//                                        ArrayListTiketBarcode.add(new TicketBarcode(
//                                                String.valueOf(record.getInt("id")),
//                                                record.getString("name"),
//                                                record.getString("date_open"),
//                                                record.getString("event_id"),
//                                                record.getString("event_ticket_id"),
//                                                record.getString("barcode_image")));
//                                    }
//                                    adapter = new AdapterTicketBarcode(ArrayListTiketBarcode);
//                                    rv.setAdapter(adapter);
//                                    adapter.notifyDataSetChanged();
//                                    swiper.setRefreshing(false);
//                                }
//                            });
//                        }
//                    }).build();
//            return null;
//        }
//    }
}
