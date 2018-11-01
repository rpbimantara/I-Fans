package com.example.nerita_hendra.i_fans;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TicketDetailActivity extends AppCompatActivity {
    TextView txtNamatiket,txtTanggaltiket,txtWaktutiket;
    Button btn_order;
    ArrayList<Tiket> ArrayListTiket;
    ProgressDialog progressDialog;
    RecyclerView rv;
    RecyclerView.LayoutManager llm;
    AdapterTicket adapter;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);
        txtNamatiket = (TextView) findViewById(R.id.txt_nama_ticket);
        txtTanggaltiket = (TextView) findViewById(R.id.txt_tgl_ticket);
        txtWaktutiket = (TextView) findViewById(R.id.txt_waktu_ticket);
        btn_order = findViewById(R.id.button_review_order);
        progressDialog = new ProgressDialog(this);
        rv = findViewById(R.id.rv_recycler_view_tiket_detail);
        llm = new LinearLayoutManager(this);
        adapter = new AdapterTicket(ArrayListTiket);
        rv.setAdapter(adapter);
        rv.setLayoutManager(llm);
        sharedPrefManager = new SharedPrefManager(this);
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        new TicketDetailTask().execute();
    }

   public class TicketDetailTask extends AsyncTask<Void,Void,String>{
       @Override
       protected void onPreExecute() {
           super.onPreExecute();
       }

       @Override
       protected void onPostExecute(String aVoid) {
           txtNamatiket.setText(aVoid);
           txtWaktutiket.setText(getIntent().getExtras().get("waktu").toString());
           txtTanggaltiket.setText(getIntent().getExtras().get("tgl").toString());
           adapter = new AdapterTicket(ArrayListTiket);
           rv.setAdapter(adapter);
           adapter.notifyDataSetChanged();
           super.onPostExecute(aVoid);
       }

       @Override
       protected String doInBackground(Void... voids) {
           ArrayListTiket = new ArrayList<>();
           String nama = "";
           Object id_event = "";
           try {
               OdooConnect oc = OdooConnect.connect(sharedPrefManager.getSpNamaUser(),sharedPrefManager.getSpPasswordUser());

               Object[] param = {new Object[]{
                       new Object[]{"jadwal_id", "=", getIntent().getExtras().get("jadwal_id")}}};

               List<HashMap<String, Object>> data = oc.search_read("event.event", param, "id","name","jadwal_id","date_begin","date_end","event_ticket_ids");
               for (int i = 0; i < data.size(); ++i) {
                   nama = data.get(i).get("name").toString();
                   id_event = data.get(i).get("id");

               }

               Object[] paramTiket = {new Object[]{
                       new Object[]{"event_id", "=", id_event}}};

               List<HashMap<String, Object>> dataTiket = oc.search_read("event.event.ticket", paramTiket, "id","name","price","seats_available","event_id");

               for (int t = 0; t < dataTiket.size(); ++t) {
                   ArrayListTiket.add(new Tiket(
                           dataTiket.get(t).get("name").toString(),
                           dataTiket.get(t).get("price").toString(),
                           "0",
                           dataTiket.get(t).get("seats_available").toString()));
               }
           } catch (Exception ex) {
               System.out.println("Error Ticket Add data: " + ex);
           }
           return nama;
       }
   }
}
