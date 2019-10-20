package com.alpha.test.persebayaapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.helper.OdooErrorException;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.OArguments;
import oogbox.api.odoo.client.helper.utils.ODomain;
import oogbox.api.odoo.client.helper.utils.OdooFields;
import oogbox.api.odoo.client.helper.utils.OdooValues;
import oogbox.api.odoo.client.listeners.IOdooResponse;

import static com.alpha.test.persebayaapp.CommonUtils.getOdooConnection;

public class CheckoutActivity extends AppCompatActivity implements AdapterCheckout.CheckoutListener {
    TextView txtTotalAmount;
    ArrayList<Checkout> ArrayListCheckout = new ArrayList<>();
    ArrayList<Checkout> ArrayListPaid;
    SharedPrefManager sharedPrefManager;
    RecyclerView rv;
    RecyclerView.LayoutManager llm;
    ProgressDialog progressDialog;
    SwipeRefreshLayout swiper;
    AdapterCheckout adapter;
    OdooClient client;
    Button btnPaid;
    int total = 0;

    @Override
    public void CheckoutCallback(Checkout checkout, Integer jumlah,String mode) {
        checkout.setQty(String.valueOf(jumlah));
        if (mode.equalsIgnoreCase("onchange")){
            updateQty(Integer.valueOf(checkout.getId()),jumlah);
        }
        calcTotal();
    }
    void calcTotal(){
        int temp = 0;
        for (Checkout c : ArrayListCheckout) {
            temp += (Integer.valueOf(c.getHarga()) * Integer.valueOf(c.getQty()));
        }
        txtTotalAmount.setText(CommonUtils.formater(Float.parseFloat(String.valueOf(temp))));
        total = temp;
    }
    @Override
    public void CheckoutDeleted(final Checkout checkout) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setMessage("Do You Want Delete This Product Now?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                client.unlink("sale.order.line", new Integer[]{Integer.valueOf(checkout.getId())}, new IOdooResponse() {
                    @Override
                    public void onResult(OdooResult result) {
                        loadCheckout();

                        Toast.makeText(CheckoutActivity.this,checkout.getNama()+" has been deleted!",Toast.LENGTH_SHORT).show();
                    }
                });
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        Toolbar toolbar = findViewById(R.id.checkout_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        btnPaid = findViewById(R.id.button_paid);
        txtTotalAmount = findViewById(R.id.total_amount_checkout);
        rv =  findViewById(R.id.rv_recycler_view_checkout);
        swiper = findViewById(R.id.swiperefresh_checkout);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadCheckout();
            }
        });
        llm = new LinearLayoutManager(this);
        adapter = new AdapterCheckout(ArrayListCheckout,CheckoutActivity.this);
        sharedPrefManager = new SharedPrefManager(this);
        progressDialog = new ProgressDialog(this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(llm);
        btnPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutActivity.this);
                builder.setTitle(R.string.app_name);
                builder.setMessage("Do You Want to Pay Now?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PayCheckout();
                        progressDialog.setMessage("Loading....");
                        progressDialog.show();
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
        client = getOdooConnection(getBaseContext());
        loadCheckout();
    }

    public  void PayCheckout(){
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
                    for (final OdooRecord record : records){
                        Confirm_so(record.getInt("id"));
                    }
                }else{
                    Toast.makeText(CheckoutActivity.this,"No item to Paid!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updateQty(final Integer id, final Integer qty){
        OdooValues values = new OdooValues();
        values.put("product_uom_qty", qty);

        client.write("sale.order.line", new Integer[]{id}, values, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                // Success response
                System.out.println(result.toString());
            }
        });
    }
    public void Confirm_so(Integer order_id){
        OArguments arguments = new OArguments();
        arguments.add(order_id);
        client.call_kw("sale.order", "confirm_so", arguments, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                Toast.makeText(getBaseContext(),"Item purchased!",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public boolean onError(OdooErrorException error) {
                progressDialog.dismiss();
                return super.onError(error);
            }
        });
    }

    public  void loadCheckout(){
        swiper.setRefreshing(true);
        OArguments arguments = new OArguments();
        arguments.add(Integer.valueOf(sharedPrefManager.getSpIdPartner()));

        client.call_kw("sale.order.line", "get_checkout_list", arguments, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                ArrayListCheckout.clear();
                // response
                OdooRecord[] Records = result.getRecords();

                for (final OdooRecord record : Records) {
                    ArrayListCheckout.add(new Checkout(
                            String.valueOf(record.getInt("id")),
                            record.getString("nama"),
                            String.valueOf(Math.round(record.getFloat("harga"))),
                            String.valueOf(record.getInt("qty")),
                            record.getString("image"),
                            String.valueOf(record.getInt("stock")),
                            record.getString("type")
                    ));
                }
                adapter.notifyDataSetChanged();
                calcTotal();
                swiper.setRefreshing(false);
            }
        });
    }

}
