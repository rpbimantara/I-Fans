package com.example.nerita_hendra.i_fans;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;

public class AccountEditActivity extends AppCompatActivity {
    EditText ETname,ETnik,ETaddress,ETbirthday,ETmail,ETphone,ETcomunity;
    Button btnSave,btnDate;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;

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
        progressDialog = new ProgressDialog(this);
//        btnDate = findViewById(R.id.btn_date);
//        btnDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

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
            if (result == true){
                progressDialog.dismiss();
                finish();
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Boolean idW = false;
            try {

                OdooConnect oc = OdooConnect.connect(sharedPrefManager.getSpNamaUser(),sharedPrefManager.getSpPasswordUser());

                idW = oc.write("res.partner", new Object[]{ Integer.valueOf(getIntent().getExtras().get("idPartner").toString()) },
                        new HashMap() {{
                            put("name", ETname.getText().toString());
                            put("nik", ETnik.getText().toString());
                            put("street", ETaddress.getText().toString());
                            put("email", ETmail.getText().toString());
                            put("phone", ETphone.getText().toString());
                            put("komunitas", ETcomunity.getText().toString());
                        }});

            } catch (Exception ex) {
                System.out.println("Error SAVE TASK: " + ex);
            }
            return idW;
        }
    }
}
