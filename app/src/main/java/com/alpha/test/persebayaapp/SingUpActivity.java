package com.alpha.test.persebayaapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

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

import static com.alpha.test.persebayaapp.CommonUtils.getDatabase;

public class SingUpActivity extends AppCompatActivity {
    public final String TAG = this.getClass().getSimpleName();
    private TextView _login;
    EditText username, email, password, confirmPassword;
    private Button btn_singup;
    ProgressDialog progressDialog;
    Boolean cek = true;
    SharedPrefManager sharedPrefManager;

    public Integer x = 0;
    OdooClient client;

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
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
        btn_singup = findViewById(R.id.btn_signup);
        btn_singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new SaveUser().execute();
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                progressDialog.setCancelable(false);
                create_user();
            }
        });
        username = findViewById(R.id.input_name_up);
        email = findViewById(R.id.input_email_up);
        password = findViewById(R.id.input_password_up);
        confirmPassword = findViewById(R.id.input_confirm_password_up);
        client = CommonUtils.getOdooConnection(getBaseContext());
    }


    //    AuthenticateListener loginCallback = new AuthenticateListener() {
//        @Override
//        public void onLoginSuccess(OdooUser user) {
//            x=1;
//            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
//            String cekemail = email.getText().toString().trim();
//            if(username.getText().length()<3){
//                username.setError("Minimun 3 Char");
//                progressDialog.dismiss();
//            }else if(username.getText().equals("")){
//                username.setError("Username cannot Null");
//                progressDialog.dismiss();
//            }else if (password.getText().length()<8){
//                password.setError("Minimum 8 Char");
//                progressDialog.dismiss();
//            }else if(!password.getText().toString().equals(confirmPassword.getText().toString())){
//                password.setError("Password not match with Confirm Password");
//                confirmPassword.setError("Password not match with Confirm Password");
//                progressDialog.dismiss();
//            }else if(!cekemail.matches(emailPattern)){
//                email.setError("Invalid E-Mail");
//                progressDialog.dismiss();
//            }else {
//                final String RegId = FirebaseInstanceId.getInstance().getToken();
//                OdooValues values = new OdooValues();
//                values.put("name", username.getText().toString());
//                values.put("login", username.getText().toString());
//                values.put("email", email.getText().toString());
//                values.put("club_id", 52);
//                values.put("password", password.getText().toString());
//                values.put("state", "active");
//                values.put("fcm_reg_ids", RegId);
//                System.out.println(values.toString());
//
//                    client.create("res.users", values, new IOdooResponse() {
//
//                        @Override
//                        public void onResult(OdooResult result) {
//                            // Success response
//                            Toast.makeText(SingUpActivity.this, "Account Created!", Toast.LENGTH_LONG).show();
//                            System.out.println(result.toString());
//                            progressDialog.dismiss();
//
//                        }
//                        @Override
//                        public boolean onError(OdooErrorException error) {
//
//                            Toast.makeText(SingUpActivity.this, String.valueOf(error.getMessage()), Toast.LENGTH_LONG).show();
//                            System.out.println(error.toString());
//                            progressDialog.dismiss();
//                            return super.onError(error);
//                        }
//                    });
//
//
//            }
//        }
//
//        @Override
//        public void onLoginFail(AuthError error) {
//            Toast.makeText(SingUpActivity.this,String.valueOf(error.toString()),Toast.LENGTH_LONG).show();
//            progressDialog.dismiss();
//        }
//    };

    public void create_user() {
        if (!is_Valid()) {
            progressDialog.dismiss();
            return;
        }else {
            is_connect();
        }
    }

    public Boolean is_Valid() {
        Boolean is_Success = false;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.+[a-z]+";

        if (!TextUtils.isEmpty(email.getText())) {
            String cekemail = email.getText().toString().trim();
            if (cekemail.matches(emailPattern)) {
                if (!TextUtils.isEmpty(username.getText())) {
                    if (username.getText().length() < 3) {
                        username.setError("Minimum 3 Char");
                    } else {
                        if (!TextUtils.isEmpty(password.getText())) {
                            if (password.getText().length() < 8) {
                                password.setError("Minimum 8 Char");
                            } else {
                                if (!TextUtils.isEmpty(password.getText())) {
                                    if (password.getText().length() < 8) {
                                        password.setError("Minimum 8 Char");
                                    } else {
                                        if (!TextUtils.isEmpty(confirmPassword.getText())) {
                                            if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
                                                confirmPassword.setError("Password not match with Confirm Password");
                                            } else {
                                                is_Success = true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                email.setError("Invalid E-Mail");
            }
        }

        return is_Success;
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

                    for(OdooRecord record: records) {
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_SESSION_ID, user.sessionId);
                        register();
                    }
                }
            });

        }
        @Override
        public void onLoginFail(AuthError error) {
            Toast.makeText(SingUpActivity.this,error.toString(),Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    };

    public void register(){
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                final String RegId = task.getResult().getToken();
                OArguments arguments = new OArguments();
                arguments.add(username.getText().toString());
                arguments.add(email.getText().toString());
                arguments.add(password.getText().toString());
                arguments.add(RegId);
                client.call_kw("res.users","create_user", arguments, new IOdooResponse() {

                    @Override
                    public void onResult(OdooResult result) {
                        // Success response
                        progressDialog.dismiss();
                        Toast.makeText(SingUpActivity.this, "Account Created!", Toast.LENGTH_LONG).show();
                        finish();

                    }

                    @Override
                    public boolean onError(OdooErrorException error) {
                        Log.d(TAG, error.getLocalizedMessage());
                        Toast.makeText(SingUpActivity.this, String.valueOf(error.getLocalizedMessage()), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        return super.onError(error);
                    }
                });
            }

        });
    }
}
