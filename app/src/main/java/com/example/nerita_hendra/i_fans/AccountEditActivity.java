package com.example.nerita_hendra.i_fans;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.HashMap;

public class AccountEditActivity extends AppCompatActivity {
    EditText ETname,ETnik,ETaddress,ETmail,ETphone,ETcomunity;
    TextView txtbirthday;
    Button btnSave,btnDate;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

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

                DatePickerDialog dialog = new DatePickerDialog(
                        AccountEditActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = month + "/" + day + "/" + year;
                txtbirthday.setText(date);
            }
        };
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
                            put("tgl_lahir",txtbirthday.getText().toString());
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
