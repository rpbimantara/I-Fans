package com.alpha.test.persebayaapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.helper.OdooErrorException;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.OdooValues;
import oogbox.api.odoo.client.listeners.IOdooResponse;

import static com.alpha.test.persebayaapp.CommonUtils.getBase64ImageString;
import static com.alpha.test.persebayaapp.CommonUtils.getOdooConnection;
import static com.alpha.test.persebayaapp.CommonUtils.tanggal;

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
                final Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(AccountEditActivity.this, AlertDialog.THEME_HOLO_LIGHT,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        SimpleDateFormat f = new SimpleDateFormat("dd MMM yyyy");
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, month);
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        txtbirthday.setText(f.format(cal.getTime()));
                    }
                },year,month,day);
                dialog.show();
            }
        });
        progressDialog = new ProgressDialog(this);
        btnSave = findViewById(R.id.button_save_account);
        client = getOdooConnection(getBaseContext());
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(is_Valid() == true){
                    saveBtn();
                }
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
                    if(record.getString("tgl_lahir").equalsIgnoreCase("tgl_lahir")){
//                        ETaddress.setText("Address");
                    }else{
                        txtbirthday.setText(tanggal(record.getString("tgl_lahir")));
                    }

                    if(record.getString("street").equalsIgnoreCase("false")){
//                        ETaddress.setText("Address");
                    }else{
                        ETaddress.setText(record.getString("street"));
                    }

                    if(record.getString("email").equalsIgnoreCase("false")){
//                        ETmail.setText("E-mail");
                    }else{
                        ETmail.setText(record.getString("email"));
                    }
                    if(record.getString("phone").equalsIgnoreCase("false")){
//                        ETphone.setText("Phone");
                    }else{
                        ETphone.setText(record.getString("phone"));
                    }
                    if(record.getString("komunitas").equalsIgnoreCase("false")){
//                        ETcomunity.setText("Community");
                    }else{
                        ETcomunity.setText(record.getString("komunitas"));
                    }
                    progressDialog.dismiss();
                }
            }
        });
    }
    public Boolean is_Valid() {
        Boolean is_Success = true;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(TextUtils.isEmpty(ETname.getText())){
            is_Success = false;
            ETname.setError("Fill Name.");
        }

        if(TextUtils.isEmpty(ETnik.getText())){
            if (ETnik.getText().length()<16){
                is_Success = false;
                ETnik.setError("Invalid NIK.");
            }
        }

        if(TextUtils.isEmpty(ETaddress.getText())){
            is_Success = false;
            ETaddress.setError("Fill Address.");
        }
        if(TextUtils.isEmpty(ETmail.getText())){
            is_Success = false;
            ETmail.setError("Fill E-Mail.");
        }else{
            String cekemail = ETmail.getText().toString().trim();
            if (!cekemail.matches(emailPattern)){
                is_Success = false;
                ETmail.setError("Invalid E-Mail.");
            }
        }
        if(TextUtils.isEmpty(ETphone.getText())){
            is_Success = false;
            ETphone.setError("Fill Phone.");
        }

        if(!txtbirthday.getText().toString().equalsIgnoreCase("")){
            DateFormat f = new SimpleDateFormat("dd MMM yyyy");
            Date date = new Date();
            try {
                if (f.parse(txtbirthday.getText().toString()).after(f.parse(f.format(date)))){
                    is_Success = false;
                    Toast.makeText(getBaseContext(),"Must fill with different day after today!",Toast.LENGTH_SHORT).show();
                }
            } catch (ParseException e) {
                throw new IllegalArgumentException(e);
            }
        }else{
            is_Success = false;
            Toast.makeText(getBaseContext(),"Fill Birthday.",Toast.LENGTH_SHORT).show();
        }
        return is_Success;
    }

    public void saveBtn(){
        progressDialog.setMessage("Saving Data........");
        progressDialog.show();
        progressDialog.setCancelable(false);

        OdooValues values = new OdooValues();
        values.put("name", ETname.getText().toString());
        values.put("nik", ETnik.getText().toString());
        values.put("tgl_lahir",txtbirthday.getText().toString());
        values.put("street", ETaddress.getText().toString());
        values.put("email", ETmail.getText().toString());
        values.put("phone", ETphone.getText().toString());
        values.put("komunitas", ETcomunity.getText().toString());

        client.write("res.partner", new Integer[]{Integer.valueOf(sharedPrefManager.getSpIdPartner())}, values, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                // Success response
                if (result.getBoolean("result") ==  true){
                    progressDialog.dismiss();
                    finish();
                }
            }

            @Override
            public boolean onError(OdooErrorException error) {
                Toast.makeText(getBaseContext(), String.valueOf(error.getLocalizedMessage()), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                finish();
                return super.onError(error);
            }
        });
    }

//    public  class SaveTask extends AsyncTask<Void,Void,Boolean>{
//        @Override
//        protected void onPreExecute() {
//            progressDialog.setMessage("Saving Data........");
//            progressDialog.show();
//            progressDialog.setCancelable(false);
//            super.onPreExecute();
//        }
//
//        @Override
//        protected void onPostExecute(Boolean result) {
//            super.onPostExecute(result);
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... voids) {
//            client = new OdooClient.Builder(getBaseContext())
//                    .setHost(sharedPrefManager.getSP_Host_url())
//                    .setSession(sharedPrefManager.getSpSessionId())
//                    .setSynchronizedRequests(false)
//                    .setConnectListener(new OdooConnectListener() {
//                        @Override
//                        public void onConnected(OdooVersion version) {
//                            OdooValues values = new OdooValues();
//                            values.put("name", ETname.getText().toString());
//                            values.put("nik", ETnik.getText().toString());
//                            values.put("tgl_lahir",txtbirthday.getText().toString());
//                            values.put("street", ETaddress.getText().toString());
//                            values.put("email", ETmail.getText().toString());
//                            values.put("phone", ETphone.getText().toString());
//                            values.put("komunitas", ETcomunity.getText().toString());
//
//                            client.write("res.partner", new Integer[]{Integer.valueOf(getIntent().getExtras().get("idPartner").toString())}, values, new IOdooResponse() {
//                                @Override
//                                public void onResult(OdooResult result) {
//                                    // Success response
//                                    if (result.getBoolean("result") ==  true){
//                                        progressDialog.dismiss();
//                                        finish();
//                                    }
//                                }
//                            });
//                        }
//                    }).build();
//            return null;
//        }
//    }
}
