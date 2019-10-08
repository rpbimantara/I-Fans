package com.alpha.test.persebayaapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import oogbox.api.odoo.client.helper.OdooErrorException;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.OArguments;
import oogbox.api.odoo.client.listeners.AuthenticateListener;
import oogbox.api.odoo.client.listeners.IOdooResponse;

public class LupaPasswordActivity extends AppCompatActivity {
    Button btnLupaPassword;
    EditText etUsername;

    private TextView _loginLink;
    OdooClient client;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password);
        sharedPrefManager =  new SharedPrefManager(this);
        progressDialog = new ProgressDialog(this);
        _loginLink = findViewById(R.id.link_login);
        client = CommonUtils.getOdooConnection(getBaseContext());
        btnLupaPassword = findViewById(R.id.btn_send_lupa_password);
        etUsername = findViewById(R.id.input_username_lupapassword);
        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(getBaseContext(),LoginActivity.class);
                startActivity(login);
                finish();
            }
        });
        btnLupaPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                is_connect();

            }
        });
    }

    public void is_connect(){
        client.authenticate("register","register", sharedPrefManager.getSP_db(), registerCallback);
    }

    AuthenticateListener registerCallback = new AuthenticateListener() {
        @Override
        public void onLoginSuccess(final OdooUser user) {
            List<Integer> ids = Arrays.asList(user.uid);
            List<String> fields = Arrays.asList("club_id","fcm_reg_ids");
            client.read("res.users", ids, fields, new IOdooResponse() {
                @Override
                public void onResult(OdooResult result) {
                    OdooRecord[] records = result.getRecords();

                    for(OdooRecord record: records) {
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_SESSION_ID, user.sessionId);
                        if (!TextUtils.isEmpty(etUsername.getText())){
                            OArguments arguments = new OArguments();
                            arguments.add(etUsername.getText().toString());
                            client.call_kw("res.users", "reset_password_users", arguments, new IOdooResponse() {
                                @Override
                                public void onResult(OdooResult result) {
                                    Toast.makeText(getBaseContext(),"Check your email to change password!",Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public boolean onError(OdooErrorException error) {
                                    Toast.makeText(getBaseContext(), String.valueOf(error.getLocalizedMessage()),Toast.LENGTH_SHORT).show();
                                    return super.onError(error);
                                }
                            });
                        }else{
                            Toast.makeText(getBaseContext(),"Input your username or email!",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

        }
        @Override
        public void onLoginFail(AuthError error) {
            Toast.makeText(getBaseContext(),error.toString(),Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    };
}
