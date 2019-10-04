package com.alpha.test.persebayaapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.OdooUser;
import oogbox.api.odoo.client.AuthError;
import oogbox.api.odoo.client.OdooVersion;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.ODomain;
import oogbox.api.odoo.client.helper.utils.OdooFields;
import oogbox.api.odoo.client.listeners.AuthenticateListener;
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooConnectListener;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText etUsername,etPassword;

    private TextView _singupLink;
    OdooClient client;
    String uname = "";
    String pass = "";
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        sharedPrefManager =  new SharedPrefManager(this);
        _singupLink = (TextView) findViewById(R.id.link_signup);
        _singupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new SignUpTask().execute();
                Intent singup = new Intent(getApplicationContext(),SingUpActivity.class);
                startActivity(singup);
                progressDialog.dismiss();
            }
        });
        btnLogin = findViewById(R.id.btn_login);
        etUsername = findViewById(R.id.input_username);
        etPassword = findViewById(R.id.input_password);
        progressDialog = new ProgressDialog(LoginActivity.this);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uname = etUsername.getText().toString();
                pass = etPassword.getText().toString();
                if (uname.isEmpty() && pass.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Wrong Username / Password!",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }else{
                    new LoginTask().execute();

                }
            }
        });
        if (sharedPrefManager.getSPSudahLogin()){
            startActivity(new Intent(LoginActivity.this, HomeActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    }

    AuthenticateListener loginCallback = new AuthenticateListener() {
        @Override
        public void onLoginSuccess(final OdooUser user) {
            List<Integer> ids = Arrays.asList(user.uid);
            List<String> fields = Arrays.asList("club_id","fcm_reg_ids");
            client.read("res.users", ids, fields, new IOdooResponse() {
                @Override
                public void onResult(OdooResult result) {
                    OdooRecord[] records = result.getRecords();

                    for(OdooRecord record: records) {
                        sharedPrefManager.saveSPInt(SharedPrefManager.SP_ID_USER, user.uid);
                        sharedPrefManager.saveSPInt(SharedPrefManager.SP_ID_CLUB, record.getInt("club_id"));
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_NAMA_CLUB, record.getString("club_id"));
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_REG_ID, record.getString("fcm_reg_ids"));
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_NAMA_USER, user.username);
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_PASSWORD_USER, pass);
                        sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, true);
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_SESSION_ID, user.sessionId);
                    }

                    startActivity(new Intent(LoginActivity.this, HomeActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });

            ODomain domain = new ODomain();
            domain.add("status_liga", "=", "valid");

            OdooFields fieldsliga = new OdooFields();
            fieldsliga.addAll("id", "nama", "create_date", "create_uid", "write_date", "write_uid");

            int offset = 0;
            int limit = 1;

            String sorting = "id DESC";

            client.searchRead("persebaya.liga", domain, fieldsliga, offset, limit, sorting, new IOdooResponse() {
                @Override
                public void onResult(OdooResult result) {
                    OdooRecord[] records = result.getRecords();
                    for (OdooRecord record : records) {
                        sharedPrefManager.saveSPInt(SharedPrefManager.SP_ID_Liga, record.getInt("id"));
                    }
                }
            });
            progressDialog.dismiss();
            finish();
        }
        @Override
        public void onLoginFail(AuthError error) {
            Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    };


    public class LoginTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            client = new OdooClient.Builder(getApplicationContext())
                    .setHost(sharedPrefManager.getSP_Host_url())
                    .setSynchronizedRequests(false)
                    .setConnectListener(new OdooConnectListener() {
                        @Override
                        public void onConnected(OdooVersion version) {
                            // Success connection
                            client.authenticate(uname,pass, sharedPrefManager.getSP_db(), loginCallback);
                        }
                    })
                    .build();
            return null;
        }

    }

//    AuthenticateListener registerCallback = new AuthenticateListener() {
//        @Override
//        public void onLoginSuccess(final OdooUser user) {
//            List<Integer> ids = Arrays.asList(user.uid);
//            List<String> fields = Arrays.asList("club_id","fcm_reg_ids");
//            client.read("res.users", ids, fields, new IOdooResponse() {
//                @Override
//                public void onResult(OdooResult result) {
//                    OdooRecord[] records = result.getRecords();
//
//                    for(OdooRecord record: records) {
//                        sharedPrefManager.saveSPString(SharedPrefManager.SP_SESSION_ID, user.sessionId);
//                    }
//                    Intent singup = new Intent(getApplicationContext(),SingUpActivity.class);
//                    startActivity(singup);
//                    progressDialog.dismiss();
//                    finish();
//                }
//            });
//
//        }
//        @Override
//        public void onLoginFail(AuthError error) {
//            Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_LONG).show();
//            progressDialog.dismiss();
//        }
//    };
//
//    public class SignUpTask extends AsyncTask<Void,Void,Void>{
//
//        @Override
//        protected void onPreExecute() {
//            progressDialog.setIndeterminate(true);
//            progressDialog.setMessage("Authenticating...");
//            progressDialog.show();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            client = new OdooClient.Builder(getApplicationContext())
//                    .setHost(sharedPrefManager.getSP_Host_url())
//                    .setSynchronizedRequests(false)
//                    .setConnectListener(new OdooConnectListener() {
//                        @Override
//                        public void onConnected(OdooVersion version) {
//                            // Success connection
//                            client.authenticate("admin","admin", sharedPrefManager.getSP_db(), registerCallback);
//                        }
//                    })
//                    .build();
//            return null;
//        }
//
//    }

}
