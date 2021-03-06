package com.alpha.test.persebayaapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.helper.OdooErrorException;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.OArguments;
import oogbox.api.odoo.client.helper.utils.ODomain;
import oogbox.api.odoo.client.helper.utils.OdooFields;
import oogbox.api.odoo.client.helper.utils.OdooValues;
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooErrorListener;

import static com.alpha.test.persebayaapp.CommonUtils.StringToBitMap;
import static com.alpha.test.persebayaapp.CommonUtils.formater;
import static com.alpha.test.persebayaapp.CommonUtils.getOdooConnection;
import static com.alpha.test.persebayaapp.CommonUtils.getOdooConnection1;
import static com.alpha.test.persebayaapp.CommonUtils.getSaldo;
import static com.alpha.test.persebayaapp.CommonUtils.tanggal;
import static com.alpha.test.persebayaapp.CommonUtils.waktu;

public class TicketDetailActivity extends AppCompatActivity implements AdapterTicket.TicketListener {
    public final String TAG = this.getClass().getSimpleName();
    TextView txtNamatiket,txtTanggaltiket,txtWaktutiket,txtTotalAmount;
    Button btn_order;
    ArrayList<Tiket> ArrayListTiket = new ArrayList<>();
    ProgressDialog progressDialog;
    RecyclerView rv;
    RecyclerView.LayoutManager llm;
    AdapterTicket adapter;
    SharedPrefManager sharedPrefManager;
    ImageView imageTiket;
    OdooClient client;
    int total = 0;

    @Override
    public void onChangeButtonTicket(Tiket tiket,int jumlah) {
        tiket.setJumlahTiket(String.valueOf(jumlah));
        int temp = 0;
        for (Tiket tkt : ArrayListTiket) {
                temp += (Integer.valueOf(tkt.getHargaTiket()) * Integer.valueOf(tkt.getJumlahTiket()));
        }
        txtTotalAmount.setText(formater(Float.parseFloat(String.valueOf(temp))));
        total = temp;
    }

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
        txtTotalAmount = findViewById(R.id.total_amount_ticket);
        imageTiket = (ImageView) findViewById(R.id.ticket_imageView);
        btn_order = findViewById(R.id.button_review_order);
        progressDialog = new ProgressDialog(this);
        rv = findViewById(R.id.rv_recycler_view_tiket_detail);
        llm = new LinearLayoutManager(this);
        adapter = new AdapterTicket(ArrayListTiket,TicketDetailActivity.this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(llm);
        sharedPrefManager = new SharedPrefManager(this);
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                progressDialog.setCancelable(false);
                AlertDialog.Builder builder = new AlertDialog.Builder(TicketDetailActivity.this);
                builder.setTitle(R.string.app_name);
                builder.setMessage("Are You Sure to Buy This Ticket?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        BuyTicket();
                        dialogInterface.dismiss();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                final AlertDialog alertDialog = builder.create();
                getSaldo(getBaseContext(), new IOdooResponse() {
                    @Override
                    public void onResult(OdooResult result) {
                        OdooRecord[] Records = result.getRecords();
                        for (final OdooRecord record : Records) {
                            if (record.getString("state").equalsIgnoreCase("draft")){
                                progressDialog.dismiss();
                                Toast.makeText(getBaseContext(), "Update your profile first!", Toast.LENGTH_SHORT).show();
                            }else{
                                if(total>0){
                                    if (record.getInt("saldo") < total ){
                                        progressDialog.dismiss();
                                        Toast.makeText(getBaseContext(), "Top up now to finish this transaction!", Toast.LENGTH_SHORT).show();
                                    }else {
                                        alertDialog.show();
                                    }
                                }
                                else{
                                    progressDialog.dismiss();
                                    Toast.makeText(getBaseContext(), "Choose at least one ticket!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });
            }
        });
        client = getOdooConnection1(getBaseContext(), new OdooErrorListener() {
            @Override
            public void onError(OdooErrorException error) {
                Log.e(TAG,"SO Id : " + error.toString());
                progressDialog.dismiss();
            }
        });
        loadClassTicket();
        TicketDetail();
    }

    public void TicketDetail(){
        List<Integer> ids = Arrays.asList(Integer.valueOf(getIntent().getExtras().get("id").toString()));
        List<String> fields = Arrays.asList("id","image","name","jadwal_id","date_begin","date_end","event_ticket_ids","address_id");

        client.read("event.event", ids, fields, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                OdooRecord[] records = result.getRecords();

                for(OdooRecord record: records) {
                    String date = CommonUtils.convertTime(record.getString("date_begin"));
                    String tgl = tanggal(date.substring(0,10));
                    String waktu = waktu(date.substring(11,17)) + " "+ "WIB";
                    txtNamatiket.setText(record.getString("name"));
                    txtTanggaltiket.setText(tgl);
                    txtWaktutiket.setText(waktu);
                    imageTiket.setImageBitmap(StringToBitMap(record.getString("image")));
                }
            }
        });
    }

    public void BuyTicket(){
        List listSource = new ArrayList();
        OArguments arguments = new OArguments();
        arguments.add(sharedPrefManager.getSpIdPartner());
        for (Tiket tkt : ArrayListTiket) {
            if (Integer.valueOf(tkt.getJumlahTiket()) > 0) {
                JSONObject values = new JSONObject();
                try {
                    values.put("product_id", Integer.valueOf(tkt.getProduct_id()));
                    values.put("event_id", Integer.valueOf(getIntent().getExtras().get("id").toString()));
                    values.put("event_ticket_id", Integer.valueOf(tkt.getId()));
                    values.put("product_uom_qty", Integer.valueOf(tkt.getJumlahTiket()));
                    listSource.add(values);
                }catch (JSONException e){
                    Log.e(TAG,e.toString());
                }
           }
        }
        arguments.addItems(listSource);
        client.call_kw("sale.order","create_so_ticket" ,arguments, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                int serverId = 0;
                OdooRecord[] records = result.getRecords();
                for (OdooRecord record : records) {
                    Log.d(TAG,"Create SO : " + result.toString());
                    serverId = serverId+record.getInt("id");
                }
                progressDialog.dismiss();
                if (serverId > 0) {
                    Confirm_so(serverId);
                }
            }

            @Override
            public boolean onError(OdooErrorException error) {
                Log.e(TAG,"Create SO : " + error.toString());
                progressDialog.dismiss();
                return super.onError(error);
            }
        });
    }

    public void Confirm_so(Integer order_id){
        OArguments arguments = new OArguments();
        arguments.add(order_id);
        client.call_kw("sale.order", "confirm_so", arguments, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                OdooRecord[] records = result.getRecords();
                Toast.makeText(getBaseContext(),"Ticket successfully purchased!",Toast.LENGTH_SHORT).show();
                Log.d(TAG,"Confirm SO : " + result.toString());
                progressDialog.dismiss();
            }

            @Override
            public boolean onError(OdooErrorException error) {
                Log.e(TAG,"Confirm SO : " + error.toString());
                progressDialog.dismiss();
                return super.onError(error);
            }
        });
    }

    public void loadClassTicket(){
        ODomain domain = new ODomain();
        domain.add("event_id", "=", Integer.valueOf(getIntent().getExtras().get("id").toString()));

        OdooFields fields = new OdooFields();
        fields.addAll("id","name","price","seats_available","product_id");

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
                            String.valueOf(Math.round(record.getFloat("seats_available"))),
                            String.valueOf(record.getInt("product_id"))));
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

}
