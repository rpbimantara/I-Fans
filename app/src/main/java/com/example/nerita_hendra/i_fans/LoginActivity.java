package com.example.nerita_hendra.i_fans;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
        System.out.println(uname +"--"+pass);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uname = txtUsername.getText().toString();
                pass = txtPassword.getText().toString();
//                if (txtUsername.getText().equals("") && txtPassword.getText().equals("")){
                    Boolean checkLogin =  login(uname,pass);
                    if (checkLogin == true){
                        int idUser = CheckActiveUser(uname,pass);
                        sharedPrefManager.saveSPInt(SharedPrefManager.SP_ID_USER, idUser);
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_NAMA_USER, uname);
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_PASSWORD_USER, pass);
                        sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, true);
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                        txtUsername.setText("");
                        txtPassword.setText("");
                    }else{
                        Toast.makeText(LoginActivity.this,"Segera aktifkan akun anda!",Toast.LENGTH_LONG).show();
                    }
//                }else{
//                    Toast.makeText(LoginActivity.this,"Masukan username/password anda dengan benar!",Toast.LENGTH_LONG).show();
//                }
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
            System.out.println("OCT" +ocT);

        } catch (Exception ex) {
            System.out.println("Error1: " + ex);
        }
        return ocT;
    }

    public int CheckActiveUser(String user, String pass){
        int id = 0;
        try {
            OdooConnect  oc = OdooConnect.connect(user, pass);

            Object[] param = {new Object[]{
                    new Object[]{"login", "=", user},
                    new Object[]{"active", "=", true}}};

            List<HashMap<String, Object>> data = oc.search_read("res.users", param, "id","partner_id");
             id = (Integer) data.get(0).get("id");

            System.out.println("ID" +id);

        } catch (Exception ex) {
            System.out.println("Error Check User Activity: " + ex);
        }
        return id ;
    }
}
