package com.example.nerita_hendra.i_fans;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {
    ArrayList<Checkout> ArrayListCheckout;
    SharedPrefManager sharedPrefManager;
    RecyclerView rv;
    RecyclerView.LayoutManager llm;
    SwipeRefreshLayout swiper;
    AdapterCheckout adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        Toolbar toolbar = findViewById(R.id.checkout_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        rv =  findViewById(R.id.rv_recycler_view_checkout);
        swiper = findViewById(R.id.swiperefresh_checkout);
        llm = new LinearLayoutManager(this);
        adapter = new AdapterCheckout(ArrayListCheckout);
        sharedPrefManager = new SharedPrefManager(this);
        rv.setAdapter(adapter );
        rv.setLayoutManager(llm);
        new LoadCheckoutAsync().execute();
    }


    public class LoadCheckoutAsync extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            swiper.setRefreshing(true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new AdapterCheckout(ArrayListCheckout);
            rv.setAdapter(adapter );
            adapter.notifyDataSetChanged();
            swiper.setRefreshing(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ArrayListCheckout = new ArrayList<>();
            try {
                OdooConnect oc = OdooConnect.connect(sharedPrefManager.getSpNamaUser(),sharedPrefManager.getSpPasswordUser());

                Object[] param = {new Object[]{
                        new Object[]{"partner_id", "=", sharedPrefManager.getSpIdPartner()},
                        new Object[]{"state", "=", "draft"}}};
                List<HashMap<String, Object>> data = oc.search_read("sale.order", param, "id");

                Object[] paramBarang = {new Object[]{
                        new Object[]{"order_id", "=",Integer.valueOf(data.get(0).get("id").toString())}}};

                List<HashMap<String, Object>> dataBarang = oc.search_read("sale.order.line", paramBarang, "id","product_id","product_uom_qty","price_unit");

                for (int i = 0; i < dataBarang.size(); ++i) {
                    String[] barang = getProduct(dataBarang.get(i).get("product_id").toString());
                    String harga = String.valueOf(Math.round(Float.parseFloat(dataBarang.get(i).get("price_unit").toString())));
                    String qty = String.valueOf(Math.round(Float.parseFloat(dataBarang.get(i).get("product_uom_qty").toString())));
                    ArrayListCheckout.add(new Checkout(
                            barang[0],
                            dataBarang.get(i).get("product_id").toString(),
                            harga,
                            qty,
                            barang[1],
                            barang[2]
                    ));
                }

            }catch (Exception ex){
                System.out.println("Error Checkout Activity : " + ex);
            }
            return null;
        }
    }

    public String[] getProduct(String product_name){
        String Id ="";
        String image="";
        String qty_available="";
        try{
            OdooConnect oc = OdooConnect.connect(sharedPrefManager.getSpNamaUser(),sharedPrefManager.getSpPasswordUser());

            Object[] param = {new Object[]{
                    new Object[]{"display_name", "=", product_name}}};
            List<HashMap<String, Object>> data = oc.search_read("product.product", param, "id","image_medium","qty_available");
            Id = data.get(0).get("id").toString();
            image = data.get(0).get("image_medium").toString();
            qty_available = String.valueOf(Math.round(Float.parseFloat(data.get(0).get("qty_available").toString())));
        }catch (Exception ex){
            System.out.println("Error get PRoduct"+ex);
        }

        return new String[] {Id,image,qty_available};
    }

}
