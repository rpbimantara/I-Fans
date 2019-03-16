package com.example.nerita_hendra.i_fans;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.HashMap;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.OdooVersion;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.OdooValues;
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooConnectListener;

public class AccountEditActivity extends AppCompatActivity {
    EditText ETname,ETnik,ETaddress,ETmail,ETphone,ETcomunity;
    TextView txtbirthday;
    Button btnSave,btnDate;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;
    OdooClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.Account_edit_toolbar);
        sharedPrefManager = new SharedPrefManager(this);
        ETname = findViewById(R.id.editText_name);
        ETnik = findViewById(R.id.editText_nik);
        ETaddress = findViewById(R.id.editText_alamat);
        ETmail = findViewById(R.id.editText_email);
        ETphone = findViewById(R.id.editText_phone);
        ETcomunity = findViewById(R.id.editText_komunitas);
        txtbirthday = findViewById(R.id.edit_date);
        txtbirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(AccountEditActivity.this, AlertDialog.THEME_HOLO_LIGHT,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = month + "/" + dayOfMonth + "/" + year;
                        txtbirthday.setText(date);
                    }
                },year,month,day);
                dialog.show();
            }
        });
        progressDialog = new ProgressDialog(this);

        ETname.setText(getIntent().getExtras().get("name").toString());
        ETnik.setText(getIntent().getExtras().get("nik").toString());
        ETaddress.setText(getIntent().getExtras().get("address").toString());
        ETmail.setText(getIntent().getExtras().get("mail").toString());
        ETphone.setText(getIntent().getExtras().get("phone").toString());
        ETcomunity.setText(getIntent().getExtras().get("comunity").toString());
        btnSave = findViewById(R.id.button_save_account);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SaveTask().execute();
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public  class SaveTask extends AsyncTask<Void,Void,Boolean>{
        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Saving Data........");
            progressDialog.show();
            progressDialog.setCancelable(false);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            client = new OdooClient.Builder(getBaseContext())
                    .setHost(sharedPrefManager.getSP_Host_url())
                    .setSession("f35afb7584ea1195be5400d65415d6ab8f7a9440")
                    .setSynchronizedRequests(false)
                    .setConnectListener(new OdooConnectListener() {
                        @Override
                        public void onConnected(OdooVersion version) {
                            OdooValues values = new OdooValues();
                            values.put("name", ETname.getText().toString());
                            values.put("nik", ETnik.getText().toString());
                            values.put("tgl_lahir",txtbirthday.getText().toString());
                            values.put("street", ETaddress.getText().toString());
                            values.put("email", ETmail.getText().toString());
                            values.put("phone", ETphone.getText().toString());
                            values.put("komunitas", ETcomunity.getText().toString());

                            client.write("res.partner", new Integer[]{Integer.valueOf(getIntent().getExtras().get("idPartner").toString())}, values, new IOdooResponse() {
                                @Override
                                public void onResult(OdooResult result) {
                                    // Success response
                                    if (result.getBoolean("result") ==  true){
                                        progressDialog.dismiss();
                                        finish();
                                    }
                                    Log.w("wadsasd",result.toString());
                                }
                            });
                        }
                    }).build();
            return null;
        }
    }
}
