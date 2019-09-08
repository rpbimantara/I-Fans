package com.alpha.test.i_fans;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.OdooVersion;
import oogbox.api.odoo.client.helper.data.OdooRecord;
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
        loadData();
    }

    public void loadData(){
        progressDialog.setMessage("Load Data........");
        progressDialog.show();
        progressDialog.setCancelable(false);
        client = new OdooClient.Builder(getApplicationContext())
                .setHost(sharedPrefManager.getSP_Host_url())
                .setSession(sharedPrefManager.getSpSessionId())
                .setSynchronizedRequests(false)
                .setConnectListener(new OdooConnectListener() {
                    @Override
                    public void onConnected(OdooVersion version) {
                        List<Integer> ids = Arrays.asList(sharedPrefManager.getSpIdPartner());
                        List<String> fields = Arrays.asList("id", "name", "nik", "tgl_lahir", "street", "email", "phone", "komunitas", "write_date", "write_uid");

                        client.read("res.partner", ids, fields, new IOdooResponse() {
                            @Override
                            public void onResult(OdooResult result) {
                                OdooRecord[] records = result.getRecords();

                                for(OdooRecord record: records) {
                                    ETname.setText(record.getString("name"));
                                    if(record.getString("nik").equalsIgnoreCase("false")){
                                        ETnik.requestFocus();
                                    }else{
                                        ETnik.setText(record.getString("nik"));
                                    }

                                    if(record.getString("street").equalsIgnoreCase("false")){
                                        ETaddress.setText("Address");
                                    }else{
                                        ETaddress.setText(record.getString("street"));
                                    }

                                    if(record.getString("email").equalsIgnoreCase("false")){
                                        ETmail.setText("E-mail");
                                    }else{
                                        ETmail.setText(record.getString("email"));
                                    }
                                    if(record.getString("phone").equalsIgnoreCase("false")){
                                        ETphone.setText("Phone");
                                    }else{
                                        ETphone.setText(record.getString("phone"));
                                    }
                                    if(record.getString("komunitas").equalsIgnoreCase("false")){
                                        ETcomunity.setText("Community");
                                    }else{
                                        ETcomunity.setText(record.getString("komunitas"));
                                    }
                                    progressDialog.dismiss();
                                }
                            }
                        });
                    }
                }).build();
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
                    .setSession(sharedPrefManager.getSpSessionId())
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
                                }
                            });
                        }
                    }).build();
            return null;
        }
    }
}
