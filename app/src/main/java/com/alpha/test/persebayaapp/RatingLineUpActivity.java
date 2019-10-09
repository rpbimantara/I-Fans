package com.alpha.test.persebayaapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.helper.OdooErrorException;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.ODomain;
import oogbox.api.odoo.client.helper.utils.OdooFields;
import oogbox.api.odoo.client.helper.utils.OdooValues;
import oogbox.api.odoo.client.listeners.IOdooResponse;

import static com.alpha.test.persebayaapp.CommonUtils.StringToBitMap;
import static com.alpha.test.persebayaapp.CommonUtils.getOdooConnection;
import static com.alpha.test.persebayaapp.CommonUtils.getSaldo;

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
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_line_up);
        Toolbar toolbar = findViewById(R.id.rating_line_up_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        context = this;
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
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.app_name);
                builder.setMessage("Do You Want to Give Rating Now?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!TextUtils.isEmpty(etComment.getText())){
                            savebtn(ratingBar.getRating(),etComment.getText().toString());
                        }
//                        new SaveRatingTask().execute(ratingBar.getRating(),etComment.getText());
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
                                Toast.makeText(getBaseContext(), "Update your profile first!", Toast.LENGTH_SHORT).show();
                            }else {
                                alertDialog.show();
                            }
                        }
                    }
                });
            }
        });
        client = getOdooConnection(getBaseContext());
        loadData();

    }

    public void loadData(){
        ODomain domain = new ODomain();
        domain.add("id", "=", getIntent().getExtras().get("id_player"));

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

    public void savebtn(Float rating,String review){
        OdooValues values = new OdooValues();
        values.put("jadwal_id", getIntent().getExtras().get("id_jadwal"));
        values.put("employee_id", getIntent().getExtras().get("id_player"));
        values.put("rating", rating);
        values.put("review", review);

        client.create("persebaya.rating", values, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                Toast.makeText(getApplicationContext(),"Rating Saved!",Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public boolean onError(OdooErrorException error) {
                Log.e("Rating error",error.toString());
                Toast.makeText(getApplicationContext(),error.odooException,Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }

//    public class SaveRatingTask extends AsyncTask<Object,Void,Void>{
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Void doInBackground(final Object... voids) {
//            client = new OdooClient.Builder(getApplicationContext())
//                    .setHost(sharedPrefManager.getSP_Host_url())
//                    .setSession(sharedPrefManager.getSpSessionId())
//                    .setSynchronizedRequests(false)
//                    .setConnectListener(new OdooConnectListener() {
//                        @Override
//                        public void onConnected(OdooVersion version) {
//                            // Success connection
//
//                            OdooValues values = new OdooValues();
//                            values.put("jadwal_id", getIntent().getExtras().get("id_jadwal"));
//                            values.put("employee_id", getIntent().getExtras().get("id_player"));
//                            values.put("rating", voids[0]);
//                            values.put("review", voids[1]);
//
//                            client.create("persebaya.rating", values, new IOdooResponse() {
//                                @Override
//                                public void onResult(OdooResult result) {
//                                    Toast.makeText(getApplicationContext(),"Rating Saved!",Toast.LENGTH_LONG).show();
//                                }
//
//                                @Override
//                                public boolean onError(OdooErrorException error) {
//                                    Log.e("Rating error",error.toString());
//                                    Toast.makeText(getApplicationContext(),error.odooException,Toast.LENGTH_LONG).show();
//                                    return true;
//                                }
//                            });
//                        }
//                    })
//                    .build();
//            return null;
//        }
//    }
}
