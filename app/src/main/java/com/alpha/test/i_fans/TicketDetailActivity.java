package com.alpha.test.i_fans;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
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
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.OdooVersion;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.ODomain;
import oogbox.api.odoo.client.helper.utils.OdooFields;
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooConnectListener;

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
    OdooClient client;

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
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setTitle(R.string.app_name);
                builder.setMessage("Are You Sure to Buy This Ticket?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        new TicketTask().execute();
        TicketDetail();
    }

    public void TicketDetail(){
        client = new OdooClient.Builder(getApplicationContext())
                .setHost(sharedPrefManager.getSP_Host_url())
                .setSession(sharedPrefManager.getSpSessionId())
                .setSynchronizedRequests(false)
                .setConnectListener(new OdooConnectListener() {
                    @Override
                    public void onConnected(OdooVersion version) {
                        List<Integer> ids = Arrays.asList(Integer.valueOf(getIntent().getExtras().get("id").toString()));
                        List<String> fields = Arrays.asList("id","image","name","jadwal_id","date_begin","date_end","event_ticket_ids","address_id");

                        client.read("event.event", ids, fields, new IOdooResponse() {
                            @Override
                            public void onResult(OdooResult result) {
                                OdooRecord[] records = result.getRecords();

                                for(OdooRecord record: records) {
                                    String tanggal = tanggal(record.getString("date_begin"));
                                    String time =  waktu(record.getString("date_begin").substring(12,16)) +" WIB ";
                                    txtNamatiket.setText(record.getString("name"));
                                    txtTanggaltiket.setText(tanggal);
                                    txtWaktutiket.setText(time);
                                    imageTiket.setImageBitmap(StringToBitMap(record.getString("image")));
                                }
                            }
                        });
                    }
                }).build();
    }

   public class TicketTask extends AsyncTask<Void,Void,Void>{
       @Override
       protected void onPreExecute() {
           super.onPreExecute();
       }


       @Override
       protected Void doInBackground(Void... voids) {
           ArrayListTiket = new ArrayList<>();
           client = new OdooClient.Builder(getApplicationContext())
                   .setHost(sharedPrefManager.getSP_Host_url())
                   .setSession(sharedPrefManager.getSpSessionId())
                   .setSynchronizedRequests(false)
                   .setConnectListener(new OdooConnectListener() {
                       @Override
                       public void onConnected(OdooVersion version) {
                           ODomain domain = new ODomain();
                           domain.add("event_id", "=", Integer.valueOf(getIntent().getExtras().get("id").toString()));

                           OdooFields fields = new OdooFields();
                           fields.addAll("id","name","price","seats_available");

                           int offset = 0;
                           int limit = 80;

                           String sorting = "id ASC";

                           client.searchRead("event.event.ticket", domain, fields, offset, limit, sorting, new IOdooResponse() {
                               @Override
                               public void onResult(OdooResult result) {
                                   OdooRecord[] records = result.getRecords();
                                   for (OdooRecord record : records) {
                                       ArrayListTiket.add(new Tiket(
                                               String.valueOf(record.getInt("id")),
                                               record.getString("name"),
                                               String.valueOf(Math.round(record.getFloat("price"))),
                                               "0",
                                               String.valueOf(Math.round(record.getFloat("seats_available")))));
                                   }
                                   adapter = new AdapterTicket(ArrayListTiket);
                                   rv.setAdapter(adapter);
                                   adapter.notifyDataSetChanged();
                               }
                           });
                       }
                   }).build();
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
