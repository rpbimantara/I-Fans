package com.alpha.test.i_fans;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

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

public class RatingLineUpActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    OdooClient client;
    Toolbar toolbar;
    SharedPrefManager sharedPrefManager;
    ImageView imagePlayer;
    TextView  txtNamaPlayer, txtNoPlayer;
    Button btnSave;
    EditText etComment;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_line_up);
        Toolbar toolbar = findViewById(R.id.rating_line_up_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        imagePlayer = findViewById(R.id.image_rating_line_up);
        txtNamaPlayer = findViewById(R.id.txt_nama_rating_line_up);
        txtNoPlayer = findViewById(R.id.txt_no_rating_line_up);
        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        ratingBar = findViewById(R.id.rating_bar_line_up);
        etComment = findViewById(R.id.editText_comment);
        btnSave = findViewById(R.id.submit_rating);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SaveRatingTask().execute(ratingBar.getRating(),etComment.getText());
            }
        });
        loadData();

    }

    public void loadData(){
        client = new OdooClient.Builder(getApplicationContext())
                .setHost(sharedPrefManager.getSP_Host_url())
                .setSession(sharedPrefManager.getSpSessionId())
                .setSynchronizedRequests(false)
                .setConnectListener(new OdooConnectListener() {
                    @Override
                    public void onConnected(OdooVersion version) {
                        // Success connection

                        ODomain domain = new ODomain();
                        domain.add("id", "=", getIntent().getStringExtra("id_player"));

                        OdooFields fields = new OdooFields();
                        fields.addAll("id","image","name", "job_id","status_pemain","no_punggung");

                        int offset = 0;
                        int limit = 80;

                        String sorting = "id DESC";

                        client.searchRead("hr.employee", domain, fields, offset, limit, sorting, new IOdooResponse() {
                            @Override
                            public void onResult(OdooResult result) {
                                OdooRecord[] records = result.getRecords();
                                for (OdooRecord record : records) {
                                    imagePlayer.setImageBitmap(StringToBitMap(record.getString("image")));
                                    txtNoPlayer.setText(String.valueOf(record.getInt("no_punggung")));
                                    txtNamaPlayer.setText(record.getString("name"));

                                }
                            }
                        });
                    }
                })
                .build();
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

    public class SaveRatingTask extends AsyncTask<Object,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(final Object... voids) {
            client = new OdooClient.Builder(getApplicationContext())
                    .setHost(sharedPrefManager.getSP_Host_url())
                    .setSession(sharedPrefManager.getSpSessionId())
                    .setSynchronizedRequests(false)
                    .setConnectListener(new OdooConnectListener() {
                        @Override
                        public void onConnected(OdooVersion version) {
                            // Success connection

                            OdooValues values = new OdooValues();
                            values.put("jadwal_id", getIntent().getStringExtra("id_jadwal"));
                            values.put("employee_id", getIntent().getStringExtra("id_player"));
                            values.put("rating", voids[0]);
                            values.put("review", voids[1]);

                            client.create("persebaya.rating", values, new IOdooResponse() {
                                @Override
                                public void onResult(OdooResult result) {
                                    Toast.makeText(getApplicationContext(),"Rating Saved!",Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public boolean onError(OdooErrorException error) {
                                    Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                                    return true;
                                }
                            });
                        }
                    })
                    .build();
            return null;
        }
    }
}