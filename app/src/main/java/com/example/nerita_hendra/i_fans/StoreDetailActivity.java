package com.example.nerita_hendra.i_fans;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class StoreDetailActivity extends AppCompatActivity {
    TextView txtNamaBarang,txtHargaBarang,txtDeskripsi;
    SharedPrefManager sharedPrefManager;
    ImageView imageStore;

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
        sharedPrefManager = new SharedPrefManager(this);
        imageStore = findViewById(R.id.store_imageView);
        txtNamaBarang.setText(getIntent().getExtras().get("nama").toString());
        txtHargaBarang.setText(getIntent().getExtras().get("harga").toString());
        txtDeskripsi.setText(getIntent().getExtras().get("deskripsi").toString());
        new StoreDetailTask().execute();

    }


    public class StoreDetailTask extends AsyncTask<Void,Void,String[]>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String[] strings) {
            imageStore.setImageBitmap(StringToBitMap(strings[0]));
            super.onPostExecute(strings);
        }

        @Override
        protected String[] doInBackground(Void... voids) {
            String imageCurrent = "";

            OdooConnect oc = OdooConnect.connect(sharedPrefManager.getSpNamaUser(),sharedPrefManager.getSpPasswordUser());

            Object[] param = {new Object[]{
                    new Object[]{"id", "=", getIntent().getExtras().get("id")}}};

            List<HashMap<String, Object>> data = oc.search_read("product.template", param, "id","image_medium");

            for (int i = 0; i < data.size(); ++i) {
               imageCurrent = String.valueOf(data.get(i).get("image_medium").toString());
            }
            return new String[]{imageCurrent};
        }
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
