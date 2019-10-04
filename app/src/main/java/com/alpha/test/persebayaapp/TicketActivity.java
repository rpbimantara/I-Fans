package com.alpha.test.persebayaapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class TicketActivity extends AppCompatActivity {

    ArrayList<TiketList> ArrayListTiketList;
    SharedPrefManager sharedPrefManager;
    int RecyclerViewItemPosition ;
    RecyclerView rv;
    RecyclerView.LayoutManager llm;
    SwipeRefreshLayout swiper;
    AdapterTiketList adapter;
    OdooClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        Toolbar toolbar = findViewById(R.id.ticket_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        rv =  findViewById(R.id.rv_recycler_view_ticket);
        swiper = findViewById(R.id.swiperefresh_ticket);
        llm = new LinearLayoutManager(this);
        rv.setAdapter(adapter );
        rv.setLayoutManager(llm);
        client = getOdooConnection(getBaseContext());
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadTicket();
//                new TicketTask().execute();
            }
        });
        rv.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(TicketActivity.this, new GestureDetector.SimpleOnGestureListener() {

                @Override public boolean onSingleTapUp(MotionEvent motionEvent) {

                    return true;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View ChildView = rv.findChildViewUnder(e.getX(), e.getY());

                if(ChildView != null && gestureDetector.onTouchEvent(e)) {

                    RecyclerViewItemPosition = rv.getChildAdapterPosition(ChildView);
                    Intent intent = new Intent(TicketActivity.this,TicketDetailActivity.class);
                    intent.putExtra("id",ArrayListTiketList.get(RecyclerViewItemPosition).getId());
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
        sharedPrefManager = new SharedPrefManager(this);
//        new TicketTask().execute();
        loadTicket();
    }

    public void loadTicket(){
        swiper.setRefreshing(true);
        ArrayListTiketList = new ArrayList<>();
        ODomain domain = new ODomain();
        domain.add("state", "=", "confirm");

        OdooFields fields = new OdooFields();
        fields.addAll("id","image","name", "date_begin","organizer_id","event_type_id");

        int offset = 0;
        int limit = 80;

        String sorting = "id ASC";

        client.searchRead("event.event", domain, fields, offset, limit, sorting, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                OdooRecord[] records = result.getRecords();
                for(OdooRecord record: records) {
                    ArrayListTiketList.add(new TiketList(
                            String.valueOf(record.getInt("id")),
                            record.getString("image"),
                            record.getString("name"),
                            record.getString("date_begin"),
                            record.getString("organizer_id"),
                            record.getString("event_type_id")
                    ));
                }
                Log.v("adsasdasd",String.valueOf(ArrayListTiketList.size()));
                adapter = new AdapterTiketList(ArrayListTiketList);
                rv.setAdapter(adapter );
                adapter.notifyDataSetChanged();
                swiper.setRefreshing(false);
            }
        });
    }


//    public class TicketTask extends AsyncTask<Void,Void,Void>{
//        @Override
//        protected void onPreExecute() {
//            swiper.setRefreshing(true);
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            ArrayListTiketList = new ArrayList<>();
//            client = new OdooClient.Builder(getBaseContext())
//                    .setHost(sharedPrefManager.getSP_Host_url())
//                    .setSession(sharedPrefManager.getSpSessionId())
//                    .setSynchronizedRequests(false)
//                    .setConnectListener(new OdooConnectListener() {
//                        @Override
//                        public void onConnected(OdooVersion version) {
//                            // Success connection
//                            ODomain domain = new ODomain();
//                            domain.add("state", "=", "confirm");
//
//                            OdooFields fields = new OdooFields();
//                            fields.addAll("id","image","name", "date_begin","organizer_id","event_type_id");
//
//                            int offset = 0;
//                            int limit = 80;
//
//                            String sorting = "id ASC";
//
//                            client.searchRead("event.event", domain, fields, offset, limit, sorting, new IOdooResponse() {
//                                            @Override
//                                            public void onResult(OdooResult result) {
//                                                OdooRecord[] records = result.getRecords();
//                                                for(OdooRecord record: records) {
//                                                     ArrayListTiketList.add(new TiketList(
//                                                            String.valueOf(record.getInt("id")),
//                                                            record.getString("image"),
//                                                             record.getString("name"),
//                                                             record.getString("date_begin"),
//                                                             record.getString("organizer_id"),
//                                                             record.getString("event_type_id")
//                                                    ));
//                                                }
//                                                Log.v("adsasdasd",String.valueOf(ArrayListTiketList.size()));
//                                                adapter = new AdapterTiketList(ArrayListTiketList);
//                                                rv.setAdapter(adapter );
//                                                adapter.notifyDataSetChanged();
//                                                swiper.setRefreshing(false);
//                                            }
//                                        });
//
//                        }
//                    })
//                    .build();
//            return null;
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_ticket, menu);
        Drawable ticket = menu.findItem(R.id.ticket).getIcon();
        DrawableCompat.setTint(ticket, ContextCompat.getColor(this,R.color.colorTrueState));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.ticket:
                Intent ticketIntent = new Intent(TicketActivity.this, TicketBarcodeActivity.class);
                startActivity(ticketIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
