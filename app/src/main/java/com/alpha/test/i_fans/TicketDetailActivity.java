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
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.OdooVersion;
import oogbox.api.odoo.client.helper.OdooErrorException;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.ODomain;
import oogbox.api.odoo.client.helper.utils.OdooFields;
import oogbox.api.odoo.client.helper.utils.OdooValues;
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooConnectListener;

import static com.alpha.test.i_fans.CommonUtils.StringToBitMap;
import static com.alpha.test.i_fans.CommonUtils.getOdooConnection;
import static com.alpha.test.i_fans.CommonUtils.tanggal;
import static com.alpha.test.i_fans.CommonUtils.waktu;

public class TicketDetailActivity extends AppCompatActivity implements AdapterTicket.TicketListener {
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
        txtTotalAmount.setText(String.valueOf(temp));
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
        rv.setAdapter(adapter);
        rv.setLayoutManager(llm);
        sharedPrefManager = new SharedPrefManager(this);
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.notifyDataSetChanged();
                if (sharedPrefManager.getSpUserState().equalsIgnoreCase("draft")){
                    Toast.makeText(getBaseContext(), "Update your profile first!", Toast.LENGTH_SHORT).show();
                }else {
                    if (total > 0) {
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
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    } else {
                        Toast.makeText(getBaseContext(), "Choose at least one ticket!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        client = getOdooConnection(getBaseContext());
        loadClassTicket();
//        new TicketTask().execute();
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
                    String tanggal = tanggal(record.getString("date_begin").substring(0,10));
                    String time =  waktu(record.getString("date_begin").substring(11,17)) +" WIB ";
                    txtNamatiket.setText(record.getString("name"));
                    txtTanggaltiket.setText(tanggal);
                    txtWaktutiket.setText(time);
                    imageTiket.setImageBitmap(StringToBitMap(record.getString("image")));
                }
            }
        });
    }

    public void BuyTicket(){
        OdooValues values = new OdooValues();
        values.put("partner_id", sharedPrefManager.getSpIdPartner());
        values.put("payment_term_id", 1);
        values.put("user_id", 1);

        client.create("sale.order", values, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                int serverId = result.getInt("result");
                if (serverId > 0) {
                    for (Tiket tkt : ArrayListTiket) {
                        if (Integer.valueOf(tkt.getJumlahTiket()) > 0) {
                            CreateSaleOrderLine(serverId, Integer.valueOf(tkt.getProduct_id()), Integer.valueOf(tkt.getId()), Integer.valueOf(tkt.getJumlahTiket()));
                        }
                    }
                }
            }
        });
    }

    public void CreateSaleOrderLine(final Integer sale_id, final Integer product_id, final Integer ticket_id, final Integer jumlah_ticket){
        OdooValues values = new OdooValues();
        values.put("order_id", sale_id);
        values.put("product_id", product_id);
        values.put("event_id", Integer.valueOf(getIntent().getExtras().get("id").toString()));
        values.put("event_ticket_id", ticket_id);
        values.put("product_uom_qty", jumlah_ticket);

        client.create("sale.order.line", values, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                int serverId = result.getInt("result");
                if (serverId > 0){
                    Toast.makeText(getBaseContext(),"Ticket successfully purchased!",Toast.LENGTH_SHORT).show();
                }
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
                System.out.println(ArrayListTiket.size());
                adapter = new AdapterTicket(ArrayListTiket,TicketDetailActivity.this);
                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }
//   public class TicketTask extends AsyncTask<Void,Void,Void>{
//       @Override
//       protected void onPreExecute() {
//           super.onPreExecute();
//       }
//
//
//       @Override
//       protected Void doInBackground(Void... voids) {
//           client = new OdooClient.Builder(getApplicationContext())
//                   .setHost(sharedPrefManager.getSP_Host_url())
//                   .setSession(sharedPrefManager.getSpSessionId())
//                   .setSynchronizedRequests(false)
//                   .setConnectListener(new OdooConnectListener() {
//                       @Override
//                       public void onConnected(OdooVersion version) {
//                           ODomain domain = new ODomain();
//                           domain.add("event_id", "=", Integer.valueOf(getIntent().getExtras().get("id").toString()));
//
//                           OdooFields fields = new OdooFields();
//                           fields.addAll("id","name","price","seats_available","product_id");
//
//                           int offset = 0;
//                           int limit = 80;
//
//                           String sorting = "id ASC";
//
//                           client.searchRead("event.event.ticket", domain, fields, offset, limit, sorting, new IOdooResponse() {
//                               @Override
//                               public void onResult(OdooResult result) {
//                                   OdooRecord[] records = result.getRecords();
//                                   for (OdooRecord record : records) {
//                                       ArrayListTiket.add(new Tiket(
//                                               String.valueOf(record.getInt("id")),
//                                               record.getString("name"),
//                                               String.valueOf(Math.round(record.getFloat("price"))),
//                                               "0",
//                                               String.valueOf(Math.round(record.getFloat("seats_available"))),
//                                               String.valueOf(record.getInt("product_id"))));
//                                   }
//                                   System.out.println(ArrayListTiket.size());
//                                   adapter = new AdapterTicket(ArrayListTiket,TicketDetailActivity.this);
//                                   rv.setAdapter(adapter);
//                                   adapter.notifyDataSetChanged();
//                               }
//                           });
//                       }
//                   }).build();
//           return null;
//       }
//   }

}
