package com.alpha.test.persebayaapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.helper.OdooErrorException;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.OdooValues;
import oogbox.api.odoo.client.listeners.IOdooResponse;

import static com.alpha.test.persebayaapp.CommonUtils.getBase64ImageString;
import static com.alpha.test.persebayaapp.CommonUtils.getOdooConnection;

public class DonationAddActivity extends AppCompatActivity {

    EditText etName,etTarget,etdeskripsi;
    TextView txtDuedate;
    ImageView imageUser;
    private Bitmap currentImage;
    SharedPrefManager sharedPrefManager;
    FloatingActionButton fabImageDonation;
    ProgressDialog progressDialog;
    Button savebtn;
    OdooClient client;
    String imageCurrent = "";
    String txtName = "";
    String txtTarget = "";
    String Duedate ="";
    String txtdeskripsi = "";
    String state = "create";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_add);
        sharedPrefManager = new SharedPrefManager(this);
        progressDialog = new ProgressDialog(this);
        imageUser = findViewById(R.id.image_Add_donation);
        imageUser.setImageResource(R.drawable.ic_account);
        fabImageDonation = findViewById(R.id.image_fab_Add_donation);
        fabImageDonation.setImageResource(R.drawable.ic_camera);
        savebtn = findViewById(R.id.button_save_donation);
        client = getOdooConnection(getBaseContext());
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state.equalsIgnoreCase("create")){
                    if(is_Valid() == true){
                        createbtn();
                    }
                }else{
//                    if(is_Valid() == true){
//                        savebtn();
//                    }
                }
            }
        });
        etName = findViewById(R.id.editText_nama_donation);
        etTarget = findViewById(R.id.editText_target_donation);
        txtDuedate = findViewById(R.id.add_due_date_donation);
        etdeskripsi = findViewById(R.id.editText_Deskripsi_donation);
        txtDuedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(DonationAddActivity.this, AlertDialog.THEME_HOLO_LIGHT,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        SimpleDateFormat f = new SimpleDateFormat("dd MMM yyyy");
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, month);
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        txtDuedate.setText(f.format(cal.getTime()));
                    }
                },year,month,day);
                dialog.show();
            }
        });

        fabImageDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });
        if (!getIntent().getExtras().get("id").toString().equalsIgnoreCase("false")){
            state = "edit";
//            etOb.setEnabled(false);
//            LoadItem();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Uri photoUri = data.getData();
            if (photoUri != null){
                try{
                    currentImage = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);
                    imageUser.setImageBitmap(currentImage);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
    }


    public void createbtn(){
        progressDialog.setMessage("Create.....");
        progressDialog.show();
        OdooValues values = new OdooValues();
        values.put("image_medium", getBase64ImageString(currentImage));
        values.put("name", etName.getText().toString());
        values.put("target_donasi", etTarget.getText().toString());
        values.put("purchase_ok", false);
        values.put("type", "donasi");
        values.put("description_sale", etdeskripsi.getText().toString());
        values.put("due_date", txtDuedate.getText().toString());
        values.put("create_uid", sharedPrefManager.getSpIdUser());

        client.create("product.template", values, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                // Success response
                progressDialog.dismiss();
                Toast.makeText(getBaseContext(),"Donation Created!",Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public boolean onError(OdooErrorException error) {
                Toast.makeText(getBaseContext(),String.valueOf(error.getMessage()),Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                return super.onError(error);
            }
        });
    }

    public Boolean is_Valid() {
        Boolean is_Success = true;
        if(!TextUtils.isEmpty(getBase64ImageString(currentImage))){
            Toast.makeText(getBaseContext(),"Choose at least 1 image!",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(etName.getText())){
            is_Success = false;
            etName.setError("Fill Name.");
        }

        if(TextUtils.isEmpty(etdeskripsi.getText())){
            is_Success = false;
            etdeskripsi.setError("Fill Description.");
        }

        if(!TextUtils.isEmpty(etTarget.getText())){
            if(Integer.valueOf(etTarget.getText().toString())<=0){
                is_Success = false;
                etTarget.setError("Must greather than 0");
            }
        }else{
            is_Success = false;
            etTarget.setError("Fill Target Donation.");
        }
        if(!txtDuedate.getText().toString().equalsIgnoreCase("")){
            DateFormat f = new SimpleDateFormat("dd MMM yyyy");
            Date date = new Date();
            try {
                if (!f.parse(txtDuedate.getText().toString()).after(f.parse(f.format(date)))){
                    is_Success = false;
                    Toast.makeText(getBaseContext(),"Must fill with different day after today!",Toast.LENGTH_SHORT).show();
                }
            } catch (ParseException e) {
                throw new IllegalArgumentException(e);
            }
        }else{
            is_Success = false;
            Toast.makeText(getBaseContext(),"Fill Due Date.",Toast.LENGTH_SHORT).show();
        }
        return is_Success;
    }

//    public class SaveDonationTask extends AsyncTask<Void,Void,Void> {
//        @Override
//        protected Void doInBackground(Void... voids) {
//            client = new OdooClient.Builder(getBaseContext())
//                    .setHost(sharedPrefManager.getSP_Host_url())
//                    .setSynchronizedRequests(false)
//                    .setSession(sharedPrefManager.getSpSessionId())
//                    .setConnectListener(new OdooConnectListener() {
//                        @Override
//                        public void onConnected(OdooVersion version) {
//                            progressDialog.setMessage("Saving.....");
//                            progressDialog.show();
//                            OdooValues values = new OdooValues();
//                            values.put("image_medium", getBase64ImageString(currentImage));
//                            values.put("name", etName.getText().toString());
//                            values.put("target_donasi", etTarget.getText().toString());
//                            values.put("purchase_ok", false);
//                            values.put("type", "donasi");
//                            values.put("description_sale", etdeskripsi.getText().toString());
//                            values.put("due_date", txtDuedate.getText().toString());
//                            values.put("create_uid", sharedPrefManager.getSpIdUser());
//
//                            client.create("product.template", values, new IOdooResponse() {
//                                @Override
//                                public void onResult(OdooResult result) {
//                                    // Success response
//                                    progressDialog.dismiss();
//                                    Toast.makeText(getBaseContext(),"Auction Created!",Toast.LENGTH_LONG).show();
//                                    finish();
//                                }
//
//                                @Override
//                                public boolean onError(OdooErrorException error) {
//                                    Toast.makeText(getBaseContext(),String.valueOf(error.getMessage()),Toast.LENGTH_LONG).show();
//                                    progressDialog.dismiss();
//                                    return super.onError(error);
//                                }
//                            });
//                        }
//                    }).build();
//            return null;
//        }
//    }
}
