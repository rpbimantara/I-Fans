package com.example.nerita_hendra.i_fans;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText txtUsername,txtPassword;

    private TextView _singupLink;
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
                Intent singup = new Intent(getApplicationContext(),SingUpActivity.class);
                startActivity(singup);
            }
        });
        btnLogin = findViewById(R.id.btn_login);
        txtUsername = findViewById(R.id.input_username);
        txtPassword = findViewById(R.id.input_password);
        progressDialog = new ProgressDialog(LoginActivity.this);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uname = txtUsername.getText().toString();
                pass = txtPassword.getText().toString();
                if (uname.isEmpty() && pass.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Masukan username/password anda dengan benar!",Toast.LENGTH_LONG).show();
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

    public Boolean login(String user, String pass){
        Boolean ocT = false;
        try {

             ocT = OdooConnect.testConnection( user, pass);

        } catch (Exception ex) {
            System.out.println("Error1: " + ex);
        }
        return ocT;
    }

    public String[] CheckActiveUser(String user, String pass){
        String id_user = "";
        String id_partner = "";
        String id_club = "";
        String nama_club = "";
        String reg_id = "";
        try {
            OdooConnect  oc = OdooConnect.connect(user, pass);

            Object[] param = {new Object[]{
                    new Object[]{"login", "=", user},
                    new Object[]{"active", "=", true}}};

            List<HashMap<String, Object>> data = oc.search_read("res.users", param, "id","partner_id","club_id","fcm_reg_ids");

            nama_club = data.get(0).get("club_id").toString();
            reg_id = data.get(0).get("fcm_reg_ids").toString();
            Object[] paramClub = {new Object[]{
                    new Object[]{"nama", "=",  data.get(0).get("club_id").toString()}}};
            List<HashMap<String, Object>> dataClub = oc.search_read("persebaya.club", paramClub, "id");
            id_user = data.get(0).get("id").toString();
            id_club = dataClub.get(0).get("id").toString();
        } catch (Exception ex) {
            System.out.println("Error Check User Activity: " + ex);
        }
        Log.e("Warning error!!!!",id_club +id_user+nama_club+id_partner);
        return new String [] {id_user,id_club,nama_club,reg_id} ;
    }

    public class LoginTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Boolean checkLogin =  login(uname,pass);
            if (checkLogin == true){
                String result[] = CheckActiveUser(uname,pass);
                sharedPrefManager.saveSPInt(SharedPrefManager.SP_ID_USER, Integer.valueOf(result[0]));
                sharedPrefManager.saveSPInt(SharedPrefManager.SP_ID_CLUB, Integer.valueOf(result[1]));
                sharedPrefManager.saveSPString(SharedPrefManager.SP_NAMA_CLUB, result[2]);
                sharedPrefManager.saveSPString(SharedPrefManager.SP_REG_ID, result[3]);
                sharedPrefManager.saveSPString(SharedPrefManager.SP_NAMA_USER, uname);
                sharedPrefManager.saveSPString(SharedPrefManager.SP_PASSWORD_USER, pass);
                sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, true);
                startActivity(new Intent(LoginActivity.this, HomeActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            }else{
                Toast.makeText(LoginActivity.this,"Segera aktifkan akun anda!",Toast.LENGTH_LONG).show();
            }
            return null;
        }
    }

}
