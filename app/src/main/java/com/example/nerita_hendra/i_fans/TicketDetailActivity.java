package com.example.nerita_hendra.i_fans;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TicketDetailActivity extends AppCompatActivity {
    TextView txtNamatiket,txtTanggaltiket,txtWaktutiket;
    Button btn_order;
    ArrayList<Tiket> ArrayListTiket;
    ProgressDialog progressDialog;
    RecyclerView rv;
    RecyclerView.LayoutManager llm;
    AdapterTicket adapter;
    SharedPrefManager sharedPrefManager;
    ImageView imageTiket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);
        Toolbar toolbar = findViewById(R.id.detail_tickt_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        txtNamatiket = (TextView) findViewById(R.id.txt_nama_ticket);
        txtTanggaltiket = (TextView) findViewById(R.id.txt_tgl_ticket);
        txtWaktutiket = (TextView) findViewById(R.id.txt_waktu_ticket);
        imageTiket = (ImageView) findViewById(R.id.ticket_imageView);
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

   public class TicketDetailTask extends AsyncTask<Void,Void,String[]>{
       @Override
       protected void onPreExecute() {
           super.onPreExecute();
       }

       @Override
       protected void onPostExecute(String[] aVoid) {
           txtNamatiket.setText(aVoid[0]);
           txtTanggaltiket.setText(aVoid[1]);
           txtWaktutiket.setText(aVoid[2]);
           imageTiket.setImageBitmap(StringToBitMap(aVoid[3]));
           adapter = new AdapterTicket(ArrayListTiket);
           rv.setAdapter(adapter);
           adapter.notifyDataSetChanged();
           super.onPostExecute(aVoid);
       }

       @Override
       protected String[] doInBackground(Void... voids) {
           ArrayListTiket = new ArrayList<>();
           String nama = "";
           String tanggal = "";
           String time = "";
           String imageCurrent = "";
           String location = "";
           try {
               OdooConnect oc = OdooConnect.connect(sharedPrefManager.getSpNamaUser(),sharedPrefManager.getSpPasswordUser());

               Object[] param = {new Object[]{
                       new Object[]{"id", "=", getIntent().getExtras().get("id")}}};

               List<HashMap<String, Object>> data = oc.search_read("event.event", param, "id","image","name","jadwal_id","date_begin","date_end","event_ticket_ids","address_id");

               for (int i = 0; i < data.size(); ++i) {
                   nama = data.get(i).get("name").toString();
                   imageCurrent = data.get(i).get("image").toString();
                   tanggal = tanggal(data.get(i).get("date_begin").toString());
                   time =  waktu(data.get(i).get("date_begin").toString().substring(12,16)) +" WIB ";
                   System.out.println(data.get(i).get("address_id").toString());
               }

               Object[] paramTiket = {new Object[]{
                       new Object[]{"event_id", "=",  Integer.valueOf(getIntent().getExtras().get("id").toString())}}};

               List<HashMap<String, Object>> dataTiket = oc.search_read("event.event.ticket", paramTiket, "id","name","price","seats_available");

               for (int t = 0; t < dataTiket.size(); ++t) {
                   ArrayListTiket.add(new Tiket(
                           dataTiket.get(t).get("id").toString(),
                           dataTiket.get(t).get("name").toString(),
                          String.valueOf(Math.round(Float.parseFloat(dataTiket.get(t).get("price").toString()))),
                           "0",
                           dataTiket.get(t).get("seats_available").toString()));
               }

           } catch (Exception ex) {
               System.out.println("Error Ticket Add data: " + ex);
           }
           return new String [] {nama,tanggal,time,imageCurrent};
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

    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
}
