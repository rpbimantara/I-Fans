package com.alpha.test.persebayaapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import oogbox.api.odoo.client.listeners.OdooErrorListener;

import static com.alpha.test.persebayaapp.CommonUtils.getDatabase;

public class LupaPasswordActivity extends AppCompatActivity {
    public final String TAG = this.getClass().getSimpleName();
    Button btnLupaPassword;
    EditText etUsername;
    WebView webView;

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
        client = CommonUtils.getOdooConnection1(getBaseContext(), new OdooErrorListener() {
            @Override
            public void onError(OdooErrorException error) {
                Log.d(TAG,"Result"+error.toString());
                progressDialog.dismiss();
            }
        });
//        webView = findViewById(R.id.webview_1);
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return false;
//            }
//        });
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.loadUrl("http://103.133.56.224:8069");
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
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                progressDialog.setCancelable(false);
                is_connect();

            }
        });
    }

    public void is_connect(){
        client.authenticate("register","register", getDatabase(), registerCallback);
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
                    Log.d(TAG,result.toString());
                    for(OdooRecord record: records) {
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_SESSION_ID, user.sessionId);

                    }
                    if (!TextUtils.isEmpty(etUsername.getText())){
                        resetPassword();
                    }else{
                        Toast.makeText(getBaseContext(),"Input your username or email!",Toast.LENGTH_SHORT).show();
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

    public void resetPassword(){
        OArguments arguments = new OArguments();
        arguments.add(etUsername.getText().toString());
        client.call_kw("res.users", "reset_password_users", arguments, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                progressDialog.dismiss();
                Log.d(TAG,"Result"+result.toString());
                Toast.makeText(getBaseContext(),"Check your email to change password!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onError(OdooErrorException error) {
                progressDialog.dismiss();
                Log.d(TAG,"Result"+error.toString());
                Toast.makeText(getBaseContext(), String.valueOf(error.getLocalizedMessage()),Toast.LENGTH_SHORT).show();
                return super.onError(error);
            }
        });
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
//                        if (!TextUtils.isEmpty(etUsername.getText())){
//                            OArguments arguments = new OArguments();
//                            arguments.add(etUsername.getText().toString());
//                            client.call_kw("res.users", "reset_password_users", arguments, new IOdooResponse() {
//                                @Override
//                                public void onResult(OdooResult result) {
//                                    progressDialog.dismiss();
//                                    Log.d(TAG,"Result"+result.toString());
//                                    Toast.makeText(getBaseContext(),"Check your email to change password!",Toast.LENGTH_SHORT).show();
//                                }
//
//                                @Override
//                                public boolean onError(OdooErrorException error) {
//                                    progressDialog.dismiss();
//                                    Log.d(TAG,"Result"+error.toString());
//                                    Toast.makeText(getBaseContext(), String.valueOf(error.getLocalizedMessage()),Toast.LENGTH_SHORT).show();
//                                    return super.onError(error);
//                                }
//                            });
//                        }else{
//                            Toast.makeText(getBaseContext(),"Input your username or email!",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//            });
//
//        }
//        @Override
//        public void onLoginFail(AuthError error) {
//            Toast.makeText(getBaseContext(),error.toString(),Toast.LENGTH_LONG).show();
//            progressDialog.dismiss();
//        }
//    };
}
