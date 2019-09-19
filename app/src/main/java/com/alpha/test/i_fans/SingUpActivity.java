package com.alpha.test.i_fans;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.OdooUser;
import oogbox.api.odoo.client.AuthError;
import oogbox.api.odoo.client.OdooVersion;
import oogbox.api.odoo.client.helper.OdooErrorException;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.OdooValues;
import oogbox.api.odoo.client.listeners.AuthenticateListener;
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooConnectListener;

public class SingUpActivity extends AppCompatActivity {

    private TextView _login;
    EditText username,email,password,confirmPassword;
    private Button  btn_singup;
    ProgressDialog progressDialog;
    Boolean cek = true;
    OdooClient client;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        progressDialog = new ProgressDialog(this);
        sharedPrefManager = new SharedPrefManager(getBaseContext());
        _login = (TextView) findViewById(R.id.link_login);
        _login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
                finish();
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
        confirmPassword = findViewById(R.id.input_confirm_password_up);
    }

    AuthenticateListener loginCallback = new AuthenticateListener() {
        @Override
        public void onLoginSuccess(OdooUser user) {
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            String cekemail = email.getText().toString().trim();
            if(username.getText().length()<3){
                username.setError("Minimun 3 Char");
                progressDialog.dismiss();
            }else if(username.getText().equals("")){
                username.setError("Username cannot Null");
                progressDialog.dismiss();
            }else if (password.getText().length()<8){
                password.setError("Minimum 8 Char");
                progressDialog.dismiss();
            }else if(!password.getText().toString().equals(confirmPassword.getText().toString())){
                password.setError("Password not match with Confirm Password");
                confirmPassword.setError("Password not match with Confirm Password");
                progressDialog.dismiss();
            }else if(!cekemail.matches(emailPattern)){
                email.setError("Invalid E-Mail");
                progressDialog.dismiss();
            }else {
                final String RegId = FirebaseInstanceId.getInstance().getToken();
                OdooValues values = new OdooValues();
                values.put("name", username.getText().toString());
                values.put("login", username.getText().toString());
                values.put("email", email.getText().toString());
                values.put("club_id", 52);
                values.put("password", password.getText().toString());
                values.put("state", "active");
                values.put("fcm_reg_ids", RegId);

                client.create("res.users", values, new IOdooResponse() {
                    @Override
                    public void onResult(OdooResult result) {
                        // Success response
                        Toast.makeText(SingUpActivity.this, "Account Created!", Toast.LENGTH_LONG).show();
                        System.out.println(result.toString());
                        progressDialog.dismiss();

                    }

                    @Override
                    public boolean onError(OdooErrorException error) {
                        Toast.makeText(SingUpActivity.this, String.valueOf(error.getMessage()), Toast.LENGTH_LONG).show();
                        System.out.println(error.toString());
                        progressDialog.dismiss();
                        return super.onError(error);
                    }
                });
            }
        }

        @Override
        public void onLoginFail(AuthError error) {
            Toast.makeText(SingUpActivity.this,String.valueOf(error.toString()),Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    };

    public class SingupTask extends AsyncTask<Void,Void,Boolean>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            client = new OdooClient.Builder(getBaseContext())
                    .setHost(sharedPrefManager.getSP_Host_url())
                    .setSynchronizedRequests(false)
                    .setConnectListener(new OdooConnectListener() {
                        @Override
                        public void onConnected(OdooVersion version) {
                            progressDialog.setMessage("Creating......");
                            progressDialog.show();
                            client.authenticate("register","register", sharedPrefManager.getSP_db(), loginCallback);
                        }
                    }).build();
            return null;
        }
    }

}
