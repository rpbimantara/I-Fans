package com.example.nerita_hendra.i_fans;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;

public class SingUpActivity extends AppCompatActivity {

    private TextView _login;
    EditText username,email,password;
    private Button  btn_singup;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        progressDialog = new ProgressDialog(this);
        _login = (TextView) findViewById(R.id.link_login);
        _login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
            }
        });
        btn_singup = findViewById(R.id.btn_signup);
        btn_singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               new  SingupTask().execute();
            }
        });
        username = findViewById(R.id.input_name_up);
        email = findViewById(R.id.input_email_up);
        password = findViewById(R.id.input_password_up);
    }


    public class SingupTask extends AsyncTask<Void,Void,Boolean>{
        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Creating......");
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            progressDialog.dismiss();
            if (aVoid == true){
                finish();
            }else{
                Toast.makeText(SingUpActivity.this,"Failed creating account!!",Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(aVoid);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Boolean cek = true;
            try {
                OdooConnect oc = OdooConnect.connect("createuser", "createuser");
                final String RegId = FirebaseInstanceId.getInstance().getToken();

                @SuppressWarnings("unchecked")
                Integer idUser = oc.create("res.users", new HashMap() {{
                    put("name", username.getText().toString());
                    put("login", username.getText().toString());
                    put("email", email.getText().toString());
                    put("password", password.getText().toString());
                    put("state", "active");
                    put("fcm_reg_ids",RegId);
                }});

                if (idUser.toString() == null){
                    cek = false;
                }else{
                    cek = true;
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex);

            }
            return cek;
        }
    }

}
