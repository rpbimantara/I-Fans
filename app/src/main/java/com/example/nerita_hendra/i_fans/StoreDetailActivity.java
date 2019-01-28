package com.example.nerita_hendra.i_fans;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class StoreDetailActivity extends AppCompatActivity {
    TextView txtNamaBarang,txtHargaBarang,txtDeskripsi;
    SharedPrefManager sharedPrefManager;
    Button btn_checkout;
    ImageView imageStore;
    ArrayList<Variant> ArrayListVariant;
    RecyclerView rv;
    RecyclerView.LayoutManager llm;
    AdapterStoreVariant adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_store_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        txtNamaBarang = findViewById(R.id.textView_nama_barang);
        txtHargaBarang = findViewById(R.id.textView_harga_barang);
        txtDeskripsi = findViewById(R.id.textView_deskripsi);
        btn_checkout = findViewById(R.id.button_checkout_store);
        rv = findViewById(R.id.rv_recycler_view_store_detail);
        llm = new LinearLayoutManager(this);
        adapter = new AdapterStoreVariant(ArrayListVariant);
        rv.setAdapter(adapter);
        rv.setLayoutManager(llm);
        sharedPrefManager = new SharedPrefManager(this);
        imageStore = findViewById(R.id.store_imageView);
        txtNamaBarang.setText(getIntent().getExtras().get("nama").toString());
        txtHargaBarang.setText(getIntent().getExtras().get("harga").toString());
        new StoreDetailTask().execute();
        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    OdooConnect oc = OdooConnect.connect(sharedPrefManager.getSpNamaUser(),sharedPrefManager.getSpPasswordUser());

                    Object[] param = {new Object[]{
                            new Object[]{"partner_id", "=", 10},
                            new Object[]{"state", "=", "draft"}}};
                    List<HashMap<String, Object>> data = oc.search_read("sale.order", param, "id", "name");

                    if (data.size() > 0){
                        for (int i = 0; i < data.size(); ++i) {
                            AddOrderLine(Integer.valueOf(data.get(i).get("id").toString()), Integer.valueOf(getIntent().getExtras().get("id").toString()));
                        }
                    }
                    System.out.println("Num. of customers: " + data.size() + "\n");
                } catch (Exception ex) {
                    System.out.println("Error Checkout Button : " + ex);
                }
            }
        });
    }

public String AddOrderLine(final Integer order_id, final Integer product_id){
        try{
            OdooConnect oc = OdooConnect.connect(sharedPrefManager.getSpNamaUser(),sharedPrefManager.getSpPasswordUser());

            @SuppressWarnings("unchecked")
            Integer idC = oc.create("sale.order.line", new HashMap() {{
                put("order_id", order_id );
                put("product_id", product_id);
            }});
            System.out.println(idC.toString());
        }catch (Exception ex) {
            System.out.println("Error Add order line : " + ex);
        }
        return null;
}
    public class StoreDetailTask extends AsyncTask<Void,Void,String[]>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String[] strings) {
            imageStore.setImageBitmap(StringToBitMap(strings[0]));
            txtDeskripsi.setText(strings[1]);
            adapter = new AdapterStoreVariant(ArrayListVariant);
            rv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            super.onPostExecute(strings);
        }

        @Override
        protected String[] doInBackground(Void... voids) {
            ArrayListVariant = new ArrayList<>();
            String imageCurrent = "";
            String variant = "Standard";
            String description = "No Item Descriptions ";
            try {
                OdooConnect oc = OdooConnect.connect(sharedPrefManager.getSpNamaUser(),sharedPrefManager.getSpPasswordUser());

                Object[] param = {new Object[]{
                        new Object[]{"product_tmpl_id", "=",Integer.valueOf(getIntent().getExtras().get("id").toString())}}};

                List<HashMap<String, Object>> data = oc.search_read("product.product", param, "id","image_medium","attribute_value_ids","qty_available","description_sale");

                for (int i = 0; i < data.size(); ++i) {
                    imageCurrent = String.valueOf(data.get(i).get("image_medium").toString());

//
                    if (!String.valueOf(data.get(i).get("attribute_value_ids").toString()).equalsIgnoreCase("")){
                       variant =  String.valueOf(Variant_Text(data.get(i).get("attribute_value_ids").toString()));
                    }

                    if (!String.valueOf(data.get(i).get("description_sale")).equalsIgnoreCase("false"))
                    {
                        description = String.valueOf(data.get(i).get("description_sale"));
                    }
                    ArrayListVariant.add(new Variant(
                            String.valueOf(data.get(i).get("id")),
                            variant,
                            String.valueOf(Math.round(Float.valueOf(data.get(i).get("qty_available").toString())))
                    ));
                }

            } catch (Exception ex) {
                System.out.println("Error Store Detail: " + ex);
            }

            return new String[]{imageCurrent,description};
        }
    }

    public String Variant_Text(String Param){
        String pattern = Param.toString().replaceAll("\\}|\\{| id=|name","");
        String[] Split = pattern.split("=");
        Param = "";
        for (int j = 0; j < Split.length; j++){
            if (!Split[j].equalsIgnoreCase("")) {
                String tes = Split[j].substring(0, Split[j].indexOf(",")+1);
                Param = Param.concat(tes);
            }
        }
        return Param.substring(0,Param.length()-1);
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
