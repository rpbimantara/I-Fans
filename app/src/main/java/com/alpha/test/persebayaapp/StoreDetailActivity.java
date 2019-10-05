package com.alpha.test.persebayaapp;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.helper.OdooErrorException;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.OArguments;
import oogbox.api.odoo.client.helper.utils.ODomain;
import oogbox.api.odoo.client.helper.utils.OdooFields;
import oogbox.api.odoo.client.helper.utils.OdooValues;
import oogbox.api.odoo.client.listeners.IOdooResponse;

import static com.alpha.test.persebayaapp.CommonUtils.StringToBitMap;
import static com.alpha.test.persebayaapp.CommonUtils.getOdooConnection;
import static com.alpha.test.persebayaapp.CommonUtils.getSaldo;
import static com.alpha.test.persebayaapp.CommonUtils.tanggal;

public class StoreDetailActivity extends AppCompatActivity {
    TextView txtNamaBarang,txtHargaBarang,txtDeskripsi,txtOwner;
    LinearLayout lnOrder,lnEdit;
    SharedPrefManager sharedPrefManager;
    Button btn_checkout,btn_buy_now,btn_edit_store;
    ImageView imageStore;
    ArrayList<Variant> ArrayListVariant;
    RecyclerView rv;
    RecyclerView.LayoutManager llm;
    AdapterStoreVariant adapter;
    Context context;
    Gson gson;
    OdooClient client;
    String imageCurrent = "";
    String variant = "Standard";
    String description = "No Item Descriptions ";
    String ownertgl = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_store_toolbar);
        setSupportActionBar(toolbar);
        context = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        txtNamaBarang = findViewById(R.id.textView_nama_barang);
        txtHargaBarang = findViewById(R.id.textView_harga_barang);
        txtDeskripsi = findViewById(R.id.textView_deskripsi);
        txtOwner = findViewById(R.id.textView_tgl_detail_store);
        btn_checkout = findViewById(R.id.button_checkout_store);
        btn_buy_now = findViewById(R.id.button_buy_now_store);
        btn_edit_store = findViewById(R.id.button_edit_store);
        lnOrder = findViewById(R.id.linearLayout_order_store);
        lnEdit = findViewById(R.id.linearLayout_edit_store);
        rv = findViewById(R.id.rv_recycler_view_store_detail);
        llm = new LinearLayoutManager(this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(llm);
        lnOrder.setVisibility(View.INVISIBLE);
        lnEdit.setVisibility(View.INVISIBLE);
        sharedPrefManager = new SharedPrefManager(this);
        gson = new Gson();
        client = getOdooConnection(getBaseContext());
        imageStore = findViewById(R.id.store_imageView);
        txtNamaBarang.setText(getIntent().getExtras().get("nama").toString());
        txtHargaBarang.setText(getIntent().getExtras().get("harga").toString());
        loadVariant();
//        new VariantTask().execute();
        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new AddToCartTask().execute();
                getSaldo(context, new IOdooResponse() {
                    @Override
                    public void onResult(OdooResult result) {
                        OdooRecord[] Records = result.getRecords();
                        for (final OdooRecord record : Records) {
                            if (record.getString("state").equalsIgnoreCase("draft")){
                                Toast.makeText(getBaseContext(), "Update your profile first!", Toast.LENGTH_SHORT).show();
                            }else {
                                AddToCartHeader();
                            }
                        }
                    }
                });
            }
        });

        btn_buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSaldo(context, new IOdooResponse() {
                    @Override
                    public void onResult(OdooResult result) {
                        OdooRecord[] Records = result.getRecords();
                        for (final OdooRecord record : Records) {
                            if (record.getString("state").equalsIgnoreCase("draft")){
                                Toast.makeText(getBaseContext(), "Update your profile first!", Toast.LENGTH_SHORT).show();
                            }else {

                            }
                        }
                    }
                });
            }
        });
        btn_edit_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               Intent fabIntent = new Intent(StoreDetailActivity.this,StoreAddActivity.class);
               fabIntent.putExtra("id",Integer.valueOf(getIntent().getExtras().get("id").toString()));
               startActivity(fabIntent);
            }
        });
    }

    public void AddToCartHeader(){
        ODomain domain = new ODomain();
        domain.add("partner_id", "=", sharedPrefManager.getSpIdPartner());
        domain.add("state", "=", "draft");

        OdooFields fields = new OdooFields();
        fields.addAll("id", "name");

        int offset = 0;
        int limit = 80;

        String sorting = "id DESC";

        client.searchRead("sale.order", domain, fields, offset, limit, sorting, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                final OdooRecord[] records = result.getRecords();
                if (result.getInt("length") > 0){
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            for (OdooRecord record : records){
                                AddToCart(record.getInt("id"));
                            }
                        }
                    });
                }else {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    final String Currentdatetime = sdf.format(new Date());

                    OdooValues values = new OdooValues();
                    values.put("partner_id", sharedPrefManager.getSpIdPartner());
                    values.put("date_order", Currentdatetime);
                    values.put("state", "draft");

                    client.create("sale.order", values, new IOdooResponse() {
                        @Override
                        public void onResult(final OdooResult result) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    AddToCart(result.getInt("result"));
                                }
                            });
                        }

                        @Override
                        public boolean onError(OdooErrorException error) {
                            Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                            return super.onError(error);
                        }
                    });
                }
            }

            @Override
            public boolean onError(OdooErrorException error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                return super.onError(error);
            }
        });
    }

//    public class AddToCartTask extends AsyncTask<Void,Void,Void>{
//        @Override
//        protected Void doInBackground(Void... voids) {
//            client = new OdooClient.Builder(getApplicationContext())
//                    .setHost(sharedPrefManager.getSP_Host_url())
//                    .setSession(sharedPrefManager.getSpSessionId())
//                    .setSynchronizedRequests(false)
//                    .setConnectListener(new OdooConnectListener() {
//                        @Override
//                        public void onConnected(OdooVersion version) {
//                            ODomain domain = new ODomain();
//                            domain.add("partner_id", "=", sharedPrefManager.getSpIdPartner());
//                            domain.add("state", "=", "draft");
//
//                            OdooFields fields = new OdooFields();
//                            fields.addAll("id", "name");
//
//                            int offset = 0;
//                            int limit = 80;
//
//                            String sorting = "id DESC";
//
//                            client.searchRead("sale.order", domain, fields, offset, limit, sorting, new IOdooResponse() {
//                                @Override
//                                public void onResult(OdooResult result) {
//                                    final OdooRecord[] records = result.getRecords();
//                                    if (result.getInt("length") > 0){
//                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                for (OdooRecord record : records){
//                                                    AddToCart(record.getInt("id"));
//                                                }
//                                            }
//                                        });
//                                    }else {
//                                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
//                                        final String Currentdatetime = sdf.format(new Date());
//
//                                        OdooValues values = new OdooValues();
//                                        values.put("partner_id", sharedPrefManager.getSpIdPartner());
//                                        values.put("date_order", Currentdatetime);
//                                        values.put("state", "draft");
//
//                                        client.create("sale.order", values, new IOdooResponse() {
//                                            @Override
//                                            public void onResult(final OdooResult result) {
//                                                new Handler(Looper.getMainLooper()).post(new Runnable() {
//                                                    @Override
//                                                    public void run() {
//                                                        AddToCart(result.getInt("result"));
//                                                    }
//                                                });
//                                            }
//
//                                            @Override
//                                            public boolean onError(OdooErrorException error) {
//                                                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
//                                                return super.onError(error);
//                                            }
//                                        });
//                                    }
//                                }
//
//                                @Override
//                                public boolean onError(OdooErrorException error) {
//                                    Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
//                                    return super.onError(error);
//                                }
//                            });
//                        }
//                    }).build();
//            return null;
//        }
//    }

    public void AddToCart (Integer order_id){
        String jsonString = sharedPrefManager.getSpReturnFromRv();
        String[] listItem = gson.fromJson(jsonString, String[].class);
            if (listItem == null){
                Toast.makeText(context, "Choose The Variant Items!", Toast.LENGTH_SHORT).show();
            }else {
                for (int j=0; j<listItem.length;j++){
                    OdooValues values = new OdooValues();
                    values.put("order_id", order_id );
                    values.put("product_id", Integer.valueOf(listItem[j]));

                    client.create("sale.order.line", values, new IOdooResponse() {
                        @Override
                        public void onResult(final OdooResult result) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "Added To Cart!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public boolean onError(OdooErrorException error) {
                            Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                            return super.onError(error);
                        }
                    });
                }
            }
    }


    public void loadVariant(){
        ArrayListVariant = new ArrayList<>();
        OArguments arguments = new OArguments();
        arguments.add(Integer.valueOf(getIntent().getExtras().get("id").toString()));
        client.call_kw("product.product", "get_detail_store", arguments, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                OdooRecord[] records = result.getRecords();
                for (OdooRecord record : records) {
                    imageCurrent = record.getString("image");

                    if (!record.getString("variant").equalsIgnoreCase("")){
                        variant =  record.getString("variant");
                    }

                    if (!record.getString("desc").equalsIgnoreCase("false"))
                    {
                        description = record.getString("desc");
                    }
                    ArrayListVariant.add(new Variant(
                            String.valueOf(record.getInt("id")),
                            variant,
                            String.valueOf(Math.round(record.getFloat("qty_available"))))
                    );
                    ownertgl = record.getString("ownername") + " - "+ tanggal(record.getString("date"));
                    if (record.getInt("owner") == sharedPrefManager.getSpIdUser()){
                        lnEdit.setVisibility(View.VISIBLE);
                    }else{
                        lnOrder.setVisibility(View.VISIBLE);
                    }
                }
                txtOwner.setText(ownertgl);
                imageStore.setImageBitmap(StringToBitMap(imageCurrent));
                txtDeskripsi.setText(description);
                adapter = new AdapterStoreVariant(ArrayListVariant);
                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }


//    public class VariantTask extends AsyncTask<Void,Void,Void>{
//        @Override
//        protected Void doInBackground(Void... voids) {
//            ArrayListVariant = new ArrayList<>();
//            client = new OdooClient.Builder(getApplicationContext())
//                    .setHost(sharedPrefManager.getSP_Host_url())
//                    .setSession(sharedPrefManager.getSpSessionId())
//                    .setSynchronizedRequests(false)
//                    .setConnectListener(new OdooConnectListener() {
//                        @Override
//                        public void onConnected(OdooVersion version) {
//                            OArguments arguments = new OArguments();
//                            arguments.add(Integer.valueOf(getIntent().getExtras().get("id").toString()));
//                            client.call_kw("product.product", "get_detail_store", arguments, new IOdooResponse() {
//                                @Override
//                                public void onResult(OdooResult result) {
//                                    OdooRecord[] records = result.getRecords();
//                                    for (OdooRecord record : records) {
//                                        imageCurrent = record.getString("image");
//
//                                        if (!record.getString("variant").equalsIgnoreCase("")){
//                                            variant =  record.getString("variant");
//                                        }
//
//                                        if (!record.getString("desc").equalsIgnoreCase("false"))
//                                        {
//                                            description = record.getString("desc");
//                                        }
//                                        ArrayListVariant.add(new Variant(
//                                                String.valueOf(record.getInt("id")),
//                                                variant,
//                                                String.valueOf(Math.round(record.getFloat("qty_available"))))
//                                        );
//                                        ownertgl = record.getString("ownername") + " - "+ tanggal(record.getString("date"));
//                                        if (record.getInt("owner") == sharedPrefManager.getSpIdUser()){
//                                            lnEdit.setVisibility(View.VISIBLE);
//                                        }else{
//                                            lnOrder.setVisibility(View.VISIBLE);
//                                        }
//                                    }
//                                    txtOwner.setText(ownertgl);
//                                    imageStore.setImageBitmap(StringToBitMap(imageCurrent));
//                                    txtDeskripsi.setText(description);
//                                    adapter = new AdapterStoreVariant(ArrayListVariant);
//                                    rv.setAdapter(adapter);
//                                    adapter.notifyDataSetChanged();
//                                }
//                            });
//                        }
//                    }).build();
//            return null;
//        }
//    }


}
