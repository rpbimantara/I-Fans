package com.example.nerita_hendra.i_fans;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TicketActivity extends AppCompatActivity {

    ArrayList<TiketList> ArrayListTiketList;
    SharedPrefManager sharedPrefManager;
    int RecyclerViewItemPosition ;
    RecyclerView rv;
    RecyclerView.LayoutManager llm;
    SwipeRefreshLayout swiper;
    AdapterTiketList adapter;

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
        adapter = new AdapterTiketList(ArrayListTiketList);
        rv.setAdapter(adapter );
        rv.setLayoutManager(llm);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new TicketTask().execute();
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
        new TicketTask().execute();
    }


    public class TicketTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            swiper.setRefreshing(true);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter = new AdapterTiketList(ArrayListTiketList);
            rv.setAdapter(adapter );
            adapter.notifyDataSetChanged();
            swiper.setRefreshing(false);
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ArrayListTiketList = new ArrayList<>();
            try {
                OdooConnect oc = OdooConnect.connect(sharedPrefManager.getSpNamaUser(),sharedPrefManager.getSpPasswordUser());

                Object[] param = {new Object[]{
                        new Object[]{"state", "=", "confirm"}}};

                List<HashMap<String, Object>> dataTiketList = oc.search_read("event.event", param, "id","image","name", "date_begin","organizer_id","event_type_id");

                for (int i = 0; i < dataTiketList.size(); ++i) {
                    ArrayListTiketList.add(new TiketList(
                            dataTiketList.get(i).get("id").toString(),
                            dataTiketList.get(i).get("image").toString(),
                            dataTiketList.get(i).get("name").toString(),
                            dataTiketList.get(i).get("date_begin").toString(),
                            dataTiketList.get(i).get("organizer_id").toString(),
                            dataTiketList.get(i).get("event_type_id").toString()
                    ));
                }
            } catch (Exception ex) {
                System.out.println("Error Ticket Add data: " + ex);
            }
//            try {
//                OdooConnect oc = OdooConnect.connect(sharedPrefManager.getSpNamaUser(),sharedPrefManager.getSpPasswordUser());
//
//                Object[] param = {new Object[]{
////                        new Object[]{"create_uid", "=", 1},
//                        new Object[]{"home", "=", sharedPrefManager.getSpNamaClub()},
//                        new Object[]{"status_jadwal", "!=", "selesai"}}};
//
//                List<HashMap<String, Object>> dataJadwal = oc.search_read("persebaya.jadwal", param, "id","liga_id", "tgl_main","home","away","stadion_id","status_jadwal");
//
//                for (int i = 0; i < dataJadwal.size(); ++i) {
//                    String tgl = tanggal(dataJadwal.get(i).get("tgl_main").toString().substring(0,10));
//                    String waktu = waktu(dataJadwal.get(i).get("tgl_main").toString().substring(12,16)) + " "+ "WIB";
//                    if (dataJadwal.get(i).get("home").toString().equalsIgnoreCase(sharedPrefManager.getSpNamaClub())){
//                        Object[] paramclub = {new Object[]{
//                                new Object[]{"nama", "=", dataJadwal.get(i).get("away")}}};
//
//                        List<HashMap<String, Object>> dataclub = oc.search_read("persebaya.club", paramclub, "foto_club");
//                        for (int c = 0; c < dataclub.size(); ++c) {
//                            ArrayListJadwal.add(new Jadwal(
//                                    dataJadwal.get(i).get("away").toString(),
//                                    String.valueOf(dataclub.get(c).get("foto_club")),
//                                    getResources().getIdentifier("ic_home","drawable",getPackageName()),
//                                    dataJadwal.get(i).get("liga_id").toString(),
//                                    tgl,
//                                    dataJadwal.get(i).get("stadion_id").toString()
//                                    , waktu,
//                                    dataJadwal.get(i).get("id").toString(),
//                                    dataJadwal.get(i).get("status_jadwal").toString()));
//                        }
//                    }
//                }
//            } catch (Exception ex) {
//                System.out.println("Error Ticket Add data: " + ex);
//            }
            return null;
        }
    }

    public String tanggal(String tgl){
        try {
            tgl = new SimpleDateFormat("dd MMM yyyy", Locale.US).format(new SimpleDateFormat("yyyy-MM-dd").parse(tgl));
        }catch (Exception ex){
            System.out.println("Error Convert Tanggal: " + ex);
        }

        return tgl;
    }

    public  String waktu(String waktu){
        int output = Integer.valueOf(waktu.substring(0,1))+7;
        waktu = String.valueOf(output) + waktu.substring(1,4);
        return waktu;
    }
}
