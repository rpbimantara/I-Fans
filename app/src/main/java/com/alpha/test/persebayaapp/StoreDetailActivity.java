package com.alpha.test.persebayaapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
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

public class StoreDetailActivity extends AppCompatActivity {
    public final String TAG = this.getClass().getSimpleName();
    TextView txtNamaBarang,txtHargaBarang,txtDeskripsi,txtOwner;
    LinearLayout lnOrder,lnEdit;
    SharedPrefManager sharedPrefManager;
    Button btn_checkout,btn_buy_now,btn_edit_store,btn_delete_store;
    ImageView imageStore;
    ArrayList<Variant> ArrayListVariant;
    RecyclerView rv;
    RecyclerView.LayoutManager llm;
    AdapterStoreVariant adapter;
    ProgressDialog progressDialog;
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
        btn_delete_store = findViewById(R.id.button_delete_store);
        lnOrder = findViewById(R.id.linearLayout_order_store);
        lnEdit = findViewById(R.id.linearLayout_edit_store);
        rv = findViewById(R.id.rv_recycler_view_store_detail);
        progressDialog = new ProgressDialog(this);
        llm = new LinearLayoutManager(this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(llm);
        lnOrder.setVisibility(View.INVISIBLE);
        lnEdit.setVisibility(View.INVISIBLE);
        sharedPrefManager = new SharedPrefManager(this);

        gson = new Gson();
        client = getOdooConnection1(getBaseContext(), new OdooErrorListener() {
            @Override
            public void onError(OdooErrorException error) {
                progressDialog.dismiss();
            }
        });
        imageStore = findViewById(R.id.store_imageView);
        txtNamaBarang.setText(getIntent().getExtras().get("nama").toString());
        txtHargaBarang.setText(formater(Float.parseFloat(getIntent().getExtras().get("harga").toString())));
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
                                progressDialog.setMessage("Loading......");
                                progressDialog.show();
                                progressDialog.setCancelable(false);
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
                Boolean checker_result = false;
                for (Variant variant: ArrayListVariant){
                    if (variant.getChecked() == Boolean.TRUE){
                        checker_result = true;
                    }
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(StoreDetailActivity.this);
                builder.setTitle(R.string.app_name);
                builder.setMessage("Are You Sure to Buy This Item?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressDialog.setMessage("Loading......");
                        progressDialog.show();
                        progressDialog.setCancelable(false);
                        PayNow();
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
                final Boolean finalChecker_result = checker_result;
                getSaldo(context, new IOdooResponse() {
                    @Override
                    public void onResult(OdooResult result) {
                        OdooRecord[] Records = result.getRecords();
                        for (final OdooRecord record : Records) {
                            if (record.getString("state").equalsIgnoreCase("draft")){
                                Toast.makeText(getBaseContext(), "Update your profile first!", Toast.LENGTH_SHORT).show();
                            }else {
                                String jsonString = sharedPrefManager.getSpReturnFromRv();
                                String[] listItem = gson.fromJson(jsonString, String[].class);
                                Integer total_now = listItem.length * Integer.valueOf(getIntent().getExtras().get("harga").toString());
                                if (record.getInt("saldo") < total_now ){
                                    Toast.makeText(getBaseContext(), "Top up now to finish this transaction!", Toast.LENGTH_SHORT).show();
                                }else {
                                    if (finalChecker_result == Boolean.FALSE){
                                        progressDialog.dismiss();
                                        Toast.makeText(context, "Choose The Variant Items!", Toast.LENGTH_SHORT).show();
                                    }else{
                                        alertDialog.show();
                                    }
                                }
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
        btn_delete_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(StoreDetailActivity.this);
                builder.setTitle(R.string.app_name);
                builder.setMessage("Are You Sure to Delete This Item?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!TextUtils.isEmpty(getIntent().getExtras().get("id").toString())) {
                            OdooValues values = new OdooValues();
                            values.put("is_deleted", true);
                            client.write("product.template", new Integer[]{Integer.valueOf(getIntent().getExtras().get("id").toString())}, values, new IOdooResponse() {
                                @Override
                                public void onResult(OdooResult result) {
                                    // Success response
                                    Toast.makeText(getApplicationContext(),"Admin will review your action.",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
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
    }

    public void AddToCartHeader(){
        ODomain domain = new ODomain();
        domain.add("partner_id", "=", sharedPrefManager.getSpIdPartner());
        domain.add("state", "=", "sent");

        OdooFields fields = new OdooFields();
        fields.addAll("id", "name");

        int offset = 0;
        int limit = 1;

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
//                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
//                    final String Currentdatetime = sdf.format(new Date());
                    OArguments arguments = new OArguments();
                    arguments.add(sharedPrefManager.getSpIdPartner());
                    List listSource = new ArrayList();
                    arguments.addItems(listSource);
                    client.call_kw("sale.order","create_so",arguments, new IOdooResponse() {
                        @Override
                        public void onResult(final OdooResult result) {
                            Log.d(TAG,result.toString());
                            OdooRecord[] records = result.getRecords();
                            for (OdooRecord record : records) {
                                AddToCart(record.getInt("id"));
                            }
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

    public void PayNow(){
        OArguments arguments = new OArguments();
        arguments.add(sharedPrefManager.getSpIdPartner());
        List listSource = new ArrayList();
        for (Variant variant: ArrayListVariant){
            if (variant.getChecked() == Boolean.TRUE){
                listSource.add(Integer.valueOf(variant.getId()));
            }
        }
        arguments.addItems(listSource);
        client.call_kw("sale.order","create_so_checkout",arguments, new IOdooResponse() {
            @Override
            public void onResult(final OdooResult result) {
                OdooRecord[] records = result.getRecords();
                for (OdooRecord record : records) {
                    Confirm_so(record.getInt("id"));
                    Toast.makeText(getBaseContext(),"Item purchased!",Toast.LENGTH_SHORT).show();
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
        Boolean checker_result = false;
        for (Variant variant: ArrayListVariant){
            if (variant.getChecked() == Boolean.TRUE){
                checker_result = true;
            }
        }
        if (checker_result == Boolean.FALSE){
            progressDialog.dismiss();
            Toast.makeText(context, "Choose The Variant Items!", Toast.LENGTH_SHORT).show();
        }else {
            for (Variant variant: ArrayListVariant) {
                if (variant.getChecked() == Boolean.TRUE){
                    OdooValues values = new OdooValues();
                    values.put("order_id", order_id );
                    values.put("product_id", Integer.valueOf(variant.getId()));
                    client.create("sale.order.line", values, new IOdooResponse() {
                        @Override
                        public void onResult(final OdooResult result) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    Toast.makeText(context, "Added To Cart!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        @Override
                        public boolean onError(OdooErrorException error) {
                            Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            return super.onError(error);
                        }
                    });
                }
            }
        }
    }

//    public void AddToCartPayNow (Integer order_id){
//        Boolean checker_result = false;
//        for (Variant variant: ArrayListVariant){
//            if (variant.getChecked() == Boolean.TRUE){
//                checker_result = true;
//            }
//        }
//        if (checker_result == Boolean.FALSE){
//            progressDialog.dismiss();
//            Toast.makeText(context, "Choose The Variant Items!", Toast.LENGTH_SHORT).show();
//        }else {
//            int count = 0;
//            for (Variant variant: ArrayListVariant) {
//                count = count +1;
//                if (variant.getChecked() == Boolean.TRUE){
//                    OdooValues values = new OdooValues();
//                    values.put("order_id", order_id );
//                    values.put("product_id", Integer.valueOf(variant.getId()));
//                    client.create("sale.order.line", values, new IOdooResponse() {
//                        @Override
//                        public void onResult(final OdooResult result) {
//                        }
//                        @Override
//                        public boolean onError(OdooErrorException error) {
//                            Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
//                            progressDialog.dismiss();
//                            return super.onError(error);
//                        }
//                    });
//                }
//                if (count == ArrayListVariant.size()){
//                    Confirm_so(order_id);
//                }
//            }
//        }
//    }
//    public void AddToCartPayNow (final Integer order_id){
//        String jsonString = sharedPrefManager.getSpReturnFromRv();
//        String[] listItem = gson.fromJson(jsonString, String[].class);
//        if (listItem.length < 1){
//            progressDialog.dismiss();
//            Toast.makeText(context, "Choose The Variant Items!", Toast.LENGTH_SHORT).show();
//        }else {
//            for (int j=0; j<listItem.length;j++){
//                OdooValues values = new OdooValues();
//                values.put("order_id", order_id );
//                values.put("product_id", Integer.valueOf(listItem[j]));
//
//                client.create("sale.order.line", values, new IOdooResponse() {
//                    @Override
//                    public void onResult(final OdooResult result) {
//                        Confirm_so(order_id);
//                    }
//
//                    @Override
//                    public boolean onError(OdooErrorException error) {
//                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
//                        progressDialog.dismiss();
//                        return super.onError(error);
//                    }
//                });
//            }
//        }
//    }

    public void Confirm_so(Integer order_id){
        OArguments arguments = new OArguments();
        arguments.add(order_id);
        client.call_kw("sale.order", "confirm_so", arguments, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                OdooRecord[] records = result.getRecords();
                for (OdooRecord record : records) {
                    if (record.getInt("id") > 0){
                        Toast.makeText(getBaseContext(),"Item purchased!",Toast.LENGTH_SHORT).show();
                        Log.d(TAG,"Confirm SO : " + result.toString());
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public boolean onError(OdooErrorException error) {
                Log.e(TAG,"Confirm SO : " + error.toString());
                progressDialog.dismiss();
                return super.onError(error);
            }
        });
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
                            String.valueOf(Math.round(record.getFloat("qty_available"))),
                            false)
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
                adapter = new AdapterStoreVariant(ArrayListVariant, new AdapterStoreVariant.StoreVariantListener() {
                    @Override
                    public void onCheckChange(Variant variant, Boolean is_checked) {
                        variant.setChecked(is_checked);
                    }
                });
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
